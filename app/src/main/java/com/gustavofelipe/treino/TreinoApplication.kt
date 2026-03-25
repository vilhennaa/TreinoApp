package com.gustavofelipe.treino

import android.app.Application
import com.gustavofelipe.treino.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TreinoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TreinoApplication)
            modules(appModule)
        }
    }
}