package io.webguru.fieldpickup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 2/3/17.
 */

public class EditValueActivity extends AppCompatActivity {

    LinearLayout radioLayout;
    LinearLayout numberLayout;
    LinearLayout stringLayout;

    TextView questionTextView;
    TextView answerTextView;
    RadioGroup answerRadioGroup;
    Spinner answerNumberView;

    RadioButton isDirtyRadioButton;

    String question;
    String answer;
    String type;
    Integer actualQuantity;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_value);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        radioLayout = (LinearLayout) findViewById(R.id.radioTypeField);
        numberLayout = (LinearLayout) findViewById(R.id.numberTypeField);
        stringLayout = (LinearLayout) findViewById(R.id.stringTypeField);
        if(bundle != null){
            question = (String) bundle.get("Question");
            answer = (String) bundle.get("Answer");
            type = (String) bundle.get("Type");
            actualQuantity = (Integer) bundle.get("ActualQuantity");



            if(type.equals("RADIO")){
                questionTextView = (TextView) findViewById(R.id.selectedRadioTypeQuestion);
                questionTextView.setText(question);
                answerRadioGroup = (RadioGroup) findViewById(R.id.selectedRadioAnswer);
                if(answer.equals("YES")) {
                    answerRadioGroup.check(R.id.selectedYes);
                } else {
                    answerRadioGroup.check(R.id.selectedNo);
                }
                stringLayout.setVisibility(View.GONE);
                numberLayout.setVisibility(View.GONE);
                radioLayout.setVisibility(View.VISIBLE);
            } else if(type.equals("NUMBER")){
                questionTextView = (TextView) findViewById(R.id.selectedNumberTypeQuestion);
                questionTextView.setText(question);
                answerNumberView = (Spinner) findViewById(R.id.selectedNumberAnswer);
                List<String> quantityList = new ArrayList<>();
                quantityList.add("Select Quantity");
                for(int i=0; i<actualQuantity.intValue(); i++){
                    quantityList.add((i+1)+"");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, quantityList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                int spinnerPosition = adapter.getPosition(answer);
                spinner = (Spinner) findViewById(R.id.selectedNumberAnswer);
                spinner.setAdapter(adapter);
                spinner.setSelection(spinnerPosition);

                stringLayout.setVisibility(View.GONE);
                numberLayout.setVisibility(View.VISIBLE);
                radioLayout.setVisibility(View.GONE);
            } else if(type.equals("STRING")){
                questionTextView = (TextView) findViewById(R.id.selectedStringTypeQuestion);
                questionTextView.setText(question);
                answerTextView = (TextView) findViewById(R.id.selectedStringAnswer);
                answerTextView.setText(answer);
                stringLayout.setVisibility(View.VISIBLE);
                numberLayout.setVisibility(View.GONE);
                radioLayout.setVisibility(View.GONE);
            }


        }

    }

    @OnClick(R.id.update_field)
    public void update() {


        String value = "";
        if(type.equals("RADIO")){
            answerRadioGroup = (RadioGroup) findViewById(R.id.selectedRadioAnswer);
            isDirtyRadioButton = (RadioButton) findViewById(answerRadioGroup.getCheckedRadioButtonId());
            if(isDirtyRadioButton == null || isDirtyRadioButton.getText().toString().equals("")){
                return;
            }
            value = isDirtyRadioButton.getText().toString();
        } else if(type.equals("NUMBER")){
            String quantity = String.valueOf(((Spinner) findViewById(R.id.selectedNumberAnswer)).getSelectedItem());
            if(quantity.equals("") || quantity.equals("Select Quantity")){
                return;
            }
            value = quantity;
        } else if(type.equals("STRING")){
            answerTextView = (EditText) findViewById(R.id.selectedStringAnswer);
            if(answerTextView.getText() == null || answerTextView.getText().toString().equals("")){
                return;
            }
            value = answerTextView.getText().toString();
        }

        Intent previousScreen = new Intent(getApplicationContext(), ReviewActivity.class);
        previousScreen.putExtra("UpdatedValue", value);
        setResult(1221, previousScreen);
        finish();
    }
}
