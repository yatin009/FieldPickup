package io.webguru.fieldpickup;

import java.util.ArrayList;

import io.webguru.fieldpickup.POJO.Docket;

/**
 * Created by yatin on 21/01/17.
 */

public class GlobalFunction {

    private static String [] pendingDockets = {"RP-415221","RP-415255","RP-415223","RP-415276","RP-415288"};
    private static String [] doneDockets = {"RP-415232","RP-415333","RP-415365","RP-415612","RP-415543"};

    private static String [] pendingCustomerName = {"Yogesh sharma","Dr. Ashwani Kumar Gupta","MANU SHARMA","Abhishek pal","puneet vashistha"};
    private static String [] doneCustomerName = {"Neeraj Joshi","Dr. Gautam Bhattacharya","Shweta Verma","manish dhar","sunil kr sharma"};

    private static String [] pendingContactNumber = {"+91-9012547691","7042067740","9769555880","8953903078","9717394845"};
    private static String [] doneContactNumber = {"9810214312","9899154267","8287356604","9810345061","9775555430"};

    private static String [] pendingAddressList = {"IEX LtD 4th Floor TDI Tower Jasola District Center, Near Apolo Hospital, New Delhi Delhi",
            "H 13 Kasturba Apartment Pitampura, Behind Bhagwan Mahavir Hospital., New Delhi Delhi",
            "644 Pocket E Mayur Vihar Phase 2 East Delhi, Pocket E, New Delhi Delhi",
            "R3A2-108 mohan garden uttam nagar new delhi-59, Chandi farm, New Delhi Delhi",
            "Vpo amberhai sec19 dwarka HOUSE NO. 68, Near panchayat ghar, New Delhi Delhi"};

    private static String [] doneAddressList = {"House No. 154 Dayanand Vihar, Near Karkardooma Metro Station, New Delhi Delhi",
            "HOUSE NO-3, GAYANODAYA SECONDARY PUBLIC SCHOOL DINPUR NAJAFGARH, NEW DELHI Delhi",
            "D 7/9 SF Exclusive Floors DLF City Phase V, DLF City Phase V, Gurgaon Haryana",
            "Duplex Bungalow No 7 Type VI M.A.M.C Campus, -, New Delhi Delhi",
            "IEX LtD 4th Floor TDI Tower Jasola District Center, Near Apolo Hospital, New Delhi Delhi"};


    public static ArrayList<Docket> getDocketList(boolean isPending){
        ArrayList<Docket> dockets = new ArrayList<>();
        for(int i=0; i<pendingDockets.length; i++){
            dockets.add(new Docket(isPending, pendingDockets[i], pendingAddressList[i], pendingContactNumber[i], pendingCustomerName[i]));
        }
        return dockets;
    }

    public static ArrayList<Docket> getDoneDocketList(){
        ArrayList<Docket> dockets = new ArrayList<>();
        for(int i=0; i<doneDockets.length; i++){
            dockets.add(new Docket(false, doneDockets[i], doneAddressList[i], doneContactNumber[i], doneCustomerName[i]));
        }
        return dockets;
    }
}
