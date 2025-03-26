package unipiloto.edu.co;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import unipiloto.edu.co.Peticiones.EditarUsuarioPeticion;

public class EditarUsuarioActivity extends AppCompatActivity {

    private EditText etNombre, etEmail, etTelefono, etDireccion, etPassword;
    private CheckBox cbCambiarPassword;
    private Button btnGuardarCambios;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etPassword = findViewById(R.id.etPassword); // 🔹 Nuevo campo para la contraseña
        cbCambiarPassword = findViewById(R.id.cbCambiarPassword); // 🔹 Checkbox para cambiar contraseña
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        // Obtener datos del usuario seleccionado
        Intent intent = getIntent();
        userID = intent.getIntExtra("ID", -1);
        etNombre.setText(intent.getStringExtra("NOMBRE"));
        etEmail.setText(intent.getStringExtra("EMAIL"));
        etTelefono.setText(intent.getStringExtra("TELEFONO"));
        etDireccion.setText(intent.getStringExtra("DIRECCION"));

        // 🔹 Manejo del checkbox para cambiar contraseña
        cbCambiarPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Guardar cambios
        btnGuardarCambios.setOnClickListener(v -> {
            Toast.makeText(EditarUsuarioActivity.this, "Botón presionado", Toast.LENGTH_SHORT).show();
            actualizarUsuario();
        });
    }

    private void actualizarUsuario() {
        String nombre = etNombre.getText().toString();
        String email = etEmail.getText().toString();
        String telefono = etTelefono.getText().toString();
        String direccion = etDireccion.getText().toString();
        String password = cbCambiarPassword.isChecked() ? etPassword.getText().toString().trim() : "";

        Log.d("EditarUsuario", "Nombre: " + nombre + ", Email: " + email + ", Teléfono: " + telefono + ", Dirección: " + direccion + ", Contraseña: " + password);

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");

                if (success) {
                    Toast.makeText(EditarUsuarioActivity.this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad
                } else {
                    Toast.makeText(EditarUsuarioActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        // Crear la petición de actualización
        EditarUsuarioPeticion peticion = new EditarUsuarioPeticion(userID, nombre, email, telefono, direccion, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(EditarUsuarioActivity.this);
        queue.add(peticion);
    }
}
