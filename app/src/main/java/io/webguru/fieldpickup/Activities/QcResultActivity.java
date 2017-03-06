package io.webguru.fieldpickup.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 6/3/17.
 */

public class QcResultActivity extends AppCompatActivity {

    private Docket docket;
    private FieldData fieldData;
    private Boolean isQCPassed;

    private RelativeLayout qcPassLayout;
    private RelativeLayout qcFailLayout;
    private TextView qcMessageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qc_result_layout);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
            fieldData = (FieldData) bundle.get("FieldData");
            isQCPassed = (Boolean) bundle.get("isQCPassed");

            qcPassLayout = (RelativeLayout) findViewById(R.id.qc_passed);
            qcFailLayout = (RelativeLayout) findViewById(R.id.qc_failed);
            qcMessageLayout = (TextView) findViewById(R.id.qc_message);



            if(isQCPassed){
                qcPassLayout.setVisibility(RelativeLayout.VISIBLE);
                qcFailLayout.setVisibility(RelativeLayout.GONE);
                qcMessageLayout.setText("Shipment is cleared for pickup");
                fieldData.setIsQcCleared(1);
            } else {
                qcPassLayout.setVisibility(RelativeLayout.GONE);
                qcFailLayout.setVisibility(RelativeLayout.VISIBLE);
                qcMessageLayout.setText("Shipment is not cleared for pickup");
                fieldData.setIsQcCleared(0);
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }


    @OnClick(R.id.proceed)
    public void proceed() {
        finish();
    }

}
