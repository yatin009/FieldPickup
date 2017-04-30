package io.webguru.fieldpickup.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Product;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 6/3/17.
 */

public class QcResultActivity extends AppCompatActivity {

    private Docket docket;
    private String isQCPassed;
    private Product product;
    private String step;

    private RelativeLayout qcPassLayout;
    private RelativeLayout qcFailLayout;
    private TextView qcMessageLayout;


    private DocketDataSource docketDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qc_result_layout);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
            isQCPassed = (String) bundle.get("isQCPassed");
            step = (String) bundle.get("Step");
            product = (Product) bundle.get("Product");

            qcPassLayout = (RelativeLayout) findViewById(R.id.qc_passed);
            qcFailLayout = (RelativeLayout) findViewById(R.id.qc_failed);
            qcMessageLayout = (TextView) findViewById(R.id.qc_message);



            if(isQCPassed.equalsIgnoreCase("yes")){
                qcPassLayout.setVisibility(RelativeLayout.VISIBLE);
                qcFailLayout.setVisibility(RelativeLayout.GONE);
                qcMessageLayout.setText("Shipment is cleared for pickup");
            } else {
                qcPassLayout.setVisibility(RelativeLayout.GONE);
                qcFailLayout.setVisibility(RelativeLayout.VISIBLE);
                qcMessageLayout.setText("Shipment is not cleared for pickup");
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }


    @OnClick(R.id.proceed)
    public void proceed() {

        if (Integer.parseInt(step) == docket.getProducts().size()) { //final Product
            //TODO add products field data in DB
            try {
                String image = null;
                for(Product product : docket.getProducts()) {
                    String productDescription = product.getDescription();
                    productDescription = productDescription.replaceAll(" ","_");
                    for(int id=1; id<=3; id++) {
                        image = docket.getAwbNumber() + "_" + productDescription + "_" + id + ".jpeg";
                        if(id == 1){
                            product.setImage1(image);
                        } else if(id == 2){
                            product.setImage2(image);
                        } else if(id == 3){
                            product.setImage3(image);
                        }
//                            moveImageFromTempToPicked(image);
                    }
                }
                docket.setIsPending(0);
                docketDataSource = new DocketDataSource(this);
                docketDataSource.open();
                docket.setStatus("Picked");
                docket.setStatusDescription("Packet Successfully Picked");

                docket.setIsQcCheckCleared(isQCPassed);
                docketDataSource.updateDocket(docket);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (docketDataSource != null) {
                    docketDataSource.close();
                }
            }
        } else {

//                Intent intent = new Intent(this, DocketUpdateActivity.class);
//                intent.putExtra("Docket", docket);
//                intent.putExtra("Product", docket.getProducts().get(Integer.parseInt(step)));
//                intent.putExtra("Step", (Integer.parseInt(step) + 1) + "");
//                startActivity(intent);
        }
        finish();
    }

}
