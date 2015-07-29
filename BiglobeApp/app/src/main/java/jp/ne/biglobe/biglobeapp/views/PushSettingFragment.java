package jp.ne.biglobe.biglobeapp.views;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import de.greenrobot.event.EventBus;
import jp.ne.biglobe.biglobeapp.BLApplication;
import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.models.BaseballTeam;
import jp.ne.biglobe.biglobeapp.models.BaseballTeamExpandableListAdapter;
import jp.ne.biglobe.biglobeapp.models.CommonProcess;
import jp.ne.biglobe.biglobeapp.models.GroupBaseballTeam;
import jp.ne.biglobe.biglobeapp.models.SettingModel;
import jp.ne.biglobe.biglobeapp.utils.MessageEvent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PushSettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PushSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PushSettingFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = PushSettingFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SettingModel settingModel;

    private OnFragmentInteractionListener mListener;
    private ExpandableListView baseballTeamList;
    private BaseballTeamExpandableListAdapter baseballTeamListAdapter;
    private SwitchCompat setting_all;
    private SwitchCompat setting_news_moring;
    private SwitchCompat setting_news_noon;
    private SwitchCompat setting_news_night;
    private SwitchCompat setting_news_dialog;
    private SwitchCompat setting_baseball_schedule;
    private SwitchCompat setting_osusume;
    private SwitchCompat setting_osusume_dialog;
    private View greyView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PushSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PushSettingFragment newInstance(String param1, String param2) {
        PushSettingFragment fragment = new PushSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PushSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Register to event bus
        EventBus.getDefault().register(this);
    }


    /**
     * Create data for ExpanableListview
     *
     * @return
     */
    private SparseArray<GroupBaseballTeam> createBaseballTeamList() {
        SparseArray<GroupBaseballTeam> groups = new SparseArray<GroupBaseballTeam>();
        GroupBaseballTeam groupBattleStart = new GroupBaseballTeam(true);
        groupBattleStart.children.addAll(settingModel.getBaseballTeams());
        groups.append(0, groupBattleStart);

        GroupBaseballTeam groupBattleEnd = new GroupBaseballTeam(false);
        groupBattleEnd.children.addAll(settingModel.getBaseballTeams());
        groups.append(1, groupBattleEnd);

        return groups;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_push_setting, container, false);
        baseballTeamList = (ExpandableListView) view.findViewById(R.id.listBattleStart);
        setting_all = (SwitchCompat) view.findViewById(R.id.swSettingAll);
        setting_news_moring = (SwitchCompat) view.findViewById(R.id.swNewsMorning);
        setting_news_noon = (SwitchCompat) view.findViewById(R.id.swNewsNoon);
        setting_news_night = (SwitchCompat) view.findViewById(R.id.swNewsNight);
        setting_news_dialog = (SwitchCompat) view.findViewById(R.id.swNewsDislog);
        setting_baseball_schedule = (SwitchCompat) view.findViewById(R.id.swBaseballSchedule);
        setting_osusume = (SwitchCompat) view.findViewById(R.id.swOsusume);
        setting_osusume_dialog = (SwitchCompat) view.findViewById(R.id.swOsusumeDialog);
        greyView = view.findViewById(R.id.greyView);

        // Set listerner
        setting_all.setOnCheckedChangeListener(this);
        setting_news_moring.setOnCheckedChangeListener(this);
        setting_news_noon.setOnCheckedChangeListener(this);
        setting_news_night.setOnCheckedChangeListener(this);
        setting_news_dialog.setOnCheckedChangeListener(this);
        setting_baseball_schedule.setOnCheckedChangeListener(this);
        setting_osusume.setOnCheckedChangeListener(this);
        setting_osusume_dialog.setOnCheckedChangeListener(this);

        initData();

        CommonProcess process = new CommonProcess(getActivity());
        process.updateBaseballMaster();

        return view;
    }

    /**
     * Get SettingModel data and apply to screen
     */
    private void initData() {
        BLApplication app = (BLApplication) getActivity().getApplication();
        settingModel = app.getSetting();
        if (settingModel != null) {
            setting_all.setChecked(settingModel.isAll());
            setting_news_moring.setChecked(settingModel.isNewsMorning());
            setting_news_noon.setChecked(settingModel.isNewsNoon());
            setting_news_night.setChecked(settingModel.isNewsNight());
            setting_news_dialog.setChecked(settingModel.isNewsDialog());
            setting_baseball_schedule.setChecked(settingModel.isBaseballSchedule());
            setting_osusume.setChecked(settingModel.isOsusume());
            setting_osusume_dialog.setChecked(settingModel.isOsusumeDialog());


            BaseballTeamExpandableListAdapter adapter =
                    new BaseballTeamExpandableListAdapter(getActivity(), createBaseballTeamList(), this);
            adapter.setList(baseballTeamList);
            baseballTeamList.setAdapter(adapter);

            // Update expandlistview height after created.
            adapter.updateListHeight();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Touch on : " + buttonView.getText());
        switch (buttonView.getId()) {
            case R.id.swSettingAll:
                if (settingModel.isAll() != isChecked) {
                    settingModel.setAll(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");

                    if (!isChecked) {
                        greyView.setVisibility(View.VISIBLE);
                    } else {
                        greyView.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.swNewsMorning:
                if (settingModel.isNewsMorning() != isChecked) {
                    settingModel.setNewsMorning(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            case R.id.swNewsNoon:
                if (settingModel.isNewsNoon() != isChecked) {
                    settingModel.setNewsNoon(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            case R.id.swNewsNight:
                if (settingModel.isNewsNight() != isChecked) {
                    settingModel.setNewsNight(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            case R.id.swNewsDislog:
                if (settingModel.isNewsDialog() != isChecked) {
                    settingModel.setNewsDialog(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            case R.id.swBaseballSchedule:
                if (settingModel.isBaseballSchedule() != isChecked) {
                    settingModel.setBaseballSchedule(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            case R.id.swOsusume:
                if (settingModel.isOsusume() != isChecked) {
                    settingModel.setOsusume(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            case R.id.swOsusumeDialog:
                if (settingModel.isOsusumeDialog() != isChecked) {
                    settingModel.setOsusumeDialog(isChecked);
                    Log.d(TAG, "Call API updatePushInfo");
                }
                break;
            default:
                break;
        }

        if (buttonView.getTag() != null) {
            BaseballTeam team = (BaseballTeam) buttonView.getTag();
            boolean oldValue = team.isBattleStartItem() ? team.getBattleStart() : team.getBattleEnd();

            if (oldValue != isChecked) {
                for (BaseballTeam t :
                        settingModel.getBaseballTeams()) {
                    if (t.getId() == team.getId()) {
                        if (team.isBattleStartItem()) {
                            t.setBattleStart(isChecked);
                            Log.d(TAG, "Call API updatePushInfo");
                        } else {
                            t.setBattleEnd(isChecked);
                            Log.d(TAG, "Call API updatePushInfo");
                        }
                        break;
                    }
                }
            }
        }


        // Save change to SharedPreferece
        settingModel.save();

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        // Register to event bus
        EventBus.getDefault().unregister(this);
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(MessageEvent event) {
        Log.d(TAG, "Receive MessageEvent: " + event.message);
        if ("MasterDataChanged".equals(event.message)) {
            BLApplication blapp = (BLApplication) getActivity().getApplication();
            if (blapp.getSetting() == null) {
                blapp.setSetting(SettingModel.load());
            }

            baseballTeamListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
