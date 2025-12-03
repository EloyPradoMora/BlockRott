package backend;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import com.example.blockrott.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class BlockerActivity extends AppCompatActivity {
    private static final String REASON_TIME_LIMIT = "TIME_LIMIT";
    private static final String REASON_GLOBAL_LOCK = "GLOBAL_LOCK";
    private static final long EXTENSION_TIME_MS = 10 * 60 * 1000L; //10 minutos, para cambiar el tiempo cambiar el primer numero solamente

    private static final String TAG = "BlockerActivityAd";
    private RewardedAd rewardedAd;
    private String adUnitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocker);
        setFinishOnTouchOutside(false);
        TextView appNameView = findViewById(R.id.blocked_app_name);
        TextView messageView = findViewById(R.id.blocked_message_text);
        Button closeButton = findViewById(R.id.close_blocker_button);

        String packageName = getIntent().getStringExtra("PACKAGE_NAME");
        String blockReason = getIntent().getStringExtra("BLOCK_REASON");

        adUnitId = getString(R.string.ad_unit_rewarded);
        loadRewardedAd(adUnitId);

        if (packageName != null) {
            appNameView.setText(packageName);

            if (REASON_TIME_LIMIT.equals(blockReason)) {
                messageView.setText("¡Límite de tiempo alcanzado! ¿Quieres una extensión?");
                closeButton.setText("Ver anuncio para usar por 10 minutos mas");
                closeButton.setOnClickListener(v -> {
                    if (rewardedAd != null) {
                        showRewardedAd(packageName); // Mostrar anuncio si está cargado
                    } else {
                        // Opcional: Si el anuncio no está listo, notificar al usuario.
                        Log.d(TAG, "Anuncio de recompensa aún no está cargado.");
                        // Aquí podrías mostrar un Toast al usuario
                    }
                });

            } else if (REASON_GLOBAL_LOCK.equals(blockReason)) {
                messageView.setText("Aplicación bloqueada por el control de tiempo global.");
                closeButton.setText("Entendido");
                closeButton.setOnClickListener(v -> {
                    finish();
                });

            } else {
                //Mensaje por defecto
                messageView.setText("Aplicación bloqueada.");
                closeButton.setText("Entendido");
                closeButton.setOnClickListener(v -> {
                    finish();
                });
            }
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadRewardedAd(String adUnitId) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, adUnitId, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d(TAG, "Anuncio fallo en cargar: " + loadAdError.toString());
                rewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                Log.d(TAG, "Anuncio Cargado.");
            }
        });
    }

    private void showRewardedAd(String packageName) {
        if (rewardedAd == null) {
            Log.e(TAG, "El auncio de recompensa aun no esta listo.");
            return;
        }
        rewardedAd.show(this, rewardItem -> {
            Log.d(TAG, "El usuario gana: " + rewardItem.getAmount());
            handleAdReward(packageName);
        });
        loadRewardedAd(adUnitId);
    }

    public void handleAdReward(String packageName) {
        Usuario.getInstance(getApplicationContext()).extenderTiempoLimite(packageName, EXTENSION_TIME_MS);
        finish();
    }
}
