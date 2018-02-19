package org.mastermind.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ImageIcon;

import org.mastermind.core.Core;
import org.mastermind.model.player.AIPlayer;
import org.mastermind.model.player.AbstractPlayer;
import org.mastermind.model.player.HumanPlayer;
import org.mastermind.observer.Observable;
import org.mastermind.observer.Observer;

/**
 * Class Model
 * 
 */
public class Model implements Observable {
	/** List pour le pattern Observer */
	private List<Observer> listObserver = new CopyOnWriteArrayList<Observer>();

	/** création de 2 joueurs */
	private AbstractPlayer player1;
	private AbstractPlayer player2;

	/** Boolean définissant la fin du jeu */
	private boolean endGame = false;

	/** Tour de jeu en cours */
	private int currentTurn = 1;

	/** Nombre maximal de touts de jeu */
	private int maxTurn = Core.config.getInt("game.turns");

	/** Manche en cours */
	private int currentRound = 1;

	/** Mode de jeu en cours */
	private String gameMode = null;
	private String gameType = null;

	/** Input passé au Model */
	private Object input = null;

	private boolean uniqueValue;
	private boolean moreLess;

	private List<Object> acceptedInputList = new ArrayList<Object>();
	private List<Object> acceptedComparChars = new ArrayList<Object>();

	/**
	 * Constructeur
	 */
	public Model() {
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
	}

	/**
	 * Reset les parametres du Model
	 */
	public void reset() {
		this.endGame = false;
		this.currentTurn = 1;
		this.input = null;
	}

	/**
	 * Initialisation du mode de jeu
	 *
	 * @param gm
	 *            Le mode de jeu
	 * @param ml
	 * @param gt
	 * @param b
	 */
	public void setGameMode(String gm, String gt, boolean ml, boolean b) {
		this.gameMode = gm;
		this.gameType = gt;

		if (Core.config.containsKey(gameType + ".uniqueValue")) {
			uniqueValue = Core.config.getBoolean(gameType + ".uniqueValue");
		} else {
			uniqueValue = b;
		}

		if (Core.config.containsKey(gameType + ".moreLess")) {
			moreLess = Core.config.getBoolean(gameType + ".moreLess");
		} else {
			moreLess = ml;
		}

		this.currentRound = 1;
		Core.logger.info("Game mode : " + gameMode);
	}

	public void initGameMode() {

		acceptedInputList.clear();
		acceptedComparChars.clear();

		String[] acceptedInput = Core.config.getArray(gameType + ".acceptedInputValues");
		for(int i= 0; i < Core.config.getInt("game.lenght"); i++ ) {
			String c = acceptedInput[i];
			if (Core.config.get(gameType + ".acceptedInputType").equals("Color")) {
				try {
					Color color = Color.decode(c);
					if (!this.acceptedInputList.contains(color) && color != null)
						this.acceptedInputList.add(color);

				} catch (Exception e) {
				}

			} else {
				this.acceptedInputList.add(c.trim());
			}
		}

		String[] acceptedChar = Core.config.getArray(gameType + ".acceptedComparChars");

		if (Core.config.get(gameType + ".acceptedComparType").equals("Icon")) {
			String imgDir = Core.config.get("imgDir") + "/";
			ImageIcon icon = new ImageIcon(imgDir + acceptedChar[0]);

			this.acceptedComparChars.add(new ImageIcon(imgDir + acceptedChar[0]));
			this.acceptedComparChars.add(new ImageIcon(imgDir + acceptedChar[1]));
			this.acceptedComparChars.add(new ImageIcon(imgDir + acceptedChar[2]));

		} else {
			this.acceptedComparChars = new ArrayList<Object>(Arrays.asList(acceptedChar));

		}

		// Choix du mode de jeu et initialisation des joueurs
		switch (gameMode) {
		case "challenger":
			this.player1 = new AIPlayer(acceptedInputList, acceptedComparChars, moreLess, uniqueValue);
			this.player2 = new HumanPlayer(acceptedInputList, acceptedComparChars, false, false);

			break;
		case "defender":
			this.player1 = new HumanPlayer(acceptedInputList, acceptedComparChars, false, false);
			this.player2 = new AIPlayer(acceptedInputList, acceptedComparChars, moreLess, uniqueValue);

			break;
		case "dual":
			if (this.currentRound % 2 == 1) {
				this.player1 = new AIPlayer(acceptedInputList, acceptedComparChars, moreLess, uniqueValue);
				this.player2 = new HumanPlayer(acceptedInputList, acceptedComparChars, false, false);
			} else {
				this.player1 = new HumanPlayer(acceptedInputList, acceptedComparChars, false, false);
				this.player2 = new AIPlayer(acceptedInputList, acceptedComparChars, moreLess, uniqueValue);
			}
			break;
		default:
			// Exception si le mode de jeu est inconnu
			try {
				throw new Exception("GameMode not found. Supported modes are Defender , Challenger & Dual");
			} catch (Exception e) {
				Core.error(e);
			}
		}

		if (this.player1.pauseToInput()) {
			notifyInitGame("compar", acceptedComparChars, acceptedComparChars, false, moreLess);
		} else if (this.player2.pauseToInput()) {
			notifyInitGame("propos", acceptedInputList, acceptedComparChars, uniqueValue, moreLess);
		}

	}

