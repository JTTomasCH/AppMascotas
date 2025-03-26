package unipiloto.edu.co;
public class Mascota {
    private int id;
    private String nombre;
    private String raza;
    private int edad;
    private String fotoUrl;


    public Mascota(int id, String nombre, String raza, int edad,  String fotoUrl) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.fotoUrl=fotoUrl;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEdadFormateada() {
        return edad == 1 ? edad + " año" : edad + " años";
    }
}