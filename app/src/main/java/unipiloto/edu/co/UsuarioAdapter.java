package unipiloto.edu.co;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.List;
import unipiloto.edu.co.Peticiones.EliminarUsuarioPeticion;
import unipiloto.edu.co.R;

public class UsuarioAdapter extends ArrayAdapter<Usuario> {

    private Context context;
    private List<Usuario> listaUsuarios;

    public UsuarioAdapter(Context context, List<Usuario> listaUsuarios) {
        super(context, R.layout.item_usuario, listaUsuarios);
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        }

        Usuario usuario = listaUsuarios.get(position);

        TextView tvNombre = convertView.findViewById(R.id.tvNombreUsuario);
        TextView tvEmail = convertView.findViewById(R.id.tvEmailUsuario);
        TextView tvTelefono = convertView.findViewById(R.id.tvTelefonoUsuario);
        TextView tvDireccion = convertView.findViewById(R.id.tvDireccionUsuario);
        ImageButton btnEliminar = convertView.findViewById(R.id.btnEliminarUsuario);
        ImageButton btnEditar = convertView.findViewById(R.id.btnEditarUsuario); // Botón de editar

        tvNombre.setText(usuario.getNombre());
        tvEmail.setText(usuario.getEmail());
        tvTelefono.setText(usuario.getTelefono());
        tvDireccion.setText(usuario.getDireccion());

        // Acción de eliminar usuario
        btnEliminar.setOnClickListener(v -> mostrarConfirmacionEliminar(usuario.getId(), position));

        // Acción de editar usuario
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditarUsuarioActivity.class);
            intent.putExtra("ID", usuario.getId());
            intent.putExtra("NOMBRE", usuario.getNombre());
            intent.putExtra("EMAIL", usuario.getEmail());
            intent.putExtra("TELEFONO", usuario.getTelefono());
            intent.putExtra("DIRECCION", usuario.getDireccion());

            // Asegurar que el contexto sea correcto
            if (!(context instanceof android.app.Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            context.startActivity(intent);
        });

        return convertView;
    }

    private void eliminarUsuario(int userID, int position) {
        System.out.println("ID enviado para eliminar: " + userID); // 📌 Depuración en Logcat

        Response.Listener<String> responseListener = response -> {
            System.out.println("Respuesta del servidor al eliminar usuario: " + response);

            Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
            listaUsuarios.remove(position);
            notifyDataSetChanged();
        };

        Response.ErrorListener errorListener = error -> {
            System.out.println("Error al eliminar usuario: " + error.getMessage());
            Toast.makeText(context, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
        };

        EliminarUsuarioPeticion eliminarPeticion = new EliminarUsuarioPeticion(String.valueOf(userID), responseListener, errorListener);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(eliminarPeticion);
    }

    private void mostrarConfirmacionEliminar(int userID, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Confirmación")
                .setMessage("¿Está seguro de que desea eliminar este usuario?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarUsuario(userID, position))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
