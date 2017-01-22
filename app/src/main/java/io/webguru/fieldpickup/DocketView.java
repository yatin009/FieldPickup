package io.webguru.fieldpickup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.webguru.fieldpickup.POJO.Docket;

public class DocketView extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    Docket docket;

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
        startActivity(new Intent(this, MainActivity.class));
    }
}
