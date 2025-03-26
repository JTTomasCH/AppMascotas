package unipiloto.edu.co.Peticiones;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistroDatosMedicosPeticion  extends StringRequest {

    private static final String MASCOTAS_URL = "http://10.0.2.2/RegistroDatosM.php";

    private final Map<String, String> parametros;

    public RegistroDatosMedicosPeticion(
            String tipo,
            String fecha,
            String nombreVeterinaria,
            String nombreVeterinario,
            String nombreProcedimiento,
            String descripcion,
            String id_mascotas,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        super(Request.Method.POST, MASCOTAS_URL, listener, errorListener);

        parametros = new HashMap<>();
        parametros.put("TIPO", tipo);
        parametros.put("FECHA", fecha);
        parametros.put("NOMBREVETERINARIA", nombreVeterinaria);
        parametros.put("NOMBREVETERINARIO", nombreVeterinario);
        parametros.put("NOMBREPROCEDIMIENTO", nombreProcedimiento);
        parametros.put("DESCRIPCION", descripcion);
        parametros.put("ID_MASCOTA", id_mascotas);
    }
    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}

