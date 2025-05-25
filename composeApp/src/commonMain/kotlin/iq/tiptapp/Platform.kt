package iq.tiptapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform