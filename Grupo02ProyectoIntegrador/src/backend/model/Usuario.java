package backend.model;

public class Usuario {
    private int numeroUsuario;
    private int dni;
    private String nombre;
    private String direccion;
    private String telefono;
    private int cantidadLibrosPrestados;
    
    public Usuario(int numeroUsuario, int dni, String nombre, String direccion, String telefono,int cantidadLibrosPrestados) {
        this.numeroUsuario = numeroUsuario;
        this.dni = dni;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.cantidadLibrosPrestados = cantidadLibrosPrestados;
    }

    public int getNumeroUsuario() {
        return numeroUsuario;
    }

    public void setNumeroUsuario(int numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getCantidadLibrosPrestados() {
        return cantidadLibrosPrestados;
    }

    public void setCantidadLibrosPrestados(int cantidadLibrosPrestados) {
        this.cantidadLibrosPrestados = cantidadLibrosPrestados;
    }

    @Override
    public String toString() {
        return "Nro Usuario: " + numeroUsuario +
           "\nDNI: " + dni +
           "\nNombre: " + nombre +
           "\nDireccion: $" + direccion +
           "\nTelefono: " + telefono +
           "\nCantidad Libros Prestados: " + cantidadLibrosPrestados +
           "\n********************************";
    }
    
    
}
