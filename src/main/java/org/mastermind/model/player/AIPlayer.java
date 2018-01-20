package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AIPlayer extends AbstractPlayer {

	private Map<String, Integer> bornCombination = new HashMap<String, Integer>();;

	@Override
	protected void hiddenCombination() {
		this.hiddenCombination.clear();
		int randomNbr;
		for(int i = 0; i < this.combinationLenght; i++) {
			//Tire un nombre aléatoire entre combinationNumbersMin et combinationNumbersMax
			randomNbr = this.combinationMin + (int)(Math.random() * ((this.combinationMax - this.combinationMin) + 1));
			this.hiddenCombination.add(randomNbr );
		}
		//Affiche la combinaison. Uniquement si le mose DEBUG est actif
		core.debug("hiddenCombination : " + this.hiddenCombination);
	}

	protected void proposCombination() {
		int min;
		int max;
		int testNumber;
		for(int i = 0 ; i < this.combinationLenght ; i++) {
			min = this.bornCombination.get( i + ".min");
			max = this.bornCombination.get( i + ".max");
			testNumber = min + (int)(Math.random() * ((max - min) + 1));
			this.proposCombination.set(i,testNumber);
		}
	}


	protected void comparToHiddenCombination() {
		int wInt = 0;
		for(int i = 0 ; i < combinationLenght; i++ ) {
			int hiddenCombinationValue = this.hiddenCombination.get(i);
			int propositionValue = this.proposCombination.get(i);

			if ( hiddenCombinationValue == propositionValue ) {
				this.comparCombination.add("=");
				wInt++;
			}else if ( hiddenCombinationValue > propositionValue ) {
				this.comparCombination.add("+");
			}else if ( hiddenCombinationValue < propositionValue ) {
				this.comparCombination.add("-");
			}
		}		
	}

	protected void comparToProposCombination() {
		for(int i = 0 ; i < this.combinationLenght ; i++) {
			int props = this.proposCombination.get(i);			
			String reponse = this.comparCombination.get(i);
			String iMin = i + ".min";
			String iMax = i + ".max";
			if ( this.bornCombination.get( iMin ) != this.bornCombination.get( iMax )) {
				switch(reponse) {
				case "=" :
					this.bornCombination.put( iMin , props );
					this.bornCombination.put( iMax , props);
					break;
				case "-":
					this.bornCombination.put( i + ".max", props-1);
					break;
				case "+":
					this.bornCombination.put( i + ".min", props+1 );
					break;
				}		
			}
		}
	}

}
