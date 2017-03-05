package io.webguru.fieldpickup.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.Database.DocketDataSource;
import io.webguru.fieldpickup.Database.FieldDataDataSource;
import io.webguru.fieldpickup.POJO.Docket;
import io.webguru.fieldpickup.POJO.FieldData;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 5/2/17.
 */

public class StatusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Calendar calendar;
    private TextView dateView;
    private int c_year, c_month, c_day;

    LinearLayout rescheduledDateLayout;
    LinearLayout remarksLayout;
    AppCompatButton statusProceed;
    Docket docket;
    String selectedStatus;

    boolean isProperDate = false;
    private static FieldDataDataSource fieldDataDataSource;
    private static DocketDataSource docketDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.status_activity);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            docket = (Docket) bundle.get("Docket");
        }
        this.setTitle("STATUS - " +docket.getAwbNumber());
        this.setFinishOnTouchOutside(false);
        rescheduledDateLayout = (LinearLayout) this.findViewById(R.id.reschedule_date_layout);
        rescheduledDateLayout.setVisibility(LinearLayout.GONE);
        remarksLayout = (LinearLayout) this.findViewById(R.id.remarks_layout);
        remarksLayout.setVisibility(LinearLayout.GONE);
        statusProceed = (AppCompatButton) this.findViewById(R.id.status_proceed);
        statusProceed.setVisibility(LinearLayout.GONE);

        Spinner spinner = (Spinner) findViewById(R.id.update_status);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String status = parent.getItemAtPosition(pos).toString();
        selectedStatus = status;
        if (status.equals("Tap To Select")) {
            remarksLayout.setVisibility(LinearLayout.GONE);
            rescheduledDateLayout.setVisibility(LinearLayout.GONE);
            statusProceed.setVisibility(LinearLayout.GONE);
            return;
        } else if (status.equals("Reschedule Pickup")) {
            dateView = (TextView) findViewById(R.id.reschedule_date);
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            c_year = calendar.get(Calendar.YEAR);

            c_month = calendar.get(Calendar.MONTH);
            c_day = calendar.get(Calendar.DAY_OF_MONTH);
            showDate(c_year, c_month + 1, c_day, "init");

            rescheduledDateLayout.setVisibility(LinearLayout.VISIBLE);
            remarksLayout.setVisibility(LinearLayout.VISIBLE);
        } else if (status.equals("Ready To Pick")) {
            remarksLayout.setVisibility(LinearLayout.GONE);
            rescheduledDateLayout.setVisibility(LinearLayout.GONE);
        } else {
            dateView = (TextView) findViewById(R.id.reschedule_date);
            rescheduledDateLayout.setVisibility(LinearLayout.GONE);
            remarksLayout.setVisibility(LinearLayout.VISIBLE);
        }
        statusProceed.setVisibility(LinearLayout.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @SuppressWarnings("deprecation")
    @OnClick(R.id.reschedule_date)
    public void setDate(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        DatePickerDialog datePickerDialog = null;
        if (id == 999) {
            datePickerDialog = new DatePickerDialog(this,
                    myDateListener, c_year, c_month, c_day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+(24*60*60*1000));
        }
        return datePickerDialog;
    }

    @OnClick(R.id.status_proceed)
    public void updateStatus() {
        FieldData fieldData;
        EditText remarks;
        TextView date;
        if (selectedStatus != null && selectedStatus.equals("Ready To Pick")) {
            Intent intent = new Intent(this, DocketDetailsActivity.class);
            intent.putExtra("Docket", docket);
            startActivity(intent);
            finish();
        } else if (selectedStatus != null && !selectedStatus.equals("Tap To Select")) {
            fieldData = new FieldData();
            fieldData.setDocketId(docket.getId());
            remarks = (EditText) findViewById(R.id.fail_remarks);
            if (remarks == null || remarks.getText().toString().equals("")) {
                Toast.makeText(this, "Enter Remarks", Toast.LENGTH_LONG).show();
                return;
            } else {
                fieldData.setAgentRemarks(remarks.getText().toString());
            }
            if (selectedStatus.equals("Reschedule Pickup")) {
                date = (TextView) findViewById(R.id.reschedule_date);
                fieldData.setStatus("Rescheduled to " + date.getText().toString());
            } else if (selectedStatus.equals("Cancelled")) {
                fieldData.setStatus("Pickup Cancelled");
                fieldData.setRescheduleDate("NA");
            } else {
                fieldData.setRescheduleDate("NA");
                String status = selectedStatus.equals("Customer Not Available") ? "Customer Not Available" : "NA";
                fieldData.setStatus(status);
            }
            fieldData.setIsAllPartsAvailable("NA");
            fieldData.setIsIssueCategoryCorrect("NA");
            fieldData.setIsProductClean("NA");
            fieldData.setIsSameProduct("NA");
            fieldData.setQuantity(0);
            try {
                fieldDataDataSource = new FieldDataDataSource(this);
                fieldDataDataSource.open();
                fieldDataDataSource.insertFieldData(fieldData);
                docketDataSource = new DocketDataSource(this);
                docketDataSource.open();
                docketDataSource.markDocketAsDone(docket.getId());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (docketDataSource != null) {
                    docketDataSource.close();
                }
                if (docketDataSource != null) {
                    fieldDataDataSource.close();
                }
            }
            super.onResume();
            finish();
        }
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate(arg1, arg2 + 1, arg3, "OnClick");
                }
            };

    private void showDate(int year, int month, int day, String event) {
        if (event.equals("OnClick") && (year < c_year || (year == c_year && month < c_month + 1) ||
                (year == c_year && month == c_month + 1 && day < c_day))) {
            Toast.makeText(this, "Invalid Date. Date should be greater than today.", Toast.LENGTH_LONG).show();
            dateView.setError("Invalid Date. Date should be greater than today.");
            dateView.requestFocus();
            isProperDate = false;
        } else {
            isProperDate = true;
            dateView.setError(null);
        }
        dateView.setText(new StringBuilder().append(year).append("-")
                .append(month < 10 ? "0" + month : month).append("-").append(day < 10 ? "0" + day : day));
    }
}
