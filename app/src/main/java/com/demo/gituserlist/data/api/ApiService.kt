package com.demo.gituserlist.data.api

import com.demo.gituserlist.model.GetUserListItem
import retrofit2.http.GET

interface ApiService {

    @GET("/users")
    suspend fun getUserList(): List<GetUserListItem>

}