package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.learn.Article
import com.botigocontigo.alfred.learn.ArticleDeserializer
import com.botigocontigo.alfred.tasks.Plan
import com.botigocontigo.alfred.tasks.PlanDeserializer
import com.botigocontigo.alfred.utils.Api
import com.botigocontigo.alfred.utils.ApiRequest
import com.botigocontigo.alfred.utils.NetworkingAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder

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

    fun favouritesGetAll(): List<Article> {
        val request = ApiRequest(this, "post", "methods/api.getFavourites")
        applyPermissions(request)

        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Article::class.java, ArticleDeserializer())
        val gson = gsonBuilder.create()

        return gson.fromJson(request.toString() , Array<Article>::class.java).toList()
    }

    fun plansGetAll(): List<Plan> {
        val request = ApiRequest(this, "post", "methods/api.getPlanList")
        applyPermissions(request)

        val gsonBuilder = GsonBuilder().serializeNulls()
        gsonBuilder.registerTypeAdapter(Plan::class.java, PlanDeserializer())
        val gson = gsonBuilder.create()

        return gson.fromJson(request.toString() , Array<Plan>::class.java).toList()
    }


    fun plansSaveAll(plans: Array<Plan>): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.insertPlanList")
        applyPermissions(request)

        //FIXME I think this need to be serialized or transform the array of tasks to a JSON
        //This transform plans to JSON object, like:
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
        val gson = Gson()
        val plansJSONObject= gson.toJson(plans)
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