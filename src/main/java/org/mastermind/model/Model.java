package  org.mastermind.model;

import java.util.ArrayList;
import java.util.List;

import org.mastermind.core.Core;
import org.mastermind.model.player.AIPlayer;
import org.mastermind.model.player.AbstractPlayer;
import org.mastermind.model.player.HumanPlayer;
import org.mastermind.model.scores.Score;
import org.mastermind.observer.Observable;
import org.mastermind.observer.Observer;

/**
 * Class Model
 * 
 */
public class Model implements Observable {

	/** Instance du core */
	protected Core core = Core.getInstance(this);

	/** List pour le pattern Observer */
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	/** création de 2 joueurs */
	private AbstractPlayer player1;
	private AbstractPlayer player2;

	/** Boolean définissant la fin du jeu */
	private boolean endGame = false;
	
	/** Boolean définissant que des paramettres de victoires ont été remplis */
	private boolean winConditions = false;

	/** Tour de jeu en cours*/
	private int currentTurn = 1;
	
	/** Nombre maximal de touts de jeu */
	private int maxTurn = core.config.getInt("gameTurns");

	/** Manche en cours */
	private int currentRound = 1;

	/** Parametre en cas de pause pour input */
	private String hiddenTo = null;
	
	/** Mode de jeu en cours */
	private String gameMode = null;

	/** Input passé au Model */
	private Object input = null;

	private Score score = Score.getInstance();

	/**
	 * Constructeur
	 */
	public Model() {

	}

	/**
	 * Reset les parametres du Model
	 */
	public void reset() {
		this.endGame = false;
		this.winConditions = false;
		this.currentTurn = 1;
		this.currentRound = 1;
		this.hiddenTo = null;
		this.input = null;
	}

	/**
	 * Initialisation du mode de jeu
	 *
	 * @param gm
	 * 		Le mode de jeu
	 */
	public void initGameMode(String gm) {

		this.currentTurn = 1;
		this.gameMode = gm;

		core.logger.info("Game mode : " + gm);
		
		//Choix du mode de jeu et initialisation des joueurs
		switch (gm) {
			case "challenger" :
				this.player1 = new AIPlayer();
				this.player2 = new HumanPlayer();
				break;
			case "defender" :
				this.player1 = new HumanPlayer();
				this.player2 = new AIPlayer();
				break;
			case "dual" : 
				if(this.currentRound%2 == 1) {
					this.player1 = new AIPlayer();
					this.player2 = new HumanPlayer();			
				}else {
					this.player1 = new HumanPlayer();
					this.player2 = new AIPlayer();		
				}
				break;
			default: 
				//Exception si le mode de jeu est inconnu
				try {
					throw new Exception("GameMode not found. Supported modes are Defender , Challenger & Dual");
				}catch(Exception e) {
					core.error(e); 
				}
		}
	}

	
	/**
	 * Demarrage du jeu
	 */
	public void startGame() {
		// nouveau round
		this.currentRound++;
		
		// Initialisation du mode de jeu en fonction du précédant utilisé
		initGameMode(this.gameMode);
		
		// Le joueur 1 definit sa combinaison secrete
		this.player1.genCombination();
		
		// Pas de fin de jeu
		this.endGame = false;
		
		gameLoop();

	}


	/**
	 * Debut de la boucle de jeu
	 */
	private void gameLoop() {
		
		// Notification du round en cours
		notifyOutput("round", core.lang.get("nbrRound") + " " + this.currentTurn + "/" + this.maxTurn);
		
		// initialisation des joueurs avec les parametres de nouveau tour
		this.player1.newRound();
		this.player2.newRound();

		setPropositionInput();

	}




	/**
	 * Met en pause si le joueur doit saisir une proposition de clef
	 */
	private void setPropositionInput() {
		// Si le joueur peut faire une saisie manuelle
		if(this.player2.pauseToInput()) {
			//Pause en attente d'une proposition du joueur 2
			notifyInput("propos");
		}else {
			setProposition();
		}
	}

