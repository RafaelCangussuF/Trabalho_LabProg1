package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQL {
    public static List<Aeroporto> ResultadoSQL() {
        List<Aeroporto> air = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc-aeroportos", "root", "Rcfecfccf3108!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from airportData2");
            int i = 0;
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
    public static void main(String[] args) {


    }
}
