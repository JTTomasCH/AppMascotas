package unipiloto.edu.co;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import unipiloto.edu.co.Peticiones.UsuariosPeticion;

public class GestionUsuarios extends AppCompatActivity {

    private ListView listViewUsuarios;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_usuarios);

        listViewUsuarios = findViewById(R.id.listViewUsuarios);
        listaUsuarios = new ArrayList<>();
        usuarioAdapter = new UsuarioAdapter(this, listaUsuarios);
        listViewUsuarios.setAdapter(usuarioAdapter);

        obtenerUsuarios();
    }

    private void obtenerUsuarios() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando usuarios...");
        progressDialog.show();

        Response.Listener<String> responseListener = response -> {
            progressDialog.dismiss();
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");

                if (success) {
                    JSONArray usuariosArray = jsonResponse.getJSONArray("usuarios");

                    listaUsuarios.clear(); // Limpiar la lista antes de agregar nuevos datos

                    for (int i = 0; i < usuariosArray.length(); i++) {
                        JSONObject usuarioObject = usuariosArray.getJSONObject(i);

                        Usuario usuario = new Usuario(
                                usuarioObject.getInt("id"),
                                usuarioObject.getString("nombre"),
                                usuarioObject.getString("email"),
                                usuarioObject.getString("telefono"),
                                usuarioObject.getString("direccion"),
                                usuarioObject.getInt("id_rol")
                        );

                        listaUsuarios.add(usuario);
                    }

                    usuarioAdapter.notifyDataSetChanged(); // Notificar cambios en la lista
                } else {
                    Toast.makeText(GestionUsuarios.this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Toast.makeText(GestionUsuarios.this, "Error al procesar los datos.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        };

        Response.ErrorListener errorListener = error -> {
            progressDialog.dismiss();
            Toast.makeText(GestionUsuarios.this, "Error en la conexión con el servidor.", Toast.LENGTH_SHORT).show();
        };

        UsuariosPeticion peticion = new UsuariosPeticion(null, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(GestionUsuarios.this);
        queue.add(peticion);
    }
}
