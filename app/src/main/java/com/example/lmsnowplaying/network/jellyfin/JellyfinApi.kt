package com.example.lmsnowplaying.network.jellyfin

import android.content.Context
import com.example.lmsnowplaying.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.extensions.authenticateUserByName
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo

object JellyfinApi {

    private var BASEURL : String = BuildConfig.JELLYFIN_URL
    private var USERNAME : String = BuildConfig.JELLYFIN_USERNAME
    private var PASSWORD : String = BuildConfig.JELLYFIN_PASSWORD
    private var APIKEY : String = BuildConfig.JELLYFIN_API_KEY


    var APITOKEN : String? = null
        private set

    fun SetAPIToken(_context: Context){

        if(APIKEY.isNullOrEmpty()) return

        val jellyfin = createJellyfin{
            clientInfo = ClientInfo("LMS Jellyfin", "0.0.0")
            context = _context
        }

        val api = jellyfin.createApi(
            baseUrl = BASEURL,
            accessToken = APIKEY
        )

        val userApi = api.userApi

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val authenticationResult by userApi.authenticateUserByName(
                    username = USERNAME,
                    password = PASSWORD,
                )
                api.accessToken = authenticationResult.accessToken
                APITOKEN = api.accessToken
            }catch (err: ApiClientException){
                println("Something went wrong: ${err.message}")
            }
        }
    }



}
