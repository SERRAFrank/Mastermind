package org.mastermind.view.consoleinterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.convert.ListDelimiterHandler;
import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.view.AbstractInterface;

public class ConsoleInterface extends AbstractInterface {

	/** Lecture des inputes */
	protected Scanner scanner = new Scanner(System.in);
	private Object editable;

	/** Constructeur */
	public ConsoleInterface(Controller controller) {
		super(controller);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

	}

	/**
	 * Affiche le message d'accueil au lancement de l'application
	 */
	@Override
	protected void helloWorld() {

		printMultiline("console.helloWorld");
		pressEnterToContinue();
		clearScreen();

	}

	/**
	 * Definition du nom du joueur
	 */
	@Override
	protected void setPlayer() {
		System.out.println(Core.lang.get("setPlayerName"));

		String p = scanner.nextLine();

		controller.setPlayerName(p);
		this.playerName = Core.score.getPlayerName();

		System.out.println(Core.lang.get("hello") + " " + this.playerName);
		getScores();
	}

	/**
	 * Initialisation de l'interface
	 */
	@Override
	protected void initInterface() {
		// nettoyage de la console
		clearScreen();
		// affichage du menu principal
		principalMenu();
	}

	/**
	 * Definition du mode de jeu
	 */
	@Override
	protected void setGameMode() {

		String title;
		Map<String, String> options;
		gameType = "numbers";
		ConsoleMenu gameModeMenu = new ConsoleMenu();

		gameModeMenu.setTitle(Core.lang.get("selectGameModeTitle"));
		for(String gm : Core.config.getArray("game.mode") ) {
			gameModeMenu.addOption(gm, Core.lang.get("selectGameMode." + gm));
		}

		gameModeMenu.addOption("return", Core.lang.get("optionMenu.return")) ;

		gameMode = gameModeMenu.showMenu();

		if (gameMode.equals("return")) {
			principalMenu();
		} else {
			setGameType();
		}

	}

	protected void setGameType() {
		String reponse = "";
		if (Core.config.containsKey(gameType + ".moreLess")) {
			moreLess = Core.config.getBoolean(gameType + ".moreLess");
		} else {
			ConsoleMenu moreLessMenu = new ConsoleMenu();
			moreLessMenu.setTitle(Core.lang.get("moreLessMenuTitle"));
			for(Object optionLine : Core.lang.getList("moreLessMenuOptions")) 
				moreLessMenu.addOption(optionLine.toString());
			
			moreLessMenu.addOption("return", Core.lang.get("optionMenu.return"));

			reponse = moreLessMenu.showMenu();
		}
		if (reponse.equals(Core.lang.get("return"))) {
			setGameMode();
		} else {
			moreLess = (reponse.equals("true") ? true : false);
			setUniqueValue();
		}
	}

	protected void setUniqueValue() {
		if (Core.config.containsKey(gameType + ".uniqueValue")) {
			uniqueValue = Core.config.getBoolean(gameType + ".uniqueValue");
		} else if (!moreLess) {
			uniqueValue = true;
		} else {
			uniqueValue = yesNo(Core.lang.get("newGameSetting.setUniqueValue"));
		}

		controller.setGameMode(gameMode, gameType, moreLess, uniqueValue);
	}

	/**
	 * Demarrage d'un nouveau jeu
	 */
	@Override
	protected void newGame() {
		ConsoleMenu.showTitle(Core.lang.get("text.gameMode." + controller.getGameMode()));
	}

	/**
	 * Demarrage d'un nouveau matche
	 */
	@Override
	protected void newRound() {
		System.out.println();
		System.out.println(">  ROUND " + currentRound + "  <");

		currentRound++;
		this.controller.newGame();
	}

	/**
	 * Menu principal
	 */
	protected void principalMenu() {
		ConsoleMenu principalMenu = new ConsoleMenu();

		principalMenu.setTitle(Core.lang.get("console.principalMenuOptionsTitle"));

		for(Object optionLine : Core.lang.getList("console.principalMenuOptions")) 
			principalMenu.addOption(optionLine.toString());

		String choice = principalMenu.showMenu();

		clearScreen();

		switch (choice) {
		case "text.rules":
			rulesView();
			break;
		case "scores":
			scoresView();
			break;
		case "text.aboutUs":
			aboutUsView();
			break;
		case "exit":
			System.exit(0);
			break;
		case "game":
		default:
			initGame();

		}
	}

	protected void getScores() {
		System.out.println(Core.lang.get("getScores"));

		System.out.println(printScores(Core.score.getScores()));

		pressEnterToContinue();
	}

	private String printScores(int[] o) {
		int w = o[0];
		int l = o[1];
		double p = 0.;
		try {
			p = (w / ((double) w + (double) l)) * 100;
		} catch (Exception e) {
		}

		p = Math.round(p * Math.pow(10, 2)) / Math.pow(10, 2);
		return "  " + w + " " + Core.lang.get("text.victory") + " / " + l + " " + Core.lang.get("text.defeat") + " ( " + p
				+ "% )";
	}

	/**
	 * Initialisation de la vue
	 */
	@Override
	protected void initView() {

	}

