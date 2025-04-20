package com.example.anniversaryapp

import android.content.Context
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
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

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

    var inGame by remember { mutableStateOf(false) }

    if (inGame) {
        MiniGameScreen(onExit = { inGame = false })
    } else {
        HomeScreen(onStartGame = { inGame = true })
    }


}

@Composable
fun HomeScreen(onStartGame: () -> Unit){
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

        if (clickCount >= maxClicks) { //Dodati konfeti efekat, i videti za long press
            Text(
                text = "4 klika za 4 godine ljubavi!\nVojim te 🐻 <3 🐸", //Dodati animaciju pri pojavljivanju teksta
                color = Color.hsl(300f, 0.6f, 0.7f),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp)) // da se napravi razmak od dugmeta mini igrica
        }

        Button(onClick = onStartGame) {
            Text("Mini igrica")
        }
    }
}


@Composable
fun MiniGameScreen(onExit: () -> Unit) {
    val scale = remember { Animatable(1f) }
    var score by remember { mutableStateOf(0) }
    var gameRunning by remember { mutableStateOf(false) }

    //Nivoi
    //Napomena - namestiti da dok traje odbrojavanje i igranje jednog nivoa, ostala dva dugmeta treba da postanu disabled
    val durations = listOf(5, 15, 30)
    var selectedDuration by remember { mutableStateOf(5) }

    var timeLeft by remember { mutableStateOf(selectedDuration) } //U zavisnosti od nivoa


    //Cuvanje high scores za svaki nivo
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
    val highScoreKey = "high_score_${selectedDuration}s"
    var highScore by remember { mutableStateOf(prefs.getInt(highScoreKey, 0)) }


    LaunchedEffect(gameRunning) {
        if (gameRunning) {
            for (i in selectedDuration downTo 0) {
                timeLeft = i
                delay(1000)
            }
            gameRunning = false
        }
    }

    LaunchedEffect(score) {
        scale.animateTo(1.2f, tween(100))
        scale.animateTo(1f, tween(100))
    }


    LaunchedEffect(!gameRunning && score > 0) {
        if (score > highScore) {
            highScore = score
            prefs.edit().putInt(highScoreKey, score).apply()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            durations.forEach { duration ->
                Button(
                    onClick = { selectedDuration = duration },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedDuration == duration) MaterialTheme.colorScheme.primary else Color.LightGray
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text("${duration}s")
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))


        //Izmenjena logika - klik na srce pokrece igricu, a ne na dugme
        Text(
            if (!gameRunning) "Klikni što više puta\n za odabrano vreme!"
            else "Najveći broj klikova: $highScore\n\nPreostalo vreme: ${timeLeft}s",
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        if (gameRunning) {
            Text("Broj klikova: $score", fontSize = 20.sp)
        }

        //Spacer(modifier = Modifier.height(6.dp))

        Image(
            painter = painterResource(id = R.drawable.pixel_heart),
            contentDescription = "Pixel Heart",
            modifier = Modifier
                .size(512.dp)
                .scale(scale.value)
                .clickable {
                    if (!gameRunning) {
                        score = 0
                        timeLeft = selectedDuration
                        gameRunning = true
                    } else {
                        score++
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onExit) {
            Text("Nazad")
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