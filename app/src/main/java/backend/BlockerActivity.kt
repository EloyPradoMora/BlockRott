package backend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.DialogProperties
import com.example.blockrott.R
import com.example.blockrott.frontend.components.AlertDialog
import com.example.blockrott.frontend.components.DialogType
import com.example.blockrott.frontend.theme.AppTheme
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class BlockerActivity : AppCompatActivity() {
    private val REASON_TIME_LIMIT = "TIME_LIMIT"
    private val REASON_GLOBAL_LOCK = "GLOBAL_LOCK"
    private val EXTENSION_TIME_MS = 10 * 1000L // 10 segundos, extender para version final
    private val TAG = "BlockerActivityAd"
    private var rewardedAd: RewardedAd? = null
    private lateinit var adUnitId: String
    private var userEarnedReward = false
    companion object {
        @JvmField
        var isRunning = false
        @JvmField
        var isAdShowing = false
    }
    private var packageNameState = mutableStateOf<String?>(null)
    private var blockReasonState = mutableStateOf<String?>(null)

    override fun onStart() {
        super.onStart()
        isRunning = true
    }

    override fun onStop() {
        super.onStop()
        isRunning = false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        packageNameState.value = intent.getStringExtra("PACKAGE_NAME")
        blockReasonState.value = intent.getStringExtra("BLOCK_REASON")
        Log.d(TAG, "onNewIntent: Package: ${packageNameState.value}, Reason: ${blockReasonState.value}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false)
        packageNameState.value = intent.getStringExtra("PACKAGE_NAME")
        blockReasonState.value = intent.getStringExtra("BLOCK_REASON")
        adUnitId = getString(R.string.ad_unit_rewarded)
        loadRewardedAd(adUnitId)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // No hacer nada para evitar que el usuario pueda volver
            }
        })
        setContent {
            AppTheme {
                val currentPackageName = packageNameState.value
                val currentBlockReason = blockReasonState.value
                val title = currentPackageName ?: "Aplicación Bloqueada"
                val explanation = when (currentBlockReason) {
                    REASON_TIME_LIMIT -> "¡Límite de tiempo alcanzado! ¿Quieres una extensión?"
                    REASON_GLOBAL_LOCK -> "Aplicación bloqueada por el control global."
                    else -> "Aplicación bloqueada."
                }
                val type = if (currentBlockReason == REASON_TIME_LIMIT) DialogType.WARNING else DialogType.INFO
                val confirmText = if (currentBlockReason == REASON_TIME_LIMIT) "Ver anuncio por 10 minutos más:" else "Entendido"
                val dismissText = if (currentBlockReason == REASON_TIME_LIMIT) "Salir" else "Cerrar"

                AlertDialog(
                    type = type,
                    dialogTitle = title,
                    dialogExplanation = explanation,
                    confirmText = confirmText,
                    dismissText = dismissText,
                    onConfirmation = {
                        if (currentBlockReason == REASON_TIME_LIMIT) {
                            if (rewardedAd != null) {
                                showRewardedAd(currentPackageName)
                            } else {
                                Log.d(TAG, "Anuncio de recompensa aún no está cargado.")
                            }
                        } else {
                            finish()
                        }
                    },
                    onDismiss = {
                        finish()
                    },
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    )
                )
            }
        }
    }

    private fun loadRewardedAd(adUnitId: String) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, adUnitId, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d(TAG, "Anuncio fallo en cargar: $loadAdError")
                rewardedAd = null
            }
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                Log.d(TAG, "Anuncio Cargado.")
            }
        })
    }

    private fun showRewardedAd(packageName: String?) {
        if (rewardedAd == null) {
            Log.e(TAG, "El anuncio de recompensa aun no esta listo.")
            return
        }
        isAdShowing = true
        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
                isAdShowing = false
                if (userEarnedReward) {
                    handleAdReward(packageName)
                }
                loadRewardedAd(adUnitId)
            }
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.e(TAG, "Ad failed to show fullscreen content.")
                rewardedAd = null
                isAdShowing = false
            }
            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
        userEarnedReward = false
        rewardedAd?.show(this) { rewardItem: RewardItem ->
            Log.d(TAG, "El usuario gana: ${rewardItem.amount}")
            userEarnedReward = true
        }
    }

    fun handleAdReward(packageName: String?) {
        packageName?.let {
            Usuario.getInstance(applicationContext).extenderTiempoLimite(it, EXTENSION_TIME_MS)
            val launchIntent = packageManager.getLaunchIntentForPackage(it)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(launchIntent)
            } else {
                Log.e(TAG, "No se pudo encontrar $it")
            }
        }
        finish()
    }
}