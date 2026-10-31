package com.safenet.browserguard;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;

    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(48, 64, 48, 48);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("🛡️  SafeNet");
        title.setTextSize(30);
        layout.addView(title);

        TextView info = new TextView(this);
        info.setText("\nFiltrage des domaines adultes dans les navigateurs sélectionnés.\n");
        info.setTextSize(16);
        layout.addView(info);

        status = new TextView(this);
        status.setText("Protection désactivée");
        status.setTextSize(18);
        layout.addView(status);

        Button start = new Button(this);
        start.setText("Activer la protection");
        start.setOnClickListener(v -> {
            Intent prepare = VpnService.prepare(this);
            if (prepare != null) startActivityForResult(prepare, 41);
            else startProtection();
        });
        layout.addView(start);

        Button stop = new Button(this);
        stop.setText("Désactiver");
        stop.setOnClickListener(v -> {
            stopService(new Intent(this, BrowserVpnService.class));
            status.setText("Protection désactivée");
        });
        layout.addView(stop);

        TextView note = new TextView(this);
        note.setText("\nSafeNet utilise le DNS familial Cloudflare (1.1.1.3). "
            + "Le filtrage DNS peut être contourné par le DNS sécurisé/DoH intégré à certains navigateurs. "
            + "Désactivez « DNS sécurisé » dans les paramètres du navigateur pour une meilleure couverture.");
        note.setTextSize(13);
        layout.addView(note);

        setContentView(layout);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 41 && resultCode == RESULT_OK) startProtection();
    }

    private void startProtection() {
        startForegroundService(new Intent(this, BrowserVpnService.class));
        status.setText("Demande de protection envoyée");
    }
}
