package  org.mastermind.model.gamemode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefenderGameMode extends AbstractGameMode {

	
	Map<String, Integer> findCombination = new HashMap<String, Integer>();
	
	/**
	 * Constructeur
	 */
	public DefenderGameMode() {
		firstPlayer = "computer";

		gameModeName = "Defender";
		
		acceptedChar.add("+");
		acceptedChar.add("-");
		acceptedChar.add("=");
	}

	@Override
	public void newGame() {
		findCombination.clear();
		hiddenCombination.clear();
		win = false;
		for(int i = 0 ; i < combinationLenght ; i++) {
			hiddenCombination.add("");
			findCombination.put(i+".min", core.config.getInt("combinationNumbersMin"));
			findCombination.put(i+".max", core.config.getInt("combinationNumbersMax"));
		}
		findCombination();
		
	}

	@Override
	public void Comparaison(List<Object> playerInput) {
		int wInt = 0;
		for(int i = 0 ; i < combinationLenght ; i++) {
			int props = (int) hiddenCombination.get(i);			
			String reponse = (String) playerInput.get(i);

			switch(reponse) {
				case "=" :
					findCombination.put( i + ".min", props );
					findCombination.put( i + ".max", props);
					wInt++;
					break;
				case "-":
					findCombination.put( i + ".max", props-1);
					break;
				case "+":
					findCombination.put( i + ".min", props+1 );
			}		
		}		
	
		// Tous les chiffres ont été trouvés, les conditions de victoire sont réunies
		if(wInt == combinationLenght) {
			output.clear();
			win = true;
		}else {
			win = false;
			findCombination();
		}
	}
	
	private void findCombination() {
		int min;
		int max;
		int testNumber;
		for(int i = 0 ; i < combinationLenght ; i++) {
			min = findCombination.get( i + ".min");
			max = findCombination.get( i + ".max");
			testNumber = min + (int)(Math.random() * ((max - min) + 1));
			hiddenCombination.set(i,testNumber);
		}
		
		
		output = hiddenCombination;
			 
	}

}
