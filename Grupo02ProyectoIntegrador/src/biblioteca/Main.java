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
    private static int cantidadUsuarios =0;
    
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
                    int opcionAuxiliar;
                    do {
                        mostrarSubMenu();
                        opcionAuxiliar = Helper.getPositiveInteger(sc, "Ingrese una Opcion: ");
                        switch (opcionAuxiliar) {
                            case 1:
                                registrarLibro();
                                break;
                            case 2:
                                registrarUsuario();
                                break;
                            case 3:
                                int codigoLibro = Helper.getPositiveInteger(sc,"Ingrese el código del libro: ");
                                buscarLibro(codigoLibro);
                                break;
                            case 4:
                                int codigoUsuario = Helper.getPositiveInteger(sc,"Ingrese el código del usuario: ");
                                buscarUsuarioPorDni(codigoUsuario);
                                break;
                            case 5:
                                System.out.println("Saliendo de Registro y Busqueda...");
                                break;
                            default:
                                System.out.println("Opcion no Valida");;
                        }
                    } while (opcionAuxiliar != 5);
                   break;
                case 2:
                    prestamoDeLibros();
                    break;
                case 3:
                    devolverLibro(sc);
                    break;
                case 4:
                    reversionDeOperaciones();
                    break;
                case 5:
                    atenderPendientes(pendientes, arregloLibros, cantidadLibros, acciones);
                    break;
                case 6:
                    int opcionSuplementaria;
                    do {
                        mostrarMenuDeConsultas();
                        opcionSuplementaria = Helper.getPositiveInteger(sc, "Ingrese una Opcion: ");
                        switch (opcionSuplementaria) {
                            case 1:
                                mostrarTodosLosLibros();
                                break;
                            case 2:
                                mostrarTodosLosUsuarios();
                                break;
                            case 3:
                                double montoTotalDeLibrosPrestados=mostrarMontoTotalLibrosPrestados(arregloLibros);
                                System.out.println("El monto total al que ascienden los libros que se encuentran en prestamo es: "+ montoTotalDeLibrosPrestados);
                                break;
                            case 4:
                                String autor= Helper.validarString(sc,"Ingrese la subcadena del autor a buscar: "); 
                                listarLibrosAutor(autor.toLowerCase());
                                break;
                            case 5:
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
                            case 6:
                                System.out.println("Saliendo de Consultas...");
                                break;
                            default:
                                System.out.println("Opcion no Valida");
                        }
                    } while (opcionSuplementaria != 6);
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
        System.out.println("1 - Registro y Busqueda de Datos");
        System.out.println("2 - Préstamo de libros");
        System.out.println("3 - Devolución de libros");
        System.out.println("4 - Reversión de operaciones");
        System.out.println("5 - Atención de pendientes");
        System.out.println("6 - Consultas");
        System.out.println("0 - Salir");
        System.out.println("==========================================");
    }

    //SubMenu para Registro y Busqueda
    private static void mostrarSubMenu(){
        System.out.println("=== Registro y Busqueda de Datos ===");
        System.out.println("1 - Registro de libros");
        System.out.println("2 - Registro de usuarios");
        System.out.println("3 - Búsqueda de libros");
        System.out.println("4 - Búsqueda de usuario");
        System.out.println("5 - Salir");
    }

    //Menu de Consultas
    private static void mostrarMenuDeConsultas(){
        System.out.println("=== Menu de Consultas ===");
        System.out.println("1 - Mostrar todos los libros de la biblioteca");
        System.out.println("2 - Mostrar la información de todos los usuarios");
        System.out.println("3 - Mostrar el monto total de los libros en préstamo");
        System.out.println("4 - Crear lista con libros cuyo autor contenga una subcadena");
        System.out.println("5 - Crear lista con usuarios que tienen cantidad igual o superior a x");
        System.out.println("6 - Salir");
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

        System.out.println(arbolLibros.toString());
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

    //Metodo 2 Registro de Usuario
    public static void registrarUsuario() {
        System.out.println("----Registro de Usuarios:----");
        int nuevos = Helper.validarEntero(sc, "Ingrese cuántos usuarios desea ingresar: ");
        if (cantidadUsuarios + nuevos > arregloUsuarios.length) {
            System.out.println("Error: no hay espacio suficiente para registrar " + nuevos + " usuarios.");
            return;
        }
        for (int i = 0; i < nuevos; i++) {
            System.out.println("Usuario " + (cantidadUsuarios + i + 1) + ":");
            int numUsuario;
            do {
                numUsuario = random.nextInt(100) + 1;
            } while (buscarUsuarioPorCodigoEnArreglo(numUsuario) != null);
            
            int dniUsuario = Helper.validarDni(sc, "Ingrese el DNI del Usuario: ");
            while (buscarUsuarioPorDni(dniUsuario) != null) {
                System.out.println("Ese DNI ya está registrado. Intente con otro.");
                dniUsuario = Helper.validarDni(sc, "Ingrese el DNI del Usuario: ");
            }
            String nombreUsuario = Helper.validarString(sc, "Ingrese el Nombre del Usuario: ");
            String direccionUsuario = Helper.validarString(sc, "Ingrese la Dirección del Usuario: ");
            String telefonoUsuario = Helper.validarString(sc, "Ingrese el Teléfono del Usuario: ");
            int cantidadLibrosPrestados = 0;

            arregloUsuarios[cantidadUsuarios + i] = new Usuario(
                numUsuario, dniUsuario, nombreUsuario, direccionUsuario, telefonoUsuario, cantidadLibrosPrestados
            );
        }

        cantidadUsuarios += nuevos;
        System.out.println("El Registro de los usuario esta completado.");
    }

        //Metodo 3: Busqueda de libros
    public static void buscarLibro(int codigoLibro){
        System.out.println("----Busqueda de Libros:----");
        Libro libroBuscar = new Libro(codigoLibro, "", "", 0.0,true); //libro temporal
        Libro libroEncontrado = arbolLibros.buscar(libroBuscar);

        if( libroEncontrado != null){
            System.out.println("Libro encontrado:");
            System.out.println(libroEncontrado.toString());
        }else {
            System.out.println("No se encontrÃ³ ningun libro con ese cÃ³digo");
        }
    }

    //metodo 4: Buscar usuario en el arreglo
    public static Usuario buscarUsuarioPorCodigoEnArreglo(int numUsuario) {
    	for (int i = 0; i < cantidadUsuarios; i++) {
            if (arregloUsuarios[i] != null && arregloUsuarios[i].getNumeroUsuario() == numUsuario) {
                return arregloUsuarios[i];
            }
        }
        return null;
    }
    public static Usuario buscarUsuarioPorDni(int dni) {
        for (int i = 0; i < cantidadUsuarios; i++) {
            if (arregloUsuarios[i] != null && arregloUsuarios[i].getDni() == dni) {
                return arregloUsuarios[i];
            }
        }
        return null;
    }

    //Metodo 5: Prestamo de libros
    private static void prestamoDeLibros() {
        int codigo= Helper.getInteger(sc, "Ingrese el codigo del libro");
        int codigoUsuario= Helper.getInteger(sc, "Ingrese el codigo del usuario: ");
        System.out.println("Ingrese fecha del prestamo(AAAA-MM-DD) por ejemplo:2025-02-20");
        String fecha= sc.nextLine();
        Usuario usuario=buscarUsuarioPorCodigoEnArreglo(codigoUsuario);
        Libro libro=arbolLibros.buscarPorCodigo(codigo);
        if (libro.isDisponible()==true) {        //Si el libro esta disponible, se crea un objeto operacion que se guardara en la lsta acciones
            usuario.incrementarLibrosPrestados();
            Operacion operacion= new Operacion("Prestamo", libro, usuario,LocalDate.parse(fecha));
            acciones.push(operacion);
            System.out.println("La operacion de prestamo aceptado");
        }else if(libro.isDisponible()==false) {     //Si el libro no esta disponible el ususario se guardara en la cola pendientes
            pendientes.add(usuario);
            System.out.println("La operacion de de prestamo fue rechazado por que el libro solicitado no esta disponible");
        }
        
    }

    //Metodo 6: devolver libro
    public static void devolverLibro(Scanner sc) {
        System.out.print("Número de dni de usuario: ");
        int dniUsuario = sc.nextInt();
        System.out.print("Código de libro: ");
        int codigoLibro = sc.nextInt();
        sc.nextLine();

        Usuario usuario = buscarUsuarioPorDni(dniUsuario);
        Libro libro = arbolLibros.buscarPorCodigo(codigoLibro);
        usuario.setCantidadLibrosPrestados(1);

        libro.setDisponible(false);

        if (usuario != null && libro != null) {
            if (!libro.isDisponible()) {
                libro.setDisponible(true);
                usuario.decrementarPrestamos();
                acciones.push(new Operacion("Devolucion", libro, usuario, LocalDate.now()));
                System.out.println("Devolución realizada.");
                System.out.println(usuario.getCantidadLibrosPrestados() +"   "+ libro.toString());
            } else {
                System.out.println("Ese libro ya estaba disponible.");
            }
        }else if(usuario == null){
            System.out.println("Usuario no encontrado.");
        }else if(libro ==null){
            System.out.println("libro no encontrado");
        }
    }

    //Metodo 7: Reversion de operaciones en la pila acciones(de prestamo a devolucion y de devolucion a prestamo)
    private static void reversionDeOperaciones(){
        Operacion accion= acciones.pop();
        if(accion.getTipoOperacion().equalsIgnoreCase("Prestamo")){
            accion.setTipoOperacion("Devolucion");
            acciones.push(accion);
            System.out.println("La reversion de operacion fue exitosa");
        }else if(accion.getTipoOperacion().equalsIgnoreCase("Devolucion")) {
            accion.setTipoOperacion("Prestamo");
            acciones.push(accion);
            System.out.println("La reversion de operacion fue exitosa");
        }
    }

    //Metodo 8: Atencion a Pendientes
    private static void atenderPendientes(Queue<Usuario> pendientes, Libro[] arregloLibros, int cantidadLibros, StackGenerica<Operacion> acciones){
        if(pendientes.isEmpty()){
            System.out.println("No hay usuarios en la cola de espera. ");
            return;
        }

        int numeroUsuario = Helper.getPositiveInteger(sc, "Ingrese el Numero del Usuario: ");
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

        int codigoLibro = Helper.getPositiveInteger(sc, "Ingrese el Codigo del Libro: ");
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

    //Metodo 9: Mostrar todos los libros de la biblioteca
    private static void mostrarTodosLosLibros(){
        System.out.println("Todos los libros de la biblioteca: ");
        for (Libro libro : arregloLibros) {
            System.out.println(libro);
        }
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
             //   System.out.println(usuario.toString()); //NO CONVIENE HACER SOLAMENTE ESTA LINEA?
                System.out.println("  Numero de Usuario: " + usuario.getNumeroUsuario());
                System.out.println("  DNI: " + usuario.getDni());
                System.out.println("  Nombre: " + usuario.getNombre());
                System.out.println("  Direccion: " + usuario.getDireccion());
                System.out.println("  Telefono: " + usuario.getTelefono());
                System.out.println("  Cantidad de libros prestados: " + usuario.getCantidadLibrosPrestados());
                System.out.println();
            }
        }
        
        System.out.println("--- Fin del listado ---");
    }

    //Metodo 11 mostrar el monto total de los libros que se encuentran en prestamo
    public static double mostrarMontoTotalLibrosPrestados(Libro[] arregloLibros) {
    	if(arregloLibros == null || arregloLibros.length == 0) {
    		System.out.println("No hay libros registrados.");
    		return 0;
    	}
    	double montoTotalLibros=0;
    	int cantidadPrestados=0;
    	for (Libro libro : arregloLibros) {
    		if(libro != null && !libro.isDisponible()) {
    			montoTotalLibros+= libro.getPrecio();
    			cantidadPrestados++;
    		}
		}
    	if(cantidadPrestados==0) {
    		System.out.println("No se encontraron libros prestados.");
    		return 0;
    	}else {
    		System.out.println("Los libros que se encuentran en estado de prestamos son: "+ cantidadPrestados);
    		return montoTotalLibros;
    	}
    }

        //Metodo 12: crear una lista con los libros cuyo autor contenga una subcadena 
    public static void listarLibrosAutor(String autor){
        System.out.println("--- Listar Libros por Autor---");
        int librosEncontrados = 0;
        SimpleLinkedList<Libro> listaLibrosAutor = new SimpleLinkedList<>();
        for(int i = 0; i < cantidadLibros; i++){
            Libro libroActual = arregloLibros[i];
            if(libroActual != null && libroActual.getAutor().toLowerCase().contains(autor.toLowerCase())){
                listaLibrosAutor.addLast(libroActual);
                librosEncontrados++;
            }
        }

        if(listaLibrosAutor.size()==0){
            System.out.println("No se encontraron libros que contengan: "+autor);
        }else{
            System.out.println("Libros encontrados: "+librosEncontrados+" que contienen: "+autor+"\n");
            for(Libro libro : listaLibrosAutor){
                System.out.println(libro.toString());
            }
        }
    }

    //Metodo 13: Listar Usuarios que se Prestaron una x cantidad de Libros
    private static DoubleLinkedList<Usuario> listarUsuariosConLibrosPrestados(Usuario[] arrelgUsuarios){
        Scanner input = new Scanner(System.in);
        DoubleLinkedList<Usuario> listaUsuarios = new DoubleLinkedList<>();
        int cantidadLibrosPrestados = Helper.getPositiveInteger(input, "Ingrese la Cantidada de Libros Prestados: ");
        for(int i = 0; i < arregloUsuarios.length; i++){
            if(arregloUsuarios[i] != null && arregloUsuarios[i].getCantidadLibrosPrestados() >= cantidadLibrosPrestados){
                listaUsuarios.addFirst(arregloUsuarios[i]);
            }
        }

        return listaUsuarios;
    }    


}
