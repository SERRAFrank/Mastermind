package org.mastermind.model.player;

import java.util.List;
import org.mastermind.core.Core;

public class HumanPlayer extends AbstractPlayer {

	public HumanPlayer(List<Object> acceptedInputList, List<Object> acceptedComparChars, boolean moreLess,
			boolean uniqueValue) {
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
	@Override
	public void genCombination() {
	}

	/**
	 * Création d'une proposition de clef
	 */
	@Override
	public void proposCombination() {
		// TODO Auto-generated method stub

	}

	/**
	 * Comparaison d'une proposition de clef avec la clef secrete
	 */
	@Override
	public void comparToHiddenCombination() {
		// TODO Auto-generated method stub

	}

	/**
	 * Analyse de la reponse de comparaison entre la proposition de clef
	 */
	@Override
	public void comparToProposCombination() {
		// TODO Auto-generated method stub

	}

	/**
	 * Definit si le joueur est victorieux ou non, et ajouter les points en fonction
	 *
	 * @param w
	 *            true si le joueur est gagnant, sinon false
	 */

	@Override
	public void win(boolean w) {
		Core.score.addPoints(w);
	}

}
