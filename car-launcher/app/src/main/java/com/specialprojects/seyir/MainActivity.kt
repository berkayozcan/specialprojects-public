package com.specialprojects.seyir

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.specialprojects.seyir.databinding.ActivityMainBinding

/**
 * The car home screen. A big clock, a greeting, and a grid of large touch tiles that
 * launch the driving essentials. Everything the driver reaches for is one tap away and
 * sized for a glance, not a careful aim.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        goImmersive()

        b.tileNavigation.setOnClickListener { Launch.navigation(this) }
        b.tileMusic.setOnClickListener { Launch.music(this) }
        b.tilePhone.setOnClickListener { Launch.phone(this) }
        b.tileRadio.setOnClickListener { Launch.radio(this) }
        b.tileBluetooth.setOnClickListener { Launch.bluetoothSettings(this) }
        b.tileCamera.setOnClickListener { Launch.camera(this) }
        b.tileApps.setOnClickListener { startActivity(Intent(this, AppDrawerActivity::class.java)) }
        b.tileSettings.setOnClickListener { Launch.settings(this) }

        b.btnDefault.setOnClickListener { Launch.openHomeSettings(this) }

        // On the home screen, Back should keep the driver on the home screen.
        onBackPressedDispatcher.addCallback(this) { /* stay home */ }
    }

    override fun onResume() {
        super.onResume()
        // Offer the "make default" shortcut only while another launcher is in charge.
        b.btnDefault.visibility = if (Launch.isDefaultLauncher(this)) View.GONE else View.VISIBLE
        goImmersive()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) goImmersive()
    }

    private fun goImmersive() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, b.root).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
