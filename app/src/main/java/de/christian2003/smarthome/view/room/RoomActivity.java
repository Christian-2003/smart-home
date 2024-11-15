package de.christian2003.smarthome.view.room;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import java.io.Serializable;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.model.data.ShRoom;
import de.christian2003.smarthome.utils.framework.SmartHomeActivity;


public class RoomActivity extends SmartHomeActivity<RoomViewModel> {

    public static final String EXTRA_ROOM = "extra_room";


    private RoomRecyclerViewAdapter adapter;


    public RoomActivity() {
        super(RoomViewModel.class, R.layout.activity_room);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(EXTRA_ROOM)) {
            Serializable room = getIntent().getSerializableExtra(EXTRA_ROOM);
            if (room instanceof ShRoom) {
                viewModel.setRoom((ShRoom)room);
            }
        }
        if (viewModel.getRoom() == null) {
            finish();
            return;
        }

        MaterialToolbar appBar = findViewById(R.id.app_bar);
        appBar.setNavigationOnClickListener(view -> finish());
        appBar.setTitle(viewModel.getRoom().getName());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RoomRecyclerViewAdapter(this, viewModel);
        recyclerView.setAdapter(adapter);
    }
}
