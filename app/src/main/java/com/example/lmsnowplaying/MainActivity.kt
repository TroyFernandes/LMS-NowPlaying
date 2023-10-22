package com.example.lmsnowplaying

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.lmsnowplaying.network.jellyfin.JellyfinApi
import com.example.lmsnowplaying.network.logitechmediaserver.LMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)


        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //println("Activity back pressed invoked")
                    val activity = MainActivity()
                    activity.finish()
                    exitProcess(0)
                }
            }
        )

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {

            if(BuildConfig.LMS_URL.isNullOrEmpty()){
                ErrorScreen()
            }else{
                CoroutineScope(Dispatchers.IO).launch(){
                    LMS.getPlayers()
                }

                JellyfinApi.SetAPIToken(LocalContext.current)

                SecondaryScreen()
            }
        }
    }

}
