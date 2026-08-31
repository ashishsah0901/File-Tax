package com.ashish.filetax.onboarding.data

import platform.Foundation.NSUserDefaults

private class UserDefaultsStore(private val defaults: NSUserDefaults) : KeyValueStore {

    override fun read(key: String): String? = defaults.stringForKey(key)

    override fun write(key: String, value: String) {
        defaults.setObject(value, key)
    }

    override fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }
}

actual fun keyValueStore(): KeyValueStore = UserDefaultsStore(NSUserDefaults.standardUserDefaults)