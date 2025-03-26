package unipiloto.edu.co;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ActividadesVet extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividadesvet);

        // Referencia al CardView de Gestión de Usuarios
        CardView cardGestionUsuarios = findViewById(R.id.cardCita);

        // Agregar el evento OnClickListener
        cardGestionUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad AdministrarUsuariosActivity
                Intent intent = new Intent(ActividadesVet.this, AgendarCitaActivity.class);
                startActivity(intent);
            }
        });
    }
}

