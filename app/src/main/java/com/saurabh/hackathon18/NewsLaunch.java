package com.saurabh.hackathon18;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class NewsLaunch extends AppCompatActivity {

    EditText Disc, Link, Title;
    Button next;
    ImageView Image;
    Bitmap photo;
    ProgressDialog progressDialog;
    ShareHolder shareHolder;
    boolean isImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_launch);
        next = findViewById(R.id.TextNext);
        Disc = findViewById(R.id.NewsText);
        Link = findViewById(R.id.TextLink);
        Title = findViewById(R.id.TextTitle);
        Image = findViewById(R.id.NewsImage);
        shareHolder = new ShareHolder(NewsLaunch.this);
        progressDialog = new ProgressDialog(NewsLaunch.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewsLaunch.this);
                View choseView = getLayoutInflater().inflate(R.layout.chose_image, null);
                builder.setView(choseView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                LinearLayout Camera = choseView.findViewById(R.id.CameraChose);
                LinearLayout Gal = choseView.findViewById(R.id.GalleryChose);

                Camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ActivityCompat.checkSelfPermission(NewsLaunch.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 102);
                        } else {
                            ActivityCompat.requestPermissions(NewsLaunch.this, new String[]{Manifest.permission.CAMERA}, 1002);
                        }
                    }
                });

                Gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);

                    }
                });

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                StringRequest PostData = new StringRequest(Request.Method.POST, "https://crummy-stuff.000webhostapp.com/Fake/NewsLaunch.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map map = new HashMap();
                        int randomNum = ThreadLocalRandom.current().nextInt(0, 1000000001);

                        if (!shareHolder.getName().equals("")){
                            map.put("name", shareHolder.getName());
                        }else{
                            map.put("name" , "Anonymous");
                        }
                        map.put("local", shareHolder.getId());
                        map.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()));
                        map.put("title", Title.getText().toString());
                        map.put("link", Link.getText().toString());
                        map.put("text", Disc.getText().toString());
                        if (isImage){
                            map.put("image", "Image/" + randomNum);
                            map.put("image_data", ImageToString());
                        }else{
                            map.put("image" , "null");
                            map.put("image_data" , "");
                        }
                        return map;
                    }
                };

                MySending.getInstance(NewsLaunch.this).addToRequestQueue(PostData);

            }
        });
    }

    private String ImageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo = ((BitmapDrawable) Image.getDrawable()).getBitmap();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] array = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(array, Base64.DEFAULT);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    photo = (Bitmap) extras.get("data");
                    Image.setImageBitmap(photo);
                    Image.setAdjustViewBounds(true);
                    isImage = true;
                }
                break;
            case 101:
                if (resultCode == RESULT_OK){
                    Image.setAdjustViewBounds(true);
                    Uri imageUri = data.getData();
                    Image.setImageURI(imageUri);
                    isImage = true;

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1002) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 102);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
