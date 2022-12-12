package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.api.ApiService;
import com.example.myapplication.model.UserRequest;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    ImageView imageViewAva;
    private static final int MY_PERMISSION_REQUEST_CAMERA = 101;
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUESTCODE =10 ;
    Button buttonVerify,buttonCapture,buttonSelect;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final String TAG = MainActivity.class.getName();
    private static final int CONTENT_REQUEST=1337;
    private File output=null;
    String imageUser;

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
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                            imageViewAva.setImageBitmap(bitmap);
                            imageUser=ConvertToString.ConvertToString(decoded);
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
        setContentView(R.layout.activity_login);
        Init();
        VerifyCLick();
        SelectImageFromCategory();
        CaptureImage();

    }
    void Init(){
        imageViewAva=findViewById(R.id.imageViewAvaLogin);
        buttonVerify=findViewById(R.id.buttonVerifyLogin);
        buttonCapture=findViewById(R.id.buttonCaptureLogin);
        buttonSelect=findViewById(R.id.buttonSelectLogin);
    }
    void VerifyCLick(){
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRequest userRequest=new UserRequest(imageUser);
                ApiService.apiService.requestLoginUser(userRequest).enqueue(new Callback<UserRequest>() {
                    @Override
                    public void onResponse(Call<UserRequest> call, Response<UserRequest> response) {
                        UserRequest useResult=response.body();
                        if (useResult!=null) {
                            String[] arrayRes=useResult.getMessage().split(" ");
                            System.out.println(useResult.getMessage());
                            String  name="";
                            double rate=0;
                            try{
                                rate= Double.parseDouble(arrayRes[arrayRes.length-1]);
                            } catch(NumberFormatException ex){
                                System.out.println(ex);// handle your exception
                            }
                            if(rate < 0.35) {
                                for (int i = 0; i < arrayRes.length; i++) {
                                    if (i != 0 && i != 1 && i != (arrayRes.length - 1)) {
                                        name = name + arrayRes[i] + " ";
                                    }
                                }

                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                name = name.trim();
                                intent.putExtra("userName", name);
                                startActivity(intent);
                            }
                            else if (rate > 0.35 && rate <0.5)
                            {
                                Toast.makeText(LoginActivity.this, "The photo is not good, you need to take it in better condition", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "ERROR API!!", Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<UserRequest> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,"Login Fail",Toast.LENGTH_LONG).show();

                    }
                });

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
    void CaptureImage(){
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    captureImage();

            }
        });

    }
    void SelectImageFromCategory()
    {
        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onClickRequestPermission();
            }
        });
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);

    }


    // When results returned
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // data dau ra
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageViewAva.setImageBitmap(bp);
                imageUser=ConvertToString.ConvertToString(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}