package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQL {
    public static List<Aeroporto> ResultadoSQL() {
        List<Aeroporto> air = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-aeroportos", "root", "Rcfecfccf3108!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from airportData2");
            while (resultSet.next()) {
                Aeroporto aero = new Aeroporto();
                aero.setSigla(resultSet.getString("Sigla"));
                aero.setMunicipio(resultSet.getString("county"));
                aero.setEstado(resultSet.getString("state"));
                aero.setLatitude(resultSet.getDouble("latitude"));
                aero.setLongitude(resultSet.getDouble("longitude"));
                air.add(aero);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return air;

    }
    public static void InserirSQL(String pesquisa, int i) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-aeroportos", "root", "Rcfecfccf3108!");

            Statement statement = connection.createStatement();



            String sql = "INSERT INTO `jdbc-aeroportos`.`historico`\n" +
                    "(`idHistórico`,\n" +
                    "`Pesquisa`)\n" +
                    "VALUES\n" +
                    "(?,\n" +
                    "?)";

            PreparedStatement prepstmt = connection.prepareStatement(sql);

            prepstmt.setInt(1, i );
            prepstmt.setString(2, pesquisa);
            prepstmt.execute();
            connection.close();


        } catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {


    }
}
