package dao;
import biblioteca.Libro;
import biblioteca.Autoria;

import java.sql.*;
import java.util.ArrayList;

public class LibroDAO {

    /**
     * Comprueba la existencia, en la tabla autorias, de la información de la autoria del libro pasado como parámetro, insertando la información en caso de
     * que no exista esta en la tabla, e inserta la información del libro en la tabla libros
     * @param l Objeto Libro
     * @return int Número de filas afectadas
     */
    public static int create(Libro l){
        int salida = -1;
        try {
            String sql = "INSERT INTO libros(isbn,titulo,idAutoria) VALUES (?,?,?)";
            Autoria autoria = AutoriaDAO.read(l.getAutoria().getId());
            if (autoria == null) {
                AutoriaDAO.create(l.getAutoria());
            }
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setString(1, l.getIsbn());
            sentencia.setString(2, l.getTitulo());
            sentencia.setInt(3, l.getAutoria().getId());
            salida = sentencia.executeUpdate();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return salida;
    }

    /**
     * Lee la fila de la tabla libros cuyo isbn sea igual al pasado como parámetro y crea y devuelve un objeto Libro con la información leida
     * @param isbn String isbn del objeto Libro
     * @return Libro Objecto Libro creado
     */
    public static Libro read(String isbn){
        Libro libro = null;
        try {
            String sql = "SELECT * FROM libros WHERE isbn = ?";
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setString(1, isbn);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                String isbn_introducir = rs.getString("isbn");
                String titulo_introducir = rs.getString("titulo");
                int idAutoria = rs.getInt("idAutoria");
                Autoria autoria_introducir = AutoriaDAO.read(idAutoria);
                libro = new Libro(isbn_introducir, titulo_introducir, autoria_introducir);
            }
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
       return libro;
    }

    /**
     * Crea o actualiza la fila de la tabla autorias con la información de la Autoria del objeto Libro pasado como parámetro y actualiza la fila de la tabla libros cuyo isbn corresponda con el del
     * libro pasado como parámetro
     * @param l Objecto libro
     * @return int Número de filas afectadas
     */
    public static int update(Libro l){
        int salida = -1;
        try {
            String sql = "UPDATE libros SET titulo = ?,  idAutoria = ? WHERE isbn = ?";
            Autoria autoria = l.getAutoria();
            ArrayList<Autoria> autorias = new ArrayList<>();
            autorias.add(autoria);
            AutoriaDAO.createOrUpdate(autorias);
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setString(1, l.getTitulo());
            sentencia.setInt(2, autoria.getId());
            sentencia.setString(3, l.getIsbn());
            salida = sentencia.executeUpdate();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return salida;
    }

    /**
     * Borra una fila de la tabla libros cuyo isbn es el mismo que el isbn pasado como parámetro y devuelve el número de filas afectadas
     * @param isbn String isbn
     * @return int Número de filas afectadas
     */
    public static int delete(String isbn){
        int salida = -1;
        try {
            String sql = "DELETE FROM libros WHERE isbn = ?";
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setString(1, isbn);
            salida = sentencia.executeUpdate();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return salida;
    }

    /**
     * Lee todas las filas de la tabla libros y devuelve una nueva colección ArrayList con la información de las filas leidas
     * @return ArrayList<Libro> Colección de libros creada
     */
    public static ArrayList<Libro> readAll(){
        ArrayList<Libro> libros = new ArrayList<>();
        try {
            String sql = "SELECT * FROM libros";
            Connection con = Conexion.conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery(sql);
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                int idAutoria = rs.getInt("idAutoria");
                Autoria autoria = AutoriaDAO.read(idAutoria);
                Libro libro = new Libro(isbn, titulo, autoria);
                libros.add(libro);
            }
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return libros;
    }

    /**
     * Analiza que objetos de una colección ArrayList de objetos Libro pasado como parámetro tienen un isbn almacenado en alguna fila en la tabla libros,
     * en caso de que así sea actualiza los datos del objeto en su fila y en caso contrario inserta la información del objeto en una fila nueva. Devuelve el
     * número de filas afectadas
     * @param l ArrayList<Libro> Colección de libros
     * @return int Número de filas afectadas
     */
    public static int createOrUpdateAll(ArrayList<Libro> l){
        try {
            ArrayList<Libro> existentes = new ArrayList<>();
            ArrayList<Libro> noExistentes = new ArrayList<>();
            for (Libro libro : l) {
                String sql = "SELECT * FROM libros WHERE isbn = ?";
                Connection con = Conexion.conectar();
                PreparedStatement sentencia = con.prepareStatement(sql);
                sentencia.setString(1, libro.getIsbn());
                ResultSet rs = sentencia.executeQuery();
                boolean existe = false;
                if (rs.next()) {
                    existe = true;
                }
                if (existe) {
                    existentes.add(libro);
                } else {
                    noExistentes.add(libro);
                }
            }
            for (Libro libro : noExistentes) {
                create(libro);
            }
            for (Libro libro : existentes) {
                update(libro);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return l.size();
    }
}
