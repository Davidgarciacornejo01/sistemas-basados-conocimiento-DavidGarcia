package examples.proyecto1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
public class AlibabaDB {
    
    // Librería de MySQL
    public String driver = "com.mysql.jdbc.Driver";

    // Nombre de la base de datos
    public String database = "alibaba";

    // Host
    public String hostname = "localhost";

    // Puerto
    public String port = "3306";

    // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    public String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";

    // Nombre de usuario
    public String username = "root";

    // Clave de usuario
    public String password = "";
    
    public Connection conexion = null;
    public AlibabaDB(){
        conectarMySQL();
    }
    public Connection conectarMySQL() {

        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, username, password);
            statement = conexion.createStatement();
            //System.out.println("conectado");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("no se conecto");
        }

        return conexion;
    }
    Statement statement;
    public void setProductos(String nombre, String tipo, String precio,String elementosDisponibles) {
        try {
            statement.executeUpdate("insert into productos(nombre,tipo,precio,elementos_disponibles) values('"+nombre+"','"+tipo+"',"+precio+","+elementosDisponibles+")");
            System.out.println("insertado");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public String getProdutos() {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql="select * from productos";
        String cadena="";
        try {
            preparedStatement=conexion.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                /*
                System.out.println(
                        "id: "+resultSet.getObject(1)+"  nombre: "+resultSet.getObject(2)+"  tipo: "+
                        resultSet.getObject(3)+"  precio: "+
                        resultSet.getObject(4)+"  elementos disponibles: "+
                        resultSet.getObject(5));*/
                
                cadena=cadena +":"+ resultSet.getObject(1) +","+ resultSet.getObject(2) +","+ resultSet.getObject(3) +","+ resultSet.getObject(4)+","+resultSet.getObject(5);
                
            }
            return cadena;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public void updateProductos(int elementos_disponibles,String id, String tipo) {
        try {
            statement.executeUpdate("update productos set elementos_disponibles="+elementos_disponibles+" where id_productos="+id+" and tipo='"+tipo+"'");
            System.out.println("actualizado");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void cerrarConexion(){
        try {
            conexion.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String[] buscarProducto(String nombre) {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql="select * from productos where nombre='"+nombre+"'";
        String[] array=new String[5]; 
        try {
            preparedStatement=conexion.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                /*
                System.out.println(
                        "id: "+resultSet.getObject(1)+"  nombre: "+resultSet.getObject(2)+"  tipo: "+
                        resultSet.getObject(3)+"  precio: "+
                        resultSet.getObject(4)+"  elementos disponibles: "+
                        resultSet.getObject(5));*/
                array[0]=new String();
                array[0] = resultSet.getObject(1).toString(); 
                array[1]=new String();
                array[1] = resultSet.getObject(2).toString();
                array[2]=new String();
                array[2] = resultSet.getObject(3).toString(); 
                array[3]=new String();
                array[3] = resultSet.getObject(4).toString();
                array[4]=new String();
                array[4] = resultSet.getObject(5).toString();
                
            }
            return array;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public int dameContador(){
        return contadorObjetos;
    }
    public void fijamosContador(int contador){
        contadorObjetos=contador;
    }
    int contadorObjetos=0;
    public String[][] dameProductos(){
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql="select * from productos";
        String[][] matriz=new String[300][5]; 
        int contador=0;
        try {
            preparedStatement=conexion.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                /*
                System.out.println(
                        "id: "+resultSet.getObject(1)+"  nombre: "+resultSet.getObject(2)+"  tipo: "+
                        resultSet.getObject(3)+"  precio: "+
                        resultSet.getObject(4)+"  elementos disponibles: "+
                        resultSet.getObject(5));*/
                matriz[contador][0]=new String();
                matriz[contador][0] = resultSet.getObject(1).toString(); 
                matriz[contador][1]=new String();
                matriz[contador][1] = resultSet.getObject(2).toString();
                matriz[contador][2]=new String();
                matriz[contador][2] = resultSet.getObject(3).toString(); 
                matriz[contador][3]=new String();
                matriz[contador][3] = resultSet.getObject(4).toString();
                matriz[contador][4]=new String();
                matriz[contador++][4] = resultSet.getObject(5).toString();
                
            }
            fijamosContador(contador);
            return matriz;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void mostrarLista(){
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String sql="select * from productos";
        try {
            preparedStatement=conexion.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                
                System.out.println(
                        "id: "+resultSet.getObject(1)+"  nombre: "+resultSet.getObject(2)+"  tipo: "+
                        resultSet.getObject(3)+"  precio: "+
                        resultSet.getObject(4)+"  elementos disponibles: "+
                        resultSet.getObject(5));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
}
