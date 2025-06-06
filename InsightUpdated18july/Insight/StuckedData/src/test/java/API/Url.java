package API;


import Utilities.Imporsanate_Url;

public class Url {
    public static String get_Appointment_LocationID = "https://api.adit.com/getappointmentlocationforactivatedapp?appalias=engage";


    public static String patientlistUrl = "https://api.adit.com/appointment";
    public static String overDuePatientURL =
            "https://api.adit.com/getpatientlistbytypenewes?filterType=overdue&pageNo=1&limit=400&sortKey=due_date&sortDirection=desc";

    public static String recallURL = "https://engmodapi.adit.com/engage/getapptlog";

    public static String OrgID;
    public static String locID;
    public static String locIDName;
    public static String Clientname;

    public static String getOrgID() {
        return OrgID;
    }

    public static void setOrgID(String orgID) {
        OrgID = orgID;
    }

    public static String getLocID() {
        return locID;
    }

    public static void setLocID(String locID) {
        Url.locID = locID;
    }

    public static String getLocIDName() {
        return locIDName;
    }

    public static void setLocIDName(String locIDName) {
        Url.locIDName = locIDName;
    }

    public static String getClientname()
    {
        return Clientname;
    }

    public static void setClientname(String clientname)
    {
        Clientname = clientname;
    }
}
