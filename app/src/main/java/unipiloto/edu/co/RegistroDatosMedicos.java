package unipiloto.edu.co;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import unipiloto.edu.co.Peticiones.RegistroDatosMedicosPeticion;
import unipiloto.edu.co.Peticiones.RegistroMascotasPeticion;

public class RegistroDatosMedicos extends AppCompatActivity {

    private Spinner spinnerTipo;

    private EditText dateInput,veterinaria, veterinario, nombreProcedimiento,descripcion;
    private int mascotaID;

    private String ID;

    private ImageView calendarIcon;
    private Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_datos_medicos);
        dateInput = findViewById(R.id.date_input);
        veterinaria=findViewById(R.id.vet_name);
        veterinario=findViewById(R.id.doctor_name);
        nombreProcedimiento=findViewById(R.id.procedure_name);
        descripcion=findViewById(R.id.procedure_description);
        spinnerTipo=findViewById(R.id.type_spinner);
        calendarIcon = findViewById(R.id.calendar_icon);
        dateInput.setOnClickListener(view -> MostrarCalendario());
        calendarIcon.setOnClickListener(view -> MostrarCalendario());
        btnRegistro=findViewById(R.id.button_save);
        btnRegistro.setOnClickListener(this::onClick);
        Intent intent = getIntent();
        mascotaID= getIntent().getIntExtra("ID_MASCOTA", -1);
        Log.d("RegistroDatosMedicos", "ID_MASCOTA recibido: " + mascotaID);
        ID= String.valueOf(mascotaID);

    }


    public void onClick(View v) {
        final String tipo = spinnerTipo.getSelectedItem().toString();
        final String fecha = dateInput.getText().toString().trim();
        final String vet = veterinaria.getText().toString().trim();
        final String vete = veterinario.getText().toString().trim();
        final String pro = nombreProcedimiento.getText().toString().trim();
        final String des = descripcion.getText().toString().trim();

        if (fecha.isEmpty() || vet.isEmpty() || vete.isEmpty() || pro.isEmpty() || des.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(RegistroDatosMedicos.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegistroDatosMedicos.this, MisMascotas.class);
                        intent.putExtra("ID_MASCOTA", mascotaID);
                        startActivity(intent);
                        finish();
                    }else {
                        String message = "ERROR DE REGISTRO";
                        if (jsonResponse.has("message")) {
                            message = jsonResponse.getString("message");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroDatosMedicos.this);
                        builder.setMessage(message);
                        builder.setNegativeButton("Reintentar", null);
                        builder.create().show();
                    }
                } catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroDatosMedicos.this);
                    builder.setMessage("Error al procesar la respuesta del servidor");
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
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroDatosMedicos.this);
                builder.setMessage(errorMessage);
                builder.setPositiveButton("Reintentar", null);
                builder.setNegativeButton("Cerrar", null);
                builder.create().show();
            }
        };

        RegistroDatosMedicosPeticion registro = new RegistroDatosMedicosPeticion(tipo,fecha,vet,vete,pro,des,ID,responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(RegistroDatosMedicos.this);
        queue.add(registro);
    }

    private void MostrarCalendario() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistroDatosMedicos.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    dateInput.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }


}