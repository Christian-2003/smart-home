package de.christian2003.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GetData(View view) {
        // Check that button on click works.
        TextView tvHeadline = findViewById(R.id.tvHeadline);
        tvHeadline.setText("Button clicked");

        // Get data form the website.
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.google.com")
                    .build();

            try{
                Response response = client.newCall(request).execute();

                if (response.isSuccessful() && response.body() != null) {
                    String htmlContent = response.body().string();
                    response.close();

                    runOnUiThread(() -> {
                        TextView tvData = findViewById(R.id.tvData);
                        tvData.setText(htmlContent);
                        System.out.println("HTML Content: " + htmlContent);
                            });
                }
                else {
                    System.out.println("Error body might be null");
                }
            }
            catch (Exception exception) {
                System.out.println("Exception: " + exception.getMessage());
            }
        }).start();
    }
}
