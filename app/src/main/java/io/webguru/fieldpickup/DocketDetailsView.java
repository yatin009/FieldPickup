package io.webguru.fieldpickup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.AndroidSlidingTab.SlidingTabLayout;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.Fragments.BlankFragment;
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


    SlidingTabLayout slidingTabLayout;


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

            BlankFragment blankFragment = new BlankFragment();

            fieldDataDataSource = new FieldDataDataSource(this);
            fieldDataDataSource.open();
            FieldData fieldData = fieldDataDataSource.getFieldData(docket.getId());
            fieldDataDataSource.close();
            if(fieldData != null){
                is_same_product_details = (TextView) findViewById(R.id.is_same_product_details);
                quantity_details = (TextView) findViewById(R.id.quantity_details);
                is_all_parts_available_details = (TextView) findViewById(R.id.is_all_parts_available_details);
                is_correct_issue_category_details = (TextView) findViewById(R.id.is_correct_issue_category_details);
                is_dirty_details = (TextView) findViewById(R.id.is_dirty_details);
                remarks_details = (TextView) findViewById(R.id.remarks_details);

                is_same_product_details.setText(fieldData.isSameProduct() ? "YES" : "NO");
                quantity_details.setText(fieldData.getQuantity()+"");
                is_all_parts_available_details.setText(fieldData.isAllPartsAvailable() ? "YES" : "NO");
                is_correct_issue_category_details.setText(fieldData.issueCategoryCorrect() ? "YES" : "NO");
                is_dirty_details.setText(fieldData.isProductDirty() ? "YES" : "NO");
                remarks_details.setText(fieldData.getAgentRemarks());
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
