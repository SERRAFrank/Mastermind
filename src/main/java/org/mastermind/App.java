package org.mastermind;

import org.mastermind.controller.Controller;
import org.mastermind.core.Core;
import org.mastermind.model.Model;
import org.mastermind.view.View;

public class App {
	

	protected Core core = Core.getInstance(this);

	public static void main(String[] args) {

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
