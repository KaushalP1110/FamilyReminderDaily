package API;

import Test.Insight;
import Utilities.Imporsanate_Url;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import static Test.Insight.date_range;
import static Utilities.EpochTimeToDateTime.epochTimeToDateTime;
import static io.restassured.RestAssured.given;

public class Patient_Scheduled
{

    public static String Appointment;
    public static String pateintfirstname;
    public static String Patientname;
    public static String patientFullName;
    public static String appntID;

    public static Response AppntLocationIDImpersonate() {
        Response response = given().contentType("application/json")
                .queryParam("organization", Insight.impersonate_Org)
                .header("accept", "application/json, text/plain, */*\n")
                .header("Origin", "https://app.adit.com")
                .header("authorization", Imporsanate_Url.token)
                .when().get(Url.get_Appointment_LocationID);

        String responsestr1 = response.asString();
        JSONObject jsonObject5 = new JSONObject(responsestr1);
        JSONArray jsonarry6 = jsonObject5.optJSONArray("data");

        if (jsonarry6 != null && jsonarry6.length() > 0) {
            appntID = jsonarry6.getJSONObject(0).optString("_id", "");
        }

        return response;
    }

    public static Response Schedule(ExtentTest parentTest) {
        Response response = given().log().all()
                .contentType("application/json")
                .queryParam("organization", Insight.impersonate_Org)
                .queryParam("location", Insight.client_LocationID)
                .queryParam("date_range_start", date_range)
                .queryParam("date_range_end", date_range)
                .queryParam("is_appointment", "WEB,EHR,PA")
                .queryParam("is_appointmentlog", "log")
                .header("accept", "application/json, text/plain, */*\n")
                .header("authorization", Imporsanate_Url.token)
                .when().get(Url.patientlistUrl);

        String responsestr = response.asString();
        JSONObject jsonObject = new JSONObject(responsestr);
        JSONObject jsonObject3 = jsonObject.optJSONObject("data");

        if (jsonObject3 == null || !jsonObject3.has("appointment")) {
            return response;
        }

        JSONArray jsonArray = jsonObject3.getJSONArray("appointment");
        boolean hasAnyFamilyReminder = false;

        ExtentTest clientNode = null;

        for (int y = 0; y < jsonArray.length(); y++) {
            JSONObject appt = jsonArray.getJSONObject(y);

            pateintfirstname = appt.optString("first_name", "");
            Patientname = appt.optString("last_name", "");
            Appointment = appt.optString("appointmentId", "");
            patientFullName = pateintfirstname + " " + Patientname;

            if (Patientname.isBlank() && pateintfirstname.isBlank()) continue;

            Response response1 = given()
                    .contentType("application/json")
                    .queryParam("locationId", Insight.client_LocationID)
                    .queryParam("organization", Insight.impersonate_Org)
                    .queryParam("apptId", Appointment)
                    .header("accept", "application/json, text/plain, */*\n")
                    .header("authorization", Imporsanate_Url.token)
                    .when().get(Url.recallURL);

            String responsestr1 = response1.asString();
            JSONObject jsonObject1 = new JSONObject(responsestr1);
            JSONArray jsonarry1 = jsonObject1.optJSONArray("data");

            if (jsonarry1 == null) continue;

            for (int j = jsonarry1.length() - 1; j >= 0; j--) {
                JSONObject logObj = jsonarry1.getJSONObject(j).optJSONObject("log");
                if (logObj == null) continue;

                String text = logObj.optString("text", "");
                long time = logObj.optLong("updated_at", 0);

                LocalDateTime logDateTime = Instant.ofEpochMilli(time)
                        .atZone(ZoneId.of(Imporsanate_Url.locationTimezone))
                        .toLocalDateTime();

                // ✅ If Family Reminder Sent within last 7 days
                if (logDateTime.isAfter(LocalDateTime.now().minusDays(7))
                        && text.contains("Family Appt. Reminder")) {

                    if (!hasAnyFamilyReminder) {
                        // Create Client Node once
                        clientNode = parentTest.createNode("🏢 Client: " + Insight.Clientname + " | 📍 Location: " + Insight.LocationName);
                        hasAnyFamilyReminder = true;
                    }

                    String recallNormalDate = epochTimeToDateTime(time, Imporsanate_Url.locationTimezone);

                    ExtentTest patientTest = clientNode.createNode(
                            "👤 Patient: " + patientFullName
                    );

                    patientTest.pass("✅ Family Reminder Sent | " + recallNormalDate + " | Message: " + text);
                    break; // Only log first Family Reminder per patient
                }
            }
        }

        return response;
    }
}
