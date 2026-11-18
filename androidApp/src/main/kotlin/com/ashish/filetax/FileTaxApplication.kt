package com.ashish.filetax

import android.app.Application
import com.ashish.filetax.documents.data.AndroidPdfSupport
import com.ashish.filetax.onboarding.data.AndroidKeyValueStore

class FileTaxApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidKeyValueStore.install(this)
        AndroidPdfSupport.install(this)
    }
}