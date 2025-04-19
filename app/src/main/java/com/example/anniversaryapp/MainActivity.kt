package com.example.anniversaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.anniversaryapp.ui.theme.AnniversaryAppTheme
import com.example.anniversaryapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnniversaryAppTheme {
                AnniversaryApp()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Tijana",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}

@Composable
fun AnniversaryApp(){

    val scale = remember { Animatable(1f) }
    var clickCount by remember { mutableStateOf(0) }
    val haptic = LocalHapticFeedback.current

    //Animacija kao bounce
    LaunchedEffect(clickCount) {
        scale.animateTo(
            1.2f,
            animationSpec = tween(500)
        )
        scale.animateTo(
            1f,
            animationSpec = tween(500)
        )
    }

    val maxClicks = 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Klikni na srce...",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 20.sp,
            color = Color.DarkGray
        )

        Image(
            painter = painterResource(id = R.drawable.pixel_heart),
            contentDescription = "Pixel Heart",
            modifier = Modifier
                .size(512.dp)
                .scale(scale.value)
                .clickable {
                    clickCount++
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                }
        )

        //Spacer(modifier = Modifier.height(16.dp))

        if (clickCount >= maxClicks) {
            Text(
                text = "4 klika za 4. godišnjicu!\nVojim te 🐻 <3 🐸",
                color = Color.hsl(300f, 0.6f, 0.7f),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AnniversaryAppTheme {
//        Greeting("Android")
//    }
//}