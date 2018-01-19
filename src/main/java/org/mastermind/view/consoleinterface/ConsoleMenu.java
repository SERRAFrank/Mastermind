package  org.mastermind.view.consoleinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.mastermind.core.Core;

public class ConsoleMenu {

	/** Instance du core */
	protected Core core = Core.getInstance(this);

	/** Titre du menu */
	private String title = "";
	
	/** Liste des propositions */
	private String options;


	/**
	 * Constructeur.
	 */
	public ConsoleMenu() {

	}

	
	public ConsoleMenu(String k) {
		this.title = core.lang.get(k+ ".Title");
		this.options = k+".Options";
	}


	/**
	 * Definit le titre du menu.
	 *
	 * @param title
	 * 		Nouveau titre
	 */
	public void setTitle(String t) {
		this.title = t;
	}


	/**
	 * Definit les options du menua partir de la clef d'un fichier Lang
	 *
	 * @param options the options
	 */
	public void setOptions(String o) {
		this.options = o;
	}

	/**
	 * Vide le titre et les options de menu.
	 */
	public void flush() {
		this.title = "";
		this.options = "";
	}



	/**
	 * Affiche des propositions et retourne la clef associée 
	 *
	 * @return the string
	 */
	public String showMenu() {

		// Liste des clefs des propositions
		List<String> optionReturn = new ArrayList<>();


		int choice;
		Scanner keyboard;

		// verrou tant que le choix ne correspond pas à une proposition valide
		boolean lock = true;	

		do {	

			// Affichage d'un titre
			if(!title.equals("")) {
				showTitle(title);
				System.out.println();

			}
			int i = 0;
			String entry = "";
			String desc = "";
			
			
			while( core.lang.keyExist(options + "." + i) ) {
				entry = core.lang.get(options + "." + i + ".key", true);
				desc = core.lang.get(options + "." + i + ".desc", true);
				optionReturn.add(entry);
				i++;				
				System.out.println("  " + i + ".  " + desc);

			}
			System.out.println();
					
			// Choix de la proposition
			keyboard = new Scanner(System.in);
			System.out.print(core.lang.get("select"));
			choice = keyboard.nextInt();

			if(choice > i || choice < 1) {
				System.out.println("Je n'ai pas compris votre choix");
			}else {
				// Sortie de boucle si le choix existe
				lock = false;
			}

		}while(lock);		
		
		flush();
		
		// retour de la clef de proposition choisie
		return optionReturn.get(choice-1);


	}


	public static void showTitle(String t) {
		int lenght;
		String separator = " ";
		if(t.length() < 15 )
			separator = repeat(" ", (15 - t.length())/2);
		
		lenght = t.length()+2+(separator.length()*2);
		
		System.out.println(repeat("-", lenght));
		System.out.println("|" + separator + t + separator + "|");
		System.out.println(repeat("-", lenght));

	}
	
	
    private static String repeat( String str, int i){
        if(i>0)
            return str + repeat(str, i-1);
        else
            return str ;
       
    }


}
