package  org.mastermind.observer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Observer {
	public void updateInput(String s);
	public void updateOutput(String s, List<?> o);
	public void updateOutput(String s, String o);
	public void updateEndGame(String e, boolean w);	
}