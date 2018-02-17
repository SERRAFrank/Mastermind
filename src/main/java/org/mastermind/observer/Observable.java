package  org.mastermind.observer;

import java.util.List;

public interface Observable {

	public void addObserver(Observer obs);
	public void notifyInput(String s);
	public void notifyOutputCompar( List<Object> o);
	public void notifyOutputPropos(List<Object> o);
	public void notifyRound(int o);
	public void notifyInitGame(String s, List<Object> l, boolean u);
	public void notifyEndGame(String e, boolean w);
	public void removeObserver();
}