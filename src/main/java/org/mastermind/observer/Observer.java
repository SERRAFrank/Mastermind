package  org.mastermind.observer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Observer {
	public void update(List<Object> o, boolean w);
	public void showScoresList(Map<String, int[]> scoresList);
	public void playerInfo(String playerName, int[] scores);
	public void firstPlayer(String player);

}