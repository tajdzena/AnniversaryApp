package com.example.anniversaryapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import kotlin.random.Random
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.decode.GifDecoder
import coil.ImageLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnniversaryAppTheme {
                AnniversaryApp()
            }
        }
    }
}


//Pokretanje svih funkcionalnosti
@Composable
fun AnniversaryApp(){
    var inGame by remember { mutableStateOf(false) }
    var showTimeScreen by remember { mutableStateOf(false) }

//    if (inGame) {
//        MiniGameScreen(onExit = { inGame = false })
//    } else {
//        HomeScreen(onStartGame = { inGame = true })
//    }

    when {
        inGame -> MiniGameScreen(onExit = { inGame = false })
        showTimeScreen -> TimeScreen(onExit = { showTimeScreen = false })
        else -> HomeScreen(
            onStartGame = { inGame = true },
            onShowTime = { showTimeScreen = true }
        )
    }


}


@Composable
fun HomeScreen(onStartGame: () -> Unit, onShowTime: () -> Unit){
    val backgroundColor = MaterialTheme.colorScheme.background
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.DarkGray

    val scale = remember { Animatable(1f) }
    var clickCount by remember { mutableStateOf(0) }
    val haptic = LocalHapticFeedback.current

    //Animacija kao bounce pri kliku
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

    val maxClicks = 4 //Do prikaza poruke

    //Konfete
    var showConfetti by remember { mutableStateOf(false) }
    var showPurpleConfetti by remember { mutableStateOf(false) }

    //Pulsiranje srca pre prvog klika i tokom neaktivnosti
    var isIdle by remember { mutableStateOf(true) }
    var lastClickTime by remember { mutableStateOf(System.currentTimeMillis()) }

    //Detekcija neaktivnosti
    LaunchedEffect(lastClickTime) {
        delay(3500)
        isIdle = true
    }

    //Pulsiranje tokom neaktivnosti
    LaunchedEffect(isIdle) {
        if (isIdle) {
            while (isIdle) {
                scale.animateTo(1.05f, animationSpec = tween(1000))
                scale.animateTo(1f, animationSpec = tween(1000))
            }
        }
    }


    // Glavni sadrzaj
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)  //Pozadina koja se menja u zavisnosti od teme
                .padding(32.dp),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Klikni na srce...",
                fontSize = 20.sp,
                color = textColor
            )

            //Slika i provera klikova
            Image(
                painter = painterResource(id = R.drawable.pixel_heart),
                contentDescription = "Pixel Heart",
                modifier = Modifier
                    .size(512.dp)
                    .scale(scale.value)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                lastClickTime = System.currentTimeMillis()
                                isIdle = false
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

            //Prikaz teksta nakon 4 klika
            if (clickCount == maxClicks) {
                Text(
                    text = "4 klika -> 4 godine ljubavi!\nVojim te 🐻 Ɛ> 🐸",
                    color = Color.hsl(300f, 0.6f, 0.7f),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                showConfetti = true
            }

            //Dugmici do igrice i merenja vremena
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onStartGame,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xED3B124D)
                    )
                ) {
                    Text("Mini igica", fontFamily = InterFont, color = Color.White)
                }
                Button(
                    onClick = onShowTime,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xED3B124D))
                ) {
                    Text("Naš dan", fontFamily = InterFont, color = Color.White)
                }
            }

        }

        //Ako postane true jedno od ovo dva stanja za konfete
        if (showConfetti || showPurpleConfetti) {
            KonfettiView(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f), //Da krenu sa samog vrha stranice
                parties = listOf(
                    Party(
                        emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(100),
                        position = Position.Relative(0.5, 0.0),
                        spread = 360,
                        speed = 30f,
                        shapes = listOf(Shape.Circle, Shape.Square),
                        colors = if (showPurpleConfetti) { //Razlika samo u bojama, da li ce biti sareniji ili ljubicasti
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

            //Reset konfeta posle 5 sekundi
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

    //Animacija pri klikovima ista kao i na pocetnom ekranu
    val scale = remember { Animatable(1f) }

    //Trenutni poeni i stanje da li je igrica u toku
    var score by remember { mutableStateOf(0) }
    var gameRunning by remember { mutableStateOf(false) }

    //Nivoi
    val durations = listOf(5, 10, 20)
    var selectedDuration by remember { mutableStateOf(5) } //Default je 5, pre nego sto bilo koji nivo odaberemo klikom

    var timeLeft by remember { mutableStateOf(selectedDuration) } //U zavisnosti od nivoa preostalo vreme

    //Cuvanje high scores za svaki nivo
    val context = LocalContext.current
    val highScoreManager = remember { HighScoreManager(context) }
    var highScore by remember { mutableStateOf(highScoreManager.getHighScore(selectedDuration)) }

    LaunchedEffect(gameRunning) {
        if (gameRunning) {
            for (i in selectedDuration downTo 1) {
                timeLeft = i
                delay(1000) //Odbrojavanje sekund po sekund
            }
            gameRunning = false //Kada nismo u petlji (isteklo vreme, pre pokretanja nivoa, klikom na dugme "Nazad")

            if (score > highScore) {
                highScoreManager.saveHighScore(selectedDuration, score)
                highScore = score

                Toast.makeText(context, "Jupi! 🎉 Novi rekord: $score", Toast.LENGTH_LONG).show()
            } else {
                highScoreManager.saveHighScore(selectedDuration, score)
                highScore = highScoreManager.getHighScore(selectedDuration) //Dohvatamo high score za taj nivo
            }
        }
    }

    LaunchedEffect(score) { //Animacija srca tokom igranja (za svaki klik -> podize se score)
        scale.animateTo(1.2f, tween(100))
        scale.animateTo(1f, tween(100))
    }

    LaunchedEffect(selectedDuration) { //Kada kliknemo na jedan od nivoa, ucitava se high score bas za taj
        highScore = highScoreManager.getHighScore(selectedDuration)
    }


    //Vizuelni sadrzaj
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(32.dp),
        verticalArrangement = Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Dugmici za nivoe poredjani u redu
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //Dohvatamo iz liste brojeve i pravimo nivoe za svaki od njih dinamicki
            durations.forEach { duration ->
                Button(
                    onClick = { selectedDuration = duration },
                    enabled = !gameRunning, //Enableovano kliktanje na sve nivoe pre pocetka igre, disable sve dugmice dok jedan nivo traje
                    colors = ButtonDefaults.buttonColors(
                        containerColor = when {
                            selectedDuration == duration -> Color(0xED3B124D) //Boja za selektovan nivo
                            else -> Color.Gray // Ostali
                        },
                        disabledContainerColor = when {
                            gameRunning && duration == selectedDuration -> Color(0xED3B124D) //Odabran nivo da izgleda drugacije kad je disabled, da se zna koji je odabran
                            else -> Color.Gray
                        }
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {//Naziv nivoa
                    Text("${duration}s", color = Color.White, fontFamily = InterFont)
                }
            }
        }

        //Razmak
        Spacer(modifier = Modifier.height(40.dp))

        //Tekst pre i tokom igrice (opis, odbrajanje, broj klikova)
        if (!gameRunning) {
            Text(
                "Klikni što više puta\n za odabrano vreme!",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = textColor
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Najveći broj klikova: $highScore\n",
                    fontSize = 20.sp,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Red od "dva teksta" iako je jedan u pitanju zbog posebne stilizacije
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Preostalo vreme: ",
                        fontSize = 20.sp,
                        color = textColor
                    )
                    Text(
                        text = "${timeLeft}s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFFFF9800)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Broj klikova: ",
                        fontSize = 20.sp,
                        color = textColor
                    )

                    Text(
                        text = "$score",
                        fontSize = 20.sp,
                        color = Color(0xFFFF69B4) // Roze
                    )
                }
            }
        }

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
                                timeLeft = selectedDuration //Pocetak odbrajanja
                                gameRunning = true
                            } else {
                                score++
                            }
                        }
                    )
                }
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



