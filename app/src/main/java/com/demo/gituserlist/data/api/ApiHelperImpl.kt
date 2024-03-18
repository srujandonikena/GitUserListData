package com.demo.gituserlist.data.api

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiHelperImpl  @Inject constructor (private val apiService: ApiService) : ApiHelper {

    override suspend fun getUserList() = flow {
        emit(value = apiService.getUserList())
    }

}