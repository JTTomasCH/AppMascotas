package unipiloto.edu.co.Peticiones;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistrarCitaPeticion extends StringRequest {

    private static final String URL_CITA = "http://10.0.2.2/RegistrarCita.php";
    private final Map<String, String> parametros;

    public RegistrarCitaPeticion(
            String mascotaID,
            String tipoCita,
            String fecha,
            String hora,
            String notas,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {

        super(Request.Method.POST, URL_CITA, listener, errorListener);

        parametros = new HashMap<>();
        parametros.put("mascota_id", mascotaID);
        parametros.put("tipo_cita", tipoCita);
        parametros.put("fecha", fecha);
        parametros.put("hora", hora);
        parametros.put("notas", notas);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}
