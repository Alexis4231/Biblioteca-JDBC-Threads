package ficheros;

import biblioteca.Autoria;
import biblioteca.Libro;

import java.io.File;
import java.util.ArrayList;

public class GestionaFicherosHilos {
    /**
     * Crea un hilo encargado de escribir en formato de texto la información de la colecciones de autorías y de libros pasadas por parámetro
     * en el archivo pasado por parámetro, con el booleano pasado por parámetro el cual indicará si se quiere sobreescribir o concatenar sobre el fichero
     * @param file File donde se guardará la información
     * @param autorias ArrayList<Autoria> colección de autorías
     * @param libros ArrayList<Libro> colección de libros
     * @param append boolean indica si se quiere contanear la información en el archivo o no
     */
    public static void escribirTexto(File file, ArrayList<Autoria> autorias, ArrayList<Libro> libros, boolean append){
        EscritorTexto hilo = new EscritorTexto(file, autorias, libros, append);
        hilo.start();
    }

    /**
     * Crea un hilo encargado de leer de un archivo escrito en formato de texto la información de la colecciones de autorías y de libros pasadas por parámetro
     * en el archivo pasado por parámetro.
     * @param file File de donde se leerá la información
     * @param autorias ArrayList<Autoria> colección de autorías
     * @param libros ArrayList<Libro> colección de libros
     */
    public static void leerTexto(File file, ArrayList<Autoria> autorias, ArrayList<Libro> libros){
        LectorTexto hilo = new LectorTexto(file, autorias, libros);
        hilo.start();
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un hilo encargado de escribir en formato binario la información de la colecciones de autorías y de libros pasadas por parámetro
     * en el archivo pasado por parámetro.
     * @param file File donde se guardará la información
     * @param autorias ArrayList<Autoria> colección de autorías
     * @param libros ArrayList<Libro> colección de libros
     */
    public static void escribirBinario(File file,ArrayList<Autoria> autorias, ArrayList<Libro> libros){
        EscritorBinario hilo = new EscritorBinario(file, autorias, libros);
        hilo.start();
    }

    /**
     * Crea un hilo encargado de leer de un archivo escrito en formato binario la información de la colecciones de autorías y de libros pasadas por parámetro
     * en el archivo pasado por parámetro.
     * @param file File de donde se leerá la información
     * @param autorias ArrayList<Autoria> colección de autorías
     * @param libros ArrayList<Libro> colección de libros
     */
    public static void leerBinario(File file,ArrayList<Autoria> autorias, ArrayList<Libro> libros) {
        LectorBinario hilo = new LectorBinario(file, autorias, libros);
        hilo.start();
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}