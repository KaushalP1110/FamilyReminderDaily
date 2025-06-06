package Utilities;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static Test.Insight.*;
import static io.restassured.RestAssured.given;
public class Imporsanate_Url
{
    public static String token;
    public static String impersonate_URL = "https://api.adit.com/auth/impersonate";
    //public static String impersonate_Org = Url.getOrgID();
    //public static String client_LocationID = Url.getLocID();
//    public static String impersonate_token ;
//    public static String date_range;
    public static String locationTimezone;
    public static Response impersonate_url()
    {
        //System.out.println("org--> "+impersonate_Org);
       // System.out.println("loc--> "+client_LocationID);
        String body = "{\n" +
                "    \"organization\": \"" + impersonate_Org + "\",\n" +
                "    \"token\": \"" + impersonate_token + "\",\n" +
                "    \"forceadmin\": \"true\",\n" +
                "    \"impersonate_admin_email\": \"mailto:darshan.joshi@adit.com\",\n" +
                "    \"impersonate_user_role\": \"devloper\",\n" +
                "    \"reseller\": \"\"\n" +
                "}";
        String contentType = "application/json";
// Login Url
        Response response = given()
                .contentType(contentType).
                header("accept", "application/json, text/plain, */*\n")
                .header("Origin", "https://app.adit.com")
                .body(body)
                .when().post(impersonate_URL);
        String response1 = response.then().extract().response().asString();
        //System.out.println(response1);
        JsonPath js = new JsonPath(response1);
        token = js.getString("token");
        System.out.println(token);
        locationTimezone = js.getString("data.location_timezone[\""+client_LocationID+"\"].zone");
      //  System.out.println(locationTimezone);
        return response;
    }
}

