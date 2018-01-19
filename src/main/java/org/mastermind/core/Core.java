package  org.mastermind.core;

import java.util.Enumeration;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Core {
	/** Instance du logger */
	public static Logger logger;
	
	/** Instance du fichier de configuration */
	public static Config config = Config.getInstance();
	
	/** Instance du systeme de langue */
	public static Lang lang = Lang.getInstance();
	
	
	/** Instance unique non préinitialisée */
	private static Core INSTANCE = null;
	
	/** Constructeur privé */
	private Core(){

	}
	
	/** Point d'accès pour l'instance unique du singleton */
	public static synchronized Core getInstance(Object ob)
	{           
		logger = Logger.getLogger(ob.getClass());
		if (INSTANCE == null){
			INSTANCE = new Core(); 
		}
		return INSTANCE;
	}
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
	public static void debug(Object debugMsg) {
		DebugMode.print(debugMsg.toString());
	}

	/**
	 * Affichage des messages d'erreurs par levee d'exception
	 *
	 * @param errorMsg
	 * 		Exception levee
	 */
	public static void error(Throwable errorMsg) {
		DebugMode.error(errorMsg);

	}


}
