package  org.mastermind.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Classe de management de la configuration de l'appli
 * La classe utilise le pattern Singleton
 * Charge un fichier config.properties par defaut
 */
public class Config {

	/** Instance du logger */
	private static Logger logger = Logger.getLogger(Config.class);

	/** Le fichier de config */
	private String configFile = "resources/config.properties";

	private Properties props = new Properties();


	/** Instance unique non préinitialisée */
	private static Config INSTANCE = null;

	/** Instance unique indiquant le mode DEBUG */
	public static boolean DEBUG;

	/** Constructeur privé */
	private Config(){
		loadConfigFile();

		// définit si le Debug Mode est activé
		if (this.props.getProperty("DEBUG").equals("true") )
			DEBUG = true;
		else
			DEBUG = false;
		
		logger.info("Debug mode : " + DEBUG );

	}


	/** Point d'accès pour l'instance unique du singleton */
	public static synchronized Config getInstance()
	{           
		if (INSTANCE == null){
			INSTANCE = new Config(); 
		}
		return INSTANCE;
	}


	/**
	 * Chargement du fichier de config
	 */
	private void loadConfigFile() {

		logger.info("Loading config file : " + this.configFile );
		FileInputStream reader = null;
		try {
			//chargement du fichier
			reader = new FileInputStream(this.configFile);
			this.props.load(reader);
		} catch (FileNotFoundException e) {
			//fichier inexistant
			DebugMode.error(e); 
		} catch (IOException e) {
			// erreur de chargement
			DebugMode.error(e); 
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					//erreur de fermeture
					DebugMode.error(e); 
				}
			}
		}
	}


	/**
	 * Mise à jour du fichier de configuration
	 */
	public void updateConfigFile() {
		OutputStream output = null;
		logger.info("Loading config file : " + configFile );

		try {
			// ouverture du fichier de config
			output = new FileOutputStream(configFile);
			//réécriture avec les parametres en cours
			this.props.store(output, null);
		} catch (final IOException e) {
			//erreur à l'ouverture
			DebugMode.error(e); 
		} finally {
			if (output != null) {
				try {
					//fermeture du fichier
					output.close();
				} catch (final IOException e) {
					//erreur à la fermeture
					DebugMode.error(e); 
				}
			}

		}

	}

	/**
	 * Retourne la valeur correspondant à une clef
	 *
	 * @param key
	 * 		La clef recherchée
	 * @return value
	 * 		La valeur de la clef
	 * @throws Exception 
	 * 		Exception si la clef n'existe pas
	 */
	public String get(String key) {
		String value = this.props.getProperty(key);
		try {
			if (value == null)
				throw new Exception("Config key '"+key+"' not found.");
		}catch(Exception e){
			DebugMode.error(e);			
		}

		return value;

	}

	/**
	 * Définit la valeur d'une clef
	 *
	 * @param key
	 * 		Le nom de la clef
	 * @param value
	 * 		La valeur de la clef
	 */
	public void set(String key, String value) {
		this.props.setProperty(key, value);
	}


	/**
	 * Retourne toutes les clefs.
	 *
	 * @return Set
	 * 	Liste des clefs
	 */	
	public Set<Object> getAllKeys(){
		Set<Object> keysList = this.props.keySet();
		return keysList;
	}


	public int getInt(String string) {
		return Integer.parseInt(get(string));
	}





}

