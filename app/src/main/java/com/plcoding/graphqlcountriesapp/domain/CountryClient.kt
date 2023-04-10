package com.plcoding.graphqlcountriesapp.domain

interface CountryClient {
    // Functions to access to country data in the api

    suspend fun getCountries(): List<SimpleCountry> // CountryQuery.Country would be a dto
    suspend fun getCountry(code: String): DetailedCountry? // could be that doesn't exist
}