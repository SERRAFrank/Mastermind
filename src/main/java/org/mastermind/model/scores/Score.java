package org.mastermind.model.scores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Score extends ScoreSerializer{


	private String playerName;
	private int winPts;
	private int loosePts;
	private int[] scores = {0, 0};

	public Score(String sf){
		super(sf);
		
	}
	

	public void addPoints(boolean winner){
		if(winner)
			this.winPts++;
		else
			this.loosePts++;
		
		updateScores();
	}

	public String getPlayerName(){
		return this.playerName;
	}

	public void setPlayerName(String player){

		this.playerName = (!player.equals("")) ? player : "ANONYMOUS";

		if(this.scoresList.containsKey(player)) {
			this.scores = scoresList.get(player);
		}
			this.winPts =  this.scores[0];
			this.loosePts = this.scores[1];	
			
		updateScores();
		
	}
	
	
	private void updateScores() {
		this.scores[0] = this.winPts;
		this.scores[1] = this.loosePts;
		scoresList.put(this.playerName, this.scores);
		save();
		
	}

	
	public int[] getScores(){
		return this.scores;
	}

	public Map<String, int[]> getScoresList(){
		return this.scoresList;
	}
}
