package jp.ne.biglobe.biglobeapp.views;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import jp.ne.biglobe.biglobeapp.BLApplication;
import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.models.BaseballTeamExpandableListAdapter;
import jp.ne.biglobe.biglobeapp.models.GroupBaseballTeam;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TutorialPage1Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TutorialPage1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialPage1Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorialPage1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorialPage1Fragment newInstance(String param1, String param2) {
        TutorialPage1Fragment fragment = new TutorialPage1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialPage1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial_page1, container, false);

        return view;
    }

    /**
     * Create date for ExpanableListview
     *
     * @return
     */
    private SparseArray<GroupBaseballTeam> createBaseballTeamList() {
        BLApplication app = (BLApplication) getActivity().getApplication();

        SparseArray<GroupBaseballTeam> groups = new SparseArray<GroupBaseballTeam>();
        GroupBaseballTeam groupBattleStart = new GroupBaseballTeam(true);
        groupBattleStart.children.addAll(app.getSetting().getBaseballTeams());
        groups.append(0, groupBattleStart);

        GroupBaseballTeam groupBattleEnd = new GroupBaseballTeam(false);
        groupBattleEnd.children.addAll(app.getSetting().getBaseballTeams());
        groups.append(1, groupBattleEnd);

        return groups;
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
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
