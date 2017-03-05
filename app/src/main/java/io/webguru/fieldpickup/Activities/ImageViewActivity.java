package io.webguru.fieldpickup.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

import butterknife.ButterKnife;
import io.webguru.fieldpickup.R;

/**
 * Created by mahto on 5/3/17.
 */

public class ImageViewActivity extends AppCompatActivity {

    private String imageName;
    private String awbNumber;
    private String imageNumber;
    private String source;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            imageName = (String) bundle.get("imageName");
            awbNumber = (String) bundle.get("awbNumber");
            imageNumber = (String) bundle.get("imageNumber");
            source = (String) bundle.get("source");
            String path;
            if(source.equals("DETAILS")){
                path = "Field Pickup/Temp/" + imageName;
            } else {
                path = "Field Pickup/Temp/" + imageName;
            }
            File f = Environment.getExternalStoragePublicDirectory(path);
            imageView = (ImageView) findViewById(R.id.image_view);
            Uri uri = Uri.parse(f.getAbsolutePath());
            imageView.setImageURI(uri);
            this.setTitle(awbNumber + " - Image " + imageNumber );
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


        }

    }

}
