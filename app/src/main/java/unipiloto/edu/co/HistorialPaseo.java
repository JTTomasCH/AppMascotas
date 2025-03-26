package unipiloto.edu.co;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import unipiloto.edu.co.Peticiones.HistorialPaseosPeticion;

public class HistorialPaseo extends AppCompatActivity {

    private TextView txtFechaSeleccionada;
    private Button btnSeleccionarFecha;
    private RecyclerView recyclerViewPaseos;
    private PaseoAdapter adapter;
    private List<Paseo> listaPaseos;
    private String mascotaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_paseos);

        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        recyclerViewPaseos = findViewById(R.id.recyclerViewPaseos);

        // Obtener ID de la mascota desde el Intent con la clave correcta
        Intent intent = getIntent();
        int idMascota = intent.getIntExtra("ID_MASCOTA", -1); // Clave corregida
        mascotaID = String.valueOf(idMascota); // Convertir a String si es necesario

        if (idMascota == -1) {
            Toast.makeText(this, "Error: No se recibió el ID de la mascota", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no se recibe el ID
            return;
        }

        listaPaseos = new ArrayList<>();
        adapter = new PaseoAdapter(this, listaPaseos); // Se agrega el contexto
        recyclerViewPaseos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPaseos.setAdapter(adapter);

        btnSeleccionarFecha.setOnClickListener(v -> mostrarDatePicker());
    }

    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    month += 1;
                    String fechaSeleccionada = year + "-" + (month < 10 ? "0" + month : month) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
                    txtFechaSeleccionada.setText("📅 Fecha seleccionada: " + fechaSeleccionada);
                    cargarPaseos(fechaSeleccionada);
                },
                anio, mes, dia
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void cargarPaseos(String fecha) {
        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");

                if (success) {
                    JSONArray paseosArray = jsonResponse.getJSONArray("paseos");
                    listaPaseos.clear();

                    for (int i = 0; i < paseosArray.length(); i++) {
                        JSONObject paseoJson = paseosArray.getJSONObject(i);
                        Paseo paseo = new Paseo(
                                paseoJson.getInt("id"),
                                paseoJson.getString("fecha"),
                                paseoJson.getString("hora_inicio"),
                                paseoJson.getString("hora_fin"),
                                paseoJson.getString("distancia"),
                                paseoJson.getString("id_realizador")
                        );
                        listaPaseos.add(paseo);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    listaPaseos.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(HistorialPaseo.this, "No hay paseos en esta fecha", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(HistorialPaseo.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
            }
        };

        Response.ErrorListener errorListener = error ->
                Toast.makeText(HistorialPaseo.this, "Error de conexión", Toast.LENGTH_LONG).show();

        HistorialPaseosPeticion paseosPeticion = new HistorialPaseosPeticion(mascotaID, fecha, responseListener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(HistorialPaseo.this);
        requestQueue.add(paseosPeticion);
    }
}
