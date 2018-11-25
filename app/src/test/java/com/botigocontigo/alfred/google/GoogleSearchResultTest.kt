package com.botigocontigo.alfred.google

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoogleSearchResultTest {

    private fun parse(json: String) : JSONObject {
        val parser = JSONParser()
        return parser.parse(json) as JSONObject
    }

    @Test
    fun titleTest() {
        val json = "{\"title\":\"Titulo generico 1\",\"link\":\"http://google.com\",\"snippet\":\"Descripcion generica 1\",\"pagemap\":{\"cse_image\":[{\"src\":\"http://google.com/image.png\"}]}}"
        val result = GoogleSearchResult(parse(json))
        Assert.assertEquals("Titulo generico 1", result.getTitle())
    }

    @Test
    fun titleWithTilTest() {
        val json = "{\"title\":\"Titulo gênerico 1\",\"link\":\"http://google.com\",\"snippet\":\"Descripcion generica 1\",\"pagemap\":{\"cse_image\":[{\"src\":\"http://google.com/image.png\"}]}}"
        val result = GoogleSearchResult(parse(json))
        Assert.assertEquals("Titulo g nerico 1", result.getTitle())
    }

    @Test
    fun titleWithSymbolsTest() {
        val json = "{\"title\":\"Titulo_generico-1/fruta\",\"link\":\"http://google.com\",\"snippet\":\"Descripcion generica 1\",\"pagemap\":{\"cse_image\":[{\"src\":\"http://google.com/image.png\"}]}}"
        val result = GoogleSearchResult(parse(json))
        Assert.assertEquals("Titulo generico 1 fruta", result.getTitle())
    }

}