@Composable
fun TimeScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    val now = remember { java.time.LocalDate.now() }
    val startDate = java.time.LocalDate.of(2021, 4, 22)
    val period = java.time.Period.between(startDate, now)

    //Odredjivanje gramatickog oblika reci (godine/godina ...)
    fun getCorrectWord(value: Int, type: String): String {
        val lastDigit = value % 10
        return when (type) {
            "year" -> when {
                lastDigit in 1..4 -> "godine"
                else -> "godina"
            }
            "month" -> when {
                lastDigit in 1..4 && value !in 11..12 -> "meseca"
                else -> "meseci"
            }
            "day" -> when {
                lastDigit == 1 -> "dan"
                else -> "dana"
            }
            else -> ""
        }
    }

    //Ucitavanje gif-a
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()


    //Glavni sadrzaj
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Center
        ) {

            Spacer(modifier = Modifier.height(92.dp))

            //Prozorcic, kao widget da izgleda racunanje vremena
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xED3B124D),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(R.drawable.godisnjica3)
                            .build(),
                        contentDescription = "Godisnjica3 gif",
                        imageLoader = imageLoader,
                        modifier = Modifier
                            .size(330.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp)) //manji razmak

                    Text(
                        "Upustili smo se u avanturu pre:",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TimeColumn(period.years, getCorrectWord(period.years, "year"))
                        TimeColumn(period.months, getCorrectWord(period.months, "month"))
                        TimeColumn(period.days, getCorrectWord(period.days, "day"))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(102.dp))

            Button(
                onClick = onExit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xED3B124D)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Nazad", color = Color.White, fontFamily = InterFont)
            }
        }
    }


}


@Composable
fun TimeColumn(value: Int, label: String) {
    //Izgled poput:
    //    3     11    30
    // godine meseci dana

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            fontSize = 38.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}
