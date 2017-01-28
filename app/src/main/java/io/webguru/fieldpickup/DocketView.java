package io.webguru.fieldpickup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;

public class DocketView extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(docket.getDocketNumber());
        }

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
                ImageView imageView = (ImageView) findViewById(R.id.capturedImageView);
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
            try {
                String isSameProduct = isSameProductRadioButton != null ? isSameProductRadioButton.getText().toString() : null;
                int qunt = isSameProductRadioButton != null && !quantity.getText().toString().equals("")? Integer.parseInt(quantity.getText().toString()) : 0;
                String isAllPartsAvailable = isAllPartsAvailableRadioButton != null ? isAllPartsAvailableRadioButton.getText().toString() : null;
                String isCorrectIssueCategory = isCorrectIssueCategoryRadioButton != null ? isCorrectIssueCategoryRadioButton.getText().toString() : null;
                String isDirty = isDirtyRadioButton != null ? isDirtyRadioButton.getText().toString() : null;
                String remarksByFe = remarks != null ? remarks.getText().toString() : null;
                boolean isAnyError = validateCapturedData(isSameProduct,qunt,isAllPartsAvailable,isCorrectIssueCategory,isDirty,remarksByFe);
                if(isAnyError){
                    return;
                }

                FieldData fielData = new FieldData(isSameProduct,qunt,isAllPartsAvailable,isCorrectIssueCategory,isDirty,remarksByFe,docket.getId());

                fieldDataDataSource = new FieldDataDataSource(this);
                fieldDataDataSource.open();
                fieldDataDataSource.insertFieldData(fielData);
                docketDataSource = new DocketDataSource(this);
                docketDataSource.open();
                docketDataSource.markDocketDocketAsDone(docket.getId());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(docketDataSource != null) {
                    docketDataSource.close();
                }
                if(docketDataSource != null) {
                    fieldDataDataSource.close();
                }
            }
        }
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean validateCapturedData(String isSameProduct,int quantity,String isAllPartsAvailable,String isCorrectIssueCategory,
                                      String isDirty,String remarksByFe){
        boolean isAnyError = false;

        ImageView imageView = (ImageView) findViewById(R.id.capturedImageView);
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
