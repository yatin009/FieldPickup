package io.webguru.fieldpickup.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 24/1/17.
 */

public class DocketDetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;
    TextView customerName;
    TextView contactNumber;
    TextView address;
    TextView productDescription;
    TextView pincode;
    TextView reason;
    TextView orderNumber;
    TextView actualQuantity;

    TextView is_same_product_details;
    TextView quantity_details;
    TextView is_all_parts_available_details;
    TextView is_correct_issue_category_details;
    TextView is_dirty_details;
    TextView remarks_details;
    TextView status;
    TextView is_damaged_details;
    TextView is_qc_cleared_details;

    private LinearLayout capturedDetailsLayout;

    private static FieldDataDataSource fieldDataDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docket_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            docket = (Docket) bundle.get("Docket");
        }

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            customerName = (TextView) findViewById(R.id.customer_name);
            contactNumber = (TextView) findViewById(R.id.contact_number);
            address = (TextView) findViewById(R.id.address);
            productDescription = (TextView) findViewById(R.id.product_description);
            pincode = (TextView) findViewById(R.id.pincode);
            reason = (TextView) findViewById(R.id.reason);
            orderNumber = (TextView) findViewById(R.id.order_number);
            actualQuantity = (TextView) findViewById(R.id.actual_quantity);

            if(docket.isPending() == 1){
                capturedDetailsLayout = (LinearLayout)this.findViewById(R.id.captured_details_layout);
                capturedDetailsLayout.setVisibility(LinearLayout.GONE);
            } else {
                ((LinearLayout) this.findViewById(R.id.update_button_layout)).setVisibility(LinearLayout.GONE);
            }
            customerName.setText(docket.getCustomerName());
            contactNumber.setText(docket.getCustomerContact());
            address.setText(docket.getCustomerAddress());
            productDescription.setText(docket.getDescription());
            pincode.setText(docket.getPincode());
            reason.setText(GlobalFunction.getReasonCodeMap().get(docket.getReason()));
            actualQuantity.setText(docket.getQuantity()+"");
            orderNumber.setText(docket.getOrderNumber());

            fieldDataDataSource = new FieldDataDataSource(this);
            fieldDataDataSource.open();
            FieldData fieldData = null;
            try {
                fieldData = fieldDataDataSource.getFieldData(docket.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            fieldDataDataSource.close();
            if(fieldData != null){
                is_same_product_details = (TextView) findViewById(R.id.is_same_product_details);
                quantity_details = (TextView) findViewById(R.id.quantity_details);
                is_all_parts_available_details = (TextView) findViewById(R.id.is_all_parts_available_details);
                is_correct_issue_category_details = (TextView) findViewById(R.id.is_correct_issue_category_details);
                is_dirty_details = (TextView) findViewById(R.id.is_dirty_details);
                remarks_details = (TextView) findViewById(R.id.remarks_details);
                status = (TextView) findViewById(R.id.status);
                is_damaged_details = (TextView) findViewById(R.id.is_damaged_details);
                is_qc_cleared_details = (TextView) findViewById(R.id.is_qc_cleared_details);

                is_same_product_details.setText(fieldData.getIsSameProduct());
                quantity_details.setText(fieldData.getQuantity()+"");
                is_all_parts_available_details.setText(fieldData.getIsAllPartsAvailable());
                is_correct_issue_category_details.setText(fieldData.getIsIssueCategoryCorrect());
                is_dirty_details.setText(fieldData.getIsProductClean());
                remarks_details.setText(fieldData.getAgentRemarks());
                status.setText(fieldData.getStatus());
                is_damaged_details.setText(fieldData.getIsDamaged() == null ? "NA" : fieldData.getIsDamaged());
                String qc = "NA";
                if(fieldData.getIsQcCleared() == null){
                    qc = "NA";
                } else if(fieldData.getIsQcCleared() == 1){
                    qc = "YES";
                } else if(fieldData.getIsQcCleared() == 0){
                    qc = "NO";
                }
                is_qc_cleared_details.setText(qc);
                setCapturedImage("1");
                setCapturedImage("2");
                setCapturedImage("3");
            }
            getSupportActionBar().setTitle(docket.getAwbNumber());

        }
    }


    @OnClick(R.id.update_docket)
    public void updateDocket() {
        Intent intent = new Intent(this, DocketUpdateActivity.class);
        intent.putExtra("Docket", docket);
        startActivity(intent);
        finish();
    }

    private Uri getImageUri(String imageName) {
        File f = Environment.getExternalStoragePublicDirectory(DocketUpdateActivity.checkForImageLocation() + "/" + imageName);
        return Uri.parse(f.getAbsolutePath());
    }

    protected void setCapturedImage(String imageId) {
        String id;
        ImageView imageView;
        if (imageId.equals("1")) {
            id = "1";
            imageView = (ImageView) findViewById(R.id.capturedImage1);
        } else if (imageId.equals("2")) {
            id = "2";
            imageView = (ImageView) findViewById(R.id.capturedImage2);
        } else if (imageId.equals("3")) {
            id = "3";
            imageView = (ImageView) findViewById(R.id.capturedImage3);
        } else {
            return;
        }
        imageView.setImageDrawable(null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
            imageView.setImageURI(getImageUri(docket.getAwbNumber() + "_" + id + ".jpeg"));
        }
    }


    @OnClick(R.id.capturedImage1)
    public void openImage1() {
        openImage("1");
    }

    @OnClick(R.id.capturedImage2)
    public void openImage2() {
        openImage("2");
    }

    @OnClick(R.id.capturedImage3)
    public void openImage3() {
        openImage("3");
    }


    private void openImage(String id){
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("imageName", docket.getAwbNumber()+ "_" + id + ".jpeg");
        intent.putExtra("awbNumber", docket.getAwbNumber());
        intent.putExtra("imageNumber", id);
        intent.putExtra("source", "DETAILS");
        startActivity(intent);
    }



}
