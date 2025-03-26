package unipiloto.edu.co;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import unipiloto.edu.co.Peticiones.ObtenerDueñosPeticion;
import unipiloto.edu.co.Peticiones.ObtenerMascotasPeticion;
import unipiloto.edu.co.Peticiones.RegistrarPaseoPeticion;
import unipiloto.edu.co.Peticiones.RegistroDatosMedicosPeticion;


public class RegistrarPaseos extends AppCompatActivity {

    private Spinner spinnerDueños, spinnerMascotas;
    private RequestQueue requestQueue;
    private ArrayList<String> dueñosList, mascotasList;
    private ArrayList<String> dueñosIdList, mascotasIdList;

    private EditText etFecha, etHoraInicio, etHoraFin, etDistancia, etComentarios;
    private Calendar calendar;
    private int horaInicio, minutoInicio, horaFin, minutoFin;

    private String userID;
    private String nombreUsuario;
    private Button btnRegistrarPaseo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_paseos);

        spinnerDueños = findViewById(R.id.spinnerNombreDueno);
        spinnerMascotas = findViewById(R.id.spinnerNombreMascota);
        etDistancia = findViewById(R.id.inputDistancia);
        etComentarios = findViewById(R.id.inputComentarios);
        btnRegistrarPaseo = findViewById(R.id.btnRegistrarPaseo);
        requestQueue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        userID = intent.getStringExtra("id");
        nombreUsuario = intent.getStringExtra("Nombre");
        cargarDueños();

        btnRegistrarPaseo.setOnClickListener(v -> registrarPaseo());

        spinnerDueños.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cargarMascotas(dueñosIdList.get(position - 1));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etFecha = findViewById(R.id.inputFecha);
        calendar = Calendar.getInstance();

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        RegistrarPaseos.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day
                );
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        etHoraInicio = findViewById(R.id.inputHoraInicio);
        etHoraFin = findViewById(R.id.inputHoraFin);
        etHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarHoraInicio();
            }
        });

        etHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarHoraFin();
            }
        });
    }

    private void seleccionarHoraInicio() {
        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    horaInicio = hourOfDay;
                    minutoInicio = minute;
                    etHoraInicio.setText(formatoHora(hourOfDay, minute));
                },
                horaActual, minutoActual, false
        );

        timePickerDialog.show();
    }

    private void seleccionarHoraFin() {
        if (horaInicio == 0 && minutoInicio == 0) {
            Toast.makeText(this, "Seleccione primero la hora de inicio", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int horaActual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoActual = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    // Validar que la hora de fin sea mayor o igual a la de inicio
                    if (hourOfDay < horaInicio || (hourOfDay == horaInicio && minute < minutoInicio)) {
                        Toast.makeText(this, "La hora de fin debe ser después de la hora de inicio", Toast.LENGTH_SHORT).show();
                    } else {
                        horaFin = hourOfDay;
                        minutoFin = minute;
                        etHoraFin.setText(formatoHora(hourOfDay, minute));
                    }
                },
                horaActual, minutoActual, false
        );

        timePickerDialog.show();
    }

    private String formatoHora(int hour, int minute) {
        String amPm;
        if (hour >= 12) {
            amPm = "PM";
            if (hour > 12) hour -= 12;
        } else {
            amPm = "AM";
            if (hour == 0) hour = 12;
        }
        return String.format("%02d:%02d %s", hour, minute, amPm);
    }

    private void cargarDueños() {
        dueñosList = new ArrayList<>();
        dueñosIdList = new ArrayList<>();
        dueñosList.add("Selecciona un dueño");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        dueñosIdList.add(obj.getString("ID"));
                        dueñosList.add(obj.getString("NOMBRE"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrarPaseos.this, android.R.layout.simple_spinner_item, dueñosList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDueños.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrarPaseos.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        };

        ObtenerDueñosPeticion peticion = new ObtenerDueñosPeticion(responseListener, errorListener);
        requestQueue.add(peticion);
    }

    private void cargarMascotas(String idUsuario) {
        mascotasList = new ArrayList<>();
        mascotasIdList = new ArrayList<>();
        mascotasList.add("Selecciona una mascota");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        mascotasIdList.add(obj.getString("ID"));
                        mascotasList.add(obj.getString("NOMBRE"));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrarPaseos.this, android.R.layout.simple_spinner_item, mascotasList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMascotas.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistrarPaseos.this, "Error al obtener mascotas", Toast.LENGTH_SHORT).show();
            }
        };

        ObtenerMascotasPeticion peticion = new ObtenerMascotasPeticion(idUsuario, responseListener, errorListener);
        requestQueue.add(peticion);
    }

    private void registrarPaseo() {
        String nombreDueno = spinnerDueños.getSelectedItem().toString();
        String nombreMascota = spinnerMascotas.getSelectedItem().toString();
        String fechaPaseo = etFecha.getText().toString();
        String horaInicioStr = etHoraInicio.getText().toString();
        String horaFinStr = etHoraFin.getText().toString();
        String distancia = etDistancia.getText().toString();
        String comentarios = etComentarios.getText().toString();

        Log.d("RegistrarPaseo", "Dueño: " + nombreDueno);
        Log.d("RegistrarPaseo", "Mascota: " + nombreMascota);
        Log.d("RegistrarPaseo", "Fecha: " + fechaPaseo);
        Log.d("RegistrarPaseo", "Hora inicio: " + horaInicioStr);
        Log.d("RegistrarPaseo", "Hora fin: " + horaFinStr);
        Log.d("RegistrarPaseo", "Distancia: " + distancia);
        Log.d("RegistrarPaseo", "Comentarios: " + comentarios);
        Log.d("RegistrarPaseo", "UserID: " + userID);

        if (nombreDueno.equals("Selecciona un dueño") || nombreMascota.equals("Selecciona una mascota") ||
                fechaPaseo.isEmpty() || horaInicioStr.isEmpty() || horaFinStr.isEmpty() || distancia.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListener = response -> {
            Toast.makeText(this, "Paseo registrado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        };

        Response.ErrorListener errorListener = error -> {
            Toast.makeText(this, "Error al registrar paseo", Toast.LENGTH_SHORT).show();
        };

        RegistrarPaseoPeticion peticion = new RegistrarPaseoPeticion(
                nombreDueno, nombreMascota, userID, fechaPaseo,
                horaInicioStr, horaFinStr, distancia, comentarios,
                responseListener, errorListener
        );

        requestQueue.add(peticion);
    }
}









