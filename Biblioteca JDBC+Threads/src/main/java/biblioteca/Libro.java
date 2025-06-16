package biblioteca;

import java.io.Serializable;

public class Libro implements Serializable {
    private String isbn;
    private String titulo;
    private Autoria autoria;

    public Libro(String isbn, String titulo, Autoria autoria) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autoria = autoria;
    }

    public String getIsbn(){
        return isbn;
    }

    public String getTitulo(){
        return titulo;
    }

    public Autoria getAutoria(){
        return autoria;
    }

    public String toString(){
        return titulo + " con isbn " + isbn + " de " + autoria;
    }
}
