package org.mastermind.core;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.interpol.Lookup;
import org.apache.log4j.Logger;

/**
 * Classe de management de la configuration de l'appli La classe utilise le
 * pattern Singleton Charge un fichier lang.properties par defaut
 */
public class Lang {

	/** Instance du logger */
	private static Logger logger = Logger.getLogger(Lang.class);

	/** Instance du fichier de configuration */
	protected Config config = Config.getInstance();

	/** Le répertoire des langues */
	private String defaultLanguage = config.get("lang.default");

	/** Le fichier langue */
	private String languageFile = config.get("dir.language") + "/" + defaultLanguage + ".lang";

	/** Instance unique non préinitialisée */
	private static Lang INSTANCE = null;

	private FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

	private FileBasedConfiguration lang;

	/** Constructeur privé */
	private Lang() {
		loadConfigFile();
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

		Parameters langParam = new Parameters();
		
		//Ajout du prefix "config" pour l'interpolation de variables
		Map<String, Lookup> lookups = new HashMap<String, Lookup>(
				ConfigurationInterpolator.getDefaultPrefixLookups());
		lookups.put("config", new ConfigLookup());

		//création du builder de Common Configuration
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(	langParam.properties()
						.setFileName(this.languageFile)
						.setEncoding("UTF-8")
						.setPrefixLookups(lookups)
						);
		try {
			lang = builder.getConfiguration();
			logger.info("Loading lang file : " + this.languageFile);
		} catch (ConfigurationException e) {
			DebugMode.error(e);
		}

	}

	/**
	 * Mise à jour du fichier de configuration
	 * 
	 * @throws ConfigurationException
	 */

	public void updateConfigFile() {
		OutputStream output = null;
		logger.info("Saving config file : " + this.languageFile);

		try {
			builder.save();
		} catch (final ConfigurationException e) {
			// erreur à l'ouverture
			DebugMode.error(e);

		}
	}

	/**
	 * Retourne la valeur correspondant à une clef
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 * @throws Exception
	 *             Exception si la clef n'existe pas
	 */
	public String get(String key) {
		if(this.lang.containsKey(key))
			return this.lang.getString(key);
		else
			return key;

	}
	
	/**
	 * Retourne la valeur correspondant à une clef sous forme d'Integer
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 */
	public int getInt(String key) {
		return lang.getInt(key);
	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme du Boolean
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 */
	public boolean getBoolean(String key) {
		return lang.getBoolean(key);
	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme d'un tableau
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 * @throws Exception
	 *             Exception si la clef n'existe pas
	 */
	public String[] getArray(String key) {
		return lang.getStringArray(key);

	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme d'une list
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 * @throws Exception
	 *             Exception si la clef n'existe pas
	 */
	public List<Object> getList(String key) {
		return lang.getList(key);

	}

	/**
	 * Indique si la clef existe.
	 *
	 * @param key
	 *            La clef a tester
	 * @return true si vrai
	 */
	public boolean keyExist(String key) {
		return lang.containsKey(key);
	}

	/**
	 * Définit la valeur d'une clef
	 *
	 * @param key
	 *            Le nom de la clef
	 * @param value
	 *            La valeur de la clef
	 */
	public void set(String key, Object value) {
		this.lang.setProperty(key, value);
	}

	/**
	 * Retourne toutes les clefs.
	 *
	 * @return Set Liste des clefs
	 */
	public Iterator<String> getAllKeys() {
		return this.lang.getKeys();
	}

	/**
	 * Supprime une clef et sa valeur
	 * 
	 * @param key
	 *            Le nom de la clef
	 */
	public void remove(String key) {
		this.lang.clearProperty(key);
	}

	public char getChar(String key) {
		return this.lang.getString(key).charAt(0);
	}

	public String get(String key, boolean b) {
		if(lang.containsKey(key))
			return this.lang.getString(key);
		else
			return null;
	}
}





/*
 * Class LookUp
 */

class ConfigLookup implements Lookup
{
	/** Instance du fichier de configuration */
	protected Config config = Config.getInstance();
	@Override
	public Object lookup(String varName)
	{
		String r = config.get(varName);
		return r;
	}
}