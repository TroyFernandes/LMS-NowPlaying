package com.example.lmsnowplaying.network.jellyfin

import android.content.Context
import com.example.lmsnowplaying.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.extensions.artistsApi
import org.jellyfin.sdk.api.client.extensions.authenticateUserByName
import org.jellyfin.sdk.api.client.extensions.imageApi
import org.jellyfin.sdk.api.client.extensions.searchApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ImageType

object JellyfinApi {

    private var BASEURL : String = BuildConfig.JELLYFIN_URL
    private var USERNAME : String = BuildConfig.JELLYFIN_USERNAME
    private var PASSWORD : String = BuildConfig.JELLYFIN_PASSWORD
    private var APIKEY : String = BuildConfig.JELLYFIN_API_KEY


    private lateinit var ApiClient: ApiClient
        private set
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

        ApiClient = api

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

    suspend fun GetArtistUrl(artistName: String): String?{

        val searchApi = ApiClient.searchApi
        val artistApi = ApiClient.artistsApi
        val imageApi = ApiClient.imageApi

        var imageUrl: String? = ""

        val result = CoroutineScope(Dispatchers.IO).async{
            val artistSearch = artistApi.getArtists(searchTerm = artistName, limit = 1, nameStartsWith = artistName)
            if(artistSearch.content.items?.isNotEmpty() == true){
                val artistUUID = artistSearch.content.items?.get(0)?.id
                imageUrl = artistUUID?.let { imageApi.getItemImageUrl(itemId = it, imageType = ImageType.BACKDROP) }
            }else{
                val search = searchApi.get(searchTerm = artistName, includeItemTypes = setOf(BaseItemKind.MUSIC_ARTIST))
                if(search.content.searchHints.isNotEmpty()){
                    imageUrl = imageApi.getItemImageUrl(itemId = search.content.searchHints.get(0).id, imageType = ImageType.BACKDROP)
                }
            }
        }
        result.await()
        return imageUrl
    }

}
