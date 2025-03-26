package unipiloto.edu.co.Peticiones;


import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RegistroPeticion extends StringRequest {
    private static final String REGISTER_URL = "http://10.0.2.2/Registro.php";
    private final Map<String, String> parametros;

    public RegistroPeticion(
            String nombre,
            String correo,
            String telefono,
            String direccion,
            String contrasena,
            String id_rol,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {

        super(Request.Method.POST, REGISTER_URL, listener, errorListener);

        parametros = new HashMap<>();
        parametros.put("NOMBRE", nombre);
        parametros.put("CORREO", correo);
        parametros.put("TELEFONO", telefono);
        parametros.put("DIRECCION", direccion);
        parametros.put("CONTRASENA", contrasena);
        parametros.put("ID_ROL", id_rol);
    }

    @Override
    public Map<String, String> getParams() {
        return parametros;
    }
}




