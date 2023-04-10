package com.plcoding.graphqlcountriesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.graphqlcountriesapp.domain.DetailedCountry
import com.plcoding.graphqlcountriesapp.domain.GetCountriesUseCase
import com.plcoding.graphqlcountriesapp.domain.GetCountryUseCase
import com.plcoding.graphqlcountriesapp.domain.SimpleCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModels have the struggle that they need a ViewModel factory in order to get custom arguments in the constructor
// But with Hilt, this is made automatically
@HiltViewModel
class CountriesViewModel @Inject constructor (
    // The ViewModel has the responsibility of using the respective use cases to query the data and map the result the ui state
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getCountryUseCase: GetCountryUseCase
) : ViewModel(){

    // Se usa el MutableStateFlow para cambiar el estado del CountriesState
    // Y se crea otro análogo llamado countriesState que es de sólo lectura para la UI
    // Así se reducen bugs relacionados al state, pues el ViewModel es el único que puede modificar el flow a través del parámetro value
    private val _countriesState = MutableStateFlow(CountriesState())
    val state = _countriesState.asStateFlow()

    /* Cuando se abre la pantalla, queremos inmediatamente consultar todos los countries
     De esta forma, el init se encarga de iniciar la consulta de los países cuando se crea el ViewModel
     y de actualizar el estado del flujo con los resultados.
     */
    init {
        // Lanza una coroutine en el viewModelScope, que es un scope que se cancela cuando el ViewModel se destruye.
        viewModelScope.launch {
            _countriesState.update { it.copy(
                // Actualiza el valor del _countriesState para indicar que está cargando los datos.
                isLoading = true
            ) }
            _countriesState.update { it.copy(
                // Ejecuta el getCountriesUseCase, que es una clase que obtiene la lista de países desde una fuente de datos.
                countries = getCountriesUseCase.execute(),
                // Actualiza el valor del _countriesState con la lista de países obtenida y cambia el estado de carga a falso.
                isLoading = false,
            ) }
        }
    }

    fun selectCountry(code: String){
        viewModelScope.launch {
            _countriesState.update {it.copy(
                selectedCountry = getCountryUseCase.execute(code)
            )
            }
        }
    }

    fun dismissCountryDialog(){
        _countriesState.update { it.copy(
            selectedCountry = null
        ) }
    }

    // State Class: summarize the different fields that could change in the ui
    // This data class is for the list of countries
    data class CountriesState(
        // setting the initial states
        val countries: List<SimpleCountry> = emptyList(),
        val isLoading: Boolean = false,
        val selectedCountry: DetailedCountry? = null,
    )
}