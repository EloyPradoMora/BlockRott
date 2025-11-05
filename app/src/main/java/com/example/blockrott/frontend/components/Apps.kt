package com.example.blockrott.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.blockrott.frontend.theme.ComponentBackground
import com.example.blockrott.frontend.theme.ComponentSurface
import com.example.blockrott.frontend.theme.PrimaryColor

@Composable
fun UsedApps(
    appName: String,
    usageTime: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(ComponentBackground)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = appName,
            modifier = Modifier.weight(1f),
            fontSize = 18.sp
        )
        Text(
            text = usageTime,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
    }
}

@Composable
fun BlockApps(
    appName: String,
    initialChecked: Boolean
) {
    var isBlocked by remember { mutableStateOf(initialChecked) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(ComponentSurface)
            .border(
                BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = appName,
            fontSize = 22.sp,
        )
        Spacer(Modifier.weight(1f))
        SwitchBlock(
            checked = isBlocked,
            onCheckedChange = { newValue ->
                isBlocked = newValue
            }
        )
    }
}