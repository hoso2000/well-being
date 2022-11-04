package com.example.kotlin_well_being

import android.app.Application
class MyApp :Application(){
    var notificationId = 0

    companion object {
        private var instance : MyApp? = null

        fun  getInstance(): MyApp {
            if (instance == null)
                instance = MyApp()

            return instance!!
        }
    }
}