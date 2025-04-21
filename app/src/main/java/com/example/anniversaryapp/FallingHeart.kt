package com.example.anniversaryapp

import androidx.compose.ui.unit.Dp

data class FallingHeart(
    val id: Int,
    val startX: Float,
    val size: Dp,
    val duration: Int, // u milisekundama
    val delay: Int
)
