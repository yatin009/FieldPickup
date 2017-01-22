package io.webguru.fieldpickup;

import java.util.ArrayList;

import io.webguru.fieldpickup.POJO.Docket;

/**
 * Created by yatin on 21/01/17.
 */

public class GlobalFunction {

    public static ArrayList<Docket> getDocketList(boolean isPending){
        ArrayList<Docket> dockets = new ArrayList<>();
        for(int i=1; i<11; i++){
            dockets.add(new Docket(isPending, "Docket#"+i, "Add "+i, "123123123"));
        }
        return dockets;
    }

    public static ArrayList<Docket> getDoneDocketList(){
        ArrayList<Docket> dockets = new ArrayList<>();
        for(int i=11; i<21; i++){
            dockets.add(new Docket(false, "Docket#"+i, "Add "+i, "123123123"));
        }
        return dockets;
    }
}
