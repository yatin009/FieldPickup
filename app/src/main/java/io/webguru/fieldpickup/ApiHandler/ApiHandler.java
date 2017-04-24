package io.webguru.fieldpickup.ApiHandler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.webguru.fieldpickup.ApiHandler.dto.DeviceDataDTO;
import io.webguru.fieldpickup.ApiHandler.dto.LoginDataDTO;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;

/**
 * Created by mahto on 15/4/17.
 */

public class ApiHandler {

    public static LoginDataDTO getLoginData(Context context){
        HttpResponse httpResponse = ApiRequestHandler.makeServiceCall("app/rest/pickup/get_login_data", null, null, context);
        String responseMessage = null;
        LoginDataDTO loginDataDTO = null;
        GlobalFunction.checkForImageLocation();
        if(httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
            try {
                responseMessage = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

                ObjectMapper objectMapper = new ObjectMapper();
                loginDataDTO = objectMapper.readValue(responseMessage, new TypeReference<LoginDataDTO>(){});

            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(context, "ERROR CODE : 100", Toast.LENGTH_LONG).show();
            }
        } else {
//            Toast.makeText(context, "No pickup request found for you", Toast.LENGTH_LONG).show();
        }
        ArrayList<Docket> dockets = new ArrayList<>();
        if(loginDataDTO != null && !loginDataDTO.getDeviceDataList().isEmpty()){

            for(DeviceDataDTO deviceDataDTO : loginDataDTO.getDeviceDataList()){
                dockets.add(new Docket(deviceDataDTO));
            }
            GlobalFunction.createDocketIntoDB(dockets,context);
        }
        return loginDataDTO;
    }
}
