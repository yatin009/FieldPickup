package io.webguru.fieldpickup.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Question;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 24/1/17.
 */

public class DocketUpdateActivity extends AppCompatActivity {

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
    private static DocketDataSource docketDataSource;

    private static final int CONTENT_REQUEST=1337;
    File output = null;

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


    Map<Integer, Integer> idMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_docket);
        ButterKnife.bind(this);



        getIdMap();

        LinearLayout questionLayout = (LinearLayout) findViewById(R.id.qc_layout);
        int index = 1;
//            LinearLayout linearLayout = new LinearLayout(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup viewGroup = null;
        for(Question question : GlobalFunction.getQuestionList()){
            View linearLayout = null;
            if(question.getType().equals("RADIO")) {
                viewGroup = (ViewGroup)inflater.inflate(R.layout.radio_type_layout,null);
                linearLayout = GlobalFunction.getRadioLayout(viewGroup, this, index, question.getQuestion(), idMap.get(index++));
            } else if(question.getType().equals("TEXT")) {
//                linearLayout = GlobalFunction.getTextLayout(this, index, question.getQuestion(), idMap.get(index++),"TEXT");
            } else if(question.getType().equals("NUMBER_DROPDOWN")) {
//                linearLayout = GlobalFunction.getTextLayout(this, index, question.getQuestion(), idMap.get(index++),"NUMBER");
            }

//            View childLayout = inflater.inflate(R.layout.radio_type_layout,(ViewGroup) findViewById(R.id.radio_lay));
            questionLayout.removeAllViews();
            questionLayout.addView(viewGroup);
        }



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

            if(docket.isPending() == 1){
                capturedDetailsLayout = (LinearLayout)this.findViewById(R.id.captured_details_layout);
                capturedDetailsLayout.setVisibility(LinearLayout.GONE);
            } else {
                qualityCheckLayout = (LinearLayout)this.findViewById(R.id.qc_layout);
                qualityCheckLayout.setVisibility(LinearLayout.GONE);
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
                status = (TextView) findViewById(R.id.status);
                imageView = (ImageView) findViewById(R.id.capturedImageView);

                is_same_product_details.setText(fieldData.getIsSameProduct());
                quantity_details.setText(fieldData.getQuantity()+"");
                is_all_parts_available_details.setText(fieldData.getIsAllPartsAvailable());
                is_correct_issue_category_details.setText(fieldData.getIsIssueCategoryCorrect());
                is_dirty_details.setText(fieldData.getIsProductDirty());
                remarks_details.setText(fieldData.getAgentRemarks());
                status.setText(fieldData.getStatus());
                File f = Environment.getExternalStoragePublicDirectory("Field Pickup/"+docket.getDocketNumber()+".jpeg");
                Uri uri = Uri.parse(f.getAbsolutePath());
                imageView.setImageURI(uri);
            }
            getSupportActionBar().setTitle(docket.getDocketNumber());

        }
    }


    private void getIdMap(){
        idMap = new HashMap<>();
        idMap.put(1,R.id.id_1);
        idMap.put(2,R.id.id_2);
        idMap.put(3,R.id.id_3);
        idMap.put(4,R.id.id_4);
        idMap.put(5,R.id.id_5);
        idMap.put(6,R.id.id_6);
        idMap.put(7,R.id.id_7);
        idMap.put(8,R.id.id_8);
        idMap.put(9,R.id.id_9);
        idMap.put(10,R.id.id_10);
        idMap.put(11,R.id.id_11);
        idMap.put(12,R.id.id_12);
        idMap.put(13,R.id.id_13);
        idMap.put(14,R.id.id_14);
        idMap.put(15,R.id.id_15);
    }

    @OnClick(R.id.image)
    public void captureImage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            docket = (Docket) bundle.get("Docket");
            String path = checkForImageLocation();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(path);
            output=new File(dir, docket.getDocketNumber()+".jpeg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, Uri.fromFile(output));
            startActivityForResult(cameraIntent,CONTENT_REQUEST);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                ImageView imageView = (ImageView) findViewById(R.id.capturedImage);
                imageView.setImageDrawable(null);
                Bitmap photo = BitmapFactory.decodeFile(output.getAbsolutePath());
                photo = Bitmap.createScaledBitmap(photo, 600, 600, true);
                Bundle bundle = getIntent().getExtras();
                if (bundle!=null) {
                    docket = (Docket) bundle.get("Docket");
                    File f = Environment.getExternalStoragePublicDirectory(checkForImageLocation()+"/"+docket.getDocketNumber()+".jpeg");
                    FileOutputStream fo = null;
                    try {
                        f.createNewFile();
                        fo = new FileOutputStream(f);
                        photo.compress(Bitmap.CompressFormat.JPEG, 70, fo);
                        fo.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Uri uri = Uri.parse(f.getAbsolutePath());
                    imageView.setImageURI(uri);
                }
            }
        }
    }

    @OnClick(R.id.update_docket)
    public void updateDocket() {
        isSameProduct = (RadioGroup) findViewById(R.id.is_same_product);
        quantity = (TextInputEditText) findViewById(R.id.product_quantity);
        remarks = (EditText) findViewById(R.id.remarks);
        isAllPartsAvailable = (RadioGroup) findViewById(R.id.is_all_parts_available);
        isCorrectIssueCategory = (RadioGroup) findViewById(R.id.is_correct_issue_category);
        isDirty = (RadioGroup) findViewById(R.id.is_dirty);

        isSameProductRadioButton = (RadioButton) findViewById(isSameProduct.getCheckedRadioButtonId());

        isAllPartsAvailableRadioButton = (RadioButton) findViewById(isAllPartsAvailable.getCheckedRadioButtonId());

        isCorrectIssueCategoryRadioButton = (RadioButton) findViewById(isCorrectIssueCategory.getCheckedRadioButtonId());

        isDirtyRadioButton = (RadioButton) findViewById(isDirty.getCheckedRadioButtonId());

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            docket = (Docket) bundle.get("Docket");
        }

        if(docket != null) {

            String isSameProduct = isSameProductRadioButton != null ? isSameProductRadioButton.getText().toString() : null;
            int qunt = isSameProductRadioButton != null && !quantity.getText().toString().equals("") ? Integer.parseInt(quantity.getText().toString()) : 0;
            String isAllPartsAvailable = isAllPartsAvailableRadioButton != null ? isAllPartsAvailableRadioButton.getText().toString() : null;
            String isCorrectIssueCategory = isCorrectIssueCategoryRadioButton != null ? isCorrectIssueCategoryRadioButton.getText().toString() : null;
            String isDirty = isDirtyRadioButton != null ? isDirtyRadioButton.getText().toString() : null;
            String remarksByFe = remarks != null ? remarks.getText().toString() : null;
            boolean isAnyError = validateCapturedData(isSameProduct, qunt, isAllPartsAvailable, isCorrectIssueCategory, isDirty, remarksByFe);
            if (isAnyError) {
                return;
            }
            FieldData fieldData = new FieldData(isSameProduct, qunt, isAllPartsAvailable, isCorrectIssueCategory, isDirty, remarksByFe, docket.getId());
            fieldData.setStatus("Package Picked");

            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("Docket", docket);
            intent.putExtra("FieldData", fieldData);
            startActivity(intent);


        }
        finish();
    }

    private boolean validateCapturedData(String isSameProduct,int quantity,String isAllPartsAvailable,String isCorrectIssueCategory,
                                         String isDirty,String remarksByFe){
        boolean isAnyError = false;

        ImageView imageView = (ImageView) findViewById(R.id.capturedImage);
        boolean hasDrawable = (imageView.getDrawable() != null);

        if(isSameProduct == null || isSameProduct.equals("")){
            Toast.makeText(this, "Set value for  \"1. Same product received ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if(quantity == 0){
            Toast.makeText(this, "Set value for  \"2. Quantity\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if(isAllPartsAvailable == null || isAllPartsAvailable.equals("")){
            Toast.makeText(this, "Set value for  \"3. All accessories/parts available ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if(isCorrectIssueCategory == null || isCorrectIssueCategory.equals("")){
            Toast.makeText(this, "Set value for  \"4. Issue Category is Correct ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if(isDirty == null || isDirty.equals("")){
            Toast.makeText(this, "Set value for  \"5. Product is dirty/used ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if(remarksByFe == null || remarksByFe.equals("")){
            Toast.makeText(this, "Set value for  \"6. Remarks\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if(!hasDrawable) {
            Toast.makeText(this, "Capture Image", Toast.LENGTH_LONG).show();
            isAnyError = true;
        }
        return isAnyError;
    }

    private String checkForImageLocation(){

        String path = "Field Pickup";
        File dir = new File(path) ;
        if (!dir.exists()) {
            try{
                dir.mkdir();
            }catch(SecurityException e){
            }
        }
        return path;
    }


}
