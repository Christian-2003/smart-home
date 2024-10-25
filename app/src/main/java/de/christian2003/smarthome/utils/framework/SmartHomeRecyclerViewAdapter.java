package de.christian2003.smarthome.utils.framework;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Class implements a generic recycler view adapter for the smart home app that handles basic
 * tasks that are required by all recycler view adapters, such as view model management.
 *
 * @param <V>   Type of the view model from which the adapter sources it's data.
 */
public abstract class SmartHomeRecyclerViewAdapter<V extends ViewModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Attribute stores the context of the recycler view adapter.
     */
    @NonNull
    protected final Context context;

    /**
     * Attribute stores the view model from which to source the data.
     */
    @NonNull
    protected final V viewModel;

    /**
     * Attribute stores the layout inflater used to inflate the child views.
     */
    @NonNull
    protected final LayoutInflater layoutInflater;

    /**
     * Attribute stores the recycler view to which this adapter is attached to.
     */
    protected RecyclerView recyclerView;


    /**
     * Constructor instantiates a new recycler view adapter.
     *
     * @param context   Context for the adapter.
     * @param viewModel View model for the adapter.
     */
    public SmartHomeRecyclerViewAdapter(@NonNull Context context, @NonNull V viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        layoutInflater = LayoutInflater.from(context);
    }


    /**
     * Method is called whenever the adapter is attached to a recycler view.
     * @param recyclerView  The RecyclerView instance which started observing this adapter.
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

}
