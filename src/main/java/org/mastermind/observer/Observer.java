package  org.mastermind.observer;

public interface Observer {
	public void updateInput(String s);
	public void updateOutput(String s, Object o);
	public void updateEndGame(String e, boolean w);
}