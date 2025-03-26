package unipiloto.edu.co;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import unipiloto.edu.co.*;

import java.util.List;

public class MascotaAdapter extends ArrayAdapter<Mascota> {

    private Context context;
    private List<Mascota> listaMascotas;

    public MascotaAdapter(Context context, List<Mascota> listaMascotas) {
        super(context, R.layout.item_mascota, listaMascotas);
        this.context = context;
        this.listaMascotas = listaMascotas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mascota, parent, false);
        }

        Mascota mascota = listaMascotas.get(position);

        TextView tvNombreMascota = convertView.findViewById(R.id.tvNombreMascota);
        TextView tvRazaMascota = convertView.findViewById(R.id.tvRazaMascota);
        TextView tvEdadMascota = convertView.findViewById(R.id.tvEdadMascota);
        Button btnVerActividad = convertView.findViewById(R.id.btnVerActividad);

        tvNombreMascota.setText(mascota.getNombre());
        tvRazaMascota.setText(mascota.getRaza());
        tvEdadMascota.setText(mascota.getEdadFormateada());

        btnVerActividad.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActividadesMascotas.class);
            intent.putExtra("ID_MASCOTA", mascota.getId());
            intent.putExtra("mascota_nombre", mascota.getNombre());
            context.startActivity(intent);
        });

        return convertView;
    }
}
