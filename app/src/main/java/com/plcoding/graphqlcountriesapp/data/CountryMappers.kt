package com.plcoding.graphqlcountriesapp.data

import com.plcoding.CountriesQuery
import com.plcoding.CountryQuery
import com.plcoding.graphqlcountriesapp.domain.DetailedCountry
import com.plcoding.graphqlcountriesapp.domain.SimpleCountry

// This mapping logic should always happen in the data layer

fun CountriesQuery.Country.toSimpleCountry(): SimpleCountry {
    return SimpleCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: "No capital",
    )
}

fun CountryQuery.Country.toDetailedCountry(): DetailedCountry {
    return DetailedCountry(
            code = code,
            name = name,
            emoji = emoji,
            capital = capital ?: "No capital",
            currency = currency ?: "No currency",
            languages = languages.map{ it.name }, // mapNotNull ignora los nulos
            continent = continent.name,
        )
}