package com.example.gusty.utilities

import androidx.compose.ui.graphics.Color

object BackGrounds {
    private var firstBackGround : Color = Color.Black
    private var secondBackGround : Color = Color.Black
    fun setFirstBackGround(backGround : Color){
        firstBackGround = backGround
    }
    fun getFirstBackGround() : Color {
        return firstBackGround
    }
    fun setSecondBackGround(backGround : Color){
        secondBackGround = backGround
    }
    fun getSecondBackGround() : Color {
        return secondBackGround
    }
}