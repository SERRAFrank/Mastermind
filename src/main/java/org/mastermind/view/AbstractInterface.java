package  org.mastermind.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.model.scores.Score;
import org.mastermind.observer.Observer;

public abstract class AbstractInterface extends JFrame implements Observer {


	/** Instance du core */
	protected Core core = Core.getInstance(this);

	/** Instance des scores */
	protected Score score = Score.getInstance();

	/** Controlleur */
	protected Controller controller;

	/** Saisies de l'ordinateur*/
	protected Object output;

	/** Mode de jeu */
	protected String gameMode;

	/** Tour en cours */
	protected int turn = 1;

	/** Partie en cours */
	protected int round = 1;	

	/** Nom du Joueur */
	protected String playerName = "";


	/**
	 * Constructeur
	 * @param c
	 * 		Instance du controlleur
	 */
	public AbstractInterface(Controller c) {
		controller = c;
		// Initialisation de la vue
		initView();

	}

	/**
	 * Initialisation de la vue
	 */
	protected abstract void initView();

	/**
	 * Demarrage de la vue
	 */
	protected void start() {
		helloWorld();
		initInterface();
	}

	/**
	 * BootScreen
	 */
	protected abstract void helloWorld();

	/**
	 * Initialisation de l'interface
	 */
	protected abstract void initInterface();

	/**
	 * Initialisation du jeu avant démarrage
	 */
	protected void initGame() {
		//Remise à 0 des paramettres du model
		this.controller.resetModel();
		turn = 1;
		round = 1;

		if(playerName.equals(""))
			setPlayer();
		// Définition du mode de jeu
		if(playerName.length()>0) {
			setGameMode();
			if(controller.getGameMode() != null) {
				// Nouveau jeu
				newGame();
				// Nouveau round
				newRound();
			}
		}
	}

	/**
	 * Definition du mode de jeu
	 */
	protected abstract void setGameMode();

	/**
	 * Demarrage d'un nouveau jeu
	 */
	protected abstract void newGame();

	/**
	 * Demarrage d'un nouveau matche
	 */
	protected abstract void newRound();

	/**
	 * Definition du joueur
	 */
	protected abstract void setPlayer();

	/**
	 * Vue des regles
	 */
	protected abstract void rulesView();

	/**
	 * Vue des scores
	 */
	protected abstract void scoresView();

	/**
	 * Vue des credits
	 */
	protected abstract void aboutUsView();

	protected List<String[]> menuList(String options){
		List<String[]> menuList = new ArrayList<String[]>();


		for(int i = 0;  core.lang.keyExist(options + "." + i); i++ ) {
			String[] value = {"", ""};
			value[0] = core.lang.get(options + "." + i + ".key", true);
			value[1] = core.lang.get(options + "." + i + ".desc", true);
			menuList.add(value);
		}
		return menuList;
	}

}
