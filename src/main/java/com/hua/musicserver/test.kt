package com.hua.musicserver

import kotlin.concurrent.thread

class test {
}

fun main() {
    val regex = Regex(pattern = "^[\\u4e00-\\u9fa5_a-zA-Z0-9-]{2,16}\$")
    while (true){
        val line = readLine()
        println(regex.containsMatchIn(line.toString()))
    }
}