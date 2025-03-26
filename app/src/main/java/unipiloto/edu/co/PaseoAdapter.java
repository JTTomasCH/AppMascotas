package unipiloto.edu.co;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PaseoAdapter extends RecyclerView.Adapter<PaseoAdapter.ViewHolder> {

    private Context context;
    private List<Paseo> listaPaseos;

    public PaseoAdapter(Context context, List<Paseo> listaPaseos) {
        this.context = context;
        this.listaPaseos = listaPaseos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paseo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Paseo paseo = listaPaseos.get(position);
        holder.txtFecha.setText("📅 Fecha: " + paseo.getFecha());
        holder.txtHorario.setText("⏰ Inicio: " + paseo.getHoraInicio());
        holder.txtHorarioFin.setText("⏰ Fin: " + paseo.getHoraFin());
        holder.txtDistancia.setText("📏 Distancia: " + paseo.getDistancia() + " km");
        holder.txtEntrenador.setText("👤 Entrenador: " + paseo.getIdRealizador());
    }

    @Override
    public int getItemCount() {
        return listaPaseos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha, txtHorario, txtHorarioFin, txtDistancia, txtEntrenador;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFechaPaseo);
            txtHorario = itemView.findViewById(R.id.txtHorario);
            txtHorarioFin = itemView.findViewById(R.id.txtHorariofin);
            txtDistancia = itemView.findViewById(R.id.txtDistancia);
            txtEntrenador = itemView.findViewById(R.id.txtEntrenador);
        }
    }
}
