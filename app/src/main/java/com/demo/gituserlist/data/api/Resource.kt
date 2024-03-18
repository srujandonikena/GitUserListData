package com.demo.gituserlist.data.api

data class Resource<out T: Any>(val status: StatusCalled, val data: T?, val message: String?) {

    companion object {

        fun <T: Any> success(data: T?): Resource<T> {
            return Resource(
                StatusCalled.SUCCESS, data, null
            )
        }

        fun <T: Any> error(msg: String, data: T?): Resource<T> {
            return Resource(
                StatusCalled.ERROR, data, msg
            )
        }

        fun <T: Any> loading(data: T?): Resource<T> {
            return Resource(
                StatusCalled.LOADING, data, null
            )
        }

    }
}