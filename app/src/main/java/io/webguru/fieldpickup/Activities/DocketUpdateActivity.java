package io.webguru.fieldpickup.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.StreamHandler;

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

    private Spinner spinner;

    private DocketDataSource docketDataSource;

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

            renderQuestion(this, docket);
            List<String> quantityList = new ArrayList<>();
            quantityList.add("Select Quantity");
            for (int i = 0; i < product.getQuantity(); i++) {
                quantityList.add((i + 1) + "");
            }
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
        getSupportActionBar().setTitle(docket.getAwbNumber() + "-" + product.getDescription());
        udpateButton.setText("Step " + step + " of " + docket.getProducts().size());

    }

    private void renderQuestion(Context context, Docket docket) {
        if (docket.getProducts() == null || docket.getProducts().isEmpty()) {
            return;
        }

        Integer index = 1;
        String helpText = null;
        String str = null;
        List<QcQuestionDTO> qcQuestionDTOs = docket.getProducts().get(Integer.parseInt(step) - 1).getQcQuestions();
        for (int i = qcQuestionDTOs.size(); i > 0; i--) {
            QcQuestionDTO qcQuestionDTO = qcQuestionDTOs.get(i - 1);
            if (qcQuestionDTO == null) {
                return;
            }
            helpText = null;
            if(qcQuestionDTO.getQuestion().toLowerCase().contains("description")){
                helpText = "Description : " + product.getDescription();
            } else if(qcQuestionDTO.getQuestion().toLowerCase().contains("quantity") || qcQuestionDTO.getQuestion().toLowerCase().contains("qty")){
                helpText = "Quantity to be picked : " + product.getQuantity();
            } else if(qcQuestionDTO.getQuestion().toLowerCase().contains("reason")){
                helpText = "Reason : " + GlobalFunction.getReasonCodeMap().get(product.getReason());
            }
            getRadioLayout(context, qcQuestionDTO, helpText, index);
//            if(qcQuestionDTO.getExpectedAnswer().contains("yes") || qcQuestionDTO.getExpectedAnswer().contains("no")){
//
//                if(qcQuestionDTO.getQuestion().contains("description")){
//                    helpText = product.getDescription();
//                }
//                getRadioLayout(context, qcQuestionDTO, helpText,index);
//            } else {
//                try {
//                    Integer.parseInt(qcQuestionDTO.getExpectedAnswer());
//                    str = qcQuestionDTO.getQuestion().toLowerCase();
//                    if(str.contains("qty") || str.contains("quantity")){
//                        helpText = "Quantity to be picked : " + qcQuestionDTO.getExpectedAnswer();
//                    }
//                    getNumberSpinnerLayout(context, qcQuestionDTO, helpText, index);
//                } catch (Exception e){
//                    getTextLayout(context, qcQuestionDTO, index);
//                }
//
//
//            }
        }
    }


    public void getRadioLayout(Context context, QcQuestionDTO qcQuestionDTO, String helpText, Integer index) {
        LinearLayout layout = null;
        LinearLayout childLinearLayout = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout qcLayout = (LinearLayout) findViewById(R.id.qc_layout);
        layout = (LinearLayout) inflater.inflate(R.layout.template_radio_group, null);
        childLinearLayout = (LinearLayout) layout.getChildAt(0);
        TextView questionTextView = (TextView) childLinearLayout.getChildAt(0);
        questionTextView.setText(qcQuestionDTO.getQuestion());
        TextView helpTextView = (TextView) childLinearLayout.getChildAt(1);
        if (helpText != null) {
            helpTextView.setText(helpText);
        } else {
            helpTextView.setVisibility(View.GONE);
        }
        int id = getResources().getIdentifier("AAA", "drawable", getPackageName());
        DocketUpdateActivity.this.findViewById(id);
        RadioGroup radioGroup = (RadioGroup) childLinearLayout.getChildAt(2);
        int resourceId = this.getResources().getIdentifier("question_" + qcQuestionDTO.getQuestionId(), "id", this.getPackageName());
        radioGroup.setId(resourceId);
        qcLayout.addView(layout, index++);

    }


    public void getTextLayout(Context context, QcQuestionDTO qcQuestionDTO, Integer index) {
        LinearLayout layout = null;
        LinearLayout childLinearLayout = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout qcLayout = (LinearLayout) findViewById(R.id.qc_layout);
        layout = (LinearLayout) inflater.inflate(R.layout.template_edit_text, null);
        childLinearLayout = (LinearLayout) layout.getChildAt(0);
        TextView questionTextView = (TextView) childLinearLayout.getChildAt(0);
        questionTextView.setText(qcQuestionDTO.getQuestion());
        EditText editTextView = (EditText) childLinearLayout.getChildAt(1);
        int resourceId = this.getResources().getIdentifier("question_" + qcQuestionDTO.getQuestionId(), "id", this.getPackageName());
        editTextView.setId(resourceId);
        qcLayout.addView(layout, index++);

    }


    public void getNumberSpinnerLayout(Context context, QcQuestionDTO qcQuestionDTO, String helpText, Integer index) {
        LinearLayout layout = null;
        LinearLayout childLinearLayout = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout qcLayout = (LinearLayout) findViewById(R.id.qc_layout);
        layout = (LinearLayout) inflater.inflate(R.layout.template_number_text_view, null);
        childLinearLayout = (LinearLayout) layout.getChildAt(0);
        TextView questionTextView = (TextView) childLinearLayout.getChildAt(0);
        questionTextView.setText(qcQuestionDTO.getQuestion());
        TextView helpTextView = (TextView) childLinearLayout.getChildAt(1);
        if (helpText != null) {
            helpTextView.setText(helpText);
        } else {
            helpTextView.setVisibility(View.GONE);
        }
        helpTextView.setId(100 + index);
        Spinner spinner = (Spinner) childLinearLayout.getChildAt(2);
        List<String> quantityList = new ArrayList<>();
        quantityList.add("Select Value");
        for (int i = 0; i < product.getQuantity(); i++) {
            quantityList.add((i + 1) + "");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, quantityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int resourceId = this.getResources().getIdentifier("question_" + qcQuestionDTO.getQuestionId(), "id", this.getPackageName());
        spinner.setId(resourceId);
        qcLayout.addView(layout, index);

    }

    @OnClick(R.id.capturedImage1)
    public void openImage1() {
        ImageView imageView = (ImageView) findViewById(R.id.capturedImage1);
        if (imageView.getDrawable() == null) {
            return;
        }
        openImage("1");
    }

    @OnClick(R.id.capturedImage2)
    public void openImage2() {
        ImageView imageView = (ImageView) findViewById(R.id.capturedImage2);
        if (imageView.getDrawable() == null) {
            return;
        }
        openImage("2");
    }

    @OnClick(R.id.capturedImage3)
    public void openImage3() {
        ImageView imageView = (ImageView) findViewById(R.id.capturedImage3);
        if (imageView.getDrawable() == null) {
            return;
        }
        openImage("3");
    }


    private void openImage(String id) {

        Intent intent = new Intent(this, ImageViewActivity.class);
        String productDescription = product.getDescription();
        productDescription = productDescription.replaceAll(" ", "_");
        intent.putExtra("imageName", docket.getAwbNumber() + "_" + productDescription + "_" + id + ".jpeg");
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
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(path);
            String productDescription = product.getDescription();
            productDescription = productDescription.replaceAll(" ", "_");
            output = new File(dir, docket.getAwbNumber() + "_" + productDescription + "_" + imageId + ".jpeg");
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
                    String productDescription = product.getDescription();
                    productDescription = productDescription.replaceAll(" ", "_");
                    File f = Environment.getExternalStoragePublicDirectory(checkForImageLocation() + "/" + docket.getAwbNumber() + "_" + productDescription + "_" + id + ".jpeg");
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
        }

        if (docket != null) {
            final Map<Integer, String> answerMap = new HashMap<>();
            List<QcQuestionDTO> qcQuestionDTOs = docket.getProducts().get(Integer.parseInt(step) - 1).getQcQuestions();
            for (QcQuestionDTO qcQuestionDTO : qcQuestionDTOs) {
                if (validateCapturedData(qcQuestionDTO, answerMap)) {
                    Toast.makeText(this, "Set value for \"" + qcQuestionDTO.getQuestion() + "\"", Toast.LENGTH_LONG).show();
                    return;
                }

            }
            if (validateImages()) {
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Update Confirmation")
                    .setMessage("Do you really want to proceed?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            update(answerMap);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        }

    }

    private void update(Map<Integer, String> answerMap) {
        for (QcQuestionDTO qcQuestionDTO : product.getQcQuestions()) {
            qcQuestionDTO.setAnswer(answerMap.get(qcQuestionDTO.getQuestionId()));
        }
        docket.getProducts().set(Integer.parseInt(step) - 1, product);
        String isQCCleared = checkIsQcCheckCleared(product);

        Intent intent = new Intent(this, QcResultActivity.class);
        intent.putExtra("Docket", docket);
        intent.putExtra("Product", product);
        intent.putExtra("Step", step);
        intent.putExtra("isQCPassed", isQCCleared);
        startActivity(intent);
        finish();
    }

    private String checkIsQcCheckCleared(Product product) {
        for (QcQuestionDTO qcQuestionDTO : product.getQcQuestions()) {
            Log.e("", new Gson().toJson(qcQuestionDTO));
            if (qcQuestionDTO.getIsMandatory() != null && qcQuestionDTO.getIsMandatory().equalsIgnoreCase("yes")) {
                if (qcQuestionDTO.getAnswer() == null || qcQuestionDTO.getAnswer().equalsIgnoreCase("no")) {
                    return "no";
                }
            }
        }

        return "yes";
    }

    private boolean validateImages() {
        boolean isAnyError = false;
        ImageView imageView1 = (ImageView) findViewById(R.id.capturedImage1);
        ImageView imageView2 = (ImageView) findViewById(R.id.capturedImage2);
        ImageView imageView3 = (ImageView) findViewById(R.id.capturedImage3);
        boolean isImage1Captured = (imageView1.getDrawable() != null);
        boolean isImage2Captured = (imageView2.getDrawable() != null);
        boolean isImage3Captured = (imageView3.getDrawable() != null);
        if (!isImage1Captured) {
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

    private boolean validateCapturedData(QcQuestionDTO qcQuestionDTO, Map<Integer, String> answerMap) {
        boolean isAnyError = false;
        String str = null;
        int resourceId = this.getResources().getIdentifier("question_" + qcQuestionDTO.getQuestionId(), "id", this.getPackageName());

        RadioGroup radioGroup = (RadioGroup) findViewById(resourceId);
        RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        String radioButtonValue = radioButton != null ? radioButton.getText().toString() : null;
        if (radioButtonValue == null || radioButtonValue.equals("")) {
            isAnyError = true;
        } else {
            radioButtonValue = radioButtonValue.toLowerCase();
        }
        answerMap.put(qcQuestionDTO.getQuestionId(), radioButtonValue);

//        if(qcQuestionDTO.getExpectedAnswer().contains("yes") || qcQuestionDTO.getExpectedAnswer().contains("no")){
//            RadioGroup radioGroup = (RadioGroup) findViewById(resourceId);
//            RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
//            String radioButtonValue = radioButton != null ? radioButton.getText().toString() : null;
//            if(radioButtonValue == null || radioButtonValue.equals("")){
//                isAnyError = true;
//            }
//            answerMap.put(qcQuestionDTO.getQuestionId(),radioButtonValue);
//        } else {
//            try {
//                Integer.parseInt(qcQuestionDTO.getExpectedAnswer());
//                str = qcQuestionDTO.getQuestion().toLowerCase();
//                if(str.contains("qty") || str.contains("quantity")){
//                    String quantity = String.valueOf(((Spinner) findViewById(resourceId)).getSelectedItem());
//                    int qunt = !quantity.equals("Select Value") ? Integer.parseInt(quantity) : 0;
//                    if(qunt == 0){
//                        isAnyError = true;
//                    }
//                    answerMap.put(qcQuestionDTO.getQuestionId(),qunt+"");
//                }
//            } catch (Exception e){
//                EditText editText = (EditText) findViewById(resourceId);
//                String editTextValue = editText != null ? editText.getText().toString() : null;
//                if(editTextValue == null || editTextValue.equals("")){
//                    isAnyError = true;
//                }
//                answerMap.put(qcQuestionDTO.getQuestionId(),editTextValue);
//            }
//        }
        return isAnyError;
    }

    public static String checkForImageLocation() {

        File direct = new File(Environment.getExternalStorageDirectory() + "/Field Pickup");
        if (!direct.exists()) {
            direct.mkdirs();
        }


        String tempDirPath = "Temp";
        File tempDir = new File(direct, tempDirPath);
        if (!tempDir.exists()) {
            try {
                tempDir.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        String path2 = "Field Pickup/Temp";

        return path2;
    }

    private void moveImageFromTempToPicked(String fileName) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try {
            File afile = new File(Environment.getExternalStorageDirectory() + "/Field Pickup/Temp/" + fileName);
            File bfile = new File(Environment.getExternalStorageDirectory() + "/Field Pickup/Picked/" + fileName);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
            afile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
