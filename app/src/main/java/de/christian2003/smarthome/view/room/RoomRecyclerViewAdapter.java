package de.christian2003.smarthome.view.room;

import android.content.Context;
import android.icu.text.IDNA;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.christian2003.smarthome.R;
import de.christian2003.smarthome.model.data.ShInfoText;
import de.christian2003.smarthome.model.data.devices.ShGenericDevice;
import de.christian2003.smarthome.model.data.devices.ShLight;
import de.christian2003.smarthome.model.data.devices.ShOpening;
import de.christian2003.smarthome.model.data.devices.ShOutlet;
import de.christian2003.smarthome.model.data.devices.ShShutter;
import de.christian2003.smarthome.model.user_information.UserInformation;
import de.christian2003.smarthome.utils.framework.SmartHomeRecyclerViewAdapter;

public class RoomRecyclerViewAdapter extends SmartHomeRecyclerViewAdapter<RoomViewModel> {

    public static class LabelViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTextView;

        public final TextView contentTextView;


        public LabelViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
            contentTextView = itemView.findViewById(R.id.text_content);
        }

    }

    public static class GenericDeviceViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameTextView;


        public GenericDeviceViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_name);
        }

    }

    public static class DeviceLightViewHolder extends GenericDeviceViewHolder {

        public final Button onButton;

        public final Button offButton;


        public DeviceLightViewHolder(View itemView) {
            super(itemView);
            onButton = itemView.findViewById(R.id.button_on);
            offButton = itemView.findViewById(R.id.button_off);
        }

    }

    public static class DeviceOpeningViewHolder extends GenericDeviceViewHolder {

        public DeviceOpeningViewHolder(View itemView) {
            super(itemView);
        }

    }

    public static class DeviceOutletViewHolder extends GenericDeviceViewHolder {

        public final TextView amperageTextView;

        public final TextView timeTextView;

        public final TextView powerTextView;

        public final Button onButton;

        public final Button offButton;


        public DeviceOutletViewHolder(View itemView) {
            super(itemView);
            amperageTextView = itemView.findViewById(R.id.text_amperage);
            timeTextView = itemView.findViewById(R.id.text_time);
            powerTextView = itemView.findViewById(R.id.text_power);
            onButton = itemView.findViewById(R.id.button_on);
            offButton = itemView.findViewById(R.id.button_off);
        }

    }

    public static class DeviceShutterViewHolder extends GenericDeviceViewHolder {

        public final TextView percentageTextView;

        public final TextView timeTextView;

        public final Button setButton;


        public DeviceShutterViewHolder(View itemView) {
            super(itemView);
            percentageTextView = itemView.findViewById(R.id.text_percentage);
            timeTextView = itemView.findViewById(R.id.text_time);
            setButton = itemView.findViewById(R.id.button_set);
        }

    }

    public static class InfoViewHolder extends RecyclerView.ViewHolder {

        public final TextView titleTextView;

        public final TextView messageTextView;

        public final ImageView stateImageView;


        public InfoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_title);
            messageTextView = itemView.findViewById(R.id.text_message);
            stateImageView = itemView.findViewById(R.id.image_state);
        }

    }

    public static class WarningViewHolder extends InfoViewHolder {

        public WarningViewHolder(View itemView) {
            super(itemView);
        }

    }

    public static class ErrorViewHolder extends InfoViewHolder {

        public ErrorViewHolder(View itemView) {
            super(itemView);
        }

    }


    private static final int TYPE_LABEL = 1;

    private static final int TYPE_LIGHT = 3;

    private static final int TYPE_OPENING = 5;

    private static final int TYPE_OUTLET = 7;

    private static final int TYPE_SHUTTER = 9;

    private static final int TYPE_WARNING = 11;

    private static final int TYPE_ERROR = 13;


    public RoomRecyclerViewAdapter(@NonNull Context context, @NonNull RoomViewModel viewModel) {
        super(context, viewModel);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (viewModel.getRoom() == null) {
            return;
        }
        int errorCount = viewModel.getErrors().size();
        int warningCount = viewModel.getWarnings().size();
        int labelCount = viewModel.getRoom().getInfos().size();
        if (holder instanceof LabelViewHolder) {
            //Label:
            LabelViewHolder viewHolder = (LabelViewHolder)holder;
            ShInfoText infoText = viewModel.getRoom().getInfos().get(position - errorCount - warningCount);
            if (infoText.getSpecifier() != null) {
                viewHolder.nameTextView.setText(context.getString(R.string.room_label_specifier).replace("{label}", infoText.getLabel()).replace("{specifier}", infoText.getSpecifier()));
            }
            else {
                viewHolder.nameTextView.setText(context.getString(R.string.room_label).replace("{label}", infoText.getLabel()));
            }
            viewHolder.contentTextView.setText(infoText.getText());
        }
        else if (holder instanceof GenericDeviceViewHolder) {
            //Device:
            GenericDeviceViewHolder vh = (GenericDeviceViewHolder)holder;
            ShGenericDevice genericDevice = viewModel.getRoom().getDevices().get(position - errorCount - warningCount - labelCount);
            if (genericDevice.getSpecifier() != null) {
                vh.nameTextView.setText(context.getString(R.string.room_label_specifier).replace("{label}", genericDevice.getName()).replace("{specifier}", genericDevice.getSpecifier()));
            }
            else {
                vh.nameTextView.setText(context.getString(R.string.room_label).replace("{label}", genericDevice.getName()));
            }

            if (holder instanceof DeviceLightViewHolder) {
                DeviceLightViewHolder viewHolder = (DeviceLightViewHolder)holder;
                ShLight device = (ShLight)genericDevice;
                viewHolder.onButton.setText(device.getOnButtonText());
                viewHolder.offButton.setText(device.getOffButtonText());
            }
            else if (holder instanceof DeviceOutletViewHolder) {
                DeviceOutletViewHolder viewHolder = (DeviceOutletViewHolder)holder;
                ShOutlet device = (ShOutlet)genericDevice;
                viewHolder.amperageTextView.setText(device.getAmperage());
                viewHolder.timeTextView.setText(device.getTime());
                viewHolder.powerTextView.setText(device.getPowerConsumption());
                viewHolder.onButton.setText(device.getOnButtonText());
                viewHolder.offButton.setText(device.getOffButtonText());
            }
            else if (holder instanceof DeviceShutterViewHolder) {
                DeviceShutterViewHolder viewHolder = (DeviceShutterViewHolder)holder;
                ShShutter device = (ShShutter)genericDevice;
                viewHolder.percentageTextView.setText(device.getPercentage());
                viewHolder.timeTextView.setText(device.getTime());
                viewHolder.setButton.setText(device.getSetButtonText());
            }
        }
        if (holder instanceof InfoViewHolder) {
            //Info
            InfoViewHolder viewHolder = (InfoViewHolder)holder;
            UserInformation info;
            if (position < errorCount) {
                info = viewModel.getErrors().get(position);
            }
            else if (position < errorCount + warningCount) {
                info = viewModel.getWarnings().get(position - errorCount);
            }
            else {
                return;
            }
            viewHolder.titleTextView.setText(info.getInformationTitle().getLocalizedTitle());
            viewHolder.messageTextView.setText(info.getDescription());
            if (info.isDescriptionVisible()) {
                viewHolder.messageTextView.setVisibility(View.VISIBLE);
                viewHolder.stateImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_collapse));
            }
            else {
                viewHolder.messageTextView.setVisibility(View.GONE);
                viewHolder.stateImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_expand));
            }
            viewHolder.itemView.setOnClickListener(v -> {
                info.setDescriptionVisible(!info.isDescriptionVisible());
                notifyItemChanged(viewHolder.getAdapterPosition());
            });
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_LABEL:
                view = layoutInflater.inflate(R.layout.item_room_label, parent, false);
                return new LabelViewHolder(view);
            case TYPE_LIGHT:
                view = layoutInflater.inflate(R.layout.item_room_light, parent, false);
                return new DeviceLightViewHolder(view);
            case TYPE_OPENING:
                view = layoutInflater.inflate(R.layout.item_room_opening, parent, false);
                return new DeviceOpeningViewHolder(view);
            case TYPE_OUTLET:
                view = layoutInflater.inflate(R.layout.item_room_outlet, parent, false);
                return new DeviceOutletViewHolder(view);
            case TYPE_SHUTTER:
                view = layoutInflater.inflate(R.layout.item_room_shutter, parent, false);
                return new DeviceShutterViewHolder(view);
            case TYPE_WARNING:
                view = layoutInflater.inflate(R.layout.item_room_warning, parent, false);
                return new WarningViewHolder(view);
            case TYPE_ERROR:
                view = layoutInflater.inflate(R.layout.item_room_error, parent, false);
                return new ErrorViewHolder(view);
            default:
                //The default case should NEVER occur!
                return null;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (viewModel.getRoom() != null) {
            int errorCount = viewModel.getErrors().size();
            int warningCount = viewModel.getWarnings().size();
            int labelCount = viewModel.getRoom().getInfos().size();
            if (position < errorCount) {
                return TYPE_ERROR;
            }
            else if (position < errorCount + warningCount) {
                return TYPE_WARNING;
            }
            if (position < errorCount + warningCount + labelCount) {
                return TYPE_LABEL;
            }
            else {
                int deviceIndex = position - errorCount - warningCount - labelCount;
                ShGenericDevice device = viewModel.getRoom().getDevices().get(deviceIndex);
                if (device instanceof ShLight) {
                    return TYPE_LIGHT;
                }
                else if (device instanceof ShOpening) {
                    return TYPE_OPENING;
                }
                else if (device instanceof ShOutlet) {
                    return TYPE_OUTLET;
                }
                else if (device instanceof ShShutter) {
                    return TYPE_SHUTTER;
                }
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (viewModel.getRoom() != null) {
            return viewModel.getRoom().getInfos().size() + viewModel.getRoom().getDevices().size() + viewModel.getErrors().size() + viewModel.getWarnings().size();
        }
        else {
            return 0;
        }
    }

}
