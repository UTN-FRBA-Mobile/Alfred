package com.botigocontigo.alfred.google

class GoogleSearchService(private val googleApi: GoogleApi) {

    fun search(query: String, callbacks: GoogleSearchCallbacks) {
        val q = getRawQuery(query)
        googleApi.siteRestrictSearch(q).call(callbacks)
    }

    private fun getRawQuery(query: String) : String {
        return query
                .replace("\"", "")
                .replace(' ', '+')
    }

}
