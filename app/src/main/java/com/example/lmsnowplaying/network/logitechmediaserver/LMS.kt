package com.example.lmsnowplaying.network.logitechmediaserver

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException

object LMS{

    private val lmsApi = ApiClient.apiService

    var isPlaying: Boolean = false
        private set

    var playerMac: String = "00:00:00:00:00:00"
        private set

    var playerName: String = "No Player"
        private set

    var players: MutableList<Pair<String, String>> = arrayListOf()
        private set

    suspend fun getPlayers(){

        val reqString = "{\"method\": \"slim.request\", \"params\": [\"\", [\"players\", \"0\", \"8\"]]}"
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        val res = lmsApi.getPlayer(requestBody)

        val jsonData: String? = res.body()?.string()
        val obj = JSONObject(jsonData)
        val getObject = obj.getJSONObject("result")

        val loop = getObject.getJSONArray("players_loop")
        for (i in 0 until loop.length()) {
            val item = loop.getJSONObject(i)
            val name = item.getString("name")
            val player_mac = item.getString("playerid")
            players.add(Pair(name, player_mac))
        }

        println(players)
    }

    fun setPlayer(name: String, mac: String){
        playerName = name
        playerMac = mac
    }

    suspend fun getSongInfo(playerMAC: String, songID: Int): JSONArray {
        val reqString =
            "{\"method\": \"slim.request\", \"params\": [\"$playerMAC\", [\"songinfo\",\"-\",100,\"track_id:$songID\"]]}"

        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        val res = lmsApi.getCurrentSong(requestBody)

        val jsonData: String? = res.body()?.string()
        val obj = JSONObject(jsonData)
        val getObject = obj.getJSONObject("result")

        return getObject.getJSONArray("songinfo_loop")
    }

    suspend fun playPause(){
        if (playerMac == "00:00:00:00:00:00") return
        val reqString: String = if(isPlaying){
            "{\"method\": \"slim.request\", \"params\": [\"$playerMac\", [\"pause\"]]}"
        }else{
            "{\"method\": \"slim.request\", \"params\": [\"$playerMac\", [\"play\"]]}"
        }
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        lmsApi.playPause(requestBody)
    }

    suspend fun playPause(play: Boolean){
        val playString: String = if(play){
            "play"
        }else{
            "pause"
        }
        val reqString = "{\"method\": \"slim.request\", \"params\": [\"$playerMac\", [\"$playString\"]]}"
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        lmsApi.playPause(requestBody)
        status()
    }

    suspend fun next(){
        if (playerMac == "00:00:00:00:00:00") return
        val reqString = "{\"method\": \"slim.request\", \"params\": [\"$playerMac\", [\"playlist\",\"index\",\"+1\"]]}"
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        lmsApi.next(requestBody)
    }

    suspend fun prev(){
        if (playerMac == "00:00:00:00:00:00") return
        val reqString = "{\"method\": \"slim.request\", \"params\": [\"$playerMac\", [\"playlist\",\"index\",\"-1\"]]}"
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        lmsApi.prev(requestBody)
    }

    suspend fun status() {
        val reqString = "{\"method\": \"slim.request\", \"params\": [\"$playerMac\", [\"status\", \"-\",1]]}"
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        val res = lmsApi.prev(requestBody)
        val jsonData: String? = res.body()?.string()
        val obj = JSONObject(jsonData)
        val getObject = obj.getJSONObject("result")
        val mode = getObject.getString("mode")
        //println("Player Status: $mode")
        isPlaying = mode == "play"
    }

    suspend fun update(playerMAC: String): Triple<String, String, String> {
        //Get the current playing song (returns the name and the ID)
        val reqString = "{\"method\": \"slim.request\", \"params\": [\"$playerMAC\", [\"status\", \"-\",1]]}"
        val requestBody = reqString.toRequestBody("application/json".toMediaTypeOrNull())
        val res = lmsApi.getCurrentSong(requestBody)
        val jsonData: String? = res.body()?.string()
        val obj = JSONObject(jsonData)
        val getObject = obj.getJSONObject("result")

        var title = ""
        var artist = ""
        var coverid = ""

        try {
            val resultLoop = getObject.getJSONArray("playlist_loop")
            val firstResult = resultLoop.getJSONObject(0)
            title = firstResult.getString("title")
            val id = firstResult.getString("id")

            //Take the id and get the artist (Returns the artist Name)
            val res2 = getSongInfo(playerMAC, id.toInt())
            artist = (res2.getJSONObject(2)).getString("artist")

            //take the id and get the album cover
            coverid = try {
                (res2.getJSONObject(3)).getString("coverid")
            }catch (e: JSONException){
                ""
            }
        }catch (e: JSONException){
            return Triple(title, artist, coverid)
        }

        return Triple(title, artist, coverid)
    }

}
