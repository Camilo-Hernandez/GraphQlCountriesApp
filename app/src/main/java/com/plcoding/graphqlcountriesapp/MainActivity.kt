package com.plcoding.graphqlcountriesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.plcoding.graphqlcountriesapp.presentation.CountriesScreen
import com.plcoding.graphqlcountriesapp.presentation.CountriesViewModel
import com.plcoding.graphqlcountriesapp.ui.theme.GraphQlCountriesAppTheme
import dagger.hilt.android.AndroidEntryPoint

// Queremos inyectar las dependencias de Hilt a los Componentes Android que los necesiten, como los activities, broadcast receivers o los servicios.
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GraphQlCountriesAppTheme {
                val viewModel = hiltViewModel<CountriesViewModel>()
                val state by viewModel.state.collectAsState()
                CountriesScreen(
                    state = state,
                    onSelectCountry = viewModel::selectCountry, // lo mismo que { viewModel.selectCountry(it) }
                    onDismissCountryDialog = viewModel::dismissCountryDialog,
                )
            }
        }
    }
}