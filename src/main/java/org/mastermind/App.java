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
	 * 
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

		CommandLine line = null;
		try {
			line = parser.parse(options, args);

			// Vue : graphic / console
			try {
				String viewOption = line.getOptionValue("view", Core.config.get("view"));
				if (!viewOption.equals("graphic") && !viewOption.equals("console"))
					throw new Exception("View not found. Supported modes are console & graphic");
				else
					Core.config.set("view", viewOption);
			} catch (Exception e) {
				Core.error(e);
			}


			//Longueur de chaine
			try {
				String l = line.getOptionValue("lenght", Core.config.get("game.lenght")) ;
				int lenghtOption = Integer.parseInt(l);
				
				if(lenghtOption < 0 || lenghtOption > 9)
					throw new Exception("Lenght is a digit between 0 and 9");
				else
					Core.config.set("game.lenght", lenghtOption);
			} 
			catch (Exception e) {
				Core.error(e); 
			} 


			// debug mode		
			Core.config.set("DEBUG", (Core.DEBUG() || line.hasOption("debug")) );
			
			// Sauvegarde des arguments
			if(line.hasOption("save"))
				Core.config.updateConfigFile();

			// Si mode aide demandé
			boolean helpMode = firstLine.hasOption("help");
			if (helpMode) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("MasterMind", options, true);
				System.exit(0);
			}

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// Creation du model
		model = new Model();

		// Creation du controller
		Controller controller = new Controller(model);

		// Creation de la vue
		View view = new View(controller);

		// Ajout des observers
		model.addObserver(view.getObserver());

		// Demarrage de la vue
		view.start();
	}

	/**
	 * Options CLI
	 * 
	 * @param firstOptions
	 * @return
	 */
	private static Options configParameters(final Options firstOptions) {

		final Option viewOption = Option.builder("v").
				longOpt("view")
				.desc("View mode : graphic or console")
				.hasArg(true)
				.argName("view")
				.required(false)
				.build();

		final Option lenghtOption = Option.builder("l")
				.longOpt("lenght")
				.desc("Combination lenght between 0 and 9")
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

		final Option forceDebugOption = Option.builder("d")
				.longOpt("debug")
				.desc("Force debug mode")
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
		options.addOption(lenghtOption);
		options.addOption(saveOption);
		options.addOption(forceDebugOption);

		return options;
	}

	/**
	 * Premier parametre d'Aide
	 * 
	 * @return
	 */
	private static Options configFirstParameters() {

		final Option helpOption = Option.builder("h").longOpt("help").desc("Help").build();

		final Options firstOptions = new Options();

		firstOptions.addOption(helpOption);

		return firstOptions;
	}

}
