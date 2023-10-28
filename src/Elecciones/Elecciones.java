package Elecciones;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;


public class Elecciones {

    public static void main(String[] args) {
        Scanner tc = new Scanner(System.in);
        
        
        realizarConfiguracionInicial();
        boolean t = true;
        while(t){
            // Menu
            System.out.println("Menú Principal");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registro");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            int opc = tc.nextInt();
            tc.nextLine();
            switch(opc){
                case 1:
                    String correo = iniciarSesion();
                    String rol = identificarRol(correo);
                    if(rol.equals("Administrador")){
                        Scanner tc2 = new Scanner(System.in);
                        System.out.println("Opciones disponibles segun su Rol" + rol);
                        //System.out.println("1. Crear Eleccion");
                        
                        int opc2 = 0;
                                do{
                                // Opción 1 para el rol actual
                                System.out.println("Rol Administrador");
                                System.out.println("1. Crear Elecciones");
                                System.out.println("2. Inscribir Candidato");
                                System.out.println("3. Eliminar Candidato");
                                System.out.println("4. Salir");
                                opc2 = tc2.nextInt();
                                switch(opc2){
                                    case 1: 
                                        
                                        crearEleccion();
                                        break;
                                    case 2: 
                                        System.out.println("Nombre de la Eleccion");
                                        String nombreEleccion = tc2.next();
                                        inscribirCandidato(nombreEleccion);
                                    case 3: 
                                        System.out.println("Nombre de la Eleccion");
                                        String nombreEleccion2 = tc2.next();
                                        System.out.println("Nombre de Candidato");
                                        String nombreCandidato = tc2.next();
                                        eliminarCandidato(nombreEleccion2, nombreCandidato);
                                        break;
                                    case 4:
                                        opc = 5;
                                        break;
                                    default: 
                                        System.out.print("Opcion invalida");
                                    }
                                    }while(opc != 5);

                        
                        
                    }else if(rol.equals("Votante")){
                        System.out.println("Opciones disponibles segun su Rol" + rol);
                        System.out.println("1. Votar");
                        int v = tc.nextInt();
                        if(v == 1 ){
                            System.out.println("Escriba el nombre de la Eleccion");
                            String nombreEleccion = tc.next();
                            System.out.println("Escriba el nombre del Candidato");
                            String nombreCandidato = tc.next();
                            System.out.println("Escriba su nombre");
                            String nombreVotante = tc.next();
                            emitirVoto(nombreEleccion,nombreCandidato,nombreVotante);
                        }
                        
                        
                        
                    }else if(rol.equals("Auditor")){
                        System.out.println("Opciones disponibles segun su Rol" + rol);
                        System.out.println("1. Generar Reportes");
                        int gr = tc.nextInt();
                        
                        if(gr==1){
                        mostrarInforme();
                        }
                            
                    }else if(rol.equals("RegistradorDeVotante")){
                        System.out.println("Opciones disponibles segun su Rol" + rol);
                        System.out.println("1. Registrar Votante");
                        int rv = tc.nextInt();
                        
                        if(rv == 1){
                            registrarVotante();}
                    }
                    
                    break;
                case 2:
                    System.out.println("Registro");
                    agregarUsuario();
                    break;
                case 3:
                    t = false;
                    break;
                default:
                    break;
            }
            
        }// fin true
        
        
    }// Fin main
    
    // Metodo de configuracion inicial 
    public static void realizarConfiguracionInicial() {
        File archivoConfig = new File("docs/config.txt");

        if (archivoConfig.exists()) {
            System.out.println("La configuración ya ha sido realizada anteriormente.");
        } else {
            Scanner scanner = new Scanner(System.in);

            try {
                System.out.println("Configuración inicial: Por favor, ingrese la contraseña de administrador:");
                String contraseniaAdmin = scanner.nextLine();

                // Crear un FileWriter para escribir en el archivo de configuración
                FileWriter fileWriter = new FileWriter(archivoConfig);

                // Escribir la contraseña de administrador en el archivo "config"
                fileWriter.write(contraseniaAdmin);
                fileWriter.close();

                System.out.println("Configuración inicial completada. La contraseña de administrador se ha guardado en 'config'.");
            } catch (IOException e) {
                System.err.println("Error al realizar la configuración inicial.");
                e.printStackTrace();
            }
        }
    }
    
