package org.mastermind.model.gamemode;

import java.util.List;
import java.util.Set;

public class DualGameMode extends AbstractGameMode {

	AbstractGameMode dualMode;
	private int alternateMode = 0;

	public DualGameMode() {
		setDualGameMode();
	}


	public void newGame() {
		alternateMode++;

		setDualGameMode() ;
		dualMode.newGame();
		getFirstPlayer();
		getOutput();
		winStatut();
	}

	private void setDualGameMode() {
		if( alternateMode % 2 == 1)
			dualMode = new DefenderGameMode();
		else
			dualMode = new ChallengerGameMode();
		
		acceptedChar = dualMode.getAcceptedChar();

	}

	@Override
	public void Comparaison(List<Object> playerInput) {
		dualMode.Comparaison(playerInput);


	}

	@Override
	public String getFirstPlayer() {
		firstPlayer = dualMode.getFirstPlayer();
		return firstPlayer;
	}

	@Override
	public List<Object> getOutput() {
		output = dualMode.getOutput();
		return output;
	}

	public boolean winStatut() {
		win = dualMode.winStatut();
		return win;
	}

}
