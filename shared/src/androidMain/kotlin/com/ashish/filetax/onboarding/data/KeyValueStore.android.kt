package com.ashish.filetax.onboarding.data

import android.content.Context
import android.content.SharedPreferences

object AndroidKeyValueStore {

    private var preferences: SharedPreferences? = null

    fun install(context: Context) {
        preferences = context.applicationContext
            .getSharedPreferences("filetax_answers", Context.MODE_PRIVATE)
    }

    internal fun preferencesOrNull(): SharedPreferences? = preferences
}

private class SharedPreferencesStore(private val preferences: SharedPreferences) : KeyValueStore {

    override fun read(key: String): String? = preferences.getString(key, null)

    override fun write(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    override fun remove(key: String) {
        preferences.edit().remove(key).apply()
    }
}

actual fun keyValueStore(): KeyValueStore =
    AndroidKeyValueStore.preferencesOrNull()?.let(::SharedPreferencesStore) ?: InMemoryKeyValueStore()