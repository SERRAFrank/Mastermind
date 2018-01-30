package org.mastermind;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.model.Model;
import org.mastermind.view.View;

public class App {


	protected static Core core = Core.getInstance("App");

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {

		
		// mise a jour de la configuration en fonction des arguments passés
		// ne modifie pas le fichier de config
		for(String argument : args) {
			//les arguments doivent etre sous la forme clef.valeur
			if(argument.contains(".")) {
				String key = argument.split("\\.")[0];
				String value = argument.split("\\.")[1];
				if(core.config.exist(key))
					core.config.set(key, value);
			}
		}		

		// creation du model
		Model model = new Model(); 

		// creation du controller
		Controller controller = new Controller(model);

		//creation de la vue
		View view = new View (controller);

		model.addObserver(view.getObserver());

		view.start();

	}


}
