package unipiloto.edu.co;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import unipiloto.edu.co.Peticiones.RegistroMascotasPeticion;

public class registroMascotas extends AppCompatActivity {

    private ImageView imgMascota;
    private Button btnSubirImagen;
    private Spinner spinnerGenero;
    private EditText nombre, raza, edad, peso, color;
    private Button btnRegistro;
    private String userID,nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_macotas);

        nombre = findViewById(R.id.etNombreMascota);
        raza = findViewById(R.id.etRaza);
        edad = findViewById(R.id.etEdad);
        peso = findViewById(R.id.etPeso);
        color = findViewById(R.id.etColor);
        btnRegistro = findViewById(R.id.btnRegistrar);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        btnRegistro.setOnClickListener(this::onClick);
        imgMascota = findViewById(R.id.imgMascota);
        btnSubirImagen = findViewById(R.id.btnSubirImagen);
        btnSubirImagen.setOnClickListener(v -> openImageChooser());

        Intent intent = getIntent();
        userID = intent.getStringExtra("id");
        nombreUsuario = intent.getStringExtra("Nombre");

        if (userID == null || userID.isEmpty()) {
            Toast.makeText(this, "No se recibió el ID del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.d("registroMascotas", "ID del usuario recibido: " + userID);
    }

    private String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void onClick(View v) {
        Log.d("registroMascotas", "Se presionó el botón de registro");

        final String name = nombre.getText().toString().trim();
        final String raz = raza.getText().toString().trim();
        final String eda = edad.getText().toString().trim();
        final String pes = peso.getText().toString().trim();
        final String colo = color.getText().toString().trim();
        final String generoSeleccionado = spinnerGenero.getSelectedItem().toString();

        // Log de los valores de los campos
        Log.d("registroMascotas", "Nombre: " + name);
        Log.d("registroMascotas", "Raza: " + raz);
        Log.d("registroMascotas", "Edad: " + eda);
        Log.d("registroMascotas", "Peso: " + pes);
        Log.d("registroMascotas", "Color: " + colo);
        Log.d("registroMascotas", "Género: " + generoSeleccionado);

        // Verificación de campos vacíos
        if (name.isEmpty() || raz.isEmpty() || eda.isEmpty() || pes.isEmpty() || colo.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("registroMascotas", "Datos de mascota: " + name + ", " + raz + ", " + eda + ", " + pes + ", " + colo + ", " + generoSeleccionado);

        Bitmap bitmap = ((BitmapDrawable) imgMascota.getDrawable()).getBitmap();
        String imageBase64 = convertImageToBase64(bitmap);

        // Log de la imagen convertida a Base64
        Log.d("registroMascotas", "Imagen convertida a Base64: " + imageBase64);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("registroMascotas", "Respuesta del servidor: " + response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(registroMascotas.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(registroMascotas.this, MisMascotas.class);
                        intent.putExtra("id", userID);
                        intent.putExtra("Nombre", nombreUsuario);
                        startActivity(intent);
                        finish();
                    } else {
                        String message = "ERROR DE REGISTRO";
                        if (jsonResponse.has("message")) {
                            message = jsonResponse.getString("message");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(registroMascotas.this);
                        builder.setMessage(message);
                        builder.setNegativeButton("Reintentar", null);
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    Log.e("registroMascotas", "Error al procesar la respuesta del servidor", e);
                    AlertDialog.Builder builder = new AlertDialog.Builder(registroMascotas.this);
                    builder.setMessage("Error al procesar la respuesta del servidor");
                    builder.setNegativeButton("Reintentar", null);
                    builder.create().show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("registroMascotas", "Error de conexión", error);
                String errorMessage = "Error de conexión";
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    errorMessage += " (Código: " + networkResponse.statusCode + ")";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(registroMascotas.this);
                builder.setMessage(errorMessage);
                builder.setPositiveButton("Reintentar", null);
                builder.setNegativeButton("Cerrar", null);
                builder.create().show();
            }
        };

        Log.d("registroMascotas", "Campos antes de enviar: " +
                "Nombre: " + name + ", Raza: " + raz + ", Edad: " + eda +
                ", Peso: " + pes + ", Color: " + colo + ", Género: " + generoSeleccionado);

        Log.d("registroMascotas", "ID de usuario enviado: " + userID);
        RegistroMascotasPeticion registroPeticion = new RegistroMascotasPeticion(name, raz, eda, pes, generoSeleccionado, colo, imageBase64, userID, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(registroMascotas.this);
        queue.add(registroPeticion);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imgMascota.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("registroMascotas", "Error al seleccionar la imagen", e);
                Toast.makeText(this, "Error al seleccionar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
