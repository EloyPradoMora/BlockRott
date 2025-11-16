package backend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import com.example.blockrott.R;

public class BlockerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocker);
        setFinishOnTouchOutside(false); // Hace que la actividad no se pueda descartar tocando fuera

        TextView appNameView = findViewById(R.id.blocked_app_name);
        Button closeButton = findViewById(R.id.close_blocker_button);

        String packageName = getIntent().getStringExtra("PACKAGE_NAME");
        if (packageName != null) {
            appNameView.setText("Tiempo límite alcanzado para " + packageName);
        }

        closeButton.setOnClickListener(v -> {
            finish();
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}
