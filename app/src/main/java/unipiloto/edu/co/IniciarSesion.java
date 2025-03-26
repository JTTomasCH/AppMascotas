package unipiloto.edu.co;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import unipiloto.edu.co.Peticiones.LoginPeticion;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

    private EditText email, contrasena;
    private Button btnLogin;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        email = findViewById(R.id.editTextText);
        contrasena = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.button);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String correo = email.getText().toString().trim();
        final String con = contrasena.getText().toString();

        if (correo.isEmpty() || con.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        String userID = jsonResponse.optString("id", "");
                        String nombre = jsonResponse.optString("Nombre", "Usuario");
                        String idRol = jsonResponse.optString("id_rol", "");

                        if (userID.isEmpty()) {
                            Toast.makeText(IniciarSesion.this, "Error: ID de usuario no recibido", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent;

                        if (idRol.equals("1")) {
                            intent = new Intent(IniciarSesion.this, MisMascotas.class);
                        } else if (idRol.equals("2")) {
                            intent = new Intent(IniciarSesion.this, ActividadesAdmin.class);
                        }  else if (idRol.equals("3")) {
                            intent = new Intent(IniciarSesion.this, ActividadesVet.class);
                        }else{
                            intent = new Intent(IniciarSesion.this, ActividadesEntren.class);
                        }

                        intent.putExtra("id", userID);
                        intent.putExtra("Nombre", nombre);
                        intent.putExtra("id_rol", idRol);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = jsonResponse.optString("message", "Error al iniciar sesión");
                        mostrarError(errorMsg);
                    }

                } catch (JSONException e) {
                    Toast.makeText(IniciarSesion.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IniciarSesion.this, "Error de conexión: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        LoginPeticion loginPeticion = new LoginPeticion(correo, con, responseListener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(IniciarSesion.this);
        requestQueue.add(loginPeticion);
    }

    private void mostrarError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IniciarSesion.this);
        builder.setTitle("Error")
                .setMessage(mensaje)
                .setNegativeButton("Reintentar", null)
                .create()
                .show();
    }

    public void registrar(View view) {
        Intent intent = new Intent(this, registroUsuarios.class);
        startActivity(intent);
    }
}
