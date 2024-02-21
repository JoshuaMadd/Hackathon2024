package com.joshuamaddelein.hackathon2024

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.SavedStateHandle
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

class IdQrService : LifecycleService(), SavedStateRegistryOwner {
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    private lateinit var contentView: View

    override fun onCreate() {
        super.onCreate()
        // init your SavedStateRegistryController
        savedStateRegistryController.performAttach() // you can ignore this line, because performRestore method will auto call performAttach() first.
        savedStateRegistryController.performRestore(null)

        // configure your ComposeView
        contentView = ComposeView(this).apply {
            setViewTreeSavedStateRegistryOwner(this@IdQrService)
            setContent {
                IdQrScreen()
            }
        }
        //ViewTreeLifecycleOwner.set(contentView, this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        // remove your view from your windowManager
    }

    // override savedStateRegistry property from SavedStateRegistryOwner interface.
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    @Composable
    fun IdQrScreen(modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.qrcode_me),
                contentDescription = "Id qr",
                modifier = modifier.size(300.dp)
            )
        }
    }
}