package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    /**
     * Crea una conexión con la base de datos biblioteca y devuelve el objeto Connection
     * @return con Objeto Connection
     */
    protected static Connection conectar(){
        Connection con = null;
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/biblioteca";
            String user = "root";
            String password = "Sandia4you";
            con = DriverManager.getConnection(url, user, password);
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return con;
    }

    /**
     * Crea las tablas autorias y libros en la BBDD
     */

    public static void createTables(){
        try{
        String sql = "CREATE TABLE IF NOT EXISTS autorias"
                +" (id int PRIMARY KEY,"
                +" nombre varchar(50),"
                +" apellido varchar(50));";
        Connection con = conectar();
        Statement sentencia = con.createStatement();
        sentencia.executeUpdate(sql);
        sql = "CREATE TABLE IF NOT EXISTS libros"
                + " (isbn varchar(13) PRIMARY KEY,"
                + " titulo varchar(100),"
                + " idAutoria int,"
                + " FOREIGN KEY (idAutoria) REFERENCES autorias(id));";
        sentencia.executeUpdate(sql);
        con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
