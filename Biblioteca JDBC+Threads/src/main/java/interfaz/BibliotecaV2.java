package interfaz;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import biblioteca.Autoria;
import biblioteca.Libro;
import dao.AutoriaDAO;
import dao.Conexion;
import dao.LibroDAO;
import ficheros.EscritorTexto;
import ficheros.GestionaFicherosHilos;

public class BibliotecaV2 {
    private static ArrayList<Autoria> autorias = new ArrayList<>();
    private static ArrayList<Libro> libros = new ArrayList<>();
    private Scanner sc;

    public BibliotecaV2() {
        sc = new Scanner(System.in);
    }

    public ArrayList<Autoria> getAutorias() {
        return autorias;
    }

    /**
     * Añade un autor a la colección de autores
     * @param autor Objeto autor a añadir
     */
    private void anadirAutoria(Autoria autor) {
        autorias.add(autor);
    }

    /**
     * Devuelve la colección de libros
     * @return libros Colección de libros
     */
    private ArrayList<Libro> getLibros() {
        return libros;
    }

    /**
     * Devuelve el archivo donde se almacena la información de las colecciones en formato binario
     * @return File del archivo biblioteca.bin
     */
    private File getFileBinario(){
        File file = new File("./files/biblioteca.bin");
        return file;
    }


    /**
     * Crea un nuevo autor, y lo añade a la colección de autores
     */
    private void crearAutor() {
        boolean repetidor = false;
        int id = -1;
        do {
            if (!repetidor) {
                System.out.print("Introduce el ID del autor: ");
            } else {
                repetidor = false;
                System.out.println("El ID que has introducido ya existe!!");
                System.out.print("Vuelve a introducir el ID del autor: ");
            }
            id = numeroScanner();
            for (Autoria a : getAutorias()) {
                if (id == a.getId()) {
                    repetidor = true;
                }
            }
        } while (repetidor);
        System.out.print("Introduce el nombre del autor ");
        String nombre = sc.next();
        System.out.print("Introduce el apellido del autor ");
        String apellido = sc.next();
        Autoria autoria = new Autoria(id, nombre, apellido);
        anadirAutoria(autoria);
        System.out.println("Filas afectadas en la BBDD: " + AutoriaDAO.create(autoria));
    }

    /**
     * Crea un nuevo libro y lo añade a la colección de libros
     */
    private void crearLibro() {
        boolean repetidor = false;
        String isbn = "";
        do {
            if (!repetidor) {
                System.out.print("Introduce el ISBN del libro: ");
            } else {
                repetidor = false;
                System.out.println("El ISBN que has introducido ya existe!!");
                System.out.print("Vuelve a introducir el ISBN del libro: ");
            }
            isbn = sc.next();
            for (Libro l : getLibros()) {
                if (isbn.equals(l.getIsbn())) {
                    repetidor = true;
                }
            }
        } while (repetidor);
        System.out.print("Introduce el titulo del libro: ");
        String titulo = sc.next();
        System.out.print("Introduce el ID del autor: ");
        int id = numeroScanner();
        Autoria autor = null;
        for (Autoria a : getAutorias()) {
            if (a.getId() == id) {
                autor = a;
            }
        }
        if (autor != null) {
            Libro libro = new Libro(isbn, titulo, autor);
            getLibros().add(libro);
            System.out.println("Filas afectadas en la BBDD: " + LibroDAO.create(libro));
        } else {
            System.out.println("El ID introducido no existe");
            esperar();
        }
    }

    /**
     * Muestra todos los autores existentes en la colección de autores
     */
    private void verAutores() {
        boolean noAutores = true;
        for (Autoria a : getAutorias()) {
            System.out.println(a.toString());
            noAutores = false;
        }
        if (noAutores) {
            System.out.println("No existen autores!!");
        }
        esperar();
    }

    /**
     * Muestra todos los libros existentes en la colección de libros
     */
    private void verLibros() {
        boolean noLibros = true;
        for (Libro l : getLibros()) {
            System.out.println(l.toString());
            noLibros = false;
        }
        if (noLibros) {
            System.out.println("No existen libros!!");
        }
        esperar();
    }

    /**
     * Elimina un libro de la colección identificándolo por su ISBN
     */
    private void eliminarLibro() {
        System.out.print("Introduce el ISBN del libro a eliminar: ");
        boolean encontrado = false;
        Libro libro_eliminar = null;
        String isbn_eliminar = sc.next();
        for (Libro l : getLibros()) {
            if (isbn_eliminar.equals(l.getIsbn())) {
                encontrado = true;
                libro_eliminar = l;
            }
        }
        if (encontrado) {
            getLibros().remove(libro_eliminar);
            System.out.println("Filas afectadas en la BBDD: " + LibroDAO.delete(libro_eliminar.getIsbn()));
        } else {
            System.out.println("El ISBN introducido no existe!!");
            esperar();
        }
    }

