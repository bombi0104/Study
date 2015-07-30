package jp.ne.biglobe.biglobeapp.views;

import android.app.Activity;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;
import jp.ne.biglobe.biglobeapp.BLApplication;
import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.api.GetPushInfoAPI;
import jp.ne.biglobe.biglobeapp.api.RegTokenAPI;
import jp.ne.biglobe.biglobeapp.api.UnregTokenAPI;
import jp.ne.biglobe.biglobeapp.api.UpdatePushInfoAPI;
import jp.ne.biglobe.biglobeapp.models.BaseballTeam;
import jp.ne.biglobe.biglobeapp.models.BaseballTeamExpandableListAdapter;
import jp.ne.biglobe.biglobeapp.models.CommonProcess;
import jp.ne.biglobe.biglobeapp.models.GroupBaseballTeam;
import jp.ne.biglobe.biglobeapp.models.SettingModel;
import jp.ne.biglobe.biglobeapp.utils.Enums;
import jp.ne.biglobe.biglobeapp.utils.BLEvent;

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
     * Create data for ExpanableListview (Battle start and battle end)
     *
     * @return
     */
    private SparseArray<GroupBaseballTeam> createBaseballTeamList() {
        SparseArray<GroupBaseballTeam> groups = new SparseArray<GroupBaseballTeam>();

        // Create data for Battle-start list
        GroupBaseballTeam groupBattleStart = new GroupBaseballTeam(true);
        groupBattleStart.children.addAll(settingModel.getBaseballTeams());
        groups.append(0, groupBattleStart);

        // Create data for Battle-end list
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

        loadData();

        CommonProcess process = new CommonProcess(getActivity());
        process.updateBaseballMaster();

        return view;
    }

    /**
     * Get SettingModel data and apply to screen
     */
    private void loadData() {
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

            // Load data for Battle-Start and Battle-End list
            if (baseballTeamListAdapter == null) {
                baseballTeamListAdapter = new BaseballTeamExpandableListAdapter(getActivity(), createBaseballTeamList(), this);
                baseballTeamListAdapter.setList(baseballTeamList);
                baseballTeamList.setAdapter(baseballTeamListAdapter);

                // Update expandlistview height after created.
                baseballTeamListAdapter.updateListHeight();
            } else {
                baseballTeamListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "Touch on : " + buttonView.getText());
        switch (buttonView.getId()) {
            case R.id.swSettingAll:
                if (settingModel.isAll() != isChecked) {
                    Log.d(TAG, "Call API updatePushInfo");

                    if (!isChecked) {
                        unregtoken();
                    } else {
                        regToken();
                    }
                }
                break;
            case R.id.swNewsMorning:
                if (settingModel.isNewsMorning() != isChecked) {
                    updatePushInfo(Enums.UPDATE_ITEMS.NEWS_MORNING, isChecked, 0);
                }
                break;
            case R.id.swNewsNoon:
                if (settingModel.isNewsNoon() != isChecked) {
                    updatePushInfo(Enums.UPDATE_ITEMS.NEWS_NOON, isChecked, 0);
                }
                break;
            case R.id.swNewsNight:
                if (settingModel.isNewsNight() != isChecked) {
                    updatePushInfo(Enums.UPDATE_ITEMS.NEWS_NIGHT, isChecked, 0);
                }
                break;
            case R.id.swNewsDislog:
                if (settingModel.isNewsDialog() != isChecked) {
                    settingModel.setNewsDialog(isChecked);
                }
                break;
            case R.id.swBaseballSchedule:
                if (settingModel.isBaseballSchedule() != isChecked) {
                    updatePushInfo(Enums.UPDATE_ITEMS.BASEBALL_SCHEDULE, isChecked, 0);
                }
                break;
            case R.id.swOsusume:
                if (settingModel.isOsusume() != isChecked) {
                    updatePushInfo(Enums.UPDATE_ITEMS.OSUSUME_CONTENT, isChecked, 0);
                }
                break;
            case R.id.swOsusumeDialog:
                if (settingModel.isOsusumeDialog() != isChecked) {
                    settingModel.setOsusumeDialog(isChecked);
                }
                break;
            default:
                break;
        }

        // When change value of item in
        // Battle Start and battle end list
        if (buttonView.getTag(R.id.switch_baseball_team_obj) != null) {
            BaseballTeam team = (BaseballTeam) buttonView.getTag(R.id.switch_baseball_team_obj);
            boolean isBattleStart = (boolean) buttonView.getTag(R.id.switch_is_battle_start);
            boolean oldValue = isBattleStart ? team.getBattleStart() : team.getBattleEnd();

            // When init listview, this method will be call when switch have change
            // data by programming, so, have to check old value and new value to known that
            // user change value action or value changed by init listview
            if (oldValue != isChecked) {
                for (BaseballTeam t : settingModel.getBaseballTeams()) {
                    if (t.getId() == team.getId()) {
                        if (isBattleStart) {
                            updatePushInfo(Enums.UPDATE_ITEMS.BASEBALL_BATTLE_START, isChecked, t.getId());
                            Log.d(TAG, "Call API updatePushInfo - Battle Start");
                        } else {
                            updatePushInfo(Enums.UPDATE_ITEMS.BASEBALL_BATTLE_END, isChecked, t.getId());
                            Log.d(TAG, "Call API updatePushInfo - Battle End");
                        }
                        break;
                    }
                }
            }
        }

        // Save change to SharedPreferece
        settingModel.save();

    }

    /**
     * Call updatePushInfo API when touch on each setting item on screen
     *
     * @param item
     * @param value
     * @param baseball_team_id
     */
    private void updatePushInfo(Enums.UPDATE_ITEMS item, boolean value, int baseball_team_id) {
        UpdatePushInfoAPI api = new UpdatePushInfoAPI(getActivity());
        api.addParam(item, value, baseball_team_id);

        api.run(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (CommonProcess.isReponseOK(response)) {
                    CommonProcess.mergeUpdatePushInfoResponseToLocalObject(response, settingModel);

                    // Reload responsed data to screen.
                    loadData();
                } else {
                    // TODO : Show error dialog

                    // Rollback value to previous status
                    loadData();
                }
                Log.d(TAG, "updatePushInfo : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // TODO: Show error dialog

                // Rollback value to previous status
                loadData();
            }
        });
    }

    /**
     * Unregister token API
     */
    private void unregtoken() {
        UnregTokenAPI api = new UnregTokenAPI(getActivity());
        api.run(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (CommonProcess.isReponseOK(response)) {
                    greyView.setVisibility(View.VISIBLE);
                    settingModel.setAll(false);
                } else {
                    // TODO : Show error dialog

                    // Rollback value to previous status
                    loadData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                // TODO : show error dialog

                // Rollback value to previous status
                loadData();
            }
        });
    }

    /**
     * Call API RegToken
     */
    private void regToken() {
        RegTokenAPI api = new RegTokenAPI(getActivity());
        api.run(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (CommonProcess.isReponseOK(response)) {
                    // Save TokenID to SharedPreferences
                    CommonProcess.getTokenIdFromResponse(response);

                    // Remove grey layer, unlock screen.
                    greyView.setVisibility(View.GONE);

                    // Setting value to model
                    settingModel.setAll(true);

                    // Call getPushInfo api for sync data with server.
                    getPushInfo();
                } else {
                    // TODO : Show error dialog

                    // Rollback value to previous status
                    loadData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                // TODO : show error dialog

                // Rollback value to previous status
                loadData();
            }
        });
    }

    /**
     * Get push info after call regtoken API
     */
    private void getPushInfo() {
        GetPushInfoAPI api = new GetPushInfoAPI(getActivity());
        api.run(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (CommonProcess.isReponseOK(response)) {
                    CommonProcess.mergeUpdatePushInfoResponseToLocalObject(response, settingModel);

                    // Reload data to screen
                    loadData();
                } else {
                    // TODO : Show error dialog

                    // Rollback value to previous status
                    loadData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                // TODO : show error dialog

                // Rollback value to previous status
                loadData();
            }
        });
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

    /**
     * @param event
     */
    public void onEvent(BLEvent event) {
        switch (event.type) {
            case UPDATE_BASEBALL_MASTER_DONE:
                BLApplication blapp = (BLApplication) getActivity().getApplication();
                blapp.setSetting(SettingModel.load());
                settingModel = blapp.getSetting();

                baseballTeamListAdapter.updateNewData(createBaseballTeamList());
                break;
            default:
                break;
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
