package io.webguru.fieldpickup.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 4/3/17.
 */

public class TestActivity extends AppCompatActivity {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.details_layout)
    LinearLayout mLinearLayout;

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
    TextView status;
    ImageView imageView;

    private LinearLayout qualityCheckLayout;
    private LinearLayout capturedDetailsLayout;

    private static FieldDataDataSource fieldDataDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
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

//            LinearLayout questionLayout = (LinearLayout) findViewById(R.id.qc_layout);
//
//            LayoutInflater layoutInflater = (LayoutInflater)
//                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            questionLayout.addView(layoutInflater.inflate(R.layout.radio_type_layout, mLinearLayout) );
//            questionLayout.addView(layoutInflater.inflate(R.layout.radio_type_layout, mLinearLayout) );
//            questionLayout.addView(layoutInflater.inflate(R.layout.radio_type_layout, mLinearLayout) );
//            questionLayout.addView(layoutInflater.inflate(R.layout.radio_type_layout, mLinearLayout) );


//
            getSupportActionBar().setTitle(docket.getDocketNumber());

        }
    }
}
