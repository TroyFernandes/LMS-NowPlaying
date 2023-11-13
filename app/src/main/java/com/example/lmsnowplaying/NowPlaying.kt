package com.example.lmsnowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.util.DebugLogger
import com.commit451.coiltransformations.BlurTransformation
import com.example.lmsnowplaying.composable.PlayPauseButton
import com.example.lmsnowplaying.composable.PlayerSelector
import com.example.lmsnowplaying.helpers.HandleButton
import com.example.lmsnowplaying.network.logitechmediaserver.BasicAuthInterceptor
import com.example.lmsnowplaying.network.jellyfin.JellyfinApi
import com.example.lmsnowplaying.network.logitechmediaserver.LMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


@Composable
@Preview(heightDp = 540, widthDp = 960)
fun SecondaryScreen(){
    val currentView = LocalView.current
    DisposableEffect(Unit){
        currentView.keepScreenOn = true
        onDispose {
            currentView.keepScreenOn = false
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .background(Color.Blue)
        .onKeyEvent {
            CoroutineScope(Dispatchers.IO).launch {
                HandleButton(it)
            }
            false
        }
        .fillMaxSize()) {
        PlayerScreen()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(){

    var songName by remember { mutableStateOf("Song Name") }
    var artistName by remember { mutableStateOf("Artist") }
    var artistArtUrl by remember { mutableStateOf("")}
    var artistDefaultArtUrl by remember { mutableStateOf("")}
    var albumArtUrl by remember { mutableStateOf("")}
    var playing by remember { mutableStateOf(LMS.isPlaying) }

    LaunchedEffect(Unit){
        while (true){
            //println("LMS is playing: ${LMS.isPlaying}")
            //println("Player MAC: ${LMS.playerMac}")
            //LMS.isPlaying
            if (LMS.playerMac != "00:00:00:00:00:00"){
                CoroutineScope(Dispatchers.IO).launch{
                    LMS.status()
                    playing = LMS.isPlaying
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val (_songName, _artistName, _coverId) = LMS.update(LMS.playerMac)
                    if(_songName == "") return@launch
                    songName = _songName
                    artistName = _artistName

                    if (_coverId == ""){
                        albumArtUrl = ""
                    }else{
                        albumArtUrl = "${BuildConfig.LMS_URL}/music/$_coverId/cover.jpg"
                        //println("Artist Name: $artistName")
                        var tempName = _artistName//.filter { it.isLetterOrDigit() }
                        //("Artist name after filter: $tempName")
                        tempName = tempName.replace(" ", "%20").replace("&", "%26")
                        //println("Encoded Name: $tempName")

                        artistDefaultArtUrl = "${BuildConfig.LMS_URL}/music/$_coverId/cover.jpg"
                        val jfUrl = JellyfinApi.GetArtistUrl(tempName)
                        artistArtUrl = if(jfUrl.isNullOrEmpty()){
                            "${BuildConfig.JELLYFIN_URL}/Artists/$tempName/Images/Backdrop/0"
                        }else{
                            jfUrl
                        }

                    }
                    //println("big update")
                }
            }
            delay(2000)
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()) {


        GetArtistArt(artistArtUrl, artistDefaultArtUrl){


            Row(modifier = Modifier
                //.background(Color.Green)
                .fillMaxWidth()
                .padding(start = 80.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier
                    .background(Color.Black)
                    .size(128.dp)){
                    GetAlbumArt(albumArtUrl)
                }
                Spacer(modifier = Modifier.size(24.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Spacer(modifier = Modifier.size(35.dp))
                    Text(songName, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1,
                        modifier = Modifier
                            .width(500.dp)
                            .basicMarquee()
                    )
                    Spacer(modifier = Modifier.size(10.dp))

                    Text(artistName, color = Color.White, maxLines = 1,
                            modifier = Modifier
                                .width(500.dp)
                                .basicMarquee()
                        )
                }
            }
            Spacer(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.size(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        LMS.prev()
                    }
                }) {
                    Icon(modifier = Modifier.size(32.dp), tint = Color.White, imageVector = Icons.Outlined.SkipPrevious, contentDescription = "Previous")
                }
                Spacer(modifier = Modifier.size(32.dp))
                TextButton(onClick = {

                }) {
                    PlayPauseButton(playing)
                }
                Spacer(modifier = Modifier.size(32.dp))
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        LMS.next()
                    }
                }) {
                    Icon(modifier = Modifier.size(32.dp), tint = Color.White, imageVector = Icons.Outlined.SkipNext, contentDescription = "Next")
                }
            }

            Spacer(modifier = Modifier.size(64.dp))

        }

    }

}


