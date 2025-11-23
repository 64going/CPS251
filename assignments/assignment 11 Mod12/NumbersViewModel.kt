package com.example.test

import androidx.lifecycle.ViewModel
import kotlin.math.abs

class NumbersViewModel : ViewModel() {

    val numbers = listOf(1,2,3,4,5,6,7,8,0)

    fun randomizeNumbers(): List<Int>{
        return numbers.shuffled()
    }
    fun isGameWon(tiles :List<Int>): Boolean{

        return tiles == numbers
    }
   fun isAdjacent(index1: Int, index2: Int, gridSize: Int): Boolean {
        val row1 = index1 / gridSize
        val col1 = index1 % gridSize
        val row2 = index2 / gridSize
        val col2 = index2 % gridSize

        return (row1 == row2 && abs(col1 - col2) == 1) ||
                (col1 == col2 && abs(row1 - row2) == 1)
    }
}