    /**
     * Exporta la información de las colecciones a un fichero de texto introducido por el usuario
     */
    private void exportarFicheroTexto() {
        System.out.print("Introduce el archivo: ");
        String f = sc.next();
        while(f.equals("files/biblioteca.bin") || f.equals("./files/biblioteca.bin")){
            System.out.println("No puedes realizar la exportacion sobre este fichero!!");
            System.out.print("Introduce el archivo: ");
            f = sc.next();
        }
        File file = new File(f);
        boolean sobreescribir = false;
        boolean salir = false;
        if (file.exists()) {
            System.out.println("El archivo ya existe");
            while (!salir) {
                System.out.print("¿Quieres sobreescribirlo? (Y/N): ");
                String sobreescribir_eleccion = sc.next();
                if (sobreescribir_eleccion.toLowerCase().equals("y")) {
                    sobreescribir = true;
                    salir = true;
                } else if (sobreescribir_eleccion.toLowerCase().equals("n")) {
                    salir = true;
                } else {
                    System.out.print("Por favor, introduce una opcion valida");
                }
            }
        }
        GestionaFicherosHilos.escribirTexto(file,getAutorias(),getLibros(),sobreescribir);
    }

    /**
     * Importa la información del archivo de texto especificado por el usuario, creando las colecciones autorías y libros
     */
    private void importarFicheroTexto() {
        System.out.print("Introduce el archivo: ");
        String f = sc.next();
        File file = new File(f);
        GestionaFicherosHilos.leerTexto(file, getAutorias(), getLibros());
        exportarBBDD();
    }

    /**
     * Exporta al fichero 'biblioteca.bin', ubicado en la carpeta 'files', las colecciones en formato binario
     */
    private void guardarFicheroBinario() {
        GestionaFicherosHilos.escribirBinario(getFileBinario(),getAutorias(),getLibros());
    }

    /**
     * Importa del fichero 'bibliotecas.bin' la información, en formato binario, de las colecciones, creándolas
     */
    private void leerFicheroBinario() {
        GestionaFicherosHilos.leerBinario(getFileBinario(),getAutorias(),getLibros());
        exportarBBDD();
    }

    /**
     * Recoge un número entero introducido por el usuario mediante un Scanner.
     * @return numero Número entero que ha recogido el Scanner
     */
    private static int numeroScanner() {
        int numero = 0;
        boolean continuar = false;
        Scanner sc = new Scanner(System.in);
        while (!continuar) {
            try {
                numero = sc.nextInt();
                continuar = true;
            } catch (InputMismatchException e) {
                System.out.print("Por favor introduce un numero entero: ");
                sc.next();
            }
        }
        return numero;
    }

    /**
     * Provoca que el programa se detenga por cinco segundos con la intención de ayudar a una lectura más comoda por parte del usuario de la información lanzada por el programa
     */
    private static void esperar() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza las filas existentes o añade nuevas filas en las tablas autorias y libros de la BBDD con la información de las
     * colecciones de autorías y de libros
     */
    private void exportarBBDD(){
        System.out.println("Filas afectadas en la BBDD (tabla libros): " + LibroDAO.createOrUpdateAll(getLibros()));
        System.out.println("Filas afectadas en la BBDD (tabla autorias): " + AutoriaDAO.createOrUpdate(getAutorias()));
    }

    public static void main(String[] args) {
        BibliotecaV2 b = new BibliotecaV2();
        Conexion.createTables();
        autorias = AutoriaDAO.readAll();
        libros = LibroDAO.readAll();
        boolean seguir = true;
        int eleccion = -1;
        while (seguir) {
            System.out.println("MENU");
            System.out.println("1- Crear autor");
            System.out.println("2- Ver autor");
            System.out.println("3- Crear libro");
            System.out.println("4- Mostrar libro");
            System.out.println("5- Eliminar libro");
            System.out.println("6- Exportar a fichero de texto");
            System.out.println("7- Importar de fichero de texto");
            System.out.println("8- Guardar en fichero binario");
            System.out.println("9- Leer de fichero binario");
            System.out.println("0- Salir");
            System.out.print("Introduce una opcion: ");
            eleccion = numeroScanner();
            switch (eleccion) {
                case 1:
                    b.crearAutor();
                    break;
                case 2:
                    b.verAutores();
                    break;
                case 3:
                    b.crearLibro();
                    break;
                case 4:
                    b.verLibros();
                    break;
                case 5:
                    b.eliminarLibro();
                    break;
                case 6:
                    b.exportarFicheroTexto();
                    break;
                case 7:
                    b.importarFicheroTexto();
                    break;
                case 8:
                    b.guardarFicheroBinario();
                    break;
                case 9:
                    b.leerFicheroBinario();
                    break;
                case 0:
                    b.guardarFicheroBinario();
                    b.exportarBBDD();
                    seguir = false;
                    break;
            }
        }
    }
}