	/**
	 * Vue des scores
	 */
	@Override
	protected void scoresView() {

		for (Entry<String, int[]> list : Core.score.getScoresList().entrySet()) {
			String playerName = list.getKey();
			int[] value = list.getValue();
			System.out.println(playerName + " : " + printScores(value));
		}
		pressEnterToContinue();
		principalMenu();
	}

	/**
	 * Vue des credits
	 */
	@Override
	protected void aboutUsView() {
		printMultiline("credits");
		pressEnterToContinue();
		principalMenu();

	}

	/**
	 * Vue des regles
	 */
	@Override
	protected void rulesView() {
		printMultiline("text.rules");
		pressEnterToContinue();
		principalMenu();

	}

	private void pressEnterToContinue() {
		System.out.println();
		System.out.println();
		System.out.println(Core.lang.get("text.pressEnterToContinue"));

		try {
			System.in.read();
		} catch (Exception e) {
		}
		clearScreen();
	}

	private boolean yesNo(String msg) {
		boolean value;
		while (true) {

			System.out.println(msg + " " + Core.lang.get("text.yesNo"));
			char choice = scanner.next().toLowerCase().charAt(0);

			if (choice == 'y' || choice == 'o') {
				value = true;
				break;
			} else if (choice == 'n') {
				value = false;
				break;
			} else {
				System.out.println(Core.lang.get("text.notUnderstand"));
			}
		}

		clearScreen();
		return value;

	}

	private void printMultiline(String k) {
		for(String l : Core.lang.getArray(k))
			System.out.println(l);

	}

	private void newRoundQuestion(String msg) {
		boolean newRound = yesNo(msg);
		if (newRound) {
			newRound();
		} else {
			principalMenu();
		}
	}

	private void clearScreen() {
		try {
			String os = System.getProperty("os.name");
			if (os.contains("Windows"))
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			else
				Runtime.getRuntime().exec("clear");
		} catch (Exception e) {
		}
	}

	/** Pattern Observer */

	/**
	 * Gestion des sorties
	 * 
	 * @param s
	 * @param o
	 *            Message à afficher
	 */
	@Override
	public void updateOutputCompar(List<Object> compar) {

		if (moreLess) {
			for (Object str : compar)
				System.out.print(str + " ");
			System.out.print(System.getProperty("line.separator"));
		} else {
			int v = 0;
			int o = 0;
			for (Object obj : compar) {
				if (obj == comparValues.get(1))
					v++;
				else if (obj == comparValues.get(2))
					o++;
			}

			System.out.println(o + " present(s) et " + v + " bien placé(s)");
		}
	}

	@Override
	public void updateOutputPropos(List<Object> o) {
		for (Object str : o)
			System.out.print(str + " ");
		System.out.print(System.getProperty("line.separator"));
	}

	@Override
	public void updateRound(int o) {
		currentRound = o;

		System.out.println(Core.lang.get("text.nbrRound") + " " + currentRound + " / " + this.maxTurn);

	}

	@Override
	public void updateInitGame(String s, List<Object> l, List<Object> r, boolean u, boolean ml) {
		this.editable = s;
		this.acceptedValues = l;
		this.uniqueValue = u;
		this.comparValues = r;
		this.moreLess = ml;
		System.out.println();
		System.out.println(Core.lang.get("newGameSetting.acceptedValues") + acceptedValues.toString());
		System.out.println(Core.lang.get("text.combinasionLenght"));
		System.out.println(Core.lang.get("newGameSetting.uniqueValue." + (uniqueValue ? "Yes" : "No")));
		System.out.println();
	}

	/**
	 * Gestion des inputs
	 * 
	 * @param o
	 *            Message à afficher
	 */
	@Override
	public void updateInput(String phase) {
		String msg = "";
		switch (phase) {
		case "propos":
			msg = Core.lang.get("input.proposCombination");
			break;
		case "compar":
			msg = Core.lang.get("input.setComparaison");
			break;
		}

		String str = "";
		boolean r = false;
		System.out.println(msg);
		while (!r) {
			str = scanner.nextLine();
			if (str.length() > 0) {
				List<String> returnedField = new ArrayList<String>(Arrays.asList(str.trim().split(" ")));
				boolean testUniqueValue = true;
				
				if(editable.equals("propos") && uniqueValue) {
				//création d'un set pour avoir des valeurs uniques 
					Set<Object> uniqueInput = new HashSet<Object>(returnedField);
					testUniqueValue = ( uniqueInput.size() == returnedField.size());
				}			
				
				
				if(testUniqueValue) 
					r = controller.setInput(phase, str);
				
				if (!r)
					System.out.println(Core.lang.get("input.error"));
			}
		}
	}

	/**
	 * Fin de jeu
	 * 
	 * @param t
	 *            Message à afficher
	 * @param w
	 *            Presence d'un gagnant
	 */

	@Override
	public void updateEndGame(String t, boolean w) {
		System.out.println(Core.lang.get(t));
		System.out.println(Core.lang.get("getScores"));
		System.out.println(printScores(Core.score.getScores()));

		if (w) {
			newRoundQuestion(Core.lang.get("newRound." + controller.getGameMode()));
		} else {
			newRoundQuestion(Core.lang.get("exaequo"));
		}

	}

}
