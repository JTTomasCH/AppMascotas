package unipiloto.edu.co;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import unipiloto.edu.co.registroMascotas;

import unipiloto.edu.co.Peticiones.MascotasPeticion;

public class MisMascotas extends AppCompatActivity {

    private TextView tvBienvenido, tvCantidadMascotas;
    private ListView lvMascotas;
    private MascotaAdapter adapter;
    private List<Mascota> listaMascotas;
    private Button fabAgregarMascota;

    private String userID;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascotas);

        tvBienvenido = findViewById(R.id.tvBienvenido);
        tvCantidadMascotas = findViewById(R.id.tvCantidadMascotas);
        lvMascotas = findViewById(R.id.lvMascotas);
        fabAgregarMascota = findViewById(R.id.btnAgregarMascota);

        Intent intent = getIntent();
        userID = intent.getStringExtra("id");
        nombreUsuario = intent.getStringExtra("Nombre");

        if (userID == null || userID.isEmpty()) {
            SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            userID = preferences.getString("user_id", "");

            if (userID.isEmpty()) {
                Toast.makeText(this, "Error: No se recibió el ID de usuario", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }

        tvBienvenido.setText("Bienvenido, " + nombreUsuario);

        listaMascotas = new ArrayList<>();
        adapter = new MascotaAdapter(this, listaMascotas);
        lvMascotas.setAdapter(adapter);

        cargarMascotas();

        fabAgregarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisMascotas.this, registroMascotas.class);
                intent.putExtra("id", userID);
                intent.putExtra("Nombre", nombreUsuario);
                startActivity(intent);
            }
        });

        lvMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mascota mascotaSeleccionada = listaMascotas.get(position);
                Toast.makeText(MisMascotas.this, "Mascota: " + mascotaSeleccionada.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarMascotas();
    }

    private void cargarMascotas() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        String userID = jsonResponse.optString("id", "");
                        int cantidad = jsonResponse.getInt("cantidad");
                        tvCantidadMascotas.setText("Tienes " + cantidad + " mascotas registradas");

                        JSONArray mascotasArray = jsonResponse.getJSONArray("mascotas");
                        listaMascotas.clear();

                        for (int i = 0; i < mascotasArray.length(); i++) {
                            JSONObject mascotaJson = mascotasArray.getJSONObject(i);
                            int id = mascotaJson.getInt("id");
                            String nombre = mascotaJson.getString("nombre");
                            String raza = mascotaJson.getString("raza");
                            int edad = mascotaJson.getInt("edad");
                            String fotoUrl = mascotaJson.has("foto") ? mascotaJson.getString("foto") : "";

                            Mascota mascota = new Mascota(id, nombre, raza, edad, fotoUrl);
                            listaMascotas.add(mascota);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        String errorMsg = jsonResponse.getString("message");
                        Toast.makeText(MisMascotas.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MisMascotas.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MisMascotas.this, "Error de conexión", Toast.LENGTH_LONG).show();
            }
        };

        MascotasPeticion mascotasPeticion = new MascotasPeticion(userID, responseListener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(MisMascotas.this);
        requestQueue.add(mascotasPeticion);
    }
}