package Utilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ReadCsvFile {

    public  static  String OrgID;
    public static  String LocID;
    public static  String LocIDName;
    public static String clientName;
    public static List<List<String>>  ReadCsvData() {
        //String filePath ="C:\\Users\\Kaushal\\Downloads\\Data.csv";
       String filePath ="C:\\Users\\Kaushal\\OneDrive\\Desktop\\Location ID.csv";
       //String filePath ="C:\\Users\\Kaushal\\Downloads\\Ascend.csv";
        List<List<String>> credentials = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            // Read the header row (and discard it)
            String[] headerRow = reader.readNext();
            // Read all rows at once
            List<String[]> data = reader.readAll();
            // Iterate over each row
            for (String[] row : data) {
                List<String> credential = new ArrayList<>();
                // Assuming the first column is username and the second column is password
                OrgID = row[0];
                LocID = row[1];
                clientName=row[2];
                LocIDName=row[3];
                // Add username and password to the list
                credential.add(OrgID);
                credential.add(LocID);
                credential.add(clientName);
                credential.add(LocIDName);
                credentials.add(credential);
                // Do something with the username and password
//                System.out.println("OrgID: " + OrgID + ", LocID: " + LocID);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return credentials;
    }
}
