package org.mastermind;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.model.Model;
import org.mastermind.view.View;



public class App {
	static Model model;
	static Controller controller;
	static View view;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) throws ParseException {


		// Options
		final Options firstOptions = configFirstParameters();
		final Options options = configParameters(firstOptions);

		// On parse l'aide
		final CommandLineParser parser = new DefaultParser();
		final CommandLine firstLine = parser.parse(firstOptions, args, true);
		
		/** Instanciation du Core pour le logger */
		Core.getInstance("App");

		String viewOption = null;
		char minOption = 0;
		char maxOption = 0;
		char lenghtOption = 0;
		boolean debugOption = Core.config.getBoolean("DEBUG");
		boolean saveOption = false;

		
		CommandLine line = null;
		try {
			line = parser.parse(options, args);

			// Vue : graphic / console
			viewOption = line.getOptionValue("view", Core.config.get("view"));
			if(!viewOption.equals("graphic") && !viewOption.equals("console") ) {
				try {
					throw new Exception("View not found. Supported modes are console & graphic");
				} catch (Exception e) {
					Core.error(e); 
				}
			}
/*
			// Borne min de la combinaison
			minOption = line.getOptionValue("min", Core.config.get("nbr.acceptedInputMin")).charAt(0) ;
			if (!Character.isDigit(minOption)){
				try {
					throw new Exception("Min is a digit between 0 and 9");
				} catch (Exception e) {
					Core.error(e); 
				}
			}

			// Borne max de la combinaison
			maxOption = line.getOptionValue("max", Core.config.get("nbr.acceptedInputMax")).charAt(0);
			if (!Character.isDigit(maxOption)){
				try {

					throw new Exception("Max is a digit between 0 and 9");
				} catch (Exception e) {
					Core.error(e); 
				}
			}
*/
			// Longueur de la combinaison
			lenghtOption = line.getOptionValue("lenght", Core.config.get("game.lenght")).charAt(0);
			if (!Character.isDigit(lenghtOption)){
				try {
					throw new Exception("Lenght is a digit between 0 and 9");
				} catch (Exception e) {
					Core.error(e); 
				}
			}
			
			// debug mode
			debugOption = line.hasOption("debug");

			//Sauvegarde des arguments
			saveOption = line.hasOption("save");
			
			// Si mode aide
			boolean helpMode = firstLine.hasOption("help");
			if (helpMode) {
			    final HelpFormatter formatter = new HelpFormatter();
			    formatter.printHelp("MasterMind", options, true);
			    System.exit(0);
			}


		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// écrasement des données pas des arguments
		Core.config.set("view", viewOption);
		//Core.config.set("combinationNumbersMin", String.valueOf(minOption) );
		//Core.config.set("combinationNumbersMax", String.valueOf(maxOption))	;
		Core.config.set("game.lenght", lenghtOption)	;
		Core.config.set("DEBUG", debugOption) ;		

		// Sauvegarde des données
		if(saveOption)
			Core.config.updateConfigFile();


		// Creation du model
		model = new Model(); 

		// Creation du controller
		Controller controller = new Controller(model);

		//Creation de la vue
		View view = new View (controller);

		//Ajout des observers
		model.addObserver(view.getObserver());

		//Demarrage de la vue
		view.start();

	}


	/**
	 * Options CLI
	 * @param firstOptions
	 * @return
	 */
	private static Options configParameters(final Options firstOptions) {
		
		final Option viewOption = Option.builder("v") 
				.longOpt("view") //
				.desc("View mode : graphic or console") 
				.hasArg(true) 
				.argName("view")
				.required(false) 
				.build();
/*
		final Option minOption = Option.builder("min") 
				.desc("Minimal number") 
				.hasArg(true) 
				.argName("min") 
				.required(false) 
				.build();

		final Option maxOption = Option.builder("max") 
				.desc("Maximal number") 
				.hasArg(true) 
				.argName("max") 
				.required(false) 
				.build();
*/
		final Option lenghtOption = Option.builder("l") 
				.longOpt("lenght") 
				.desc("Combination lenght") 
				.hasArg(true) 
				.argName("lenght")
				.required(false) 
				.build();    

		final Option saveOption = Option.builder("s") 
				.longOpt("save") 
				.desc("Save arguments") 
				.hasArg(false) 
				.argName("save")
				.required(false) 
				.build();



		final Options options = new Options();
		
	    // First Options
	    for (final Option fo : firstOptions.getOptions()) {
	        options.addOption(fo);
	    }
	    

		options.addOption(viewOption);
		//options.addOption(minOption);
		//options.addOption(maxOption);
		options.addOption(lenghtOption);
		options.addOption(saveOption);

		return options;
	}
	
	/**
	 * Premier parametre d'Aide
	 * @return
	 */
	private static Options configFirstParameters() {

		final Option helpOption = Option.builder("h") 
				.longOpt("help") 
				.desc("Affiche le message d'aide") 
				.build();


	    final Options firstOptions = new Options();

	    firstOptions.addOption(helpOption);

	    return firstOptions;
	}
	
	
}
