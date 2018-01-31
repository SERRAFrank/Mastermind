package  org.mastermind.observer;

public interface Observable {

	public void addObserver(Observer obs);
	public void notifyInput(String s);
	public void notifyOutput(String s, Object o);
	public void notifyEndGame(String e, boolean w);
	public void removeObserver();
}