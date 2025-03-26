package unipiloto.edu.co.Peticiones;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class ObtenerDueñosPeticion extends StringRequest {
    private static final String URL_DUENOS = "http://10.0.2.2/obtener_duenos.php";
    private final Map<String, String> parametros;

    public ObtenerDueñosPeticion(
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL_DUENOS, listener, errorListener);
        parametros = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}