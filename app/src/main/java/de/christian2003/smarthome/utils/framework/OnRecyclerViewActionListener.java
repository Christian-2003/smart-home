package de.christian2003.smarthome.utils.framework;


/**
 * Interface provides a callback for a recycler view adapter.
 */
public interface OnRecyclerViewActionListener {

    /**
     * Method is called whenever the action is invoked.
     *
     * @param position  Adapter position at which the action is invoked.
     */
    void invoke(int position);

}
