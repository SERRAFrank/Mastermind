package  org.mastermind.view.consoleinterface;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.view.AbstractInterface;

public class ConsoleInterface extends AbstractInterface{

	/** Lecture des inputes */
	protected Scanner scanner = new Scanner(System.in);


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

		printMultiline("helloWorld");
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

		System.out.println(Core.lang.get("hello") + " " + this.playerName );
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

		String title ;
		List<String[]> options;
		gameType = "nbr";

		title = Core.lang.get("selectGameMode.Title");
		options = menuList("selectGameMode.Options");

		String[] returnOption = {Core.lang.get("return.key"), Core.lang.get("return.desc")  };
		options.add(returnOption);

		ConsoleMenu gameModeMenu = new ConsoleMenu(title, options);
		gameMode = gameModeMenu.showMenu();

		if(gameMode.equals(Core.lang.get("return.key"))) { 
			principalMenu();
		}else {
			setGameType();
		}

	}

	protected void setGameType() {
		if(Core.config.containsKey(gameType+".moreLess") ) {
			moreLess = Core.config.getBoolean(gameType+".moreLess") ;
		}else {
			String[][] o = { {"true", Core.lang.get("combType.moreLessTrue")  } ,
					{"false", Core.lang.get("combType.moreLessFalse")  },
					{Core.lang.get("return.key"), Core.lang.get("return.desc")  } };
			ConsoleMenu moreLessMenu = new ConsoleMenu(Core.lang.get("selectCombType"), new ArrayList<>(Arrays.asList(o)));

			String r = moreLessMenu.showMenu();
			
			if(r.equals(Core.lang.get("return.key"))) { 
				setGameMode();
			}else {
				moreLess = (r.equals("true")? true : false ) ;
				setUniqueValue();
			}
		


		}
	}


	protected void setUniqueValue() {
		if(Core.config.containsKey(gameType+".uniqueValue")) {
			moreLess = Core.config.getBoolean(gameType+".uniqueValue") ;
		}else if(!moreLess){
			uniqueValue = true;
		}else {
			uniqueValue = yesNo(Core.lang.get("setUniqueValue") );
		}

 
			controller.setGameMode(gameMode, gameType, moreLess, uniqueValue);
	}

	/**
	 * Demarrage d'un nouveau jeu
	 */
	@Override
	protected void newGame() {
		ConsoleMenu.showTitle(Core.lang.get("gameMode." + controller.getGameMode()));
	}

	/**
	 * Demarrage d'un nouveau matche
	 */
	@Override
	protected void newRound() {
		System.out.println();
		System.out.println(">  ROUND " + currentRound + "  <") ;

		currentRound++;	
		this.controller.newGame();
	}

	/**
	 * Menu principal
	 */
	protected void principalMenu() {	
		String t = Core.lang.get("menu.Title");
		List<String[]> o = menuList("menu.Options");

		ConsoleMenu menu = new ConsoleMenu(t, o);

		String choice = menu.showMenu();

		clearScreen();


		switch(choice) {
		case "rules":
			rulesView();
			break;
		case "scores" :
			scoresView();
			break;			
		case "aboutUs" :
			aboutUsView();
			break;
		case "exit" :		
			System.exit(0);
			break;
		case "game":
		default :
			initGame();

		}
	}


	protected void getScores() {
		System.out.println(Core.lang.get("getScores"));

		System.out.println( printScores( Core.score.getScores() ) );

		pressEnterToContinue();
	}


	private String printScores(int[] o ) {
		int w = o[0];
		int l = o[1];
		double p = 0.;
		try{
			p =   (w / ((double)w + (double)l)) * 100;
		}catch(Exception e) {}

		p = Math.round(p * Math.pow(10,2)) / Math.pow(10,2);
		return "  " + w + " " +  Core.lang.get("victory") + " / " +  l + " " + Core.lang.get("defeat") +  " ( " + p + "% )";
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

		for(Entry<String, int[]> list : Core.score.getScoresList().entrySet()) {
			String playerName = list.getKey();
			int[] value = list.getValue();
			System.out.println( playerName + " : " +  printScores(value) );
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
		printMultiline("rules");
		pressEnterToContinue();
		principalMenu();

	}

	private void pressEnterToContinue() { 
		System.out.println();
		System.out.println();
		System.out.println(Core.lang.get("pressEnterToContinue"));

		try{
			System.in.read();
		}catch(Exception e) {} 
		clearScreen();	
	}

	private boolean yesNo(String msg) { 
		boolean value;
		while(true) {

			System.out.println(msg + " " + Core.lang.get("yesno"));
			char choice = scanner.next().toLowerCase().charAt(0);

			if(choice == 'y' || choice == 'o' ) {
				value = true;
				break;
			} else if(choice == 'n' ) {
				value = false;
				break;
			} else {
				System.out.println(Core.lang.get("notUnderstand"));
			}
		}

		clearScreen();
		return value;

	}	

	private void printMultiline(String k) {
		int i = 0;

		while(Core.lang.keyExist(k + "." + i) ) {
			System.out.println(Core.lang.get(k + "." + i));
			i++;
		}

	}

	private void newRoundQuestion(String msg) {
		boolean newRound = yesNo(msg);
		if (newRound) {
			newRound();
		}else {
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
		}catch(Exception e) {
		}
	}


	/** Pattern Observer */

	/**
	 * Gestion des sorties
	 * @param s 
	 * @param o
	 * 		Message à afficher
	 */	
	public void updateOutputCompar(List<Object> compar) {

		if(moreLess) {
			for(Object str : compar)
				System.out.print(str + " ");
			System.out.print(System.getProperty("line.separator"));
		}else {
			int v = 0;
			int o = 0;
			for(Object obj : compar) {
				if(obj == comparValues.get(1))
					v++;
				else if(obj == comparValues.get(2))
					o++;
			}

			System.out.println( o + " present(s) et " + v + " bien placé(s)" );		
		}
	}

	public void updateOutputPropos(List<Object> o) {
		for(Object str : o)
			System.out.print(str + " ");
		System.out.print(System.getProperty("line.separator"));
	}

	public void updateRound(int o) {
		currentRound = o;

		System.out.println(Core.lang.get("nbrRound") + " " + currentRound + " / " + this.maxTurn);

	}




	public void updateInitGame(String s, List<Object> l, List<Object> r,  boolean u, String gt) {
		this.acceptedValues = l;
		this.uniqueValue = u;
		this.comparValues = r;
		this.gameType = gt;
		System.out.println();
		System.out.println(Core.lang.get("acceptedValues") + acceptedValues.toString());
		System.out.println(Core.lang.get("combLenght"));
		System.out.println(Core.lang.get("uniqueValue" + (uniqueValue?"Yes":"No")));
		System.out.println();
	}

	/**
	 * Gestion des inputs
	 * @param o
	 * 		Message à afficher
	 */
	public void updateInput(String phase) {
		String msg = "";
		switch(phase) {
		case "propos" :
			msg = Core.lang.get("input.proposCombination");
			break;
		case "compar":
			msg = Core.lang.get("input.setComparaison");
			break;
		}


		String str = "";
		boolean r = false;
		System.out.println(msg);
		while(!r) {
			str = scanner.nextLine();
			if(str.length() > 0) {
				r = controller.setInput(phase, str);
				if(!r)
					System.out.println(Core.lang.get("input.error"));
			}
		}
	}

	/**
	 * Fin de jeu
	 * @param t
	 * 		Message à afficher
	 * @param w
	 * 		Presence d'un gagnant
	 */

	public void updateEndGame(String t, boolean w) {	
		System.out.println(Core.lang.get(t));
		System.out.println(Core.lang.get("getScores"));
		System.out.println( printScores( Core.score.getScores() ) );

		if(w) {
			newRoundQuestion(Core.lang.get(gameMode + ".newRound"));
		} else {
			newRoundQuestion(Core.lang.get(gameMode + ".exaequo"));
		}

	}



}
