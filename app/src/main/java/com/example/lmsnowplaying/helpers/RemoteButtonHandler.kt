package com.example.lmsnowplaying.helpers

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.example.lmsnowplaying.network.logitechmediaserver.LMS


suspend fun HandleButton(keyEvent: KeyEvent){
    //println(keyEvent)
    if (keyEvent.type == KeyEventType.KeyUp){
        //whem
        if(keyEvent.key == Key.MediaFastForward){
            //println("Media Fast Forward Up")
            LMS.next()
        }else if (keyEvent.key == Key.MediaRewind){
            //println("Media Rewind Up")
            LMS.prev()
        }
        else if (keyEvent.key == Key.MediaPlayPause){
            //println("Media Plau Pause Up")
            LMS.playPause()
        }
        else if (keyEvent.key == Key.MediaPause){
            LMS.playPause(false)
        }
        else if (keyEvent.key == Key.MediaPlay){
            LMS.playPause(true)
        }
    }

}