@Composable
fun GetAlbumArt(url: String){

    var _url by remember { mutableStateOf(url) }
    _url = url

    val imageLoaderLMS = ImageLoader.Builder(LocalContext.current)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(BuildConfig.LMS_USERNAME,BuildConfig.LMS_PASSWORD))
                .build()
        }
        //(DebugLogger())
        .build()

    val imageRequestLMS = ImageRequest.Builder(LocalContext.current)
        .data(_url)
        .crossfade(500)
        .build()

    AsyncImage(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
        imageLoader = imageLoaderLMS,
        model = imageRequestLMS,
        contentDescription = "Album Art",
    )
}
@Composable
fun GetArtistArt(
    url: String,
    defaultUrl: String,
    content: @Composable () -> Unit
) {

    var _url by remember { mutableStateOf(url) }
    var _defaultUrl by remember { mutableStateOf(defaultUrl) }
    var _pn by remember { mutableStateOf(LMS.playerName) }
    _pn = LMS.playerName
    _url = url
    _defaultUrl = defaultUrl

    val contrast = 0.40f // 0f..10f (1 should be default)
    val brightness = -10f // -255f..255f (0 should be default)
    val colorMatrix = floatArrayOf(
        contrast, 0f, 0f, 0f, brightness,
        0f, contrast, 0f, 0f, brightness,
        0f, 0f, contrast, 0f, brightness,
        0f, 0f, 0f, 1f, 0f
    )

    val imageLoaderLMS = ImageLoader.Builder(LocalContext.current)
        .okHttpClient {
            OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor(BuildConfig.LMS_USERNAME,BuildConfig.LMS_PASSWORD))
                .build()
        }
        //.logger(DebugLogger())
        .build()

    val imageLoaderJellyfin: ImageLoader

    if (!JellyfinApi.APITOKEN.isNullOrEmpty()){
        imageLoaderJellyfin = ImageLoader.Builder(LocalContext.current)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor{chain ->
                        val original = chain.request()
                        val requestBuilder = original.newBuilder()
                            .header("Authorization", "MediaBrowser Client=\"LMS Jellyfin\", Device=\"my-script\", DeviceId=\"0.0.0\", Version=\"0.0.0\", Token=\"${JellyfinApi.APITOKEN}\"")
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }
                    .build()
            }
            //.logger(DebugLogger())
            .build()
    }else{
        imageLoaderJellyfin = ImageLoader.Builder(LocalContext.current)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor(BasicAuthInterceptor(BuildConfig.LMS_USERNAME,BuildConfig.LMS_PASSWORD))
                    .build()
            }
            //.logger(DebugLogger())
            .build()
    }


    val imageRequestLMS = ImageRequest.Builder(LocalContext.current)
        .data(defaultUrl)
        .crossfade(500)
        .transformations(BlurTransformation(LocalContext.current,25f,1f))
        .build()


    val imageRequestJellyfin: ImageRequest = if (JellyfinApi.APITOKEN.isNullOrEmpty()){
        ImageRequest.Builder(LocalContext.current)
            .data(defaultUrl)
            .crossfade(500)
            .transformations(BlurTransformation(LocalContext.current,20f,1f))
            .build()
    }else{
        ImageRequest.Builder(LocalContext.current)
            .data(_url)
            .crossfade(500)
            //.transformations(BlurTransformation(LocalContext.current,2f,1f))
            .build()

    }

    val defaultPainter = rememberAsyncImagePainter(
        imageLoader = imageLoaderLMS,
        model = imageRequestLMS,
        contentScale = ContentScale.FillBounds,
    )

    val painter = rememberAsyncImagePainter(
        imageLoader = imageLoaderJellyfin,
        model = imageRequestJellyfin,
        contentScale = ContentScale.FillBounds,
        error = defaultPainter
    )


    Column(modifier = Modifier
        .paint(
            painter,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
        ),horizontalAlignment = Alignment.CenterHorizontally){

        PlayerSelector(_pn)

        Spacer(modifier = Modifier
            .fillMaxWidth()
            //.width(800.dp)
            //.height(1.dp)
            //.background(Color.Red)
            .weight(1f)
        )
        content()
    }

}
