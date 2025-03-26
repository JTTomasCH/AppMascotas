package unipiloto.edu.co;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private int idRol;

    public Usuario(int id, String nombre, String email, String telefono, String direccion, int idRol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.idRol = idRol;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getIdRol() {
        return idRol;
    }
}
