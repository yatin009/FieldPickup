package io.webguru.fieldpickup.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.GlobalFunction;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.POJO.Product;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 5/3/17.
 */

public class DocketUpdateActivity extends AppCompatActivity {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.update_docket)
    AppCompatButton udpateButton;

    private Docket docket;
    private Product product;
    private String step = "";

    private String imageId;

    private static final int CONTENT_REQUEST = 1337;
    File output = null;

    RadioGroup isSameProduct;
    EditText remarks;
    RadioGroup isAllPartsAvailable;
    RadioGroup isCorrectIssueCategory;
    RadioGroup isDirty;
    RadioGroup isDamaged;
    RadioButton isSameProductRadioButton;
    RadioButton isAllPartsAvailableRadioButton;
    RadioButton isCorrectIssueCategoryRadioButton;
    RadioButton isDirtyRadioButton;
    RadioButton isDamagedRadioButton;

    TextView prodDesc;
    TextView actualQuantity;
    TextView actualReason;

    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docket_update);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
            product = (Product) bundle.get("Product");
            step = bundle.getString("Step");

            List<String> quantityList = new ArrayList<>();
            quantityList.add("Select Quantity");
            for(int i=0; i<product.getQuantity(); i++){
                quantityList.add((i+1)+"");
            }
            spinner = (Spinner) findViewById(R.id.quantity);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, quantityList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            prodDesc = (TextView) findViewById(R.id.prod_desc);
            actualQuantity = (TextView) findViewById(R.id.actual_quantity);
            actualReason = (TextView) findViewById(R.id.actual_reason);
            prodDesc.setText(product.getDescription());
            actualQuantity.setText("Quantity to be picked : " + product.getQuantity()+"");
            actualReason.setText("Reason : " + GlobalFunction.getReasonCodeMap().get(product.getReason()));
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
        getSupportActionBar().setTitle(docket.getAwbNumber() + "-"+product.getDescription());
        udpateButton.setText("Step "+step +" of "+docket.getProducts().size());

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


    private void openImage(String id){

        Intent intent = new Intent(this, ImageViewActivity.class);
        intent.putExtra("imageName", docket.getAwbNumber()+ "_" + id + ".jpeg");
        intent.putExtra("awbNumber", docket.getAwbNumber());
        intent.putExtra("imageNumber", id);
        intent.putExtra("source", "UPDATE");
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
            String path = checkForImageLocation();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(path);
            output = new File(dir, docket.getAwbNumber() + "_" + imageId + ".jpeg");
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
            cameraIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, Uri.fromFile(output));
            startActivityForResult(cameraIntent, CONTENT_REQUEST);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
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
                Bitmap photo = BitmapFactory.decodeFile(output.getAbsolutePath());
                photo = Bitmap.createScaledBitmap(photo, 600, 600, true);
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    docket = (Docket) bundle.get("Docket");
                    File f = Environment.getExternalStoragePublicDirectory(checkForImageLocation() + "/" + docket.getAwbNumber() + "_" + id +".jpeg");
                    FileOutputStream fo = null;
                    try {
                        f.createNewFile();
                        fo = new FileOutputStream(f);
                        photo.compress(Bitmap.CompressFormat.JPEG, 70, fo);
                        fo.close();
                    } catch (Exception e) {
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
        String quantity = String.valueOf(((Spinner) findViewById(R.id.quantity)).getSelectedItem());
        remarks = (EditText) findViewById(R.id.remarks);
        isAllPartsAvailable = (RadioGroup) findViewById(R.id.is_all_parts_available);
        isCorrectIssueCategory = (RadioGroup) findViewById(R.id.is_correct_issue_category);
        isDirty = (RadioGroup) findViewById(R.id.is_product_clean);
        isDamaged = (RadioGroup) findViewById(R.id.is_product_damaged);

        isSameProductRadioButton = (RadioButton) findViewById(isSameProduct.getCheckedRadioButtonId());

        isAllPartsAvailableRadioButton = (RadioButton) findViewById(isAllPartsAvailable.getCheckedRadioButtonId());

        isCorrectIssueCategoryRadioButton = (RadioButton) findViewById(isCorrectIssueCategory.getCheckedRadioButtonId());

        isDirtyRadioButton = (RadioButton) findViewById(isDirty.getCheckedRadioButtonId());
        isDamagedRadioButton = (RadioButton) findViewById(isDamaged.getCheckedRadioButtonId());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
        }

        if (docket != null) {

            String isSameProduct = isSameProductRadioButton != null ? isSameProductRadioButton.getText().toString() : null;
            int qunt = !quantity.equals("Select Quantity") ? Integer.parseInt(quantity) : 0;
            String isAllPartsAvailable = isAllPartsAvailableRadioButton != null ? isAllPartsAvailableRadioButton.getText().toString() : null;
            String isCorrectIssueCategory = isCorrectIssueCategoryRadioButton != null ? isCorrectIssueCategoryRadioButton.getText().toString() : null;
            String isDirty = isDirtyRadioButton != null ? isDirtyRadioButton.getText().toString() : null;
            String isDamaged = isDamagedRadioButton != null ? isDamagedRadioButton.getText().toString() : null;
            String remarksByFe = remarks != null ? remarks.getText().toString() : null;
            boolean isAnyError = validateCapturedData(isSameProduct, qunt, isAllPartsAvailable, isCorrectIssueCategory, isDirty, remarksByFe, isDamaged);
            if (isAnyError) {
                return;
            }

            FieldData fieldData = new FieldData(isSameProduct, qunt, isAllPartsAvailable, isCorrectIssueCategory, isDirty, remarksByFe, docket.getId(),isDamaged, null, product.getId());
            fieldData.setStatus("Package Picked");

            product.setFieldData(fieldData);
            docket.getProducts().set(Integer.parseInt(step)-1, product);

            if(Integer.parseInt(step) == docket.getProducts().size()){ //final Product
                //TODO add products field data in DB
            }else{
                Intent intent = new Intent(this, DocketUpdateActivity.class);
                intent.putExtra("Docket", docket);
                intent.putExtra("Product", docket.getProducts().get(Integer.parseInt(step)));
                intent.putExtra("Step", (Integer.parseInt(step)+1)+"");
                startActivity(intent);
            }
//            Intent intent = new Intent(this, ReviewActivity.class);
//            intent.putExtra("Docket", docket);
//            intent.putExtra("FieldData", fieldData);
//            startActivity(intent);


        }
        finish();
    }

    private boolean validateCapturedData(String isSameProduct, int quantity, String isAllPartsAvailable, String isCorrectIssueCategory,
                                         String isDirty, String remarksByFe, String isDamaged) {
        boolean isAnyError = false;

        ImageView imageView1 = (ImageView) findViewById(R.id.capturedImage1);
        ImageView imageView2 = (ImageView) findViewById(R.id.capturedImage2);
        ImageView imageView3 = (ImageView) findViewById(R.id.capturedImage3);
        boolean isImage1Captured = (imageView1.getDrawable() != null);
        boolean isImage2Captured = (imageView2.getDrawable() != null);
        boolean isImage3Captured = (imageView3.getDrawable() != null);


        if (isSameProduct == null || isSameProduct.equals("")) {
            Toast.makeText(this, "Set value for  \"1. Same product received ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (quantity == 0) {
            Toast.makeText(this, "Set value for  \"2. What are the number of item picked up ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (isAllPartsAvailable == null || isAllPartsAvailable.equals("")) {
            Toast.makeText(this, "Set value for  \"3. Are all accessories/parts available with brand box ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (isCorrectIssueCategory == null || isCorrectIssueCategory.equals("")) {
            Toast.makeText(this, "Set value for  \"4. Is the reason of return varified ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (isDirty == null || isDirty.equals("")) {
            Toast.makeText(this, "Set value for  \"5. Is the product clean/not used ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (isDamaged == null || isDamaged.equals("")) {
            Toast.makeText(this, "Set value for  \"6. Is the product damaged ?\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (remarksByFe == null || remarksByFe.equals("")) {
            Toast.makeText(this, "Set value for  \"7. Remarks\"", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (!isImage1Captured) {
            Toast.makeText(this, "Capture Image 1", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (!isImage2Captured) {
            Toast.makeText(this, "Capture Image 2", Toast.LENGTH_LONG).show();
            isAnyError = true;
        } else if (!isImage3Captured) {
            Toast.makeText(this, "Capture Image 3", Toast.LENGTH_LONG).show();
            isAnyError = true;
        }
        return isAnyError;
    }

    public static String checkForImageLocation() {

        String path = "Field Pickup";
        File dir = new File(path);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

//        path = "Field Pickup/Picked";
//        dir = new File(path);
//        if (!dir.exists()) {
//            try {
//                dir.mkdir();
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            }
//        }


        String path2 = "Field Pickup/Temp";
        File dir2 = new File(path2);
        if (!dir2.exists()) {
            try {
                dir2.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return path2;
    }


}
