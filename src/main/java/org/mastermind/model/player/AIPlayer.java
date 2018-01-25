package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AIPlayer extends AbstractPlayer {

	/** Liste des bornes min et max pour la génération des combinaisons */
	private Map<String, Integer> bornCombination = new HashMap<String, Integer>();;

	public AIPlayer() {
		// Identifiant
		ID = "AI";
		
		// Definition des bornes
		for(int i = this.combinationMin; i <= this.combinationMax; i++ ) {
			this.acceptedInputChar.add(i);
			this.bornCombination.put( i + ".min", this.combinationMin);
			this.bornCombination.put( i + ".max", this.combinationMax);

		}
	}


	/**
	 * Génération d'une clef secrete
	 */
	public void genCombination() {
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

	/**
	 * Création d'une proposition de clef en tirant un nombre aléatoire entre les bornes min et max
	 */
	public void proposCombination() {
		int min;
		int max;
		int testNumber;
		for(int i = 0 ; i < this.combinationLenght ; i++) {
			min = this.bornCombination.get( i + ".min");
			max = this.bornCombination.get( i + ".max");
			// Generation de la proposition de clef en tirant un nombre aléatoire entre les bornes
			testNumber = min + (int)(Math.random() * ((max - min) + 1));
			this.proposCombination.add(testNumber);
		}
	}

	/**
	 * Comparaison d'une proposition de clef avec la clef secrete
	 */
	public void comparToHiddenCombination() {
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

	/**
	 * Analyse de la reponse de comparaison entre la proposition de clef
	 */
	public void comparToProposCombination() {
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

	/**
	 * Definit si le joueur est victorieux ou non, et ajouter les points en fonction
	 *
	 * @param w
	 * 		true si le joueur est gagnant, sinon false
	 */

	public void win(boolean w) {

		// L'IA ne gagne pas de points
	}



}
