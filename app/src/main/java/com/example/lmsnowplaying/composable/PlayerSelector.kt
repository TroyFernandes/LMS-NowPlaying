package com.example.lmsnowplaying.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lmsnowplaying.network.logitechmediaserver.LMS

@Composable
fun PlayerSelector(pn: String){

    var _playerName by remember { mutableStateOf(pn) }
    _playerName = LMS.playerName

    //println("LMS PLAYER NAME: " + LMS.playerName)

    //modifier = Modifier.width(800.dp).padding(start = 80.dp, top = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
    Row(modifier = Modifier.width(800.dp).padding(start = 80.dp, top = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End)
    {

        Surface(color = Color.Transparent) {
            Text(text = _playerName, color = Color.LightGray, fontSize = 12.sp)
        }
        MyUI()
    }

}



