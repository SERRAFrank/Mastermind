package  org.mastermind.model.gamemode;

import java.util.ArrayList;
import java.util.List;

public class ChallengerGameMode extends AbstractGameMode  {
	
	/**
	 * Constructeur
	 */
	public ChallengerGameMode() {
		firstPlayer = "player";
		gameModeName = "Challenger";
		
		for(int i = core.config.getInt("combinationNumbersMin"); i <= core.config.getInt("combinationNumbersMax"); i++ )
			acceptedChar.add(i);
	}

		
	/**
	 * Initialise une nouvelle partie en créant une nouvelle combinaison
	 */
	@Override
	public void newGame() { 
		hiddenCombination.clear();
		win = false;
		//Definit le type 
		if(combinationType.equals("numbers") || viewMode.equals("console")) {
			initNbrCombination();
		}else if(combinationType.equals("colors")) { 
			initColorCombination();
		}else { 
			//Retourne une erreur si le type de combinaison n'existe pas.
			try {
				throw new Exception("Undefined combination type! ");
			}catch(Exception e) {
				core.error(e);
			}
		}
		
		//Affiche la combinaison. Uniquement si le mose DEBUG est actif
		core.debug("hiddenCombination : " + hiddenCombination);

	}

	/**
	 * Génère une combinaison par nombre /*
	 */	
	private void initNbrCombination() {
		int randomNbr;
		for(int i = 0; i < combinationLenght; i++) {
			//Tire un nombre aléatoire entre combinationNumbersMin et combinationNumbersMax
			randomNbr = combinationNumbersMin + (int)(Math.random() * ((combinationNumbersMax - combinationNumbersMin) + 1));
			hiddenCombination.add(randomNbr );
		}

	}
	
	/**
	 * Génère une combinaison par couleur /*
	 */	
	private void initColorCombination() {
		int randomColorIndex;
		
		for(int i = 0; i < combinationLenght; i++) {
			//Tire un nombre aléatoire entre 0 et le nombre d'éléments de la liste de couleurs
			randomColorIndex = (int) (Math.random() * combinationColors.size());
			hiddenCombination.add(combinationColors.get(randomColorIndex) );
		}

	}	
	
	
	/**
	 * Comparaison etre la combinaison cachée et la proposition du joueur
	 * @param proposition
	 * 		Proposition du joueur sous forme de List
	 * @return returnList
	 * 		Résultat de la comparaison
	 */
	@Override
	public  void Comparaison(List<Object> playerInput) {
		List<Object> returnList = new ArrayList<Object>();
		int wInt = 0;
		for(int i = 0 ; i < combinationLenght; i++ ) {
			int hiddenCombinationValue = (int) hiddenCombination.get(i);
			int propositionValue = (int) playerInput.get(i);
			
			if ( hiddenCombinationValue == propositionValue ) {
				returnList.add("=");
				wInt++;
			}else if ( hiddenCombinationValue > propositionValue ) {
				returnList.add("+");
			}else if ( hiddenCombinationValue < propositionValue ) {
				returnList.add("-");
			}
		}

		// Tous les chiffes ont été trouvés, les conditions de victoire sont réunies
		if(wInt == combinationLenght) {
			win = true;
			output.clear();
		}else {
			win = false;
			output = returnList;
		}
		
	}
}
