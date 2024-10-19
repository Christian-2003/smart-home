package de.christian2003.smarthome.view.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends SmartHomeActivity<MainViewModel> {

    public MainActivity() {
        super(MainViewModel.class, R.layout.activity_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
