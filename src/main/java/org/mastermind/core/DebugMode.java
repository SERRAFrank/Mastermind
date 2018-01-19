package  org.mastermind.core;

import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class DebugMode {
	/** Instance du logger */
	private static Logger logger = Logger.getLogger(DebugMode.class);

	/** Instance du fichier de configuration */
	private static Config config = Config.getInstance();

	private static boolean consoleMode = false;

	/**
	 * Test si la console est utilisée par le logger
	 */
	private static void testConsole() {
		//liste des Appenders
		Enumeration e = logger.getRootLogger().getAllAppenders();
		boolean loggerConsole = false;
		
		while(e.hasMoreElements()) {
			if(e.nextElement().toString().contains("ConsoleAppender") )
				loggerConsole = true;
		}

		if( (logger.getRootLogger().getLevel().toInt() > Level.DEBUG.toInt()) || loggerConsole == false)
			consoleMode = false;
		else
			consoleMode = true;
	}

	

	/**
	 * Affichage des messages de debug
	 * 
	 * @param debugMsg
	 * 		Message a passer
	 */
	public static void print(String debugMsg) {
		testConsole();
		
		if(config.DEBUG && consoleMode)
			logger.debug(debugMsg);
		else
			System.out.println(debugMsg);
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

	}

}
