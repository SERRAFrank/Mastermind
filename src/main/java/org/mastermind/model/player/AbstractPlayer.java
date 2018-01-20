package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.mastermind.core.Core;

public abstract class  AbstractPlayer {
	/** Instance du core */
	protected Core core = Core.getInstance(this);

	/** Combinaison à trouver */
	protected List<Integer> hiddenCombination =  new ArrayList<Integer>();

	/** Combinaison proposée à comparer */
	protected List<Integer> proposCombination =  new ArrayList<Integer>();
	
	/** Reponse de la comparaison de la proposition de chaine et  de la cachée */
	protected List<String> comparCombination =  new ArrayList<String>();

	
	protected List<Object> input;
	
	protected List<Object> output;
	
	protected abstract void hiddenCombination();
	
	protected abstract void proposCombination();
	
	protected abstract void comparToHiddenCombination();

	protected abstract void comparToProposCombination();
	
	/** Longueur de la combinaison */
	protected int combinationLenght = core.config.getInt("combinationLenght");
	
	/** Type de combinaison */
	protected String combinationType = core.config.get("combinationType");
	
	/** List des couleurs utilisables (combinaison de type couleur/ */
	protected List<String> combinationColors = new ArrayList<String>(Arrays.asList(core.config.get("combinationColors").split(",")));
	

	/** Min et max pour les combinaisons de type chiffre */
	protected int combinationMin = (combinationType.equals("numbers")) ? core.config.getInt("combinationNumbersMin") : 0 ;
	protected int combinationMax = (combinationType.equals("numbers")) ? core.config.getInt("combinationNumbersMax") : core.config.get("combinationColors").split(",").length ;
	

	
	public boolean solvedCombination() {
		return new HashSet<String>(comparCombination).size() == 1;
	}


	
		

}
