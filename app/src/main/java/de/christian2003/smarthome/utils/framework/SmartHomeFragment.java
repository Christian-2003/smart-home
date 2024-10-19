package de.christian2003.smarthome.utils.framework;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


/**
 * Class implements a base fragment for all fragments of the app. The class handles tasks that are
 * required by all the fragments within the app, such as view model management.
 *
 * @param <V>   Type of the view model to create for the fragment.
 */
public class SmartHomeFragment<V extends ViewModel> extends Fragment {

    /**
     * Attribute stores the view model of the fragment.
     */
    protected V viewModel;

    /**
     * Attribute stores the view of the fragment. This is {@code nul} before calling
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     */
    protected View fragmentView;

    /**
     * Attribute stores the view model class info required to get a view model.
     */
    @Nullable
    private final Class<V> viewModelType;

    /**
     * Attribute stores the resource id of the layout resource for the fragment.
     */
    @LayoutRes
    private final int layoutRes;


    /**
     * Constructor instantiates a new fragment.
     *
     * @param viewModelType Type info of the view model. Pass {@code null} if no view model is
     *                      required.
     * @param layoutRes     Layout resource for the fragment.
     */
    public SmartHomeFragment(@Nullable Class<V> viewModelType, @LayoutRes int layoutRes) {
        this.viewModelType = viewModelType;
        this.layoutRes = layoutRes;
    }


    /**
     * Method is called whenever the fragment is created.
     *
     * @param savedInstanceState    Previously saved state of the instance.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (viewModelType != null) {
            viewModel = new ViewModelProvider(requireActivity()).get(viewModelType);
        }
    }

    /**
     * Method is called whenever a new view needs to be created for the fragment. After calling this
     * method, the inflated view is available to subclasses through {@link #fragmentView}.
     *
     * @param inflater              Layout inflater to use to inflate the fragment layout.
     * @param container             Parent for the fragment view.
     * @param savedInstanceState    Previously saved state of the instance.
     * @return                      Inflated view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(layoutRes, container, false);
        return fragmentView;
    }

}
