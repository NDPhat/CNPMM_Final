package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.UserRequest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptureActivity extends AppCompatActivity {


    Button buttonCapture,buttonRecognition,buttonSelect;
    ImageView imageAva;
    EditText textEnterName;
    Bitmap imageBitmap;
    String stringImage;
    private static final int REQUESTCODE =10 ;
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_VIDEO_CAPTURE = 101;
    private ActivityResultLauncher<Intent> mActivivityResultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG,"onActivityResult*");
                    if (result.getResultCode()== Activity.RESULT_OK)
                    {
                        Intent data=result.getData();
                        if (data==null)
                        {
                            return;
                        }
                        Uri uri=data.getData();
                        try {
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imageAva.setImageBitmap(bitmap);
                            stringImage=ConvertToString.ConvertToString(bitmap);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        Init();
        CaptureImage();
        recognitionCLick();
        selectRegister();
    }
    void Init(){
        buttonCapture=findViewById(R.id.buttonCap);
        imageAva=findViewById(R.id.imageViewAva);
        textEnterName=findViewById(R.id.editTextName);
        buttonRecognition=findViewById(R.id.buttonrecognition);
        buttonSelect=findViewById(R.id.buttonSelectRegis);

    }
    void selectRegister(){
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });
    }
    void recognitionCLick(){
        buttonRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRequest userRequest=new UserRequest(stringImage,textEnterName.getText().toString());
                ApiService.apiService.requestRegisterUser(userRequest).enqueue(new Callback<UserRequest>() {
                    @Override
                    public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                        UserRequest useResult=response.body();
                        if(useResult!=null) {
                            Toast.makeText(CaptureActivity.this, "Register Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(CaptureActivity.this, HomeActivity.class);
                            intent.putExtra("userName", textEnterName.getText().toString());
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(CaptureActivity.this, "ERROR API!!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRequest> call, Throwable t) {
                        Toast.makeText(CaptureActivity.this,"Register Fail",Toast.LENGTH_LONG).show();

                    }
                });


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
    private void OpenGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        mActivivityResultLauncher.launch(Intent.createChooser(intent,"Choose Picture"));
    }
    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M)
        {
            OpenGallery();
            return;
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            OpenGallery();
        }
        else {
            String[]permission={Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,REQUESTCODE);
        }
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
                stringImage=ConvertToString.ConvertToString(imageBitmap);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }



}

