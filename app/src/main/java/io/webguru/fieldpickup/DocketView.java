package io.webguru.fieldpickup;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.AndroidSlidingTab.SlidingTabLayout;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;

public class DocketView extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;

    RadioGroup isSameProduct;
    TextView quantity;
    EditText remarks;
    RadioGroup isAllPartsAvailable;
    RadioGroup isCorrectIssueCategory;
    RadioGroup isDirty;
    RadioButton isSameProductRadioButton;
    RadioButton isAllPartsAvailableRadioButton;
    RadioButton isCorrectIssueCategoryRadioButton;
    RadioButton isDirtyRadioButton;

    private static FieldDataDataSource fieldDataDataSource;
    private static DocketDataSource docketDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docket_view);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            docket = (Docket) bundle.get("Docket");
        }
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(docket.getDocketNumber());
        }
    }

    @OnClick(R.id.update_docket)
    public void updateDocket(){

        isSameProduct = (RadioGroup) findViewById(R.id.is_same_product);
        quantity = (TextInputEditText) findViewById(R.id.product_quantity);
        remarks = (EditText) findViewById(R.id.remarks);
        isAllPartsAvailable = (RadioGroup) findViewById(R.id.is_all_parts_available);
        isCorrectIssueCategory = (RadioGroup) findViewById(R.id.is_correct_issue_category);
        isDirty = (RadioGroup) findViewById(R.id.is_dirty);

        FieldData fieldData = new FieldData();

        isSameProductRadioButton = (RadioButton) findViewById(isSameProduct.getCheckedRadioButtonId());

        isAllPartsAvailableRadioButton = (RadioButton) findViewById(isAllPartsAvailable.getCheckedRadioButtonId());

        isCorrectIssueCategoryRadioButton = (RadioButton) findViewById(isCorrectIssueCategory.getCheckedRadioButtonId());

        isDirtyRadioButton = (RadioButton) findViewById(isDirty.getCheckedRadioButtonId());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            docket = (Docket) bundle.get("Docket");
        }

        if(docket != null) {
            fieldDataDataSource = new FieldDataDataSource(this);
            fieldDataDataSource.open();
            fieldDataDataSource.insertFieldData(docket.getId(), isSameProductRadioButton.getText().toString().equals("YES") ? true : false, Integer.parseInt(quantity.getText().toString()),
                    isAllPartsAvailableRadioButton.getText().toString().equals("YES") ? true : false, isCorrectIssueCategoryRadioButton.getText().toString().equals("YES") ? true : false,
                    isDirtyRadioButton.getText().toString().equals("YES") ? true : false, remarks.getText() != null ? remarks.getText().toString() : null);
            fieldDataDataSource.close();

            docketDataSource = new DocketDataSource(this);
            docketDataSource.open();
            docketDataSource.markDocketDocketAsDone(docket.getId());
            docketDataSource.close();

        }
        Log.i("Field data ----- >>>>  ",fieldData.toString());
        startActivity(new Intent(this, MainActivity.class));
    }
}
