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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import com.example.anniversaryapp.ui.theme.InterFont
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import kotlin.random.Random

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
    val backgroundColor = MaterialTheme.colorScheme.background
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.DarkGray

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

    //Konfete
    var showConfetti by remember { mutableStateOf(false) }
    var showPurpleConfetti by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Glavni sadrzaj
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)  // Pozadina koja se menja u zavisnosti od teme
                .padding(32.dp),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Klikni na srce...",
                fontSize = 20.sp,
                color = textColor //Podesiti za dark theme?
            )

            Image(
                painter = painterResource(id = R.drawable.pixel_heart),
                contentDescription = "Pixel Heart",
                modifier = Modifier
                    .size(512.dp)
                    .scale(scale.value)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                clickCount++
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onLongPress = {
                                showPurpleConfetti = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        )
                    }
            )

            if (clickCount == maxClicks) {
                Text(
                    text = "4 klika za 4 godine ljubavi!\nVojim te 🐻 <3 🐸",
                    color = Color.hsl(300f, 0.6f, 0.7f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                showConfetti = true
            }

            Button(onClick = onStartGame,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xED3B124D)
                )) {
                Text("Mini igrica", fontFamily = InterFont, color = Color.White)
            }
        }

        if (showConfetti || showPurpleConfetti) {
            KonfettiView(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                parties = listOf(
                    Party(
                        emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(100),
                        position = Position.Relative(0.5, 0.0),
                        spread = 360,
                        speed = 30f,
                        shapes = listOf(Shape.Circle, Shape.Square),
                        colors = if (showPurpleConfetti) {
                            listOf(
                                0xFFB6A0FF.toInt(),
                                0xFF957DAD.toInt(),
                                0xFFB48DEF.toInt(),
                                0xFF9C27B0.toInt(),
                                0xFFCE93D8.toInt()
                            )
                        } else {
                            listOf(
                                0xfce18a.toInt(),
                                0xff726d.toInt(),
                                0xf4306d.toInt(),
                                0xb48def.toInt()
                            )
                        },
                        size = listOf(Size.SMALL, Size.LARGE)
                    )
                )
            )
            // Reset posle 3 sekunde
            LaunchedEffect(showConfetti, showPurpleConfetti) {
                delay(5000)
                showConfetti = false
                showPurpleConfetti = false
            }
        }
    }
}





@Composable
fun MiniGameScreen(onExit: () -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.background
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.DarkGray

    val scale = remember { Animatable(1f) }
    var score by remember { mutableStateOf(0) }
    var gameRunning by remember { mutableStateOf(false) }

    //Nivoi
    //Napomena - namestiti da dok traje odbrojavanje i igranje jednog nivoa, ostala dva dugmeta treba da postanu disabled
    val durations = listOf(5, 10, 20)
    var selectedDuration by remember { mutableStateOf(5) }

    var timeLeft by remember { mutableStateOf(selectedDuration) } //U zavisnosti od nivoa


    //Cuvanje high scores za svaki nivo
    val context = LocalContext.current
    val highScoreManager = remember { HighScoreManager(context) }
    var highScore by remember { mutableStateOf(highScoreManager.getHighScore(selectedDuration)) }

    LaunchedEffect(gameRunning) {
        if (gameRunning) {
            for (i in selectedDuration downTo 1) {
                timeLeft = i
                delay(1000)
            }
            gameRunning = false
            highScoreManager.saveHighScore(selectedDuration, score)
            highScore = highScoreManager.getHighScore(selectedDuration)
        }
    }

    LaunchedEffect(score) {
        scale.animateTo(1.2f, tween(100))
        scale.animateTo(1f, tween(100))
    }

    LaunchedEffect(selectedDuration) {
        highScore = highScoreManager.getHighScore(selectedDuration)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                    enabled = !gameRunning, //disable ostala dva dugmica dok jedan nivo traje
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            selectedDuration == duration -> Color(0xED3B124D) // Boja za selektovan nivo kad nije igra
                            else -> Color.Gray // Ostali
                        },
                        disabledContainerColor = when {
                            gameRunning && duration == selectedDuration -> Color(0xED3B124D) //aktivni nivo da izgleda isto i kad je disabled - (custom boja kasnije - Color(0xFFB00020) hex kod)
                            else -> Color.Gray
                        }
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text("${duration}s", color = Color.White, fontFamily = InterFont)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))


        //Izmenjena logika - klik na srce pokrece igricu, a ne na dugme
        Text(
            if (!gameRunning) "Klikni što više puta\n za odabrano vreme!"
            else "Najveći broj klikova: $highScore\n\nPreostalo vreme: ${timeLeft}s",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = textColor //Podesiti da tekst bude bele boje u dark modu
        )

        if (gameRunning) {
            Text("Broj klikova: $score", fontSize = 20.sp, color = textColor)
        }

        //Spacer(modifier = Modifier.height(6.dp))

        Image(
            painter = painterResource(id = R.drawable.pixel_heart),
            contentDescription = "Pixel Heart",
            modifier = Modifier
                .size(512.dp)
                .scale(scale.value)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (!gameRunning) {
                                score = 0
                                timeLeft = selectedDuration
                                gameRunning = true
                            } else {
                                score++
                            }
                        }
                    )
                }
//                .clickable {
//                    if (!gameRunning) {
//                        score = 0
//                        timeLeft = selectedDuration
//                        gameRunning = true
//                    } else {
//                        score++
//                    }
//                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExit,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xED3B124D)
            )
        ) {
            Text("Nazad", color = Color.White, fontFamily = InterFont)
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