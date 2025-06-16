package ficheros;

import biblioteca.Autoria;
import biblioteca.Libro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LectorTexto extends Thread{
    private File file;
    private ArrayList<Autoria> autorias;
    private ArrayList<Libro> libros;

    public LectorTexto(File file, ArrayList<Autoria> autorias, ArrayList<Libro> libros){
        this.file = file;
        this.autorias = autorias;
        this.libros = libros;
    }

    @Override
    public void run() {
        importar();
    }

    /**
     * Añade la información (la cual se encuentra en formato de texto) del archivo file (atributo de esta clase) a las colecciones de autorías y libros (atributos de esta clase)
     */
    public void importar(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String linea = br.readLine();
            if (linea.equals("***AUT***")) {
                autorias.clear();
                libros.clear();
                int id = -1;
                String nombre = "";
                String apellido = "";
                linea = br.readLine();
                String isbn = "";
                String titulo = "";
                int id_autoria = -1;
                Autoria autoria = null;
                String seccion = "***AUT***";
                while (linea != null) {
                    if (linea.equals("***LIB***")) {
                        seccion = "***LIB***";
                    }
                    if (seccion.equals("***AUT***")) {
                        id = Integer.parseInt(linea);
                        nombre = br.readLine();
                        apellido = br.readLine();
                        autorias.add(new Autoria(id, nombre, apellido));
                        linea = br.readLine();
                    } else if (seccion.equals("***LIB***")) {
                        if (linea.equals("***LIB***")) {
                            isbn = br.readLine();
                        } else {
                            isbn = linea;
                        }
                        titulo = br.readLine();
                        id_autoria = Integer.parseInt(br.readLine());
                        for (Autoria a : autorias) {
                            if (a.getId() == id_autoria) {
                                autoria = a;
                            }
                        }
                        libros.add(new Libro(isbn, titulo, autoria));
                        linea = br.readLine();
                    }
                }
            }
            br.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
