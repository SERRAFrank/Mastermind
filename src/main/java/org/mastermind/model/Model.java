package  org.mastermind.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mastermind.core.Core;
import org.mastermind.core.DebugMode;
import org.mastermind.model.gamemode.AbstractGameMode;
import org.mastermind.model.gamemode.ChallengerGameMode;
import org.mastermind.model.gamemode.DefenderGameMode;
import org.mastermind.model.gamemode.DualGameMode;
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

	protected AbstractGameMode gameMode;
	
	protected Score scores = new Score(core.config.get("scoreFile"));

	/**
	 * Constructeur
	 */
	public Model() {
		
	}

	
	public void initGameMode(String gm) {
		//Initialisation du mode de jeu
		core.logger.info("Game mode : " + gm);
		switch (gm) {
			case "defender" :
				this.gameMode = new DefenderGameMode();
				break;
			case "challenger" : 
				this.gameMode = new ChallengerGameMode(); 
				break;
			case "dual" : 
				this.gameMode = new DualGameMode(); 
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
	
	public void newGame() {
		try {
			this.gameMode.newGame();
			getFirstPlayerObserver();
			notifyObserver();
		} catch (Exception e) {
			core.error(e);
		}
		
	}


	public void Comparaison(List<Object> playInput){
		this.gameMode.Comparaison(playInput);
		notifyObserver();
	}
	
		
	/** Implémentation du pattern observer */
	public void addObserver(Observer obs) {
		this.listObserver.add(obs);
	}

	public void notifyObserver() {
		for(Observer obs : listObserver)
			obs.update(this.gameMode.getOutput(), this.gameMode.winStatut());
	}

	public void removeObserver() {
		this.listObserver = new ArrayList<Observer>();
	}


	public void getPlayerObserver(){
		for(Observer obs : this.listObserver)
			obs.playerInfo(this.scores.getPlayerName(), this.scores.getScores());		
	}

	public void getScoresListObserver(){
		for(Observer obs : this.listObserver)
			obs.showScoresList(this.scores.getScoresList());		
	}

	public void getFirstPlayerObserver(){
		for(Observer obs : this.listObserver)
			obs.firstPlayer(this.gameMode.getFirstPlayer());		
	}
	
	public void setPlayerName(String p) {
		this.scores.setPlayerName(p);
		getPlayerObserver();
	}


	public void setPoints() {
		boolean b;
		if(this.gameMode.getFirstPlayer().equals("player"))
			b = this.gameMode.winStatut();
		else
			b = !this.gameMode.winStatut(); 
		this.scores.addPoints( b );
	}


	public boolean acceptedChar(Object o) {
		return this.gameMode.acceptedChar(o);
	}


}
