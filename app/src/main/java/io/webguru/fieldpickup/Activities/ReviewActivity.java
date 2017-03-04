package io.webguru.fieldpickup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 2/3/17.
 */

public class ReviewActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;

    FieldData fieldData;

    private static FieldDataDataSource fieldDataDataSource;
    private static DocketDataSource docketDataSource;


    TextView is_same_product_details;
    TextView quantity_details;
    TextView is_all_parts_available_details;
    TextView is_correct_issue_category_details;
    TextView is_dirty_details;
    TextView remarks_details;
    TextView status;

    String query = "";


    private static final int CONTENT_REQUEST = 1221;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
            fieldData = (FieldData) bundle.get("FieldData");

            is_same_product_details = (TextView) findViewById(R.id.is_same_product_details);
            quantity_details = (TextView) findViewById(R.id.quantity_details);
            is_all_parts_available_details = (TextView) findViewById(R.id.is_all_parts_available_details);
            is_correct_issue_category_details = (TextView) findViewById(R.id.is_correct_issue_category_details);
            is_dirty_details = (TextView) findViewById(R.id.is_dirty_details);
            remarks_details = (TextView) findViewById(R.id.remarks_details);
            status = (TextView) findViewById(R.id.status);

            is_same_product_details.setText(fieldData.getIsSameProduct());
            quantity_details.setText(fieldData.getQuantity() + "");
            is_all_parts_available_details.setText(fieldData.getIsAllPartsAvailable());
            is_correct_issue_category_details.setText(fieldData.getIsIssueCategoryCorrect());
            is_dirty_details.setText(fieldData.getIsProductDirty());
            remarks_details.setText(fieldData.getAgentRemarks());

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }

    }

    @OnClick(R.id.is_same_product_details)
    public void updateIsSameProduct() {
        query = "Same Product";
        openUpdateWindow("Same product received ?", fieldData.getIsSameProduct(), "RADIO");
    }

    @OnClick(R.id.quantity_details)
    public void updateQuantity() {
        query = "Quantity";
        openUpdateWindow("Picked Quantity", fieldData.getQuantity() + "", "NUMBER");
    }

    @OnClick(R.id.is_all_parts_available_details)
    public void updateIsAllParts() {
        query = "All Parts";
        openUpdateWindow("All accessories/parts available ?", fieldData.getIsAllPartsAvailable(), "RADIO");
    }


    @OnClick(R.id.is_correct_issue_category_details)
    public void updateIsCategory() {
        query = "Category Issue";
        openUpdateWindow("Issue Category is Correct ?", fieldData.getIsIssueCategoryCorrect(), "RADIO");
    }

    @OnClick(R.id.is_dirty_details)
    public void updateIsDirty() {
        query = "Dirty Product";
        openUpdateWindow("Product is dirty/used ?", fieldData.getIsProductDirty(), "RADIO");
    }

    @OnClick(R.id.remarks_details)
    public void updateRemarks() {
        query = "Remarks";
        openUpdateWindow("Remarks", fieldData.getAgentRemarks(), "STRING");
    }

    private void openUpdateWindow(String question, String answer, String type) {


        Intent intent = new Intent(this, EditValueActivity.class);
        intent.putExtra("Question", question);
        intent.putExtra("Type", type);
        intent.putExtra("Answer", answer);
        startActivityForResult(intent, CONTENT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            String updatedValue = null;
            if(data == null){
                return;
            }
            updatedValue = data.getStringExtra("UpdatedValue");

            if (query.equals("Same Product")) {
                fieldData.setIsSameProduct(updatedValue);
                is_same_product_details.setText(fieldData.getIsSameProduct());
            } else if (query.equals("Quantity")) {
                Integer value = Integer.parseInt(updatedValue);
                fieldData.setQuantity(value);
                quantity_details.setText(fieldData.getQuantity() + "");
            } else if (query.equals("All Parts")) {
                fieldData.setIsAllPartsAvailable(updatedValue);
                is_all_parts_available_details.setText(fieldData.getIsAllPartsAvailable());
            } else if (query.equals("Category Issue")) {
                fieldData.setIsIssueCategoryCorrect(updatedValue);
                is_correct_issue_category_details.setText(fieldData.getIsIssueCategoryCorrect());
            } else if (query.equals("Dirty Product")) {
                fieldData.setIsProductDirty(updatedValue);
                is_dirty_details.setText(fieldData.getIsProductDirty());
            } else if (query.equals("Remarks")) {
                fieldData.setAgentRemarks(updatedValue);
                remarks_details.setText(fieldData.getAgentRemarks());
            }
        }

    }

    @OnClick(R.id.update_docket)
    public void updateDocket() {
        try {
            fieldDataDataSource = new FieldDataDataSource(this);
            fieldDataDataSource.open();
            fieldDataDataSource.insertFieldData(fieldData);
            docketDataSource = new DocketDataSource(this);
            docketDataSource.open();
            docketDataSource.markDocketAsDone(docket.getId());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (docketDataSource != null) {
                docketDataSource.close();
            }
            if (docketDataSource != null) {
                fieldDataDataSource.close();
            }
        }
        finish();
    }

}
