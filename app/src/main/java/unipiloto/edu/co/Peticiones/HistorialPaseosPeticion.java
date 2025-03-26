package unipiloto.edu.co.Peticiones;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class HistorialPaseosPeticion extends StringRequest {
    private static final String URL = "http://10.0.2.2/ObtenerPaseos.php";
    private final Map<String, String> parametros;

    public HistorialPaseosPeticion(String mascotaID, String fecha, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL, listener, errorListener);
        parametros = new HashMap<>();
        parametros.put("mascota_id", mascotaID);
        parametros.put("fecha", fecha);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}
