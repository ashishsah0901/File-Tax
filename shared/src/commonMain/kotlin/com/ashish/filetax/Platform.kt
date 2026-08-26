package com.ashish.filetax

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform