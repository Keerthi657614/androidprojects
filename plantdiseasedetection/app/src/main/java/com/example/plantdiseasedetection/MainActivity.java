package com.example.plantdiseasedetection;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private TextView resultText;
    private Bitmap capturedImage;

    private static final String API_URL = "https://plantdiseasedetection-xkov.onrender.com/predict"; // Your API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        resultText = findViewById(R.id.resultText);
        Button captureButton = findViewById(R.id.captureButton);
        Button predictButton = findViewById(R.id.predictButton);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);

        captureButton.setOnClickListener(view -> openCamera());

        predictButton.setOnClickListener(view -> {
            if (capturedImage != null) {
                sendImageToAPI(capturedImage);
            } else {
                resultText.setText("Please capture an image first!");
            }
        });
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            capturedImage = (Bitmap) extras.get("data");
            imageView.setImageBitmap(capturedImage);
        }
    }

    private void sendImageToAPI(Bitmap bitmap) {
        OkHttpClient client = new OkHttpClient();

        // Convert bitmap to Base64
        String base64Image = encodeImageToBase64(bitmap);

        JSONObject json = new JSONObject();
        try {
            json.put("image", base64Image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json.toString());

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> resultText.setText("API Request Failed!"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String prediction = jsonResponse.getString("prediction");
                        resultText.setText("Prediction: " + prediction);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        resultText.setText("Invalid Response!");
                    }
                });
            }
        });
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
