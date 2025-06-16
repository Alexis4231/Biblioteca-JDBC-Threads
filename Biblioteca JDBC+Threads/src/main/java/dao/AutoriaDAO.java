package dao;

import biblioteca.Autoria;

import java.sql.*;
import java.util.ArrayList;

public class AutoriaDAO {

    /**
     * inserta una nueva fila en la tabla autorias de la BBDD con los datos del objeto Autoria pasado por parámetro
     * @param a objeto Autoria
     * @return salida Número de filas afectadas
     */
    public static int create(Autoria a){
        int salida = -1;
        try {
            String sql = "INSERT INTO autorias(id,nombre,apellido) VALUES (?,?,?)";
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, a.getId());
            sentencia.setString(2, a.getNombre());
            sentencia.setString(3, a.getApellido());
            salida = sentencia.executeUpdate();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return salida;
    }

    /**
     * Lee la fila de la tabla autorias cuyo id sea igual al pasado como parámetro y crea y devuelve un objeto Autoria con la información leida
     * @param id int identificador
     * @return autoria Objeto Autoria creado
     */

    public static Autoria read(int id){
        Autoria autoria = null;
        try{
            String sql = "SELECT * FROM autorias WHERE id = ?";
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                autoria = new Autoria(id, nombre, apellido);
            }
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return autoria;
    }

    /**
     * Actualiza la fila de la tabla autorias cuyo id es el mismo que el id del objeto Autoria pasado como parámetro y devuelve el número de filas afectadas
     * @param a Objeto autoria
     * @return salida Número de filas afectadas
     */
    public static int update(Autoria a){
        int salida = -1;
        try {
            String sql = "UPDATE autorias SET nombre = ?, apellido = ? WHERE id = ?";
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setString(1, a.getNombre());
            sentencia.setString(2, a.getApellido());
            sentencia.setInt(3, a.getId());
            salida = sentencia.executeUpdate();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return salida;
    }

    /**
     * Borra una fila de la tabla autorias cuyo id es el mismo que el id pasado como parámetro y devuelve el número de filas afectadas
     * @param id int identificador
     * @return salida Número de filas afectadas
     */
    public static int delete(int id){
        int salida = -1;
        try {
            String sql = "DELETE FROM autorias WHERE id = ?";
            Connection con = Conexion.conectar();
            PreparedStatement sentencia = con.prepareStatement(sql);
            sentencia.setInt(1, id);
            salida = sentencia.executeUpdate();
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return salida;
    }

    /**
     * Lee todas las filas de la tabla autorias y devuelve una nueva colección ArrayList con la información de las filas leidas
     * @return ArrayList<Autoria> Colección de autorias con la información de todas las filas
     */
    public static ArrayList<Autoria> readAll(){
        ArrayList<Autoria> autorias = new ArrayList<>();
        try {
            String sql = "SELECT * FROM autorias";
            Connection con = Conexion.conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                Autoria autoria = new Autoria(id, nombre, apellido);
                autorias.add(autoria);
            }
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return autorias;
    }

    /**
     * Analiza que objetos de una colección ArrayList de objetos Autoria pasado como parámetro tienen un id almacenado en alguna fila en la tabla autorias,
     * en caso de que así sea actualiza los datos del objeto en su fila y en caso contrario inserta la información del objeto en una fila nueva. Devuelve el
     * número de filas afectadas
     * @param a Colección ArrayList de objetos Autoria
     * @return int Número de filas afectadas
     */
    public static int createOrUpdate(ArrayList<Autoria> a){
        try {
            ArrayList<Autoria> existentes = new ArrayList<>();
            ArrayList<Autoria> noExistentes = new ArrayList<>();
            for (Autoria autoria : a) {
                String sql = "SELECT * FROM autorias WHERE id = ?";
                Connection con = Conexion.conectar();
                PreparedStatement sentencia = con.prepareStatement(sql);
                sentencia.setInt(1, autoria.getId());
                ResultSet rs = sentencia.executeQuery();
                boolean existe = false;
                if (rs.next()) {
                    existe = true;
                }
                if (existe) {
                    existentes.add(autoria);
                } else {
                    noExistentes.add(autoria);
                }
            }
            for (Autoria autoria : noExistentes) {
                create(autoria);
            }
            for (Autoria autoria : existentes) {
                update(autoria);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return a.size();
    }
}
