package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.utils.Api
import com.botigocontigo.alfred.utils.ApiRequest
import com.botigocontigo.alfred.utils.NetworkingAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class BotigocontigoApi(adapter: NetworkingAdapter, val permissions: Permissions) : Api(adapter) {

    // usage example:
    // api.learnQuery().call(callbacks)

    fun registerUser(email: String, password: String, name: String): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.RegisterUser")
        request.put("email", email)
        request.put("password", password)
        request.put("name", name)
        return request
    }

    fun loginUser(email: String, password: String): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.login")
        request.put("email", email)
        request.put("password", password)
        return request
    }

    fun learnQuery(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.query")
        applyPermissions(request)
        return request
    }

    fun favouritesSave(title: String, description: String, link: String): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.insertFavourite")
        applyPermissions(request)
        val favouriteJSONObject= "{ \"title\": $title, \"description\": $description, \"link\": $link }"
        request.put("userId", favouriteJSONObject)
        return request
    }

    fun favouritesGetAll(): ApiRequest {
        val gson = Gson()
        val request = ApiRequest(this, "post", "methods/api.getFavourites")
        applyPermissions(request)
        val favouriteList: List<Article> = gson.fromJson(request , Array<Article>::class.java).toList()

        return request
    }

    fun plansGetAll(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.getPlanList")
        applyPermissions(request)
        //TODO transform JSON plans to Mobile Plan Class type using GSON, example:
//        val gson = Gson()
//        val person1 : Person = gson.fromJson(json, Person::class.java)
        val transformedPlans = request
        return transformedPlans
    }

    //FIXME change plans type for the real one
    fun plansSaveAll(plans: String): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.insertPlanList")
        applyPermissions(request)
        //TODO transform plans to JSON object, must be like:
        /*
        {
        userId,
        userEmail,
        businessArea,
        type,
        tasks: array of {
            responsibleID,
            supervisorID,
            taskDescription,
            frequency {
                type,
                value
            },
            status,
            completed
        }
        }

                secondaryValue,
                */
        val plansJSONObject= ""
        request.put("plans", plansJSONObject)
        return request
    }

    private fun applyPermissions(request: ApiRequest) {
        val userId = permissions.getUserId()
        request.put("userId", userId)
    }

    override fun getUrl(path: String): String {
        return "http://178.128.229.73:3300/$path"
    }

    override fun getQueueName() : String {
        return "botigocontigo-api"
    }

}