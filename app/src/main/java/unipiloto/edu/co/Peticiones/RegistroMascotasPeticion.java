package unipiloto.edu.co.Peticiones;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistroMascotasPeticion extends StringRequest {

    private static final String MASCOTAS_URL = "http://10.0.2.2/RegistroMascotas.php";

    private final Map<String, String> parametros;

    public RegistroMascotasPeticion(
            String nombre,
            String raza,
            String edad,
            String peso,
            String genero,
            String color,
            String foto,
            String userID,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {

        super(Request.Method.POST, MASCOTAS_URL, listener, errorListener);

        parametros = new HashMap<>();
        parametros.put("NOMBRE", nombre);
        parametros.put("RAZA", raza);
        parametros.put("EDAD", edad);
        parametros.put("PESO", peso);
        parametros.put("GENERO", genero);
        parametros.put("COLOR", color);
        parametros.put("FOTO_BASE64", foto);
        parametros.put("user_id", String.valueOf(Integer.parseInt(userID.trim())));
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }

}

