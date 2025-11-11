package backend.model;

public class Libro implements Comparable<Libro> {
    private int codigo;
    private String titulo;
    private String autor;
    private double precio;
    private boolean disponible;

    public Libro(int codigo, String titulo, String autor, double precio, boolean disponible) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.disponible = disponible;
    }
    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    @Override
    public int compareTo(Libro otro) {
        return Integer.compare(this.codigo, otro.codigo);
    }
    
    @Override
    public String toString() {
         return "Código: " + codigo +
           "\nTítulo: " + titulo +
           "\nAutor: " + autor +
           "\nPrecio: $" + precio +
           "\nDisponible: " + (disponible ? "Sí" : "No") +
           "\n********************************";
    }
}
