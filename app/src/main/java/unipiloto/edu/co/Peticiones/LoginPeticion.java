package unipiloto.edu.co.Peticiones;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginPeticion extends StringRequest {
    private static final String LOGIN_URL = "http://10.0.2.2/Login.php";
    private final Map<String, String> parametros;

    public LoginPeticion(
            String correo,
            String contrasena,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {

        super(Request.Method.POST, LOGIN_URL, listener, errorListener);

        parametros = new HashMap<>();
        parametros.put("username", correo);
        parametros.put("password", contrasena);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}