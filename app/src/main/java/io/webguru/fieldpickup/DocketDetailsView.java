package io.webguru.fieldpickup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;

/**
 * Created by mahto on 24/1/17.
 */

public class DocketDetailsView extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;
    TextView customerName;
    TextView contactNumber;
    TextView address;
    TextView productDescription;

    TextView is_same_product_details;
    TextView quantity_details;
    TextView is_all_parts_available_details;
    TextView is_correct_issue_category_details;
    TextView is_dirty_details;
    TextView remarks_details;
    ImageView imageView;

    private LinearLayout updateButtonLayout;
    private LinearLayout capturedDetailsLayout;

    private static FieldDataDataSource fieldDataDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docket_details_view);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            docket = (Docket) bundle.get("Docket");
        }

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            customerName = (TextView) findViewById(R.id.customer_name);
            contactNumber = (TextView) findViewById(R.id.contact_number);
            address = (TextView) findViewById(R.id.address);
            productDescription = (TextView) findViewById(R.id.product_description);

            if(docket.isPending() == 1){
                capturedDetailsLayout = (LinearLayout)this.findViewById(R.id.captured_details_layout);
                capturedDetailsLayout.setVisibility(LinearLayout.GONE);
            } else {
                updateButtonLayout = (LinearLayout)this.findViewById(R.id.update_button_layout);
                updateButtonLayout.setVisibility(LinearLayout.GONE);
            }
            customerName.setText(docket.getCustomerName());
            contactNumber.setText(docket.getCustoumerContact());
            address.setText(docket.getCustoumerAddress());
            productDescription.setText(docket.getDescription());

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
                imageView = (ImageView) findViewById(R.id.capturedImageView);

                is_same_product_details.setText(fieldData.getIsSameProduct());
                quantity_details.setText(fieldData.getQuantity()+"");
                is_all_parts_available_details.setText(fieldData.getIsAllPartsAvailable());
                is_correct_issue_category_details.setText(fieldData.getIsIssueCategoryCorrect());
                is_dirty_details.setText(fieldData.getIsProductDirty());
                remarks_details.setText(fieldData.getAgentRemarks());
                File f = Environment.getExternalStoragePublicDirectory("Field Pickup/"+docket.getDocketNumber()+".jpeg");
                Uri uri = Uri.parse(f.getAbsolutePath());
                imageView.setImageURI(uri);
            }
            getSupportActionBar().setTitle(docket.getDocketNumber());

        }
    }

    @OnClick(R.id.open_update_docket)
    public void openUpdateDocket(){
        Intent intent = new Intent(this, DocketView.class);
        intent.putExtra("Docket", docket);
        startActivity(intent);
    }

}
