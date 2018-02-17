package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mastermind.core.Core;
import org.mastermind.observer.Observable;
import org.mastermind.observer.Observer;

public class HumanPlayer extends AbstractPlayer {

	public HumanPlayer(List<Object> acceptedInputList, List<Object> acceptedComparChars, boolean moreLess, boolean uniqueValue) {
		super(acceptedInputList, acceptedComparChars, moreLess, uniqueValue);
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		// Identifiant
		ID = "human";

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
		Core.score.addPoints(w);
	}

}
