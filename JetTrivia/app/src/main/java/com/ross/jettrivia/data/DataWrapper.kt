package com.ross.jettrivia.data

data class DataWrapper<T>(
       var questions: T? = null,
       var loading: Boolean = true,
       var e: Exception? = null)