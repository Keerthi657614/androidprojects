package com.example.mad4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initializing Buttons
        Button visitUrl = findViewById(R.id.button);
        Button dialPad = findViewById(R.id.button2);
        Button whatsapp = findViewById(R.id.button3);
        Button sendMail = findViewById(R.id.button4);
        Button setAlarm = findViewById(R.id.button5);

        // Initializing EditTexts
        EditText urlInput = findViewById(R.id.editTextText);
        EditText phoneInput = findViewById(R.id.editTextText2);
        EditText whatsappInput = findViewById(R.id.editTextText3);
        EditText emailInput = findViewById(R.id.editTextText4);
        EditText timeInput = findViewById(R.id.editTextText6);

        // Open URL
        visitUrl.setOnClickListener(v -> {
            String url = urlInput.getText().toString();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url; // Ensures valid URL
            }
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        });

        // Open Dial Pad
        dialPad.setOnClickListener(v -> {
            String phoneNumber = phoneInput.getText().toString();
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(dialIntent);
        });

        // Open WhatsApp
        whatsapp.setOnClickListener(v -> {
            String number = whatsappInput.getText().toString();
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
            whatsappIntent.setData(Uri.parse("https://wa.me/" + number)); // WhatsApp API
            startActivity(whatsappIntent);
        });

        // Send Email
        sendMail.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Email body here");
            try {
                startActivity(Intent.createChooser(emailIntent, "Choose an email client"));
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        // Set Alarm
        setAlarm.setOnClickListener(v -> {
            String time = timeInput.getText().toString();
            try {
                int hour = Integer.parseInt(time.split(":")[0]);
                int minute = Integer.parseInt(time.split(":")[1]);

                Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
                alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm Set via App");

                if (alarmIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(alarmIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
