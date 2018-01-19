package  org.mastermind.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.observer.Observer;

public abstract class AbstractInterface implements Observer{ 


	/** Instance du core */
	protected Core core = Core.getInstance(this);


	protected Controller controller;

	/** Saisies de l'ordinateur*/
	protected List<Object> output = new ArrayList<Object>();;

	/** Conditions de victoire réunies */
	protected boolean win;

	/** Mode de jeu */
	protected String gameMode;

	/** Tour en cours */
	protected int turn = 1;

	/** Partie en cours */
	protected int round = 1;	

	/** Nombre de tours de jeu */
	protected int gameTurns = core.config.getInt("gameTurns");

	/** Nombre de paries de jeu */
	protected int gameRounds = core.config.getInt("gameRounds");	

	/** Premier joueur */ 
	protected String firstPlayer;
	
	protected int[] playerScore;

	protected String playerName;


	protected Map<String, int[]> scoresList;
	
	

	public AbstractInterface(Controller c) {
		controller = c;

		initView();

	}

	protected abstract void initView();

	protected abstract void helloWorld();

	protected void start() {
		helloWorld();
		initInterface();

	}



	protected abstract void initInterface();

	protected void initGameLoop() {

		this.output.clear();
		this.win = false;
		this.turn = 1;
		this.controller.newGame();

	}

	protected abstract void setPlayer();
	
	protected abstract void setGameMode();

	protected void initGame() {
		setGameMode();		
		initGameLoop();
		gameLoop();		
	}

	protected abstract void gameLoop();

	protected abstract void rules();

	protected abstract void scores();

	protected abstract void credits();


	protected abstract void input();

	protected abstract void output();


	protected abstract void endGame();

	public void playerInfo(String playerName, int[] scores) {
		this.playerName = playerName;
		this.playerScore = scores;

		
	}

	public void update(List<Object> o, boolean w) {
		this.output = o;
		this.win = w;
	}


	public void showScoresList( Map<String, int[]> scoresList) {
		this.scoresList = scoresList;
	}
	
	
	public void  firstPlayer(String player) {
		this.firstPlayer = player;
	}
		
}
