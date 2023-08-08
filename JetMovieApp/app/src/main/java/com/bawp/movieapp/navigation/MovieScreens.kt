package com.bawp.movieapp.navigation

import java.lang.IllegalArgumentException

enum class MovieScreens {
    HomeScreen,
    DetailsScreen;

    companion object {
        fun fromRoute(route:String?): MovieScreens  =
            when (route?.substringBefore("/")){
                HomeScreen.name -> HomeScreen
                DetailsScreen.name -> DetailsScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException ("Route $route is not recognized screen")
            }
    }
}