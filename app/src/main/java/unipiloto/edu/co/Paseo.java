package unipiloto.edu.co;

public class Paseo {
    private int id;
    private String fecha;
    private String horaInicio;
    private String horaFin;
    private String distancia;
    private String idRealizador;

    public Paseo(int id, String fecha, String horaInicio, String horaFin, String distancia, String idRealizador) {
        this.id = id;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.distancia = distancia;
        this.idRealizador = idRealizador;
    }

    public int getId() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public String getDistancia() {
        return distancia;
    }

    public String getIdRealizador() {
        return idRealizador;
    }

    @Override
    public String toString() {
        return "Paseo{" +
                "id=" + id +
                ", fecha='" + fecha + '\'' +
                ", horaInicio='" + horaInicio + '\'' +
                ", horaFin='" + horaFin + '\'' +
                ", distancia='" + distancia + '\'' +
                ", idRealizador='" + idRealizador + '\'' +
                '}';
    }
}
