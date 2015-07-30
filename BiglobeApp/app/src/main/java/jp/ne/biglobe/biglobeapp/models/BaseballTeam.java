package jp.ne.biglobe.biglobeapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by taipa on 7/2/15.
 */
public class BaseballTeam {
    private int id;
    private int sort;
    private String name;
    private boolean battleStart;
    private boolean battleEnd;

    public int getId() {
        return id;
    }

    public int getSort() {
        return sort;
    }

    public String getName() {
        return name;
    }

    public boolean getBattleStart() {
        return battleStart;
    }

    public boolean getBattleEnd() {
        return battleEnd;
    }

    public BaseballTeam(int id, int sort, String name) {
        this.id = id;
        this.sort = sort;
        this.name = name;
        this.battleStart = false;
        this.battleEnd = false;
    }

    public void setBattleStart(boolean value) {
        this.battleStart = value;
    }

    public void setBattleEnd(boolean value) {
        this.battleEnd = value;
    }

    /**
     * Get Baseball Teams list from JSON string.
     *
     * @param json
     * @return
     */
    public static ArrayList<BaseballTeam> getTeamListFromJSON(String json) {
        ArrayList<BaseballTeam> baseballteams = new ArrayList<BaseballTeam>();
        try {
            JSONObject masterData = new JSONObject(json);
            JSONArray teams = masterData.getJSONArray("master");

            for (int i = 0; i < teams.length(); i++) {
                int id = teams.getJSONObject(i).getInt("id");
                int sort = teams.getJSONObject(i).getInt("sort");
                String name = teams.getJSONObject(i).getString("name");
                baseballteams.add(new BaseballTeam(id, sort, name));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return baseballteams;
    }
}
