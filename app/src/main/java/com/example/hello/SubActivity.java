package com.example.hello;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        MainActivity.UserInfo userInfo = getIntent().getExtras().getParcelable("userInfo");

        ImageView iv = findViewById(R.id.iv);
        byte[] arr = userInfo.getUserProfile();
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        iv.setImageBitmap(image);
    }
}
