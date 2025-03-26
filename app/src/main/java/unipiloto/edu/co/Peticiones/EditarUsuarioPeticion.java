package unipiloto.edu.co.Peticiones;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditarUsuarioPeticion extends StringRequest {
    private static final String URL = "http://10.0.2.2/EditarUsuario.php";
    private Map<String, String> params;

    public EditarUsuarioPeticion(int id, String nombre, String email, String telefono, String direccion, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("id", String.valueOf(id));
        params.put("nombre", nombre);
        params.put("email", email);
        params.put("telefono", telefono);
        params.put("direccion", direccion);

        if (!password.isEmpty()) {
            params.put("password", password);
        }

        Log.d("VolleyParams", "Enviando datos: " + params.toString());
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
