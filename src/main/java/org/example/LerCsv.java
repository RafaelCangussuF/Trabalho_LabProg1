package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LerCsv {
    private static final String CSV_Path = "C:\\Users\\rafae\\OneDrive\\Área de Trabalho\\3º Ano\\2 semestre\\LabProg2\\airportData.csv";

    public static void main(String[] args) {
        List<Aeroporto> air = new ArrayList<>();
        List<String[]> Data = readData(CSV_Path);


        //System.out.println("\n\n\n\\n\n\n\n\n");
        for (String[] row : Data) {
            Aeroporto aero = new Aeroporto();

            for (String cell : row) {
                     String[] sep = cell.split(",");
                         aero.setSigla(sep[1]);
                        // System.out.print(cell+"\t");
                         aero.setMunicipio(sep[4]);
                         aero.setEstado(sep[5]);
                         aero.setLatitude(sep[16]);
                         aero.setLongitude(sep[17]);


            }
            air.add(aero);
            System.out.println();
        }
        System.out.println();
        int i=0;
        while(i<40){
            System.out.println(air.get(i).getSigla() + ","+ air.get(i).getEstado()+","+air.get(i).getMunicipio()+","+air.get(i).getLatitude()+","+air.get(i).getLongitude());
            i++;
        }
    }

    public static List<String[]> readData(String File) {
        List<String[]> allData = null;
        try {
            int i = 0;
            // Create an object of filereader class
            // with CSV file as a parameter.
            FileReader filereader = new FileReader(File);

            // create csvReader object
            // and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();
            allData = csvReader.readAll();

            // print Data
           /* for (String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allData;
    }
}
