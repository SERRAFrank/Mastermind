package org.mastermind.model.scores;

import java.util.Map;

import org.mastermind.core.Core;

public class Score extends ScoreSerializer{


	private static Score INSTANCE = null;

	private String playerName;
	private int winPts;
	private int loosePts;
	private int[] scores = {0, 0};

	private Score(){
		super();
		
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
	}


	/** Point d'accès pour l'instance unique du singleton */
	public static synchronized Score getInstance()
	{           
		if (INSTANCE == null){
			INSTANCE = new Score(); 
		}
		return INSTANCE;
	}

	/**
	 * Ajout de points en fonction du statut gagné ou perdu de la partie
	 * @param winner
	 * 	true si gagnée, sinon false
	 */
	public void addPoints(boolean winner){
		if(winner)
			//nbr de parties gagnées
			this.winPts++;
		else
			//nbr de parties perdues
			this.loosePts++;

		//mise à jour du fichier score
		updateScores();
	}

	/**
	 * Definit le nom du joueur et charge son score
	 * @param player
	 * 		Nom du joueur
	 */
	public void setPlayerName(String player){

		//Si la chaine est vide, le joueur s'appelle ANONYMOUS
		this.playerName = (!player.equals("")) ? player : "ANONYMOUS";

		// Reccuperation des points du joueur s'il est déja enregistré
		if(scoreList.containsKey(this.playerName))
			// Ecrasement des données scores par defaut par les données enregistrées
			this.scores = scoreList.get(this.playerName);
		this.winPts =  this.scores[0];
		this.loosePts = this.scores[1];	

		// Mise à jour du fichier score
		updateScores();

	}

	/**
	 * Renvoie le nom du joueur
	 * @return
	 * 		Nom du joueur
	 */
	public String getPlayerName(){
		return this.playerName;
	}

	/**
	 * Mise à jour du fichier des scores
	 */
	private void updateScores() {
		// Ecrasement des données scores
		this.scores[0] = this.winPts;
		this.scores[1] = this.loosePts;
		scoreList.put(this.playerName, this.scores);
		
		// Enregistrement du fichier
		save();


	}


	/**
	 * Revoie un tableau du score du joueur
	 * @return
	 * 		Scores sous la forme {nbrPatiesGagnées ; nbrPatiesPerdues}
	 */
	public int[] getScores(){
		return this.scores;
	}
	
	/**
	 *  
	 * Liste de tous les scores enregistrés
	 *
	 * @return 
	 * 		Map sous la forme nomDuJoueur =>  {nbrPatiesGagnées ; nbrPatiesPerdues} 
	 */
	public Map<String, int[]> getScoresList(){
		return this.scoreList;
	}
}
