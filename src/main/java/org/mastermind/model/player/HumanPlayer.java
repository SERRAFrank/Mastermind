package org.mastermind.model.player;

import java.util.List;

import org.mastermind.observer.Observable;
import org.mastermind.observer.Observer;

public class HumanPlayer extends AbstractPlayer {


	public HumanPlayer() {
		// Identifiant
		ID = "human";

		// Definition des bornes
		acceptedInputChar.add("+");
		acceptedInputChar.add("-");
		acceptedInputChar.add("=");
		this.pauseToInput = true;
	}


	/**
	 * Génération d'une clef secrete
	 */
	public void genCombination() {		
	}

	/**
	 * Création d'une proposition de clef
	 */
	public void proposCombination() {
		// TODO Auto-generated method stub

	}

	/**
	 * Comparaison d'une proposition de clef avec la clef secrete
	 */
	public void comparToHiddenCombination() {
		// TODO Auto-generated method stub

	}

	/**
	 * Analyse de la reponse de comparaison entre la proposition de clef
	 */
	public void comparToProposCombination() {
		// TODO Auto-generated method stub

	}
 
	/**
	 * Definit si le joueur est victorieux ou non, et ajouter les points en fonction
	 *
	 * @param w
	 * 		true si le joueur est gagnant, sinon false
	 */

	public void win(boolean w) {
		scores.addPoints(w);
	}

}
