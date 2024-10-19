package de.christian2003.smarthome.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.utils.framework.SmartHomeFragment;
import de.christian2003.smarthome.view.url.UrlActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Class implements the home fragment for the app.
 */
public class HomeFragment extends SmartHomeFragment<HomeViewModel> {

    /**
     * Constructor instantiates a new fragment.
     */
    public HomeFragment() {
        super(HomeViewModel.class, R.layout.fragment_home);
    }


    /**
     * Method is called whenever the fragment view is created.
     *
     * @param inflater              Layout inflater to use to inflate the fragment layout.
     * @param container             Parent for the fragment view.
     * @param savedInstanceState    Previously saved state of the instance.
     * @return                      Created fragment view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view == null) {
            return null;
        }

        view.findViewById(R.id.button_url).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UrlActivity.class);
            intent.putExtra(UrlActivity.EXTRA_CLOSEABLE, true);
            startActivity(intent);
        });

        return view;
    }


    /**
     * TODO: Remove this.
     * @param view
     */
    public void GetData(View view) {
        // Check that button on click works.
        TextView tvHeadline = fragmentView.findViewById(R.id.tvHeadline);
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

                    requireActivity().runOnUiThread(() -> {
                        TextView tvData = fragmentView.findViewById(R.id.tvData);
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
