package jp.ne.biglobe.biglobeapp.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import jp.ne.biglobe.biglobeapp.BLApplication;
import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.models.CommonProcess;
import jp.ne.biglobe.biglobeapp.models.SettingContent;
import jp.ne.biglobe.biglobeapp.models.SettingModel;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 * interface.
 */
public class PushSettingFragmentOld extends ListFragment {
    private static final String TAG = PushSettingFragmentOld.class.getSimpleName();
    SettingAdapter adapter = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PushSettingFragmentOld() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register to event bus
        EventBus.getDefault().register(this);


        // TODO: Change Adapter to display your content
//        if (Build.VERSION.SDK_INT >= 21) {
//            setListAdapter(new ArrayAdapter<SettingContent.SettingItem>(getActivity(),
//                    R.layout.list_item_switch_m, android.R.id.text1, SettingContent.ITEMS));
//        } else {
//            setListAdapter(new ArrayAdapter<SettingContent.SettingItem>(getActivity(),
//                    R.layout.list_item_switch, android.R.id.text1, SettingContent.ITEMS));
//        }
        BLApplication blapp = (BLApplication)getActivity().getApplication();
        if (blapp.getSetting() == null) {
            blapp.setSetting(SettingModel.load());
        }


        adapter = new SettingAdapter(getActivity(), blapp.getSetting().generateSettingItems(this.getActivity()));
        setListAdapter(adapter);


        CommonProcess process = new CommonProcess(getActivity());
        process.updateBaseballMaster();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick: " + position);
        super.onListItemClick(l, v, position, id);
    }


    /**
     *
     */
    private class SettingAdapter extends ArrayAdapter<SettingContent.SettingItem> {
        private final String TAG = SettingAdapter.class.getSimpleName();
        private Context mContext;
        private ArrayList<SettingContent.SettingItem> mItems;

        public SettingAdapter(Context context, ArrayList<SettingContent.SettingItem> items) {
            super(context, R.layout.list_item_switch_m, items);
            mContext = context;
            mItems = items;
        }

        /**
         *
         * @param items
         */
        public void updateItems(ArrayList<SettingContent.SettingItem> items) {
            mItems.clear();
            mItems.addAll(items);

            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.list_item_switch, parent, false);
            SwitchCompat tvTitle = (SwitchCompat) rowView.findViewById(android.R.id.text1);
            tvTitle.setText(mItems.get(position).content);
            tvTitle.setChecked(mItems.get(position).value);
            tvTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mContext.getString(R.string.setting_push).equals(buttonView.getText())) {
                        BLApplication blapp = (BLApplication) getActivity().getApplication();
                        blapp.getSetting().setAll(isChecked);
                        blapp.getSetting().save();
                    }
                }
            });


            Log.d(TAG, "Text : " + tvTitle.getText() + "     Value : " + tvTitle.isChecked());

            return rowView;
        }
    }
}
