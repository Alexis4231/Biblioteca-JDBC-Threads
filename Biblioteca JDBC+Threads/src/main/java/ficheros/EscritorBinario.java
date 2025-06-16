package ficheros;

import biblioteca.Autoria;
import biblioteca.Libro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EscritorBinario extends Thread{
    private File file;
    private ArrayList<Autoria> autorias;
    private ArrayList<Libro> libros;

    public EscritorBinario(File file,ArrayList<Autoria> autorias, ArrayList<Libro> libros){
        this.file = file;
        this.autorias = autorias;
        this.libros = libros;
    }

    @Override
    public void run() {
        guardarBin();
    }

    /**
     * Escribe en el archivo file (atributo de esta clase) las colecciones de autorías y libros (atributos de esta clase)
     * en formato binario.
     */

    public void guardarBin(){
        synchronized (file) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(autorias);
                oos.writeObject(libros);
                oos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
