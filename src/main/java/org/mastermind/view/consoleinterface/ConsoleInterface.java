package  org.mastermind.view.consoleinterface;


import java.util.Map.Entry;
import java.util.Scanner;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.view.AbstractInterface;

public class ConsoleInterface extends AbstractInterface{

	/** Instance du core */
	protected Core core = Core.getInstance(this);

	protected Scanner scanner = new Scanner(System.in);

	/** Constructeur */
	public ConsoleInterface(Controller controller) {
		super(controller);
	}



	@Override
	protected void initView() {

	}



	/* 
	 * Affiche le message d'accueil au lancement de l'application
	 */
	@Override
	protected void helloWorld() {

		printMultiline("helloWorld");
		pressEnterToContinue();
		clearScreen();

	}



	@Override
	protected void setPlayer() {
		System.out.println(core.lang.get("setPlayerName"));

		scanner.nextLine();	
		String p = scanner.nextLine();

		controller.setPlayerName(p);

		System.out.println(core.lang.get("hello") + " " + playerName);
		getScores();		
	}



	@Override
	protected void initInterface() {
		setPlayer();
		clearScreen();
		principalMenu();

	}



	@Override
	protected void setGameMode() {

		ConsoleMenu gameModeMode = new ConsoleMenu("selectGameMode");
		gameMode = gameModeMode.showMenu();
		if(gameMode.equals("return"))
			principalMenu();
		else
			controller.setGameMode(gameMode);

	}


	@Override
	protected void gameLoop() {
		ConsoleMenu.showTitle(core.lang.get("gameMode." + controller.getGameMode()));

		while( (turn <= gameTurns) && (win == false) ) {
			System.out.println(core.lang.get("nbrTurn") + turn + "/" + gameTurns);
			if(firstPlayer == "player") {
				input();
				output();
			}else {
				output();
				input();
			}
			turn++;
		}

		endGame();

	}



	@Override
	protected void input() {
		String str = "";
		boolean r = false;
		System.out.println(core.lang.get("input." + firstPlayer));
		while(!r) {
			str = scanner.nextLine();
			if(str.length() > 0) {
				r = controller.setInput(str);
				if(!r)
					System.out.println(core.lang.get("input.error"));
			}
		}
	}

	@Override
	protected void output() {
		for(Object s : output)
			System.out.print(s + " ");

		System.out.print(System.getProperty("line.separator"));
	}


	protected void principalMenu() {
		ConsoleMenu menu = new ConsoleMenu("menu");
		String choice = menu.showMenu();

		clearScreen();


		switch(choice) {
		case "rules":
			rules();
			break;
		case "scores" :
			scores();
			break;			
		case "credits" :
			credits();
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
		System.out.println(core.lang.get("getScores"));

		System.out.println( printScores(this.playerScore[0], this.playerScore[1] ) );

		pressEnterToContinue();
	}


	private String printScores(int w, int l ) {
		double p = 0.;
		try{
			p =   ((double)w / ((double)w + (double)l)) * 100;
		}catch(Exception e) {
		}

		p = Math.round(p * Math.pow(10,2)) / Math.pow(10,2);


		return "  " + w + " " +  core.lang.get("victory") + " / " +  l + " " + core.lang.get("defeat") +  " ( " + p + "% )";
	}


	@Override
	protected void scores() {

		for(Entry<String, int[]> list : scoresList.entrySet()) {
			String playerName = list.getKey();
			int[] value = list.getValue();
			System.out.println( playerName + " : " +  printScores(value[0], value[1] ) );
		}   
		pressEnterToContinue();
		principalMenu();
	}


	@Override
	protected void credits() {
		printMultiline("credits");
		pressEnterToContinue();
		principalMenu();

	}


	@Override
	protected void rules() {
		printMultiline("rules");
		pressEnterToContinue();
		principalMenu();

	}

	@Override
	protected void endGame() {
		String idMsg;
		if(gameMode.equals("dual"))
			idMsg = gameMode + "." + firstPlayer  + ((win)?".win":".lost");	
		else
			idMsg = gameMode + "." + ((win)?"win":"lost");


		controller.setPoints();
		System.out.println(core.lang.get( idMsg) );

		System.out.println(core.lang.get("getScores"));
		System.out.println( printScores(this.playerScore[0], this.playerScore[1] ) );

		restartGame(core.lang.get(gameMode + ".restart"));

	}





	private void pressEnterToContinue() { 
		System.out.println();
		System.out.println();
		System.out.println(core.lang.get("pressEnterToContinue"));

		try{
			System.in.read();
		}catch(Exception e) {} 
		clearScreen();	
	}

	private boolean yesNo(String msg) { 
		boolean value;
		while(true) {

			System.out.println(msg + " " + core.lang.get("yesno"));
			char choice = scanner.next().toLowerCase().charAt(0);

			if(choice == 'y' || choice == 'o' ) {
				value = true;
				break;
			} else if(choice == 'n' ) {
				value = false;
				break;
			} else {
				System.out.println(core.lang.get("notUnderstand"));
			}
		}

		clearScreen();
		return value;

	}	

	private void printMultiline(String k) {
		int i = 0;

		while(core.lang.keyExist(k + "." + i) ) {
			System.out.println(core.lang.get(k + "." + i));
			i++;
		}

	}

	private void restartGame() {
		restartGame(core.lang.get("restart"));
	}

	private void restartGame(String msg) {
		boolean restartGame = yesNo(msg);
		if (restartGame) {
			initGameLoop();
			gameLoop();
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

}
