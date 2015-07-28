package jp.ne.biglobe.biglobeapp.models;

import android.app.Activity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import jp.ne.biglobe.biglobeapp.R;
import jp.ne.biglobe.biglobeapp.views.PushSettingFragment;

/**
 * Created by taipa on 7/27/15.
 */
public class BaseballTeamExpandableListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = BaseballTeamExpandableListAdapter.class.getSimpleName();
    private final SparseArray<GroupBaseballTeam> groups;
    public LayoutInflater inflater;
    public Activity activity;
    private PushSettingFragment fragment;
    private int groupItemHeight = 0;
    private int childItemHeight = 0;
    private ExpandableListView list = null;

    public BaseballTeamExpandableListAdapter(Activity act, SparseArray<GroupBaseballTeam> groups, PushSettingFragment frm) {
        activity = act;
        this.groups = groups;
        fragment = frm;
        inflater = act.getLayoutInflater();
    }

    public void setList(ExpandableListView exlist) {
        list = exlist;
    }

    public void updateNewData(SparseArray<GroupBaseballTeam> groups) {


        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final BaseballTeam children = (BaseballTeam) getChild(groupPosition, childPosition);
        SwitchCompat switchCompat = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_switch, null);
        }

        switchCompat = (SwitchCompat) convertView.findViewById(R.id.switchItem);
        switchCompat.setText(children.getName());
        children.setIsBattleStartItem(groups.get(groupPosition).isBattleStart);
        switchCompat.setTag(children);

        // Set changed listener to PushSettingFragment
        switchCompat.setOnCheckedChangeListener(fragment);


        // Set value for switch
        if (groups.get(groupPosition).isBattleStart){
            switchCompat.setChecked(children.getBattleStart());
        } else {
            switchCompat.setChecked(children.getBattleEnd());
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
        updateListHeight();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        updateListHeight();
    }

    /**
     * Update ExpandListView height
     * Battle-Start and Battle-End list
     */
    public void updateListHeight() {
        int h = getGroupItemHeight() * 2;

        if (list.isGroupExpanded(0)) {
            h = h + (getChildItemHeight() + list.getDividerHeight()) * getChildrenCount(0);
            Log.d(TAG, "isGroupExpanded 0 = " + h);
        }

        if (list.isGroupExpanded(1)) {
            h = h + (getChildItemHeight() + list.getDividerHeight()) * getChildrenCount(0);
            Log.d(TAG, "isGroupExpanded 1 = " + h);
        }

        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = h;

        list.setLayoutParams(params);
        list.requestLayout();
    }

    /**
     * Get Group id height
     *
     * @return height
     */
    private int getGroupItemHeight() {
        if (groupItemHeight == 0) {
            View v = getGroupView(0, false, null, list);
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            groupItemHeight = v.getMeasuredHeight();
        }

        return groupItemHeight;
    }

    /**
     * Get child item height
     *
     * @return height
     */
    private int getChildItemHeight() {
        if (childItemHeight == 0) {
            View v = getChildView(0, 0, false, null, list);
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            childItemHeight = v.getMeasuredHeight();
        }

        return childItemHeight;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        GroupBaseballTeam group = (GroupBaseballTeam) getGroup(groupPosition);

        TextView tvHeader = (TextView) convertView.findViewById(R.id.tvHeader);
        TextView tvDescrition = (TextView) convertView.findViewById(R.id.tvDescription);
        if (group.isBattleStart) {
            tvHeader.setText(R.string.setting_header_baseball_battle_start);
            tvDescrition.setText(R.string.setting_header_baseball_battle_start_description);
        } else {
            tvHeader.setText(R.string.setting_header_baseball_battle_end);
            tvDescrition.setText(R.string.setting_header_baseball_battle_end_description);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
