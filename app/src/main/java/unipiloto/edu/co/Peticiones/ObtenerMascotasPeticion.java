package unipiloto.edu.co.Peticiones;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class ObtenerMascotasPeticion extends StringRequest {
    private static final String URL_MASCOTAS = "http://10.0.2.2/obtener_mascotas.php";
    private final Map<String, String> parametros;

    public ObtenerMascotasPeticion(
            String idUsuario,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        super(Request.Method.POST, URL_MASCOTAS, listener, errorListener);
        parametros = new HashMap<>();
        parametros.put("id_usuario", idUsuario);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}

