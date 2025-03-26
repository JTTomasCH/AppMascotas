package unipiloto.edu.co;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import unipiloto.edu.co.Peticiones.RegistroPeticion;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class registroUsuarios extends AppCompatActivity implements View.OnClickListener {

    EditText nombre, email, direccion, telefono, contrasena, confirmar;
    Button btnRegistro;

    private Spinner spinnerTipo;
    private static final String TAG = "RegistroUsuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_usuarios);

        nombre = findViewById(R.id.etNombreUsuario);
        email = findViewById(R.id.etEmail);
        direccion = findViewById(R.id.etDireccion);
        telefono = findViewById(R.id.etTelefono);
        contrasena = findViewById(R.id.etContrasena);
        confirmar = findViewById(R.id.etConfirmarContrasena);
        btnRegistro = findViewById(R.id.btnRegistrarUsuario);
        btnRegistro.setOnClickListener(this);
        spinnerTipo=findViewById(R.id.spinnerTipoUsuario);
    }

    @Override
    public void onClick(View v) {
        final String name = nombre.getText().toString().trim();
        final String correo = email.getText().toString().trim();
        final String dir = direccion.getText().toString().trim();
        final String tel = telefono.getText().toString().trim();
        final String con = contrasena.getText().toString().trim();
        final String conf = confirmar.getText().toString().trim();
        final String tipo = spinnerTipo.getSelectedItem().toString();

        if (name.isEmpty() || correo.isEmpty() || dir.isEmpty() || tel.isEmpty() || con.isEmpty() || conf.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!con.equals(conf)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Por favor ingrese un correo válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Enviando datos: Nombre=" + name + ", Email=" + correo + ", Dirección=" + dir + ", Teléfono=" + tel+ " "+tipo);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respuesta del servidor: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (!jsonResponse.has("success")) {
                        Log.e(TAG, "La respuesta no contiene el campo 'success'");
                        throw new JSONException("El campo 'success' no está en la respuesta");
                    }

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(registroUsuarios.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(registroUsuarios.this, IniciarSesion.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String message = jsonResponse.optString("message", "ERROR DE REGISTRO");

                        AlertDialog.Builder builder = new AlertDialog.Builder(registroUsuarios.this);
                        builder.setMessage(message);
                        builder.setNegativeButton("Reintentar", null);
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Error al procesar JSON: ", e);
                    AlertDialog.Builder builder = new AlertDialog.Builder(registroUsuarios.this);
                    builder.setMessage("Error al procesar la respuesta del servidor.");
                    builder.setNegativeButton("Reintentar", null);
                    builder.create().show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Error de conexión";
                NetworkResponse networkResponse = error.networkResponse;

                if (networkResponse != null) {
                    errorMessage += " (Código: " + networkResponse.statusCode + ")";
                    Log.e(TAG, "Error de red. Código: " + networkResponse.statusCode);

                    if (networkResponse.data != null) {
                        String responseBody = new String(networkResponse.data);
                        Log.e(TAG, "Respuesta del servidor con error: " + responseBody);
                    }
                } else {
                    Log.e(TAG, "Error de conexión sin respuesta del servidor", error);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(registroUsuarios.this);
                builder.setMessage(errorMessage);
                builder.setPositiveButton("Reintentar", null);
                builder.setNegativeButton("Cerrar", null);
                builder.create().show();
            }
        };

        RegistroPeticion registroPeticion = new RegistroPeticion(name, correo, tel, dir, con,tipo, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(registroUsuarios.this);
        queue.add(registroPeticion);
    }
}
