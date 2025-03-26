package unipiloto.edu.co.Peticiones;


import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RegistrarPaseoPeticion extends StringRequest {
    private static final String URL = "http://10.0.2.2/RegistroPaseos.php";
    private final Map<String, String> parametros;

    public RegistrarPaseoPeticion(
            String nombreDueno,
            String nombreMascota,
            String idRealizador,
            String fechaPaseo,
            String horaInicio,
            String horaFin,
            String distancia,
            String comentarios,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {

        super(Request.Method.POST, URL, listener, errorListener);
        parametros = new HashMap<>();
        parametros.put("nombre_dueño", nombreDueno);
        parametros.put("nombre_mascota", nombreMascota);
        parametros.put("id_realizador", idRealizador);
        parametros.put("fecha_paseo", fechaPaseo);
        parametros.put("hora_inicio", horaInicio);
        parametros.put("hora_fin", horaFin);
        parametros.put("distancia", distancia);
        parametros.put("comentarios", comentarios);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}

