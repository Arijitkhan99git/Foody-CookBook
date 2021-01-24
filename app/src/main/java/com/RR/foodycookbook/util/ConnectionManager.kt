package com.RR.foodycookbook.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun checkConnetivity(context: Context):Boolean
    {
        //create a a variable of connectivityManager
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo?=connectivityManager.activeNetworkInfo

        if (activeNetwork?.isConnected!=null)
        {
            return activeNetwork.isConnected
        }
        else{
            return false
        }
    }
}