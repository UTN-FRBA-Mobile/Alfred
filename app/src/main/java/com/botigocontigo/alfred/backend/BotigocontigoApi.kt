package com.botigocontigo.alfred.backend

import com.botigocontigo.alfred.storage.db.entities.Area
import com.botigocontigo.alfred.tasks.Plan
import com.botigocontigo.alfred.utils.Api
import com.botigocontigo.alfred.utils.ApiRequest
import com.botigocontigo.alfred.utils.NetworkingAdapter
import com.google.gson.Gson

class BotigocontigoApi(adapter: NetworkingAdapter, private val permissions: Permissions) : Api(adapter) {

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

    fun favoriteArticles(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.getFavourites")
        applyPermissions(request)
        return request
    }

    fun saveFavoriteArticle(title: String, description: String, link: String, imageUrl: String?): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.insertFavourite")
        val userId = permissions.getUserId()
        val articleBody = if(imageUrl != null) "{\"title\":\"$title\",\"description\":\"$description\",\"link\":\"$link\",\"imageUrl\":\"$imageUrl\"}"
        else "{\"title\":\"$title\",\"description\":\"$description\",\"link\":\"$link\"}"
        val body = "{\"favourite\":$articleBody,\"userId\":\"$userId\"}"
        request.body = body
        return request
    }

    fun deleteFavoriteArticle(link: String): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.deleteFavourites")
        val userId = permissions.getUserId()
        val body = "{\"link\":\"$link\",\"userId\":\"$userId\"}"
        request.body = body
        return request
    }

    fun plansGetAll(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.getPlanList")
        applyPermissions(request)
        return request
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


    fun areasGetAll(): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.getAreas")
        applyPermissions(request)
        return request
    }

    fun areasSaveAll(areas: Array<Area>): ApiRequest {
        val request = ApiRequest(this, "post", "methods/api.saveAreas")
        applyPermissions(request)

        //FIXME if this fails we need to transform each area and concat the strings as a JSON array
        val gson = Gson()
        val areasJSONObject= gson.toJson(areas)
        request.put("areas", areasJSONObject)
        return request
    }

    private fun applyPermissions(request: ApiRequest) {
        val userId = permissions.getUserId()
        val body = "{\"userId\":\"$userId\"}"
        request.body = body
    }

    override fun getUrl(path: String): String {
        return "http://178.128.229.73:3300/$path"
    }

    override fun getQueueName() : String {
        return "botigocontigo-api"
    }

}