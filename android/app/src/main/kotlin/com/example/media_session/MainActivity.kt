package com.example.media_session

import android.media.AudioManager
import android.view.KeyEvent
import androidx.core.content.ContextCompat.getSystemService
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodChannel

class FlutterMediaSessionManagerPlugin: FlutterPlugin {
    private val channelID = "com.example.media_session/session"
    private lateinit var audioManager: AudioManager
    private var appBinding:  FlutterPlugin.FlutterPluginBinding? = null

    private val isReady: Boolean
        get() = ::audioManager.isInitialized

    private val isMusicActive: Boolean
        get() {
            if (!isReady) {
                throw IllegalStateException("AudioManager is not initialized")
            }

            return audioManager.isMusicActive
        }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        appBinding = binding
        MethodChannel(appBinding!!.binaryMessenger, channelID).setMethodCallHandler { call, result ->
            handleMethodChannelCommand(call.method, result)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        appBinding = null
    }

    private fun handleMethodChannelCommand(command: String, result: MethodChannel.Result) {
        when (command) {
            "initialize" -> {
                result.success(initialize())
            }
            "pausePlayback" -> {
                result.success(pausePlayback())
            }
            "resumePlayback" -> {
                result.success(resumePlayback())
            }
            "isReady" -> {
                result.success(isReady)
            }
            "isMusicActive" -> {
                result.success(isMusicActive)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    private fun initialize(): Boolean {
        if (isReady) {
            return true
        }

        audioManager = getSystemService(appBinding!!.applicationContext, AudioManager::class.java) as AudioManager

        return isReady
    }

    private fun pausePlayback(): Boolean {
        if (!isReady || !isMusicActive) {
            return false
        }

        audioManager.dispatchMediaKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE)
        )
        audioManager.dispatchMediaKeyEvent(
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE)
        )

        return true
    }

    private fun resumePlayback(): Boolean {
        if (!isReady) {
            return false
        }

        audioManager.dispatchMediaKeyEvent(
            KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
        )
        audioManager.dispatchMediaKeyEvent(
            KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY)
        )

        return true
    }
}
