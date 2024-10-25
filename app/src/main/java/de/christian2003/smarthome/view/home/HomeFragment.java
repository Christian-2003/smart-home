package de.christian2003.smarthome.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.model.data.ShRoom;
import de.christian2003.smarthome.utils.framework.SmartHomeFragment;
import de.christian2003.smarthome.view.room.RoomActivity;


/**
 * Class implements the home fragment for the app.
 */
public class HomeFragment extends SmartHomeFragment<HomeViewModel> {

    /**
     * Attribute stores the recycler view adapter for the fragment.
     */
    private HomeRecyclerViewAdapter adapter;

    /**
     * Attribute stores the progress bar shown while loading the webpage content.
     */
    private ProgressBar progressBar;

    /**
     * Attribute stores the recycler view of the fragment.
     */
    private RecyclerView recyclerView;


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

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        viewModel.initIfRequired(this::dataLoaded, true);

        return view;
    }


    /**
     * Callback method is invoked after the webpage content is loaded.
     */
    private void dataLoaded() {
        requireActivity().runOnUiThread(() -> {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            adapter = new HomeRecyclerViewAdapter(requireActivity(), viewModel);
            adapter.setRoomClickedListener(this::onRoomClicked);
            recyclerView.setAdapter(adapter);
        });
    }


    /**
     * Method is called whenever a room is clicked.
     *
     * @param position  Position of the room within the recycler view adapter.
     */
    private void onRoomClicked(int position) {
        ShRoom room = viewModel.getRooms().get(position);
        Intent intent = new Intent(getActivity(), RoomActivity.class);
        intent.putExtra(RoomActivity.EXTRA_ROOM, room);
        startActivity(intent);
    }

}
