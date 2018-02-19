/*
 * 
 */
package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.List;
import org.mastermind.core.Core;

public abstract class AbstractPlayer {
	/** Combinaison à trouver */
	protected List<Object> hiddenCombination = new ArrayList<Object>();

	/** Combinaison proposée à comparer */
	protected List<Object> proposCombination = new ArrayList<Object>();

	/** Reponse de la comparaison de la proposition de chaine et de la cachée */
	protected List<Object> comparCombination = new ArrayList<Object>();

	/** Liste des symboles acceptés */
	protected List<Object> acceptedInputList = new ArrayList<Object>();

	/** Liste des caracteres acceptés */
	protected List<Object> acceptedComparChars = new ArrayList<Object>();

	/** Identifiant du joueur */
	protected String ID;

	/** Définit si le joueur peut mettre en pause le jeu pour des inputs */
	protected boolean pauseToInput = false;

	/** Longueur de la combinaison */
	protected int combinationLenght = Core.config.getInt("game.lenght");

	protected int wInt = 0;

	protected boolean uniqueValue;

	protected boolean moreLess;

	/**
	 * Generation de la clef secrete
	 */
	public abstract void genCombination();

	/**
	 * Proposition d'une clef
	 */
	public abstract void proposCombination();

	/**
	 * Comparaison de la proposition et de la clef secrete
	 */
	public abstract void comparToHiddenCombination();

	/**
	 * Comparaison de la reponse joueur et de la proposition de clef
	 */
	public abstract void comparToProposCombination();

	public AbstractPlayer(List<Object> acceptedInputList, List<Object> acceptedComparChars, boolean moreLess,
			boolean uniqueValue) {
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		this.acceptedInputList = acceptedInputList;
		this.acceptedComparChars = acceptedComparChars;
		this.uniqueValue = uniqueValue;
		this.moreLess = moreLess;

	}

	/**
	 * Reinitialisation pour une nouvelle manche
	 */
	public void newRound() {
		proposCombination.clear();
		comparCombination.clear();
	}

	/**
	 * Teste si la combinaison est résolue
	 * 
	 * @return boolean
	 */
	public boolean solvedCombination() {
		boolean solved = (wInt == combinationLenght);
		wInt = 0;
		return solved;
	}

	/**
	 * Retourne si le caractere o est utilisable dans un input
	 * 
	 * @param o
	 *            Caractère (Integer ou String)
	 * @return true si utilisable, sinon false
	 */

	public boolean acceptedInputChar(Object o) {
		if (acceptedInputList.contains(o) || acceptedComparChars.contains(o))
			return true;
		else
			return false;
	}

	/**
	 * Actions a réaliser en cas de victoire
	 */
	public abstract void win(boolean w);

	public boolean pauseToInput() {
		return pauseToInput;
	}

	/**
	 * Definit la proposition de clef.
	 *
	 * @param p
	 *            Nouvelle proposition
	 */
	public void setProposition(List<Object> p) {
		this.proposCombination = p;
	}

	/**
	 * Retourne la proposition de clef.
	 *
	 * @return La proposition
	 */
	public List<Object> getProposition() {
		return this.proposCombination;
	}

	/**
	 * Definit la comparaison de clefs.
	 *
	 * @param c
	 *            La nouvelle comparaison
	 */
	public void setComparaison(List<Object> c) {
		this.comparCombination = c;
	}

	/**
	 * Retourne la comparaison de clefs.
	 *
	 * @return La comparaison
	 */
	public List<Object> getComparaison() {
		return this.comparCombination;
	}

	/**
	 * Retourn l'ID du joueur
	 *
	 * @return
	 */
	public String getID() {
		return ID;
	}

}