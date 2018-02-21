package org.mastermind.core;

import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

/**
 * Classe de management de la configuration de l'appli La classe utilise le
 * pattern Singleton Charge un fichier config.properties par defaut
 */
public class Config {

	/** Instance du logger */
	private static Logger logger = Logger.getLogger(Config.class);

	/** Le fichier de config */
	private String configFile = "resources/config.properties";

	Configuration config;

	/** Instance unique non préinitialisée */
	private static Config INSTANCE = null;

	FileBasedConfigurationBuilder<FileBasedConfiguration> builder;

	/** Constructeur privé */
	private Config() {
		loadConfigFile();
	}

	/** Point d'accès pour l'instance unique du singleton */
	public static synchronized Config getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Config();
		}
		return INSTANCE;
	}

	/**
	 * Chargement du fichier de config
	 */
	private void loadConfigFile() {

		Parameters params = new Parameters();
		// Read data from this file
		File propertiesFile = new File(this.configFile);

		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(params.properties().setFileName(this.configFile)
						.setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
		try {
			config = builder.getConfiguration();
			logger.info("Loading config file : " + this.configFile);
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
		logger.info("Saving config file : " + configFile);

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
		return this.config.getString(key);

	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme d'Integer
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 */
	public int getInt(String key) {
		return config.getInt(key);
	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme du Boolean
	 *
	 * @param key
	 *            La clef recherchée
	 * @return value La valeur de la clef
	 */
	public boolean getBoolean(String key) {
		if(this.config.containsKey(key)) {
			return config.getBoolean(key);
		}else {
			//Core.debug("Key "+ key + " not found");
			return false;
		}
			
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
		return config.getStringArray(key);

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
		return config.getList(key);

	}

	/**
	 * Indique si la clef existe.
	 *
	 * @param key
	 *            La clef a tester
	 * @return true si vrai
	 */
	public boolean containsKey(String key) {
		return config.containsKey(key);
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
		this.config.setProperty(key, value);
	}

	/**
	 * Retourne toutes les clefs.
	 *
	 * @return Set Liste des clefs
	 */
	public Iterator<String> getAllKeys() {
		return this.config.getKeys();
	}

	/**
	 * Supprime une clef et sa valeur
	 * 
	 * @param key
	 *            Le nom de la clef
	 */
	public void remove(String key) {
		this.config.clearProperty(key);
	}
}
