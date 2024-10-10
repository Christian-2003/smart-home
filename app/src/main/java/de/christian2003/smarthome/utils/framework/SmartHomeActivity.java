package de.christian2003.smarthome.utils.framework;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


/**
 * Class implements a base activity for all activities of the app. The class handles tasks that are
 * required by all the activities within the app, such as view model management.
 *
 * @param <V>   Type of the view model to create for the activity.
 */
public class SmartHomeActivity<V extends ViewModel> extends AppCompatActivity {

    /**
     * Attribute stores the view model. This attribute is set once the {@link #onCreate(Bundle)}-method
     * is called and the view model type passed with the constructor is not {@code null}.
     */
    protected V viewModel;

    /**
     * Attribute stores the type for the view model.
     */
    @Nullable
    private final Class<V> viewModelType;

    /**
     * Attribute stores the layout resource ID for the activity.
     */
    @LayoutRes
    private final int layoutRes;


    /**
     * Constructor instantiates a new smart home activity for the passed layout resource. The activity
     * will create a view model of the specified view model type once {@link #onCreate(Bundle)} is
     * called.
     *
     * @param viewModelType Type for the view model for the activity.
     * @param layoutRes     Layout resource ID for the activity.
     */
    public SmartHomeActivity(@Nullable Class<V> viewModelType, @LayoutRes int layoutRes) {
        this.viewModelType = viewModelType;
        this.layoutRes = layoutRes;
    }


    /**
     * Method is called whenever the activity is created.
     *
     * @param savedInstanceState    Previously saved state of the instance.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes);
        if (viewModelType != null) {
            viewModel = new ViewModelProvider(this).get(viewModelType);
        }
    }

}
