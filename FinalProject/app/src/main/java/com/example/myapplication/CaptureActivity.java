package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CaptureActivity extends AppCompatActivity {


    Button buttonCapture,buttonRegis;
    ImageView imageAva;
    EditText enterName;
    Bitmap imageBitmap;

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_VIDEO_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        Init();
        CaptureImage();
        pushImageToFirebase();
    }
    void Init(){
        buttonCapture=findViewById(R.id.buttonCap);
        imageAva=findViewById(R.id.imageViewAva);
       enterName=findViewById(R.id.editTextName);
        buttonRegis=findViewById(R.id.buttonRegis);

    }
    void pushImageToFirebase()
    {
        buttonRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushDataToFireBase();
                pushNameToFireBase();
            }
        });
    }
    void CaptureImage(){
        this.buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
    }


    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }
    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // data dau ra
                imageBitmap = (Bitmap) data.getExtras().get("data");
                this.imageAva.setImageBitmap(imageBitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
    void pushDataToFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef2 = database.getReference("user/"+enterName.getText().toString()+"/Name");
        myRef2.setValue(enterName.getText().toString());

    }
    void pushNameToFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user/"+enterName.getText().toString()+"/Image");
        myRef.setValue(ConvertToString.ConvertToString(imageBitmap).toString());
    }

}

