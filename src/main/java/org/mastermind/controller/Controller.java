package  org.mastermind.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mastermind.core.Core;
import org.mastermind.model.Model;
import org.mastermind.model.scores.Score;


public class Controller {

	/** Instance du model */
	private Model model;

	/** Mode de jeu */
	private String gameMode;

	/** Instance du core */
	protected Core core = Core.getInstance(this);
	
	/**
	 * Controlleur
	 * @param m
	 * 		Model
	 */
	public Controller(Model m) {
		this.model = m;
	}
	/**
	 * Initialisation d'une nouvelle partie
	 */
	public void newGame() {
		this.model.newGame();
	}

	/**
	 * Retourne le mode de jeu
	 * @return
	 * 		Mode de jeu
	 */
	public String getGameMode() {
		return this.gameMode;
	}

	/**
	 * Definit le mode de jeu
	 * @param gm
	 * 		Mode de jeu
	 */
	public void setGameMode(String gm) {
		this.gameMode = gm;
		this.model.initGameMode(gameMode);
	}


	/**
	 * Traite et met en forme la saisie du joueur avant de l'envoyer au model
	 * Renvoie true si la saisie est coherente avec le model attendu
	 * @param i
	 * 	Chaine de caractère
	 * 	
	 * @return
	 */
	public boolean setInput(String i) {
		List<Object> tempInput = new ArrayList<Object>();
		i = i.trim();
		if( !i.equals("") ) {
			for (String v : i.split(" ")) {
				// Definit si v est un entier ou non et le rentre;
				tempInput.add( ( isInteger(v) )?Integer.valueOf(v):v );
			}
			
			return setInput(tempInput);
		}else {
			return false;
		}
	}

	/**
	 * Traite et met en forme la saisie du joueur avant de l'envoyer au model
	 * Renvoie true si la saisie est coherente avec le model attendu
	 * @param i
	 * 	Chaine de caractère
	 * 	
	 * @return
	 */
	public boolean setInput(List<Object> input) {
		int acceptedChar = 0;
		int combinationLenght = core.config.getInt("combinationLenght");
		
		for(Object o : input) {
			if(this.model.acceptedChar(o))
				acceptedChar++;
		}
		
		
		if(input.size() == combinationLenght && acceptedChar == combinationLenght ) {
			this.model.Comparaison(input);
			return true;
		}else {
			return false;
		}
	}	


	/**
	 * Definit le nom du joueur et le met en form pour les scores
	 *
	 * @param p
	 * 	Le nom du joueur
	 */
	public void setPlayerName(String p) {
		//formatage en majuscule
		p = p.trim().toUpperCase();
		//passage au model
		this.model.setPlayerName(p);
		//appel de l'observer pour mettre à jour la vue
		this.model.getScoresListObserver();
		
	}
	
	public void setPoints() {
		this.model.setPoints();
	}
	
	/**
	 * Test si le texte passé est un entier
	 * @param s
	 * 		Le string à tester
	 * @return
	 * 		true si c'est un entier
	 */
	private boolean isInteger(String s) {
		  boolean isInteger = false;
		  try {
		   Integer.parseInt(s);
		   // s est un entier
		   isInteger = true;
		  } catch (NumberFormatException e) {
		   // sinon, levée d'éxception non gérée
		  }
		  return isInteger;
	}



}