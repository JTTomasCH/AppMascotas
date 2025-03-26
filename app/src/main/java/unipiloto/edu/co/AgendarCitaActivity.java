// AgendarCitaActivity.java
package unipiloto.edu.co;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import unipiloto.edu.co.Peticiones.RegistrarCitaPeticion;

public class AgendarCitaActivity extends AppCompatActivity {

    private Spinner spinnerMascotas;
    private Spinner spinnerTipoCita;
    private EditText fechaInput, horaInput, notasInput;
    private Button btnAgendar;
    private Map<String, String> mapaNombreIdMascota = new HashMap<>();
    private RequestQueue requestQueue;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        spinnerTipoCita = findViewById(R.id.spinnerTipoCita);
        fechaInput = findViewById(R.id.etFecha);
        horaInput = findViewById(R.id.etHora);
        notasInput = findViewById(R.id.etNotas);
        btnAgendar = findViewById(R.id.btnAgendarCita);

        requestQueue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");

        cargarMascotas(userID);
        cargarTiposCitaDesdeRecursos();

        fechaInput.setOnClickListener(v -> mostrarSelectorFecha());
        horaInput.setOnClickListener(v -> mostrarSelectorHora());

        btnAgendar.setOnClickListener(v -> registrarCita());
    }

    private void cargarMascotas(String userId) {
        String url = "http://10.0.2.2/ObtenerMascotasUsuario.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONArray mascotasArray = response.getJSONArray("mascotas");
                            List<String> nombres = new ArrayList<>();

                            for (int i = 0; i < mascotasArray.length(); i++) {
                                JSONObject obj = mascotasArray.getJSONObject(i);
                                String id = obj.getString("ID");
                                String nombre = obj.getString("NOMBRE");
                                nombres.add(nombre);
                                mapaNombreIdMascota.put(nombre, id);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerMascotas.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, "No se encontraron mascotas", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }

    private void cargarTiposCitaDesdeRecursos() {
        String[] tipos = getResources().getStringArray(R.array.tipo_dato_medico);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList(tipos));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoCita.setAdapter(adapter);
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            String fechaMySQL = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
            fechaInput.setText(fechaMySQL);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    private void mostrarSelectorHora() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hour, minute) ->
                horaInput.setText(String.format("%02d:%02d", hour, minute)),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void registrarCita() {
        String nombreSeleccionado = spinnerMascotas.getSelectedItem().toString();
        String mascotaID = mapaNombreIdMascota.get(nombreSeleccionado);
        String tipo = spinnerTipoCita.getSelectedItem().toString();
        String fecha = fechaInput.getText().toString().trim();
        String hora = horaInput.getText().toString().trim();
        String notas = notasInput.getText().toString().trim();

        if (tipo.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        RegistrarCitaPeticion peticion = new RegistrarCitaPeticion(
                mascotaID, tipo, fecha, hora, notas,
                response -> {
                    Toast.makeText(this, "Cita registrada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ActividadesVet.class);
                    intent.putExtra("user_id", userID);
                    startActivity(intent);
                    finish();
                },
                error -> Toast.makeText(this, "Error al registrar cita", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(peticion);
    }
}