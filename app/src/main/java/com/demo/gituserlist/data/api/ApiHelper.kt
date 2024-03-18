package com.demo.gituserlist.data.api

import com.demo.gituserlist.model.GetUserListItem
import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    suspend fun getUserList(): Flow<List<GetUserListItem>>
}