    // Metodo inicio de sesion  
    public static String iniciarSesion() {
        Scanner scanner = new Scanner(System.in);
        String correoIngresado = "";
        System.out.println("Inicio de Sesión:");
        System.out.print("Ingrese su correo: ");
        correoIngresado = scanner.nextLine();
        System.out.print("Ingrese su contraseña: ");
        String contraseniaIngresada = scanner.nextLine();

        // Validar las credenciales leyendo el archivo "usuarios.txt"
        if (validarCredenciales(correoIngresado, contraseniaIngresada)) {
            // Usuario autenticado
            System.out.println("Inicio de sesión exitoso.");
            // Aquí debes determinar el rol del usuario y mostrar las funcionalidades correspondientes.
        } else {
            System.out.println("Credenciales incorrectas. Inicio de sesión fallido.");
        }
        return correoIngresado;
    }
    
    // Metodo para validar 
    private static boolean validarCredenciales(String correo, String contrasenia) {
        try (BufferedReader reader = new BufferedReader(new FileReader("docs/usuarios.txt"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                String correoRegistrado = partes[2]; // Suponiendo que el correo es el tercer elemento
                String contraseniaRegistrada = partes[3]; // Suponiendo que la contraseña es el cuarto elemento

                if (correoRegistrado.equals(correo) && contraseniaRegistrada.equals(contrasenia)) {
                    return true; // Credenciales válidas
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de usuarios.");
            e.printStackTrace();
        }
        return false; // Credenciales incorrectas
    }
    
    // Metodo para registrarse
    public static void agregarUsuario() {
        String rol = "";
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Ingrese nombre: ");
            String nombre = scanner.nextLine();

            System.out.println("Ingrese apellido: ");
            String apellido = scanner.nextLine();

            System.out.println("Ingrese correo: ");
            String correo = scanner.nextLine();

            System.out.println("Ingrese contraseña: ");
            String pass = scanner.nextLine();

            System.out.println("Ingrese rol: ");
            rol = scanner.nextLine();

            // Crear un FileWriter para escribir en el archivo
            FileWriter fileWriter = new FileWriter("docs/usuarios.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Escribir los datos en el archivo
            printWriter.println(nombre + "," + apellido + "," + correo + "," + pass + "," + rol);

            // Cerrar el archivo
            printWriter.close();
            System.out.println("Usuario agregado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo.");
            e.printStackTrace();
        }
    }
    
    // Validar Rol
    public static String identificarRol(String correoBuscado) {
        // Ruta al archivo CSV
        String archivo = "docs/usuarios.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en valores separados por comas
                String[] valores = linea.split(",");
                
                // Verificar si la línea tiene al menos cinco valores
                if (valores.length >= 5) {
                    // El tercer valor (índice 2) es el correo
                    String correo = valores[2].trim();
                    
                    // Comparar con el correo buscado
                    if (correo.equals(correoBuscado)) {
                        // Si es el correo buscado, retornar el quinto valor (índice 4) que es el rol
                        return valores[4].trim();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Si no se encuentra el correo, puedes devolver un valor predeterminado o lanzar una excepción, según tus necesidades.
        return "Correo no encontrado";
    }

    // Crear Eleccion
    public static void crearEleccion() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Creación de Elección:");
            System.out.println("Ingrese el Título de la Elección:");
            String titulo = scanner.nextLine();

            System.out.println("Ingrese el Propósito de la Elección:");
            String proposito = scanner.nextLine();

            System.out.println("Ingrese una Descripción de la Elección:");
            String descripcion = scanner.nextLine();

            System.out.println("Ingrese un Código de Identificación Único:");
            String codigoIdentificacion = scanner.nextLine();

            System.out.println("Ingrese la Fecha y Hora de Inicio de Inscripción (YYYY-MM-DD HH:MM):");
            String fechaInicioInscripcion = scanner.nextLine();

            System.out.println("Ingrese la Fecha y Hora de Fin de Inscripción (YYYY-MM-DD HH:MM):");
            String fechaFinInscripcion = scanner.nextLine();

            // Crear un FileWriter para escribir en el archivo "elecciones.txt"
            FileWriter fileWriter = new FileWriter("docs/elecciones.txt", true);
            fileWriter.write(
                titulo + "," + proposito + "," + descripcion + "," + codigoIdentificacion + "," +
                fechaInicioInscripcion + "," + fechaFinInscripcion + System.lineSeparator()
            );
            fileWriter.close();

            System.out.println("Elección creada con éxito.");
        } catch (IOException e) {
            System.err.println("Error al crear la elección.");
            e.printStackTrace();
        }
    }
    
    // Inscribir Candidato
    public static boolean eleccionExiste(String nombreEleccion) {
        try {
            // Leer el archivo de elecciones y verificar si el nombre coincide
            File archivoElecciones = new File("docs/elecciones.txt");
            Scanner scanner = new Scanner(archivoElecciones);

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(",");
                String nombreEleccionEnArchivo = partes[0];

                if (nombreEleccionEnArchivo.equals(nombreEleccion)) {
                    scanner.close();
                    return true; // La elección existe
                }
            }

            scanner.close();
        } catch (IOException e) {
            System.err.println("Error al verificar si la elección existe.");
            e.printStackTrace();
        }

        return false; // La elección no existe
    }
    
   //inscribir candidatos en una elección
    public static void inscribirCandidato(String nombreEleccion) {
        Scanner scanner = new Scanner(System.in);

        try {
            if (eleccionExiste(nombreEleccion)) {
                System.out.println("Inscripción de Candidato en la Elección '" + nombreEleccion + "':");

                // Solicitar información del candidato
                System.out.println("Ingrese el Nombre del Candidato:");
                String nombreCandidato = scanner.nextLine();

                System.out.println("Ingrese la Formación del Candidato:");
                String formacionCandidato = scanner.nextLine();

                System.out.println("Ingrese la Experiencia Profesional del Candidato:");
                String experienciaCandidato = scanner.nextLine();

                // Agregar el candidato a un archivo "candidatos.txt" asociándolo a la elección
                FileWriter fileWriter = new FileWriter("docs/candidatos.txt", true);
                fileWriter.write(
                    nombreCandidato + "," + formacionCandidato + "," + experienciaCandidato + "," + nombreEleccion + System.lineSeparator()
                );
                fileWriter.close();

                System.out.println("Candidato inscrito con éxito en la Elección '" + nombreEleccion + "'.");
            } else {
                System.out.println("La elección '" + nombreEleccion + "' no existe o no está disponible para inscripción de candidatos.");
            }
        } catch (IOException e) {
            System.err.println("Error al inscribir el candidato.");
            e.printStackTrace();
        }
    }
    
    // eliminar candidatos de una elección
    public static void eliminarCandidato(String nombreEleccion, String nombreCandidato) {
        try {
            // Leer el archivo de candidatos y crear un nuevo archivo temporal sin el candidato a eliminar
            File archivoCandidatos = new File("docs/candidatos.txt");
            File archivoTemporal = new File("docs/temporal.txt");

            Scanner scanner = new Scanner(archivoCandidatos);
            FileWriter fileWriter = new FileWriter(archivoTemporal, true);

            boolean encontrado = false;

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                String[] partes = linea.split(",");
                String candidatoNombre = partes[0];
                String eleccionNombre = partes[3];

                if (!candidatoNombre.equals(nombreCandidato) || !eleccionNombre.equals(nombreEleccion)) {
                    fileWriter.write(linea + System.lineSeparator());
                } else {
                    encontrado = true;
                }
            }

            scanner.close();
            fileWriter.close();

            if (encontrado) {
                archivoCandidatos.delete();
                archivoTemporal.renameTo(archivoCandidatos);
                System.out.println("Candidato '" + nombreCandidato + "' eliminado de la Elección '" + nombreEleccion + "'.");
            } else {
                System.out.println("Candidato '" + nombreCandidato + "' no encontrado en la Elección '" + nombreEleccion + "'.");
            }
        } catch (IOException e) {
            System.err.println("Error al eliminar el candidato.");
            e.printStackTrace();
        }
    }
    
    // Método para verificar si una persona es mayor de 18 años
    public static boolean esMayorDe18(String fechaNacimiento) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate fechaNac = LocalDate.parse(fechaNacimiento, formato);
            LocalDate fechaHoy = LocalDate.now();
            int edad = fechaHoy.getYear() - fechaNac.getYear();
            return edad >= 18;
        } catch (Exception e) {
            System.err.println("Error al verificar la edad.");
            return false;
        }
    }

    // Método para generar una contraseña aleatoria
    public static String generarContraseniaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder contrasenia = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(caracteres.length());
            contrasenia.append(caracteres.charAt(index));
        }

        return contrasenia.toString();
    }

    // Método para registrar votantes
    public static void registrarVotante() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Registro de Votante:");
            System.out.println("Ingrese Nombres Completos:");
            String nombres = scanner.nextLine();

            System.out.println("Ingrese Apellidos Completos:");
            String apellidos = scanner.nextLine();

            System.out.println("Ingrese CUI:");
            String cui = scanner.nextLine();

            System.out.println("Ingrese Sexo:");
            String sexo = scanner.nextLine();

            System.out.println("Ingrese Fecha de Nacimiento (DD/MM/AAAA):");
            String fechaNacimiento = scanner.nextLine();

            if (esMayorDe18(fechaNacimiento)) {
                System.out.println("Ingrese Dirección de Residencia:");
                String direccionResidencia = scanner.nextLine();

                System.out.println("Ingrese Departamento de Residencia:");
                String departamentoResidencia = scanner.nextLine();

                System.out.println("Ingrese Municipio de Residencia:");
                String municipioResidencia = scanner.nextLine();

                System.out.println("Ingrese País de Residencia:");
                String paisResidencia = scanner.nextLine();

                // Generar una contraseña aleatoria
                String contrasenia = generarContraseniaAleatoria();

                // Crear un FileWriter para escribir en el archivo "votante.txt"
                FileWriter fileWriter = new FileWriter("docs/votante.txt", true);
                fileWriter.write(nombres + "," + apellidos + "," + cui + "," + sexo + "," + fechaNacimiento + "," +
                        direccionResidencia + "," + departamentoResidencia + "," + municipioResidencia + "," +
                        paisResidencia + "," + contrasenia + System.lineSeparator());
                fileWriter.close();

                System.out.println("Votante registrado con éxito. Contraseña generada: " + contrasenia);
            } else {
                System.out.println("El votante debe ser mayor de 18 años para registrarse.");
            }
        } catch (IOException e) {
            System.err.println("Error al registrar el votante.");
            e.printStackTrace();
        }
    }
    
    // Método para mostrar un informe con usuarios, elecciones y votos
    public static void mostrarInforme() {
        System.out.println("Informe del Auditor");
        System.out.println("");
        System.out.println("");
        System.out.println("Usuarios Registrados:");
        System.out.println("");
        System.out.println("");
        mostrarContenidoArchivo("docs/usuarios.txt");
        System.out.println("");
        System.out.println("");
        System.out.println("\nElecciones Disponibles:");
        mostrarContenidoArchivo("docs/elecciones.txt");
        System.out.println("");
        System.out.println("");
        System.out.println("\nVotos Emitidos:");
        mostrarContenidoArchivo("docs/votos.txt");
        
    }
    
    // mostrar el contenido de un archivo
    public static void mostrarContenidoArchivo(String nombreArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + nombreArchivo);
            e.printStackTrace();
        }
    }

    // emitir un voto por un candidato y guardar el voto en un archivo
    public static void emitirVoto(String nombreEleccion, String nombreCandidato, String nombreVotante) {
        try {
            // Crear un FileWriter para escribir en el archivo "votos.txt"
            FileWriter fileWriter = new FileWriter("docs/votos.txt", true);
            fileWriter.write(
                nombreEleccion + "," + nombreCandidato + "," + nombreVotante + System.lineSeparator()
            );
            fileWriter.close();

            System.out.println("Voto emitido con éxito en la elección '" + nombreEleccion + "' por el candidato '" + nombreCandidato + "'.");
        } catch (IOException e) {
            System.err.println("Error al emitir el voto.");
            e.printStackTrace();
        }
    }
    

}



    

