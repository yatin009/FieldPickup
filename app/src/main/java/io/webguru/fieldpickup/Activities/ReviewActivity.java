package io.webguru.fieldpickup.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

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
 * Created by mahto on 2/3/17.
 */

public class ReviewActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;

    FieldData fieldData;

    private String imageId;

    File output = null;

    private static FieldDataDataSource fieldDataDataSource;
    private static DocketDataSource docketDataSource;


    TextView is_same_product_details;
    TextView edit_prod_desc_ques;
    TextView edit_reason_ques;
    TextView edit_quantity_ques;
    TextView quantity_details;
    TextView is_all_parts_available_details;
    TextView is_correct_issue_category_details;
    TextView is_dirty_details;
    TextView remarks_details;
    TextView is_damaged_details;

    String query = "";


    private static final int CONTENT_REQUEST = 1221;
    private static final int CAMERA_REQUEST = 1222;

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
            edit_prod_desc_ques = (TextView) findViewById(R.id.edit_prod_desc_ques);
            edit_quantity_ques = (TextView) findViewById(R.id.edit_quantity_ques);
            edit_reason_ques = (TextView) findViewById(R.id.edit_reason_ques);
            quantity_details = (TextView) findViewById(R.id.quantity_details);
            is_all_parts_available_details = (TextView) findViewById(R.id.is_all_parts_available_details);
            is_correct_issue_category_details = (TextView) findViewById(R.id.is_correct_issue_category_details);
            is_dirty_details = (TextView) findViewById(R.id.is_dirty_details);
            remarks_details = (TextView) findViewById(R.id.remarks_details);
            is_damaged_details = (TextView) findViewById(R.id.is_damaged_details);

            is_same_product_details.setText(fieldData.getIsSameProduct());
            edit_prod_desc_ques.setText("1. Same product received ?\n\n   (" + docket.getDescription() + ")");
            edit_quantity_ques.setText("2. What are the number of item picked up ?\n\n   (Quantity to be Picked : " + docket.getQuantity() + ")");
            edit_reason_ques.setText("4. Is the reason of return verified ?\n\n   (" + GlobalFunction.getReasonCodeMap().get(docket.getReason()) + ")");
            quantity_details.setText(fieldData.getQuantity() + "");
            is_all_parts_available_details.setText(fieldData.getIsAllPartsAvailable());
            is_correct_issue_category_details.setText(fieldData.getIsIssueCategoryCorrect());
            is_dirty_details.setText(fieldData.getIsProductClean());
            remarks_details.setText(fieldData.getAgentRemarks());
            is_damaged_details.setText(fieldData.getIsDamaged());
            setCapturedImage("1","DETAILS");
            setCapturedImage("2","DETAILS");
            setCapturedImage("3","DETAILS");

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

    @OnClick(R.id.edit_prod_desc)
    public void updateIsSameProduct() {
        query = "Same Product";
        openUpdateWindow("Same product received ?", fieldData.getIsSameProduct(), "RADIO");
    }

    @OnClick(R.id.edit_quantity)
    public void updateQuantity() {
        query = "Quantity";
        openUpdateWindow("What are the number of item picked up ?", fieldData.getQuantity() + "", "NUMBER");
    }

    @OnClick(R.id.edit_all_parts_available)
    public void updateIsAllParts() {
        query = "All Parts";
        openUpdateWindow("Are all accessories/parts available with brand box ?", fieldData.getIsAllPartsAvailable(), "RADIO");
    }


    @OnClick(R.id.edit_reason)
    public void updateReason() {
        query = "Category Issue";
        openUpdateWindow("Is the reason of return varified ?", fieldData.getIsIssueCategoryCorrect(), "RADIO");
    }

    @OnClick(R.id.edit_is_clean)
    public void updateIsClean() {
        query = "Is Clean";
        openUpdateWindow("Is the product clean/not used ?", fieldData.getIsProductClean(), "RADIO");
    }

    @OnClick(R.id.edit_is_damaged)
    public void updateIsDamaged() {
        query = "Is Damaged";
        openUpdateWindow("Is the product damaged ?", fieldData.getIsDamaged(), "RADIO");
    }

    @OnClick(R.id.edit_remarks)
    public void updateRemarks() {
        query = "Remarks";
        openUpdateWindow("Remarks", fieldData.getAgentRemarks(), "STRING");
    }

    private void openUpdateWindow(String question, String answer, String type) {


        Intent intent = new Intent(this, EditValueActivity.class);
        intent.putExtra("Question", question);
        intent.putExtra("Type", type);
        intent.putExtra("Answer", answer);
        intent.putExtra("ActualQuantity", docket.getQuantity());
        startActivityForResult(intent, CONTENT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            String updatedValue = null;
            if (data == null) {
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
            } else if (query.equals("Is Clean")) {
                fieldData.setIsProductClean(updatedValue);
                is_dirty_details.setText(fieldData.getIsProductClean());
            }  else if (query.equals("Is Damaged")) {
                fieldData.setIsDamaged(updatedValue);
                is_damaged_details.setText(fieldData.getIsDamaged());
            } else if (query.equals("Remarks")) {
                fieldData.setAgentRemarks(updatedValue);
                remarks_details.setText(fieldData.getAgentRemarks());
            }
        } else if (requestCode == CAMERA_REQUEST) {
            setCapturedImage(imageId,"CAMERA");
        }

    }

    @OnClick(R.id.update_docket)
    public void updateDocket() {

        Integer reasonCode = Integer.parseInt(docket.getReason());
        boolean isQCPassed = getQcResult(reasonCode, docket, fieldData);


        if(isQCPassed){
            fieldData.setIsQcCleared(1);
        } else {
            fieldData.setIsQcCleared(0);
        }


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
            if (fieldDataDataSource != null) {
                fieldDataDataSource.close();
            }
        }


        String path = "Field Pickup/Picked";
        File dir = new File(path);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        try{
            File image1 = new File("Field Pickup/Temp/" + docket.getAwbNumber() + "_1.jpeg");
            image1.renameTo(new File("Field Pickup/Picked/" + docket.getAwbNumber() + "_1.jpeg"));
            File image2 = new File("Field Pickup/Temp/" + docket.getAwbNumber() + "_2.jpeg");
            image2.renameTo(new File("Field Pickup/Picked/" + docket.getAwbNumber() + "_2.jpeg"));
            File image3 = new File("Field Pickup/Temp/" + docket.getAwbNumber() + "_3.jpeg");
            image3.renameTo(new File("Field Pickup/Picked/" + docket.getAwbNumber() + "_3.jpeg"));
        } catch (Exception e){

        }

        String dirPath = "Field Pickup/Temp";
        dir = new File(dirPath);
        try {
            dir.delete();
        } catch (SecurityException ex) {
        }

        Intent intent = new Intent(this, QcResultActivity.class);
        intent.putExtra("Docket", docket);
        intent.putExtra("FieldData", fieldData);
        intent.putExtra("isQCPassed", isQCPassed);
        startActivity(intent);
        finish();
    }

    private boolean getQcResult(Integer reasonCode, Docket docket, FieldData fieldData){
        Map<Integer,Map<String,String>> qcMatrix = GlobalFunction.getQcMatrix();
        if(qcMatrix.containsKey(reasonCode)){
            Map<String,String> valueMap = qcMatrix.get(reasonCode);

            if(valueMap.get("Product Description").equals("YES")){
                if(fieldData.getIsSameProduct().equals("NO")){
                    return false;
                }
            }
            if(valueMap.get("Quantity").equals("YES")){
                if(!docket.getQuantity().equals(fieldData.getQuantity())){
                    return false;
                }
            }
            if(valueMap.get("Accessories in brand box").equals("YES")){
                if(fieldData.getIsAllPartsAvailable().equals("NO")){
                    return false;
                }
            }
            if(valueMap.get("Reason").equals("YES")){
                if(fieldData.getIsIssueCategoryCorrect().equals("NO")){
                    return false;
                }
            }
            if(valueMap.get("Clean/Not Used").equals("YES")){
                if(fieldData.getIsProductClean().equals("NO")){
                    return false;
                }
            }
            if(valueMap.get("Is Damaged").equals("YES")){
                if(fieldData.getIsDamaged().equals("YES")){
                    return false;
                }
            }

        }
        return true;
    }



    @OnClick(R.id.capturedImage1)
    public void openImage1() {
        ImageView imageView = (ImageView) findViewById(R.id.capturedImage1);
        if (imageView.getDrawable() == null){
            return;
        }
        openImage("1");
    }

    @OnClick(R.id.capturedImage2)
    public void openImage2() {
        ImageView imageView = (ImageView) findViewById(R.id.capturedImage2);
        if (imageView.getDrawable() == null){
            return;
        }
        openImage("2");
    }

    @OnClick(R.id.capturedImage3)
    public void openImage3() {
        ImageView imageView = (ImageView) findViewById(R.id.capturedImage3);
        if (imageView.getDrawable() == null){
            return;
        }
        openImage("3");
    }

    private void openImage(String id) {
        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("imageName", docket.getAwbNumber() + "_" + id + ".jpeg");
        intent.putExtra("awbNumber", docket.getAwbNumber());
        intent.putExtra("imageNumber", id);
        intent.putExtra("source", "REVIEW");
        startActivity(intent);
    }

    @OnClick(R.id.image1)
    public void captureImage1() {
        imageId = "1";
        capture("1");
    }

    @OnClick(R.id.image2)
    public void captureImage2() {
        imageId = "2";
        capture("2");
    }

    @OnClick(R.id.image3)
    public void captureImage3() {
        imageId = "3";
        capture("3");
    }


    public void capture(String imageId) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
            String path = DocketUpdateActivity.checkForImageLocation();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(path);
            output = new File(dir, docket.getAwbNumber() + "_" + imageId + ".jpeg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, Uri.fromFile(output));
            startActivityForResult(cameraIntent, CAMERA_REQUEST);

        }
    }


    protected void setCapturedImage(String imageId, String source) {
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
            if(source.equals("CAMERA")) {
                imageView.setImageURI(getImageUri(docket.getAwbNumber() + "_" + id + ".jpeg"));
            } else {
                File f = Environment.getExternalStoragePublicDirectory(DocketUpdateActivity.checkForImageLocation() + "/" + docket.getAwbNumber() + "_" + id + ".jpeg");
                imageView.setImageURI(Uri.parse(f.getAbsolutePath()));
            }
        }
    }

    private Uri getImageUri(String imageName) {
        String path = DocketUpdateActivity.checkForImageLocation();
        File dir = Environment.getExternalStoragePublicDirectory(path);
        output = new File(dir, imageName);
        Bitmap photo = BitmapFactory.decodeFile(output.getAbsolutePath());
        photo = Bitmap.createScaledBitmap(photo, 600, 600, true);

        File f = Environment.getExternalStoragePublicDirectory(DocketUpdateActivity.checkForImageLocation() + "/" + imageName);
        FileOutputStream fo = null;
        try {
            f.createNewFile();
            fo = new FileOutputStream(f);
            photo.compress(Bitmap.CompressFormat.JPEG, 70, fo);
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(f.getAbsolutePath());
    }

}
