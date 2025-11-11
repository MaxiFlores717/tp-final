package backend.model;

import java.time.LocalDate;

public class Operacion {
    private static int contador = 0;
    private int identificador;
    private String tipoOperacion;
    private Libro libro;
    private Usuario usuario;
    private LocalDate fecha;
    
    public Operacion(String tipoOperacion, Libro libro, Usuario usuario, LocalDate fecha) {
        this.identificador = contador++;
        this.tipoOperacion = tipoOperacion;
        this.libro = libro;
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Operacion [identificador=" + identificador + ", tipoOperacion=" + tipoOperacion + ", libro=" + libro
                + ", usuario=" + usuario + ", fecha=" + fecha + "]";
    }

    
}
