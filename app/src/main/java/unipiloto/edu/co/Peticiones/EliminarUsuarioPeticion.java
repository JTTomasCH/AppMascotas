package unipiloto.edu.co.Peticiones;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EliminarUsuarioPeticion extends StringRequest {
    private static final String URL = "http://10.0.2.2/EliminarUsuario.php"; // Asegúrate de que este archivo existe en el servidor
    private Map<String, String> params;
    public EliminarUsuarioPeticion(String userID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("id", userID);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
