package unipiloto.edu.co;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActividadesEntren extends AppCompatActivity {

    private String userID;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadesentre);
        Intent intent = getIntent();
        userID = intent.getStringExtra("id");
        nombreUsuario = intent.getStringExtra("Nombre");

        if (userID == null || userID.isEmpty()) {
            SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            userID = preferences.getString("user_id", "");

            if (userID.isEmpty()) {
                Toast.makeText(this, "Error: No se recibió el ID de usuario", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void registrarPaseos(View view) {
        Intent intent = new Intent(this, RegistrarPaseos.class);
        intent.putExtra("id", userID);
        intent.putExtra("Nombre", nombreUsuario);
        startActivity(intent);
    }
}

