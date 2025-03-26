package unipiloto.edu.co;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ActividadesMascotas extends AppCompatActivity {

    private int mascotaId;
    private String mascotaNombre;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_mascotas);

        Intent intent = getIntent();

        if (intent != null) {
            mascotaId = intent.getIntExtra("ID_MASCOTA", -1);
            mascotaNombre = intent.getStringExtra("mascota_nombre");
        }

        if (mascotaId == -1) {
            Toast.makeText(this, "Error: No se recibió la mascota", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        findViewById(R.id.cardHistorialPaseos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHistorial = new Intent(ActividadesMascotas.this, HistorialPaseo.class);
                intentHistorial.putExtra("ID_MASCOTA", mascotaId);
                intentHistorial.putExtra("mascota_nombre", mascotaNombre);
                startActivity(intentHistorial);
            }
        });
    }
}

