package com.specialprojects.seyir

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.specialprojects.seyir.databinding.ActivityAppDrawerBinding
import java.util.Locale

/** One installed app the drawer can launch. */
data class AppInfo(val label: String, val packageName: String, val icon: Drawable)

/**
 * A full-screen grid of every launchable app on the unit, sorted by name. Tap to open,
 * long-press for the app's system info screen. Columns scale with the display width so
 * the same code looks right on a 7" unit and a 12" one.
 */
class AppDrawerActivity : AppCompatActivity() {

    private lateinit var b: ActivityAppDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAppDrawerBinding.inflate(layoutInflater)
        setContentView(b.root)
        goImmersive()

        val columns = (resources.configuration.screenWidthDp / 150).coerceIn(4, 8)
        b.recycler.layoutManager = GridLayoutManager(this, columns)
        b.recycler.setHasFixedSize(true)
        b.recycler.adapter = AppListAdapter(
            loadApps(),
            onClick = ::launchApp,
            onLongClick = ::openAppInfo
        )

        b.btnBack.setOnClickListener { finish() }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) goImmersive()
    }

    private fun loadApps(): List<AppInfo> {
        val pm = packageManager
        val main = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(main, 0)
            .asSequence()
            .filter { it.activityInfo.packageName != packageName }
            .map {
                AppInfo(
                    label = it.loadLabel(pm).toString(),
                    packageName = it.activityInfo.packageName,
                    icon = it.loadIcon(pm)
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase(Locale.getDefault()) }
            .toList()
    }

    private fun launchApp(app: AppInfo) {
        val intent = packageManager.getLaunchIntentForPackage(app.packageName)
        if (intent != null) {
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        } else {
            Toast.makeText(this, R.string.no_app, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAppInfo(app: AppInfo) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${app.packageName}")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { startActivity(intent) }
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
