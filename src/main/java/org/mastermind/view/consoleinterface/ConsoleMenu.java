package org.mastermind.view.consoleinterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.mastermind.core.Core;

public class ConsoleMenu {
	/** Titre du menu */
	private String title = "";

	/** Liste des propositions */
	private Map<String, String> options = new LinkedHashMap<String, String>();

	/**
	 * Constructeur par defaut
	 */
	public ConsoleMenu() {
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);
	}

	/**
	 * Constructeur avancé
	 * 
	 * @param k
	 *            Clef des parametres de menu dans le fichier Lang
	 */
	public ConsoleMenu(String t, Map<String, String> o) {
		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		this.title = Core.lang.get(t);
		this.options = o;
	}

	/**
	 * Definit le titre du menu.
	 *
	 * @param title
	 *            Nouveau titre
	 */
	public void setTitle(String t) {
		this.title = t;
	}

	/**
	 * Definit les options du menua partir de la clef d'un fichier Lang
	 *
	 * @param options
	 *            the options
	 */
	public void setOptions(Map<String, String> o) {
		this.options = o;
	}


	public void addOption(String k, String v) {
		this.options.put(k, v);
	}

	public void addOption(String kv) { 	
		DefaultListDelimiterHandler handler = new DefaultListDelimiterHandler('|');
		List<String> kvlist = (List<String>) handler.split(kv, true);

		addOption(kvlist.get(0), kvlist.get(1));
	}	

	/**
	 * Vide le titre et les options de menu.
	 */
	public void flush() {
		this.title = "";
		this.options.clear();
	}

	/**
	 * Affiche des propositions et retourne la clef associée
	 *
	 * @return the string
	 */
	public String showMenu() {
		int choice;
		Scanner keyboard;

		// verrou tant que le choix ne correspond pas à une proposition valide
		boolean lock = true;
		List<String> keyList = new ArrayList<String>();

		do {

			// Affichage d'un titre
			if (title != null && !title.equals("")) {
				System.out.println();
				showTitle(title);

			}

			int index = 1;
			// Creation des options du menu
			for (Map.Entry<String, String> entry : options.entrySet()) {
				keyList.add( entry.getKey() );
				System.out.println(index + ". " + entry.getValue());
				index++;
			}


			// Choix de la proposition
			keyboard = new Scanner(System.in);
			System.out.print(Core.lang.get("text.select"));
			choice = keyboard.nextInt();


			if (choice > options.size() || choice < 1) {
				System.out.println("Je n'ai pas compris votre choix");
			} else {
				// Sortie de boucle si le choix existe
				lock = false;
			}

		} while (lock);

		String key = keyList.get(choice - 1);

		flush();

		return key;

	}

	/**
	 * Mise en forme et affichage d'un titre
	 * 
	 * @param t
	 *            Le titre
	 */
	public static void showTitle(String t) {
		int lenght;
		String separator = " ";
		if (t.length() < 15)
			separator = repeat(" ", (15 - t.length()) / 2);

		lenght = t.length() + 2 + (separator.length() * 2);

		System.out.println(repeat("-", lenght));
		System.out.println("|" + separator + t + separator + "|");
		System.out.println(repeat("-", lenght));

	}

	/**
	 * Fonction de répétition d'un string
	 *
	 * @param str
	 *            la chaine de caractère à repeter
	 * @param r
	 *            Le nombre de répétitions
	 * @return La chaine répétée r fois
	 */
	private static String repeat(String str, int r) {
		String strReturn = "";
		for (int i = 0; i < r; i++)
			strReturn += str;

		return strReturn;

	}



	public static <T, E> String getKeysByValue(Map<T, E> map, E value) {
		String key = null;
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				key = (String) entry.getKey();
			}
		}
		return key;

	}

}
