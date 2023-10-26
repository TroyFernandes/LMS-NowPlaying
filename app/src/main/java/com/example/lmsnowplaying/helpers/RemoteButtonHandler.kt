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
        when(keyEvent.key){
            Key.MediaFastForward -> LMS.next()
            Key.MediaRewind -> LMS.prev()
            Key.MediaPlayPause -> LMS.playPause()
            Key.MediaPause -> LMS.playPause(false)
            Key.MediaPlay -> LMS.playPause(true)
        }
    }
}