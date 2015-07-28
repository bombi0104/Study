package jp.ne.biglobe.biglobeapp.utils;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by taipa on 7/2/15.
 */
public class GsonExample {
    private static String TAG = "GsonExample";
    public void example01(){
        Team t = new Team(1,2,"abc");
        Gson gson = new Gson();
        String json = gson.toJson(t);
        Log.d(TAG, "JSON: " + json);

        Team t2 = gson.fromJson(json, Team.class);
        Log.d(TAG, t2.toString());
    }

    public void example02(){
        Gson gson = new Gson();

    }

    public class Team {
        private int id;
        private int sort;
        private String name;
        private boolean battleStart;
        private boolean battleEnd;
        private ArrayList<Mem> mems;

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

        public Team(){

        }

        public Team(int id, int sort, String name) {
            this.id = id;
            this.sort = sort;
            this.name = name;
            this.battleStart = false;
            this.battleEnd = false;
            mems = new ArrayList<Mem>();

            for (int i = 0; i < 5; i++){
                mems.add(new Mem("Name", i));
            }
        }

        public void setBattleStart(boolean value) {
            this.battleStart = value;
        }

        public void setBattleEnd(boolean value) {
            this.battleEnd = value;
        }


        public String toString(){
            return "Team : id = " + id + " - sort = " + sort + " - name = " + name + " - BattleStart = " + battleStart
                    + " - BattleEnd = " + battleEnd;
        }
    }

    public class Mem {
        private String name;
        private int Old;

        public Mem(){

        }

        public Mem(String name, int Old){
            this.name = name;
            this.Old = Old;
        }
    }
}
