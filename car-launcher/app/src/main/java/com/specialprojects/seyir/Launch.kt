package com.specialprojects.seyir

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast

/**
 * Intent helpers for the home tiles.
 *
 * Every launch tries a short, ordered list of candidates and starts the first one that
 * resolves — an implicit intent first (so the user's own default app wins), then the
 * launch intents of a few packages common on Android head units. If nothing handles it,
 * the user sees a single, plain message instead of a crash.
 */
object Launch {

    fun navigation(ctx: Context) = tryStart(
        ctx,
        buildList {
            // Opens the user's default maps/navigation app at their location.
            add(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=")))
            add(Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=")))
            addAll(
                launchIntents(
                    ctx,
                    "com.google.android.apps.maps",
                    "com.waze",
                    "ru.yandex.yandexnavi",
                    "com.yandex.yandexmaps",
                    "com.sygic.aura",
                    "com.tomtom.gplay.navapp"
                )
            )
        }
    )

    fun music(ctx: Context) = tryStart(
        ctx,
        buildList {
            add(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MUSIC))
            addAll(
                launchIntents(
                    ctx,
                    "com.spotify.music",
                    "com.google.android.apps.youtube.music",
                    "com.google.android.music",
                    "com.android.music"
                )
            )
        }
    )

    fun phone(ctx: Context) = tryStart(ctx, listOf(Intent(Intent.ACTION_DIAL)))

    fun radio(ctx: Context) = tryStart(
        ctx,
        launchIntents(
            ctx,
            "com.android.fmradio",
            "com.mediatek.fmradio",
            "com.caf.fmradio",
            "com.quicinc.fmradio",
            "com.spreadtrum.fmradio"
        )
    )

    fun camera(ctx: Context) = tryStart(
        ctx,
        listOf(Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA))
    )

    fun bluetoothSettings(ctx: Context) = tryStart(
        ctx,
        listOf(Intent(Settings.ACTION_BLUETOOTH_SETTINGS), Intent(Settings.ACTION_SETTINGS))
    )

    fun settings(ctx: Context) = tryStart(ctx, listOf(Intent(Settings.ACTION_SETTINGS)))

    /** Opens the system screen where the user picks their default home app. */
    fun openHomeSettings(ctx: Context) = tryStart(
        ctx,
        listOf(Intent(Settings.ACTION_HOME_SETTINGS), Intent(Settings.ACTION_SETTINGS))
    )

    fun isDefaultLauncher(ctx: Context): Boolean {
        val home = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val res = ctx.packageManager.resolveActivity(home, PackageManager.MATCH_DEFAULT_ONLY)
        return res?.activityInfo?.packageName == ctx.packageName
    }

    private fun launchIntents(ctx: Context, vararg packages: String): List<Intent> =
        packages.mapNotNull { ctx.packageManager.getLaunchIntentForPackage(it) }

    private fun tryStart(ctx: Context, intents: List<Intent>): Boolean {
        for (intent in intents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ctx.startActivity(intent)
                return true
            } catch (_: ActivityNotFoundException) {
                // try the next candidate
            } catch (_: SecurityException) {
                // try the next candidate
            }
        }
        Toast.makeText(ctx, R.string.no_app, Toast.LENGTH_SHORT).show()
        return false
    }
}