	/**
	 * Demarrage du jeu
	 */
	public void startGame() {
		// Initialisation du mode de jeu en fonction du précédant utilisé
		initGameMode();
		this.currentTurn = 1;
		this.currentRound++;

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
		notifyRound(this.currentTurn);

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
		if (this.player2.pauseToInput()) {
			// Pause en attente d'une proposition du joueur 2
			notifyInput("propos");
		} else {
			setProposition();
		}
	}

	/**
	 * Definit la proposition de clef du joueur 2
	 */
	private void setProposition() {
		this.player2.proposCombination();
		this.player1.setProposition(this.player2.getProposition());

		if (!this.player2.pauseToInput()) {
			notifyOutputPropos(this.player2.getProposition());
		}
		setComparaisonInput();
	}

	/**
	 * Met en pause si le joueur doit saisir une comparaison
	 */
	private void setComparaisonInput() {
		if (this.player1.pauseToInput()) {
			notifyInput("compar");
		} else {
			setComparaison();
		}
	}

	/**
	 * Analyse la comparaison de clef du joueur 1
	 */
	private void setComparaison() {
		this.player1.comparToHiddenCombination();
		this.player2.setComparaison(this.player1.getComparaison());
		if (!this.player1.pauseToInput())
			notifyOutputCompar(this.player1.getComparaison());
		endGame();
	}

	/**
	 * Fin de manche
	 */
	private void endGame() {
		// Incremente le nombre de tours si la manche arrive à sa fin
		this.currentTurn++;

		// identifiant du gagnant
		String winnerID = null;

		// présence d'un gagnant
		boolean win = true;
		player2.comparToProposCombination();

		// Si le joueur 2 a résolu la clef
		if (this.player1.solvedCombination() || this.player2.solvedCombination()) {
			// Fin de jeu
			this.endGame = true;
			// Joueur 1 perd
			this.player1.win(false);
			// Joueur 2 gagne
			this.player2.win(true);

			winnerID = this.player2.getID() + ".win";

			// si le nombre de tours max est atteint
		} else if (this.currentTurn > maxTurn) {
			// Fin de jeu
			this.endGame = true;

			// Si mode Duel
			if (this.gameMode.equals("dual")) {

				winnerID = this.player2.getID() + ".lost";
				win = false;

			} else {
				// Joueur 1 gagne
				this.player1.win(true);
				// Joueur 2 perd
				this.player2.win(false);
				winnerID = this.player1.getID() + ".win";
			}
		}

		if (endGame) {
			// Notifie à la vue lesparamettres de fin de partie
			notifyEndGame(winnerID, win);
			reset();
		} else {
			// Nouveau tour
			gameLoop();
		}
	}

	/**
	 * Reccupere les inputs
	 *
	 * @param i
	 *            Inputs du joueur
	 */
	public void setInput(String phase, Object i) {
		this.input = i;

		// Selectionne l'action a réalisée en fonction du moment de pause
		switch (phase) {
		case "propos":
			player2.setProposition((List<Object>) this.input);
			setProposition();
			break;
		case "compar":
			player1.setComparaison((List<Object>) input);
			setComparaison();
			break;
		}
	}

	/**
	 * Retourne si le caractere o est utilisable dans un input
	 * 
	 * @param o
	 *            Caractère (Integer ou String)
	 * @return true si utilisable, sinon false
	 */
	public boolean acceptedChar(Object o) {
		return this.player1.acceptedInputChar(o);
	}

	/**
	 * Definit le nom du joueur et le passe au score
	 * 
	 * @param p
	 *            Nom du joueur
	 */
	public void setPlayerName(String p) {
		Core.score.setPlayerName(p);
	}

	/** Implementation du pattern observer */

	@Override
	public void addObserver(Observer obs) {
		if (!listObserver.contains(obs))
			this.listObserver.add(obs);
	}

	@Override
	public void removeObserver() {
		this.listObserver = new ArrayList<Observer>();
	}

	@Override
	public void notifyInput(String s) {
		for (Observer obs : listObserver)
			obs.updateInput(s);

	}

	@Override
	public void notifyInitGame(String s, List<Object> l, List<Object> r, boolean u, boolean ml) {

		for (Observer obs : listObserver)
			obs.updateInitGame(s, l, r, u, ml);

	}

	@Override
	public void notifyOutputCompar(List<Object> o) {
		for (Observer obs : listObserver)
			obs.updateOutputCompar(o);

	}

	@Override
	public void notifyOutputPropos(List<Object> o) {
		for (Observer obs : listObserver)
			obs.updateOutputPropos(o);

	}

	@Override
	public void notifyRound(int o) {
		for (Observer obs : listObserver)
			obs.updateRound(o);

	}

	@Override
	public void notifyEndGame(String e, boolean w) {
		for (Observer obs : listObserver)
			obs.updateEndGame(e, w);
	}

}