	/**
	* Definit la proposition de clef du joueur 2
	*/
	private void setProposition() {
		this.player2.proposCombination();
		this.player1.setProposition(this.player2.getProposition());
		if(!this.player2.pauseToInput())
			notifyOutput( "propos", this.player2.getProposition());
		setComparaisonInput();
	}


	/**
	 * Met en pause si le joueur doit saisir une comparaison
	 */
	private void setComparaisonInput() {
		if(this.player1.pauseToInput()) {
			notifyInput("compar");
		}else {
			setComparaison();
		}
	}

	/**
	* Analyse la comparaison de clef du joueur 1
	*/
	private void setComparaison() {
		this.player1.comparToHiddenCombination();
		this.player2.setComparaison(this.player1.getComparaison());
		if(!this.player1.pauseToInput())
			notifyOutput("compar", this.player1.getComparaison());
		endGame();
	}	

	/**
	 * Fin de manche
	 */
	private void endGame() {
		//Incremente le nombre de tours si la manche arrive à sa fin
		this.currentTurn++;
		
		boolean forceReset = false;

		// identifiant du gagnant
		String winnerID = null;
		
		// présence d'un gagnant
		boolean win = true;
		player2.comparToProposCombination();

		
		// Si le joueur 2 a résolu la clef
		if( this.player2.solvedCombination()){
			//Fin de jeu
			this.endGame = true;
			// Joueur 1 perd
			this.player1.win(false);
			// Joueur 2 gagne
			this.player2.win(true);

			winnerID = this.player2.getID() + ".win";

			forceReset = true;

		// si le nombre de tours max est atteint
		}else if(this.currentTurn > maxTurn) {	
			//Fin de jeu
			this.endGame = true;

			// Si mode Duel
			if(this.gameMode.equals("dual")) {
				
				winnerID = this.player1.getID() + ".lost";	
				win = false;
				
			}else {
				// Joueur 1 gagne
				this.player1.win(true);
				// Joueur 2 perd
				this.player2.win(false);
				winnerID = this.player1.getID() + ".win";
				forceReset = true;

				
			}
		} 

		if(endGame) {
			//Notifie à la vue lesparamettres de fin de partie
			notifyEndGame(winnerID, win);
			
			// reset forcé
			if(forceReset)
				reset();
		}else {
			//Nouveau tour
			gameLoop();
		}
	}


	/**
	 * Reccupere les inputs
	 *
	 * @param i
	 * 		Inputs du joueur
	 */
	public void setInput(String phase, Object i) {
		this.input = i;
		
		//Selectionne l'action a réalisée en fonction du moment de pause
		switch(phase) {
			case "propos":
				player2.setProposition((List<Integer>) this.input);
				setProposition();
				break;
			case "compar":
				player1.setComparaison((List<String>) input);
				setComparaison();
				break;
		}
	}

	/**
	 * Retourne si le caractere o est utilisable dans un input
	 * @param o
	 * 		Caractère (Integer ou String)
	 * @return
	 * 		true si utilisable, sinon false
	 */
	public boolean acceptedChar(Object o) {
		return this.player1.acceptedInputChar(o);
	}
	
	/**
	 * Definit le nom du joueur et le passe au score
	 * @param p
	 * 		Nom du joueur
	 */
	public void setPlayerName(String p) {
		score.setPlayerName(p);
	}	


	/** Implementation du pattern observer */
	public void addObserver(Observer obs) {
		if(!listObserver.contains(obs))
			this.listObserver.add(obs);
	}


	public void removeObserver() {
		this.listObserver = new ArrayList<Observer>();
	}


	public void notifyInput(String s) {
		for(Observer obs : listObserver)
			obs.updateInput(s);

	}

	public void notifyOutput(String s, List<?> o) {
		for(Observer obs : listObserver)
			obs.updateOutput(s, o);

	}
	public void notifyOutput(String s, String o) {
		for(Observer obs : listObserver)
			obs.updateOutput(s, o);
	}

	
	public void notifyEndGame(String e, boolean w) {
		for(Observer obs : listObserver)
			obs.updateEndGame(e, w);
	}


}
