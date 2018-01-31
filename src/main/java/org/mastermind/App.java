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

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {

		/** Instanciation du Core pour le logger */
		Core.getInstance("App");

		String viewOption = null;
		char minOption = 0;
		char maxOption = 0;
		char lenghtOption = 0;
		boolean debugOption = (Core.config.get("DEBUG").equals("true") ) ? true : false ;
		boolean saveOption = false;

		final Options options = configParameters();

		final CommandLineParser parser = new DefaultParser();
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


			minOption = line.getOptionValue("min", Core.config.get("combinationNumbersMin")).charAt(0) ;
			if (!Character.isDigit(minOption)){
				try {
					throw new Exception("Min is a digit between 0 and 9");
				} catch (Exception e) {
					Core.error(e); 
				}
			}

			maxOption = line.getOptionValue("max", Core.config.get("combinationNumbersMax")).charAt(0);
			if (!Character.isDigit(maxOption)){
				try {

					throw new Exception("Max is a digit between 0 and 9");
				} catch (Exception e) {
					Core.error(e); 
				}
			}

			char lenghtChar = line.getOptionValue("lenght", Core.config.get("combinationLenght")).charAt(0);
			if (!Character.isDigit(maxOption)){
				try {

					throw new Exception("Lenght is a digit between 0 and 9");
				} catch (Exception e) {
					Core.error(e); 
				}
			}
			
			
			debugOption = line.hasOption("debug");

			saveOption = line.hasOption("save");


		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		Core.config.set("view", viewOption);
		Core.config.set("combinationNumbersMin", String.valueOf(minOption) );
		Core.config.set("combinationNumbersMax", String.valueOf(maxOption))	;
		Core.config.set("combinationLenght", String.valueOf(lenghtOption))	;
		Core.config.set("DEBUG", (debugOption)? "true" : "false") ;		

		if(saveOption)
			Core.config.updateConfigFile();


		// creation du model
		Model model = new Model(); 

		// creation du controller
		Controller controller = new Controller(model);

		//creation de la vue
		View view = new View (controller);

		model.addObserver(view.getObserver());

		view.start();

	}


	private static Options configParameters() {

		final Option viewOption = Option.builder("v") 
				.longOpt("view") //
				.desc("View mode : graphic or console") 
				.hasArg(true) 
				.argName("view")
				.required(false) 
				.build();

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
				.required(false) 
				.build();

		final Option helpOption = Option.builder("h") 
				.longOpt("help") 
				.desc("Affiche le message d'aide") 
				.build();


		final Options options = new Options();

		options.addOption(viewOption);
		options.addOption(minOption);
		options.addOption(maxOption);
		options.addOption(lenghtOption);
		options.addOption(saveOption);
		options.addOption(helpOption);


		return options;
	}
}
