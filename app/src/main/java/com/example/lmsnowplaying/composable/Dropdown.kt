package com.example.lmsnowplaying.composable

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lmsnowplaying.network.logitechmediaserver.LMS

@Preview
@Composable
fun MyUI() {

    val data = remember { mutableStateListOf<Pair<String, String>>() }
    data.clear()
    data.addAll(LMS.players)


    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        // 3 vertical dots icon
        IconButton(onClick = {
            expanded = true
        }, modifier = Modifier.padding(top = 2.dp)
            ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Open Options",
                tint = Color.LightGray,
                modifier = Modifier.size(15.dp)

            )
        }

        // drop down menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            // adding items

            data.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(text =  {
                    Text(text = itemValue.first)
                },
                    onClick = {
                        Toast.makeText(contextForToast, " Connected to: ${itemValue.first}", Toast.LENGTH_SHORT).show()
                        LMS.setPlayer(itemValue.first, itemValue.second)
                        expanded = false
                    }
                )
            }
        }
    }
}