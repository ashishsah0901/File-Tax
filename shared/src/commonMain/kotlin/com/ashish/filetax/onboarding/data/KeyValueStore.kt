package com.ashish.filetax.onboarding.data

interface KeyValueStore {
    fun read(key: String): String?

    fun write(key: String, value: String)

    fun remove(key: String)
}

expect fun keyValueStore(): KeyValueStore