package jp.ne.biglobe.biglobeapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taipa on 7/27/15.
 */
public class GroupBaseballTeam {
    public boolean isBattleStart;
    public final List<BaseballTeam> children = new ArrayList<BaseballTeam>();

    public GroupBaseballTeam(boolean isBattleStart) {
        this.isBattleStart = isBattleStart;
    }
}
