package com.example.lmsnowplaying.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayCircleFilled
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import com.example.lmsnowplaying.network.logitechmediaserver.LMS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayPauseButton(playing: Boolean){

    var _isPlaying by remember {
        mutableStateOf(playing)
    }
    _isPlaying = playing
    //println("PlayPauseButton is playing: $_isPlaying")

    IconToggleButton(
        checked = _isPlaying,
        modifier = Modifier,
        onCheckedChange = { _checked1 ->
            if(LMS.playerMac != "00:00:00:00:00:00"){
                CoroutineScope(Dispatchers.IO).launch(){
                    LMS.playPause(_checked1)
                }
                _isPlaying = _checked1
                CoroutineScope(Dispatchers.IO).launch(){
                    LMS.status()
                }
            }

        }
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            tint = Color.White,
            imageVector = if (_isPlaying) Icons.Outlined.Pause else Icons.Outlined.PlayCircleFilled,
            contentDescription = "Play Pause Button",

        )
    }
}
