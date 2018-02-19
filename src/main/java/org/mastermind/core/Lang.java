package org.mastermind.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Lang {
	/** Instance du logger */
	private static Logger logger = Logger.getLogger(Config.class);

	/** Instance du fichier de configuration */
	protected Config config = Config.getInstance();

	/** Le répertoire des langues */
	private String defaultLanguage = config.get("lang.default");

	/** Le fichier langue */
	private String languageFile = config.get("dir.language") + "/" + defaultLanguage + ".lang";

	private Properties props = new Properties();

	/** Liste des clefs du fichier Lang */
	private Set<String> keysList;

	/** Instance unique non préinitialisée */
	private static Lang INSTANCE = null;

	/** Constructeur privé */
	private Lang() {
		loadConfigFile();
		updateKeysList();

	}

	/** Point d'accès pour l'instance unique du singleton */
	public static synchronized Lang getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Lang();
		}
		return INSTANCE;
	}

	/**
	 * Chargement du fichier de config
	 */
	private void loadConfigFile() {

		logger.info("Loading language file : " + this.languageFile);
		FileInputStream reader = null;
		try {
			// chargement du fichier
			reader = new FileInputStream(this.languageFile);

			// prise en charge de l'accentuation
			props.load(new InputStreamReader(reader, Charset.forName("UTF-8")));

			this.props.load(reader);
		} catch (FileNotFoundException e) {
			// fichier inexistant
			DebugMode.error(e);
		} catch (IOException e) {
			// erreur de chargement
			DebugMode.error(e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					// erreur de fermeture
					DebugMode.error(e);
				}
			}
		}

	}

	/**
	 * Retourne la langue.
	 *
	 * @return la langue
	 */
	public String getLang() {
		return this.defaultLanguage;

	}

	/**
	 * Recherche la valeur d'une clef de langue
	 *
	 * @param key
	 *            La clef
	 * @param returnNull
	 *            Retourne null si la clef n'existe pas, sinon il retourne la clef
	 * @return La valeur de la clef
	 */
	private String getProperty(String key, boolean returnNull) {
		String value;

		if (returnNull)
			value = this.props.getProperty(key);
		else
			value = this.props.getProperty(key, key);

		// Regex pour les variables
		String regex = "\\{\\{([a-zA-Z]+\\.[a-zA-Z]+)\\}\\}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher;
		String[] substring;
		String replace;

		/**
		 * Remplacement des variables du fichier par des variables lang ou config sous
		 * la forme {{lang.xxx}}
		 */
		while (true) {
			matcher = pattern.matcher(value);
			if (matcher.find()) {
				substring = matcher.group(1).split("\\.");

				if (substring[0].equals("config"))
					// Remplace par une valeur de config
					replace = config.get(substring[1]);
				else if (substring[0].equals("lang"))
					// Remplace par une valeur de lang
					replace = get(substring[1]);
				else
					replace = "{" + matcher.group(1) + "}";

				value = value.replace("{{" + matcher.group(1) + "}}", replace);
			} else {
				break;
			}

		}

		return value;
	}

	/**
	 * Retourne la valeur correspondant à une clef
	 *
	 * @param key
	 *            La clef recherchée
	 * @param returnNull
	 *            Retourne null si la clef n'existe pas, sinon il retourne la clef
	 * 
	 * @return value La valeur de la clef
	 */
	public String get(String key, boolean returnNull) {
		return getProperty(key, returnNull);

	}

	/**
	 * Retourne la valeur correspondant à une clef
	 *
	 * @param key
	 *            La clef recherchée
	 * 
	 * @return value La valeur de la clef
	 */
	public String get(String key) {
		return getProperty(key, false);
	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme de Character
	 *
	 * @param key
	 *            La clef recherchée
	 * 
	 * @return value La valeur de la clef
	 */
	public char getChar(String key) {
		return getProperty(key, false).charAt(0);
	}

	/**
	 * Retourne la valeur correspondant à une clef sous dorme de Integer
	 *
	 * @param key
	 *            La clef recherchée
	 * 
	 * @return value La valeur de la clef
	 */
	public int getInt(String key) {
		return Integer.parseInt(getProperty(key, false));
	}

	/**
	 * Retourne une liste de toutes les clefs.
	 *
	 * @return Set Liste des clefs
	 */
	public Set<String> getKeysList() {
		return keysList;
	}

	/**
	 * Met à jour la liste des clefs.
	 */
	public void updateKeysList() {
		keysList = ((Map) this.props).keySet();
	}

	/**
	 * Verifie si une clef existe
	 * 
	 * @param k
	 *            La clef
	 * @return true si elle existe, sinon false
	 */
	public boolean keyExist(String k) {
		boolean result = false;
		for (String key : keysList) {
			if (key.contains(k))
				result = true;
		}
		return result;
	}

}
