package  org.mastermind.observer;

import java.util.List;
import java.util.Map;

import org.mastermind.model.Model;
import org.mastermind.model.scores.Score;

public interface Observable {

	public void addObserver(Observer obs);
	public void notifyInput(String s);
	public void notifyOutput(String s, Object o);
	public void notifyEndGame(String e, boolean w);
	public void removeObserver();
}