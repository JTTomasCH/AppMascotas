package unipiloto.edu.co.Peticiones;

import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MascotasPeticion extends StringRequest {
    private static final String MASCOTAS_URL = "http://10.0.2.2/ObtenerMascotas.php";
    private final Map<String, String> parametros;

    public MascotasPeticion(
            String userID,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        super(Request.Method.POST, MASCOTAS_URL, listener, errorListener);
        parametros = new HashMap<>();
        parametros.put("user_id", userID);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}