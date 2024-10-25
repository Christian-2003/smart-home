package de.christian2003.smarthome.view.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.model.data.ShRoom;
import de.christian2003.smarthome.utils.framework.OnRecyclerViewActionListener;
import de.christian2003.smarthome.utils.framework.SmartHomeRecyclerViewAdapter;


/**
 * Class implements the recycler view adapter for the {@link HomeFragment}.
 */
public class HomeRecyclerViewAdapter extends SmartHomeRecyclerViewAdapter<HomeViewModel> {

    /**
     * Class implements the view holder for the room item.
     */
    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        /**
         * Attribute stores the text view to display the name of the room.
         */
        public TextView nameTextView;


        /**
         * Constructor instantiates a new view holder for the specified view.
         *
         * @param itemView  Inflated view for which to create the view holder.
         */
        public RoomViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
        }

    }


    /**
     * Attribute stores the action listener to invoke when a room is clicked.
     */
    @Nullable
    public OnRecyclerViewActionListener roomClickedListener;


    /**
     * Constructor instantiates a new recycler view adapter.
     *
     * @param context   Context for the adapter.
     * @param viewModel View model from which to source the data.
     */
    public HomeRecyclerViewAdapter(@NonNull Context context, @NonNull HomeViewModel viewModel) {
        super(context, viewModel);
        roomClickedListener = null;
    }


    /**
     * Method changes the action listener to invoke whenever a room is clicked.
     *
     * @param roomClickedListener   New action listener to invoke.
     */
    public void setRoomClickedListener(@Nullable OnRecyclerViewActionListener roomClickedListener) {
        this.roomClickedListener = roomClickedListener;
    }


    /**
     * Method is called whenever the contents of a view holder shall be updated.
     *
     * @param holder    The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RoomViewHolder) {
            RoomViewHolder viewHolder = (RoomViewHolder)holder;
            ShRoom room = viewModel.getRooms().get(position);
            viewHolder.nameTextView.setText(room.getName());
            viewHolder.itemView.setOnClickListener(view -> {
                if (roomClickedListener != null) {
                    roomClickedListener.invoke(holder.getAdapterPosition());
                }
            });
        }
    }


    /**
     * Method is called whenever a new view holder shall be created.
     *
     * @param parent    The ViewGroup into which the new View will be added after it is bound to
     *                  an adapter position.
     * @param viewType  The view type of the new View.
     * @return          Created view holder.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_room, parent, false);
        RoomViewHolder holder = new RoomViewHolder(itemView);
        return holder;
    }


    /**
     * Method returns the number of items for the recycler view.
     *
     * @return   Number of items for the adapter.
     */
    @Override
    public int getItemCount() {
        return viewModel.getRooms().size();
    }

}
