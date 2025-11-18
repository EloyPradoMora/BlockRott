package backend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import com.example.blockrott.R;

public class BlockerActivity extends AppCompatActivity {
    private static final String REASON_TIME_LIMIT = "TIME_LIMIT";
    private static final String REASON_GLOBAL_LOCK = "GLOBAL_LOCK";

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

        if (packageName != null) {
            appNameView.setText(packageName);

            if (REASON_TIME_LIMIT.equals(blockReason)) {
                messageView.setText("¡Límite de tiempo alcanzado! ¿Quieres una extensión?");
                closeButton.setText("Ver anuncio para usar por 10 minutos mas");
                closeButton.setOnClickListener(v -> {
                    //IMPORTANTE
                    // Aqui agregamos el anuncio mas adelante
                    //IMPORTATNE
                    finish();
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
}
