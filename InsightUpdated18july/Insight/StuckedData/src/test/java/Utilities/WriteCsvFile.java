package Utilities;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WriteCsvFile {
    public static void writeToCSV(List<Map<String, Object>> dataList, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            String[] header = {"Location ID", "Location Name", "Organization ID", "Client Name", "Patient Name", "Redy to sent Cron Issue","sent to cron Issue","sms Cron Issue","intialfound Cron Issue","redytosend Cron Issue","Inserted Cron Issue"};
            writer.writeNext(header);

            // Write data
            for (Map<String, Object> data : dataList) {
                String[] line = {
                        data.get("Location ID").toString(),
                        data.get("Location Name").toString(),
                        data.get("Organization ID").toString(),
                        data.get("Client Name").toString(),
                        data.get("Patient Name").toString(),
                        data.get("Redy to sent Cron Issue").toString(),
                        data.get("sent to cron Issue").toString(),
                        data.get("sms Cron Issue").toString()  ,
                        data.get("intialfound Cron Issue").toString(),
                        data.get("redytosend Cron Issue").toString(),
                        data.get("Inserted Cron Issue").toString()

                };
                writer.writeNext(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
