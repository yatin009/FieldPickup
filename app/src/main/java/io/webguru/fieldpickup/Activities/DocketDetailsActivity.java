package io.webguru.fieldpickup.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.ApiHandler.dto.QcQuestionDTO;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Product;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 24/1/17.
 */

public class DocketDetailsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.products_layout)
    LinearLayout productsParentLayout;
    @Bind(R.id.captured_details_layout)
    LinearLayout fieldDataParentLayout;

    Docket docket;
    TextView customerName;
    TextView contactNumber;
    TextView address;
    TextView pincode;
    TextView pickupStatus;
    TextView statusDescription;
    TextView isQcPassed;
    TextView orderNumber;

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
            pincode = (TextView) findViewById(R.id.pincode);
            pickupStatus = (TextView) findViewById(R.id.pickup_status);
            statusDescription = (TextView) findViewById(R.id.status_description);
            isQcPassed = (TextView) findViewById(R.id.is_qc_passed);
            orderNumber = (TextView) findViewById(R.id.order_number);

            if(docket.isPending() == 1){
                capturedDetailsLayout = (LinearLayout)this.findViewById(R.id.captured_details_layout);
                capturedDetailsLayout.setVisibility(LinearLayout.GONE);
            } else {
                ((LinearLayout) this.findViewById(R.id.update_button_layout)).setVisibility(LinearLayout.GONE);
                renderQuestionDetails(docket);
            }
            customerName.setText(docket.getCustomerName());
            contactNumber.setText(docket.getCustomerContact());
            address.setText(docket.getCustomerAddress());
            pincode.setText(docket.getPincode());
            pickupStatus.setText(docket.getStatus());
            statusDescription.setText(docket.getStatusDescription());
            isQcPassed.setText(docket.getIsQcCheckCleared());
            orderNumber.setText(docket.getOrderNumber());

            inflateProductLayoutInfo();

            inflateFieldData();
            getSupportActionBar().setTitle(docket.getAwbNumber());

        }
    }

    private void renderQuestionDetails(Docket docket) {

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.details_layout);
        List<Product> productList = docket.getProducts();
        for(Product product : productList){
            TextView textView = new TextView(this);
            textView.setText("Product : " + product.getDescription());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20,50,20,30);
            textView.setLayoutParams(params);
            textView.setTextSize(15);
            linearLayout.addView(textView);
            for(QcQuestionDTO qcQuestionDTO : product.getQcQuestions()){
                linearLayout.addView(renderQuestionDetailsView(qcQuestionDTO, product.getDescription()));
            }

            String productDescription = product.getDescription();
            productDescription = productDescription.replaceAll(" ","_");
            linearLayout.addView(renderImageView(docket.getAwbNumber(), productDescription));

        }

    }

    private LinearLayout renderQuestionDetailsView(QcQuestionDTO qcQuestionDTO, String productDescription) {

        LinearLayout layout = null;
        LinearLayout childLinearLayout = null;
        TextView childTextView1 = null;
        TextView childTextView2 = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.template_question_details, null);
        childLinearLayout = (LinearLayout) layout.getChildAt(0);
        childTextView1 = (TextView) childLinearLayout.getChildAt(0);
        childTextView2 = (TextView) childLinearLayout.getChildAt(1);
        childTextView1.setText(qcQuestionDTO.getQuestion());
        childTextView2.setText(qcQuestionDTO.getAnswer() != null ? qcQuestionDTO.getAnswer() : "NA");
        return layout;
    }

    private LinearLayout renderImageView(final String awbNumber, final String productDescription) {

        final String imageName = awbNumber + "_" + productDescription;
        LinearLayout layout = null;
        LinearLayout childLinearLayout = null;
        ImageView childImageView1 = null;
        ImageView childImageView2 = null;
        ImageView childImageView3 = null;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (LinearLayout) inflater.inflate(R.layout.template_image_layout, null);
        childLinearLayout = (LinearLayout) layout.getChildAt(0);
        childImageView1 = (ImageView) childLinearLayout.getChildAt(0);
        childImageView2 = (ImageView) childLinearLayout.getChildAt(1);
        childImageView3 = (ImageView) childLinearLayout.getChildAt(2);
        childImageView1.setImageDrawable(null);
        childImageView1.setImageURI(getImageUri(imageName + "_1.jpeg"));
        childImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof ImageView)
                {
                    openImage("1", awbNumber, imageName + "_1.jpeg");
                }
            }
        });
        childImageView2.setImageDrawable(null);
        childImageView2.setImageURI(getImageUri(imageName + "_2.jpeg"));
        childImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof ImageView)
                {
                    openImage("2", awbNumber, imageName + "_2.jpeg");
                }
            }
        });
        childImageView3.setImageDrawable(null);
        childImageView3.setImageURI(getImageUri(imageName + "_3.jpeg"));
        childImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof ImageView)
                {
                    openImage("3", awbNumber, imageName + "_3.jpeg");
                }
            }
        });

        return layout;
    }

    private void inflateProductLayoutInfo(){
        LayoutInflater inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        for(Product product : docket.getProducts()){
            View view = inflater.inflate(R.layout.prodcut_info_layout, productsParentLayout, false);
            TextView productDescription = (TextView) view.findViewById(R.id.product_description);
            TextView reason = (TextView) view.findViewById(R.id.reason);
            TextView actualQuantity = (TextView) view.findViewById(R.id.actual_quantity);
            productDescription.setText(product.getDescription());
            reason.setText(GlobalFunction.getReasonCodeMap().get(product.getReason()));
            actualQuantity.setText(product.getQuantity()+"");
            productsParentLayout.addView(view);
        }
    }

    private void inflateFieldData(){
        if(docket.getProducts().get(0).getFieldData()==null){
            return;
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        for(Product product : docket.getProducts()) {
            FieldData fieldData = product.getFieldData();
            if (fieldData != null) {
                View view = inflater.inflate(R.layout.fielddata_info_layout, fieldDataParentLayout, false);
                TextView is_same_product_details = (TextView) view.findViewById(R.id.is_same_product_details);
                TextView quantity_details = (TextView) view.findViewById(R.id.quantity_details);
                TextView is_all_parts_available_details = (TextView) view.findViewById(R.id.is_all_parts_available_details);
                TextView is_correct_issue_category_details = (TextView) view.findViewById(R.id.is_correct_issue_category_details);
                TextView is_dirty_details = (TextView) view.findViewById(R.id.is_dirty_details);
                TextView remarks_details = (TextView) view.findViewById(R.id.remarks_details);
                TextView status = (TextView) view.findViewById(R.id.status);
                TextView is_damaged_details = (TextView) view.findViewById(R.id.is_damaged_details);
                TextView is_qc_cleared_details = (TextView) view.findViewById(R.id.is_qc_cleared_details);

                remarks_details.setText(fieldData.getAgentRemarks());
                status.setText(fieldData.getStatus());
                String qc = "NA";
                if (fieldData.getIsQcCleared() == null) {
                    qc = "NA";
                } else if (fieldData.getIsQcCleared() == 1) {
                    qc = "YES";
                } else if (fieldData.getIsQcCleared() == 0) {
                    qc = "NO";
                }
                is_qc_cleared_details.setText(qc);
                fieldDataParentLayout.addView(view);
//                setCapturedImage("1");
//                setCapturedImage("2");
//                setCapturedImage("3");
            }
        }
    }

    @OnClick(R.id.update_docket)
    public void updateDocket() {
        Intent intent = new Intent(this, DocketUpdateActivity.class);
        intent.putExtra("Docket", docket);
        intent.putExtra("Product", docket.getProducts().get(0));
        intent.putExtra("Step", "1");
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


//    @OnClick(R.id.capturedImage1)
//    public void openImage1() {
//        openImage("1");
//    }
//
//    @OnClick(R.id.capturedImage2)
//    public void openImage2() {
//        openImage("2");
//    }
//
//    @OnClick(R.id.capturedImage3)
//    public void openImage3() {
//        openImage("3");
//    }


    private void openImage(String imageId, String awbNumber, String imageName){
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("imageName", imageName);
        intent.putExtra("awbNumber", awbNumber);
        intent.putExtra("imageNumber", imageId);
        intent.putExtra("source", "DETAILS");
        startActivity(intent);
    }



}
