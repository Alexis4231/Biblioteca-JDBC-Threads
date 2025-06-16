package ficheros;

import biblioteca.Autoria;
import biblioteca.Libro;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LectorBinario extends Thread{
    private File file;
    private ArrayList<Autoria> autorias;
    private ArrayList<Libro> libros;

    public LectorBinario(File file, ArrayList<Autoria> autorias, ArrayList<Libro> libros){
        this.file = file;
        this.autorias = autorias;
        this.libros = libros;
    }

    @Override
    public void run() {
        leerBin();
    }

    /**
     * Añade la información (la cual se encuentra en formato binario) del archivo file (atributo de esta clase) a las colecciones de autorías y libros (atributos de esta clase)
     */
    public void leerBin() {
        synchronized (file) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                ArrayList<Autoria> autorias_import = (ArrayList<Autoria>) ois.readObject();
                ArrayList<Libro> libros_import = (ArrayList<Libro>) ois.readObject();
                autorias.clear();
                libros.clear();
                autorias.addAll(autorias_import);
                libros.addAll(libros_import);
                ois.close();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
