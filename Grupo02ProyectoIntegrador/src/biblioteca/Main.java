package biblioteca;

import backend.base.*;
import backend.help.Helper;
import backend.model.*;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

public class Main {
    
    // Estructuras de datos para libros
    private static Libro[] arregloLibros = new Libro[1000];
    private static int cantidadLibros = 0;
    private static BinarySearchTree<Libro> arbolLibros = new BinarySearchTree<>();
    
    // Estructuras de datos para usuarios
    private static Usuario[] arregloUsuarios = new Usuario[1000];
    private static int cantidadUsuarios = 0;
    
    // Estructuras auxiliares
    private static StackGenerica<Operacion> acciones = new StackGenerica<>();
    private static Queue<Usuario> pendientes = new Queue<>();
    
    private static Scanner sc = new Scanner(System.in);
    private static Random random = new Random();
    
    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = Helper.getInteger("Seleccione una opción: ");
            
            switch (opcion) {
                case 1:
                    registrarLibro();
                    break;
                case 2:
                    // Registro de usuarios 
                    break;
                case 3:
                    // Busqueda de libros 
                    break;
                case 4:
                    // Busqueda de usuario 
                    break;
                case 5:
                    // Prestamo de libros 
                    break;
                case 6:
                    // Devolucion de libros 
                    break;
                case 7:
                    // Reversion de operaciones 
                    break;
                case 8:
                    atenderPendientes(pendientes, arregloLibros, cantidadLibros, acciones);
                    break;
                case 9:
                    // Mostrar todos los libros 
                    break;
                case 10:
                    mostrarTodosLosUsuarios();
                    break;
                case 11:
                    // Monto total de libros en prestamo 
                    break;
                case 12:
                    // Lista de libros por autor 
                    break;
                case 13:
                    DoubleLinkedList<Usuario> usuariosConLibrosPrestados = listarUsuariosConLibrosPrestados(arregloUsuarios);
                    if(usuariosConLibrosPrestados.size() == 0){
                        System.out.println("No hay Usuarios con esa Cantidad de Libros Prestados. ");
                    }else{
                        System.out.println("Usuarios con mas Libros Prestados");
                        for (Usuario usuario : usuariosConLibrosPrestados) {
                            System.out.println(usuario.toString());
                        }
                    }
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);
        
    }
    
    private static void mostrarMenu() {
        System.out.println("=== SISTEMA DE GESTIÓN DE BIBLIOTECA ===");
        System.out.println("1 - Registro de libros");
        System.out.println("2 - Registro de usuarios");
        System.out.println("3 - Búsqueda de libros");
        System.out.println("4 - Búsqueda de usuario");
        System.out.println("5 - Préstamo de libros");
        System.out.println("6 - Devolución de libros");
        System.out.println("7 - Reversión de operaciones");
        System.out.println("8 - Atención de pendientes");
        System.out.println("9 - Mostrar todos los libros de la biblioteca");
        System.out.println("10 - Mostrar la información de todos los usuarios");
        System.out.println("11 - Mostrar el monto total de los libros en préstamo");
        System.out.println("12 - Crear lista con libros cuyo autor contenga una subcadena");
        System.out.println("13 - Crear lista con usuarios que tienen cantidad igual o superior a x");
        System.out.println("0 - Salir");
        System.out.println("==========================================");
    }
    
    // Método 1: Registro de libros
    private static void registrarLibro() {
        System.out.println("=== REGISTRO DE LIBRO ===");
        
        // Generar codigo aleatorio unico
        int codigo;
        boolean codigoUnico = false;
        do {
            codigo = random.nextInt(9000) + 1000; // Codigo entre 1000 y 9999
            if (buscarLibroPorCodigoEnArreglo(codigo) == null) {
                codigoUnico = true;
            }
        } while (!codigoUnico);
        
        System.out.println("Codigo generado automaticamente: " + codigo);
        
        // Solicitar datos del libro
        System.out.print("Ingrese el titulo del libro: ");
        String titulo = sc.nextLine();
        
        System.out.print("Ingrese el autor del libro: ");
        String autor = sc.nextLine();
        
        double precio = Helper.getPositiveDouble(sc, "Ingrese el precio del libro: ");
        
        // Crear el libro con disponible = true
        Libro nuevoLibro = new Libro(codigo, titulo, autor, precio, true);
        
        // Verificar que hay espacio en el arreglo
        if (cantidadLibros >= arregloLibros.length) {
            System.out.println("Error: No hay espacio disponible en el arreglo de libros.");
            return;
        }
        
        // Agregar al arreglo
        arregloLibros[cantidadLibros] = nuevoLibro;
        cantidadLibros++;
        
        // Agregar al arbol binario de busqueda
        try {
            arbolLibros.add(nuevoLibro);
            System.out.println("Libro registrado exitosamente!");
            System.out.println("Informacion del libro:");
            System.out.println(nuevoLibro.toString());
        } catch (Exception e) {
            // Si hay error al agregar al arbol, revertir el arreglo
            cantidadLibros--;
            arregloLibros[cantidadLibros] = null;
            System.out.println("Error al registrar el libro en el árbol: " + e.getMessage());
        }
    }
    
    // Metodo auxiliar para buscar un libro por codigo en el arreglo (para verificar unicidad)
    private static Libro buscarLibroPorCodigoEnArreglo(int codigo) {
        for (int i = 0; i < cantidadLibros; i++) {
            if (arregloLibros[i] != null && arregloLibros[i].getCodigo() == codigo) {
                return arregloLibros[i];
            }
        }
        return null;
    }

    //Metodo 8: Atencion a Pendientes
    private static void atenderPendientes(Queue<Usuario> pendientes, Libro[] arregloLibros, int cantidadLibros, StackGenerica<Operacion> acciones){
        Scanner input = new Scanner(System.in);
        if(pendientes.isEmpty()){
            System.out.println("No hay usuarios en la cola de espera. ");
            return;
        }

        int numeroUsuario = Helper.getPositiveInteger(input, "Ingrese el Numero del Usuario: ");
        Queue<Usuario> auxiliar = new Queue<>();
        Usuario encontrado = null;
        while(!pendientes.isEmpty()){
            Usuario usuario = pendientes.remove();
            if(usuario.getNumeroUsuario() == numeroUsuario){
                encontrado = usuario;
                auxiliar.add(usuario);
            }else{
                auxiliar.add(usuario);
            }
        }

        while(!auxiliar.isEmpty()){
            pendientes.add(auxiliar.remove());
        }
        if(encontrado == null){
            System.out.println("No se encontro al Usuario");
            return;
        }else{
            System.out.println("Atendiendo a usuario: " + encontrado);
        }

        int codigoLibro = Helper.getPositiveInteger(input, "Ingrese el Codigo del Libro: ");
        Libro libro = buscarLibroPorCodigoEnArreglo(codigoLibro);
        if(libro == null){
            System.out.println("Libro no Encontrado");
            return;
        }

        if(!libro.isDisponible()){
            System.out.println("Libro no Disponible. Usuario esta en la cola de Espera.");
            return;
        }

        pendientes.remove();
        libro.setDisponible(false);
        encontrado.setCantidadLibrosPrestados(encontrado.getCantidadLibrosPrestados() + 1);
        Operacion operacion = new Operacion("Prestamo", libro, encontrado, LocalDate.now());
        acciones.push(operacion);
        System.out.println("Prestamo registrado para Usuario atendido. ");
    }

    // Metodo 10: Mostrar la informacion de todos los usuarios
    private static void mostrarTodosLosUsuarios() {
        System.out.println("LISTADO DE TODOS LOS USUARIOS");
        
        if (cantidadUsuarios == 0) {
            System.out.println("No hay usuarios registrados en la biblioteca.");
            return;
        }
        
        System.out.println("Total de usuarios: " + cantidadUsuarios);
        System.out.println("--- Informacion de Usuarios ---");
        
        for (int i = 0; i < cantidadUsuarios; i++) {
            Usuario usuario = arregloUsuarios[i];
            if (usuario != null) {
                System.out.println("Usuario #" + (i + 1) + ":");
                System.out.println("  Numero de Usuario: " + usuario.getNumeroUsuario());
                System.out.println("  DNI: " + usuario.getDni());
                System.out.println("  Nombre: " + usuario.getNombre());
                System.out.println("  Direccion: " + usuario.getDirrecion());
                System.out.println("  Telefono: " + usuario.getTelefono());
                System.out.println("  Cantidad de libros prestados: " + usuario.getCantidadLibrosPrestados());
                System.out.println();
            }
        }
        
        System.out.println("--- Fin del listado ---");
    }

    //Metodo 13: Listar Usuarios que se Prestaron una x cantidad de Libros
    private static DoubleLinkedList<Usuario> listarUsuariosConLibrosPrestados(Usuario[] arregloUsuarios){
        Scanner input = new Scanner(System.in);
        DoubleLinkedList<Usuario> listaUsuarios = new DoubleLinkedList<>();
        int cantidadLibrosPrestados = Helper.getPositiveInteger(input, "Ingrese la Cantidada de Libros Prestados: ");
        for(int i = 0; i < arregloUsuarios.length; i++){
            if(arregloUsuarios[i] != null && arregloUsuarios[i].getCantidadLibrosPrestados() > cantidadLibrosPrestados){
                listaUsuarios.addFirst(arregloUsuarios[i]);
            }
        }

        return listaUsuarios;
    }
}


