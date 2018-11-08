package com.botigocontigo.alfred.google

interface GoogleSearchResultsHandler {
    fun success(results: List<GoogleSearchResult>)
    fun error(query: String)
}
