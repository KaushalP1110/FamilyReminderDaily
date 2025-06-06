package Test;

import API.Patient_Scheduled;
import API.Url;
import Utilities.EmailUtility;
import Utilities.Imporsanate_Url;
import io.github.cdimascio.dotenv.Dotenv;
import Utilities.ReadCsvFile;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import javax.swing.text.Utilities;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
public class Insight {
    ExtentReports extent;
    public static String reportPath;
    public static String impersonate_Org;
    public static String client_LocationID;
    public static String LocationName;
    public static String Clientname;
    public static String inputFilePath;
    public static String outputFilePath;

    static Dotenv dotenv = Dotenv.load();


    public static String date_range;
    public static String impersonate_token;
    public static String Location_id;

    // Default report file name if Ehr is null or empty
    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());


    static
    {
        boolean github = true;
        if (github) {

            // OrganizationUrl =System.getenv("OrganizationUrl");
            date_range=System.getenv("date_range");
            impersonate_token =System.getenv("impersonate_token :");
        }
        else {
            //  OrganizationUrl =dotenv.get("OrganizationUrl");
            date_range =dotenv.get("date_range");
            impersonate_token =dotenv.get("impersonate_token");
        }
    }

    @BeforeSuite
    public void setUp() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportPath = "./Reports/ExtentReport_" + timeStamp + ".html";
        ExtentHtmlReporter reporter = new ExtentHtmlReporter(reportPath);
        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    @Test(priority = 1, dataProvider = "LoginCredentials", enabled = true)
    public void logInAndSchedule(String orgId, String locId, String locName, String clientname) {
        ExtentTest logInTest = extent.createTest("LogIn Test - Location: " + LocationName);
        setupUrlData(orgId, locId, locName, clientname);
        logInTest.info("Organization ID: " + orgId);
        logInTest.info("Location ID: " + locId);
        logInTest.info("Location Name: " + locName);


        Response logInResponse = Imporsanate_Url.impersonate_url();
        System.out.println(orgId+ locId+ locName + clientname);
        try {
            Assert.assertEquals(logInResponse.getStatusCode(), 200);
            logInTest.pass("LogIn Test Passed");
        } catch (AssertionError e) {
            logInTest.fail("LogIn Test Failed: " + e.getMessage());
            throw e; // Re-throw to make sure test fails
        }

        ExtentTest scheduleTest = extent.createTest("Schedule Test - Location: " + LocationName + " Of " + Clientname);
        setupUrlData(orgId, locId, locName, clientname);
        scheduleTest.info("Organization ID: " + orgId);
        scheduleTest.info("Location ID: " + locId);
        scheduleTest.info("Location Name: " + locName);

        Response scheduleResponse = Patient_Scheduled.Schedule(scheduleTest);
        try
        {
            Assert.assertEquals(scheduleResponse.getStatusCode(), 200);
            scheduleTest.pass("Schedule Test Passed");
        } catch (AssertionError e)
        {
            scheduleTest.fail("Schedule Test Failed: " + e.getMessage());
            throw e; // Re-throw to make sure test fails
        }
        extent.flush(); // Flush the report for each test
    }
    private void setupUrlData(String orgId, String clientname, String locId, String locName) {
        impersonate_Org = orgId;
        Clientname = clientname;
        client_LocationID = locId;
        LocationName = locName;
        Url.setOrgID(orgId);
        Url.setLocID(locId);
        Url.setLocIDName(locName);
        Url.setClientname(clientname);
    }

    @DataProvider(name = "LoginCredentials")
    public Object[][] credentialsProvider() {
        List<List<String>> credentials = ReadCsvFile.ReadCsvData();
        Object[][] data = new Object[credentials.size()][4];
        for (int i = 0; i < credentials.size(); i++) {
            List<String> credential = credentials.get(i);
            data[i][0] = credential.get(0); // Org ID
            data[i][1] = credential.get(1); // Loc ID
            data[i][2] = credential.get(2); // Location Name
            data[i][3] = credential.get(3); // Client name
        }
        return data;
    }
    @AfterSuite
    public void tearDown()
    {
        extent.flush();
        sendEmailReport();
    }
    private void sendEmailReport()
    {
        try {
            String host = "smtp.gmail.com";
            String port = "587";
            String userName = "kaushal.patel@adit.com";
            String password = "jqcohlxyyrgesvex"; // Or app-specific password if using 2FA

            // List of recipients
            List<String> toAddresses = Arrays.asList(
//"darshan.joshi@adit.com",
                    //  "pranav@adit.com"
                    "shivam.bhayani@adit.com",
                    "darshan.joshi@adit.com"

            );

            String subject = "Automated Test Report";
            String message = "Hello @Pranav Sir, @Darshan Bhai. This Is A Insight report of date: " + date_range;

            // Iterate through each recipient and send email
            for (String toAddress : toAddresses)
            {
                EmailUtility.sendEmailWithAttachment(host, port, userName, password, toAddress, subject, message, reportPath);
                System.out.println("Email  sent successfully to " + toAddress);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to send email. " + e.getMessage());
        }
    }
}
