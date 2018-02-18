package  org.mastermind.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

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
		logger.info("Saving config file : " + configFile );

		try {
			Properties ordoredProps = new Properties() {
			    @Override
			    public synchronized Enumeration<Object> keys() {
			        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
			    }
			};
			ordoredProps.putAll(props);
			
			// ouverture du fichier de config
			output = new FileOutputStream(configFile);
			//réécriture avec les parametres en cours
			ordoredProps.store(output, null);
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
	 * Retourne la valeur correspondant à une clef sous forme d'Integer
	 *
	 * @param key
	 * 		La clef recherchée
	 * @return value
	 * 		La valeur de la clef
	 */
	public int getInt(String string) {
		return Integer.parseInt(get(string));
	}


	/**
	 * Retourne la valeur correspondant à une clef sous forme du Boolean
	 *
	 * @param key
	 * 		La clef recherchée
	 * @return value
	 * 		La valeur de la clef
	 */
	public boolean getBoolean(String key) {
		String value = get(key).toUpperCase();
	
		if(value.equals("TRUE"))
			return true;
		else
			return false;
	}

	/**
	 * Retourne la valeur correspondant à une clef sous forme d'un tableau
	 *
	 * @param key
	 * 		La clef recherchée
	 * @return value
	 * 		La valeur de la clef
	 * @throws Exception 
	 * 		Exception si la clef n'existe pas
	 */
	public String[] getArray(String key) {
		String[] value = get(key).split(",");
		return value;

	}
	


	/**
	 * Indique si la clef existe.
	 *
	 * @param key
	 * 		La clef a tester
	 * @return 
	 * 		true si vrai
	 */
	public boolean exist(String key) {
		if( this.props.getProperty(key) == null)
			return false;
		else
			return true;
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
	 * Définit la valeur d'une clef
	 *
	 * @param key
	 * 		Le nom de la clef
	 * @param value
	 * 		La valeur de la clef
	 */
	public void set(String key, boolean b) {
		String value;
		if(b)
			value = "TRUE";
		else
			value = "FALSE";
		
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


	public void remove(String string) {
		this.props.remove(string);

	}





}

