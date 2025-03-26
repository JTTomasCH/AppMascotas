package unipiloto.edu.co;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActividadesAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actividades_admin);

        // Referencia al CardView de Gestión de Usuarios
        CardView cardGestionUsuarios = findViewById(R.id.cardGestionUsuarios);

        // Agregar el evento OnClickListener
        cardGestionUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad AdministrarUsuariosActivity
                Intent intent = new Intent(ActividadesAdmin.this, GestionUsuarios.class);
                startActivity(intent);
            }
        });
    }
}