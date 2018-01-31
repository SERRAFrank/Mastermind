package  org.mastermind.core;

import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class DebugMode {
	/** Instance du logger */
	private static Logger logger = Logger.getLogger(DebugMode.class);


	/** mode Debug */
	private static boolean debugMode = false;

	/** Vrai si le logger utilise la console */
	private static boolean consoleMode = false;

	/**
	 * Test si la console est utilisée par le logger
	 */
	private static void testConsole() {
		//liste des Appenders
		Enumeration<?> e = Logger.getRootLogger().getAllAppenders();
		boolean loggerConsole = false;

		//Revoir true si ConsoleAppender est utilisé
		while(e.hasMoreElements()) {
			if(e.nextElement().toString().contains("ConsoleAppender") )
				loggerConsole = true;
		}

		// Si le niveau du logger est supérieur au mode DEBUG  ou que ConsoleAppender n'estpas utilisé
		if( (Logger.getRootLogger().getLevel().toInt() > Level.DEBUG.toInt()) || loggerConsole == false)
			consoleMode = false;
		else
			consoleMode = true;
	}


	/**
	 * Definit si le mode debug est actif.
	 *
	 * @param d
	 * 		true pour activer le mode DEBUG
	 */

	public static void setDebugMode(boolean d) {
		debugMode = d;
	}
	
	
	/**
	 * Affichage des messages de debug
	 * 
	 * @param debugMsg
	 * 		Message a passer
	 */
	public static void print(String debugMsg) {
		testConsole();
		if(debugMode) {
			if(consoleMode)
				logger.debug(debugMsg);
			else
				System.out.println(debugMsg);
		}
	}

	/**
	 * Affichage des messages d'erreurs par levee d'exception
	 *
	 * @param errorMsg
	 * 		Exception levee
	 */
	public static void error(Throwable errorMsg) {
		testConsole();

		if(consoleMode)
			logger.fatal( "FATAL ERROR!", errorMsg );			
		else
			errorMsg.printStackTrace();

		System.exit(3);
	}

}
