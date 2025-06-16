package ficheros;


import biblioteca.Autoria;
import biblioteca.Libro;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EscritorTexto extends Thread{
    private File file;
    private ArrayList<Autoria> autorias;
    private ArrayList<Libro> libros;
    private boolean append;

    public EscritorTexto(File file,ArrayList<Autoria> autorias, ArrayList<Libro> libros, boolean append){
        this.file = file;
        this.autorias = autorias;
        this.libros = libros;
        this.append = append;
    }

    @Override
    public void run() {
        exportar();
    }

    /**
     * Escribe en el archivo file (atributo de esta clase) las colecciones de autorías y libros (atributos de esta clase)
     * en formato de texto
     */
    public void exportar(){
        synchronized (file) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, append));
                if (autorias.size() != 0) {
                    bw.write("***AUT***");
                    for (Autoria a : autorias) {
                        bw.newLine();
                        bw.write(String.valueOf(a.getId()));
                        bw.newLine();
                        bw.write(a.getNombre());
                        bw.newLine();
                        bw.write(a.getApellido());
                    }
                }
                if (libros.size() != 0) {
                    bw.newLine();
                    bw.write("***LIB***");
                    for (Libro l : libros) {
                        bw.newLine();
                        bw.write(l.getIsbn());
                        bw.newLine();
                        bw.write(l.getTitulo());
                        bw.newLine();
                        bw.write(String.valueOf(l.getAutoria().getId()));
                    }
                }
                bw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
