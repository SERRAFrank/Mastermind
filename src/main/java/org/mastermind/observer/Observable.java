package  org.mastermind.observer;

import java.util.Map;

import org.mastermind.model.Model;
import org.mastermind.model.scores.Score;

public interface Observable {

	public void addObserver(Observer obs);
	public void notifyObserver();
	public void getFirstPlayerObserver();	
	public void getPlayerObserver();
	public void getScoresListObserver();				
	public void removeObserver();
}