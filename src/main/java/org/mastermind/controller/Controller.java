package  org.mastermind.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mastermind.core.Core;
import org.mastermind.model.Model;
import org.mastermind.observer.Observer;

/**
 * Class Controller
 * @author frank
 *
 */
public class Controller {

	/** Instance du model */
	private Model model;

	/** Mode de jeu */
	private String gameMode;

	/**
	 * Controlleur
	 * @param m
	 * 		Model
	 */
	public Controller(Model m) {
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
		
		this.model = m;
	}
	/**
	 * Initialisation d'une nouvelle partie
	 */
	public void newGame() {
		this.model.startGame();
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
	 * @param combType 
	 * @param gt 
	 * @param b 
	 */
	public void setGameMode(String gm, String gt, boolean moreLess, boolean b) {
		this.gameMode = gm;
		this.model.setGameMode(gameMode, gt, moreLess, b);
	}


	/**
	 * Traite et met en forme la saisie du joueur en mode console avant de l'envoyer au model
	 * Renvoie true si la saisie est coherente avec le model attendu
	 * @param i
	 * 	Chaine de caractère
	 * 	
	 * @return
	 */
	public boolean setInput(String phase, String i) {
		List<Object> tempInput;
		//retrait des espace en debut et fin de chaine
		i = i.trim();
				
		if( i.length() > 0 ) {
			tempInput = new ArrayList<Object>(Arrays.asList(i.split(" ")));
			return setInput(phase, tempInput);
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
	public boolean setInput(String phase, List<Object> input) {
		int acceptedChar = 0;
		int combinationLenght = Core.config.getInt("combinationLenght");
		// verification si les caractères sont acceptés
		for(Object o : input) {
			if(this.model.acceptedChar(o))
				acceptedChar++;
		}
		
		if(input.size() == combinationLenght && acceptedChar == combinationLenght ) {
			this.model.setInput(phase, input);
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

	/**
	 * Reinitialisation du model pour un nouveau jeu
	 */
	public void resetModel() {
		this.model.reset();
	}
	
	
	public void addObserver(Observer obs) {
		this.model.addObserver(obs);
	}


}