/*
 * 
 */
package org.mastermind.model.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mastermind.core.Core;
import org.mastermind.model.scores.Score;

public abstract class  AbstractPlayer {
	/** Instance du core */
	protected Core core = Core.getInstance(this);
	
	/** Instance de score */
	protected Score scores = Score.getInstance();

	/** Combinaison à trouver */
	protected List<Integer> hiddenCombination =  new ArrayList<Integer>();

	/** Combinaison proposée à comparer */
	protected List<Integer> proposCombination =  new ArrayList<Integer>();
	
	/** Reponse de la comparaison de la proposition de chaine et  de la cachée */
	protected List<String> comparCombination =  new ArrayList<String>();

	/** Liste des caracteres acceptés */
	protected Set<Object> acceptedInputChar = new HashSet<Object>();
	
	/** Identifiant du joueur */
	protected String ID;
	
	/** Définit si le joueur peut mettre en pause le jeu pour des inputs */
	protected boolean pauseToInput = false;
	
	/** Longueur de la combinaison */
	protected int combinationLenght = core.config.getInt("combinationLenght");

	/** Min et max pour les combinaisons de type chiffre */
	protected int combinationMin = core.config.getInt("combinationNumbersMin");
	protected int combinationMax = core.config.getInt("combinationNumbersMax");


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
	

	/**
	 * Reinitialisation pour une nouvelle manche
	 */
	public void newRound() {
		proposCombination.clear();
		comparCombination.clear();
	}
	
	/**
	 * Teste si la combinaison est résolue
	 * @return boolean
	 */
	public boolean solvedCombination() {
		Set<String> tempSet = new HashSet<String>(comparCombination);
		
		if(tempSet.size() == 1 && tempSet.contains("=")) {
			return true;
		}else {
			return false;
		}

	}

	/**
	 * Retourne si le caractere o est utilisable dans un input
	 * @param o
	 * 		Caractère (Integer ou String)
	 * @return
	 * 		true si utilisable, sinon false
	 */
	
	public boolean acceptedInputChar(Object o) {
		return acceptedInputChar.contains(o);
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
	 * 		Nouvelle proposition
	 */
	public void setProposition(List<Integer> p) {
		this.proposCombination = p;
	}

	/**
	 * Retourne la proposition de clef.
	 *
	 * @return 
	 * 		La proposition
	 */
	public List<Integer> getProposition(){
		return this.proposCombination;
	}

	/**
	 * Definit la comparaison de clefs.
	 *
	 * @param c 
	 * 		La nouvelle comparaison
	 */
	public void setComparaison(List<String> c) {
		this.comparCombination = c;
	}

	/**
	 * Retourne la comparaison de clefs.
	 *
	 * @return
	 * 		La comparaison
	 */
	public List<String> getComparaison(){
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