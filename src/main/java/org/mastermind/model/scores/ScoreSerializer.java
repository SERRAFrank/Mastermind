package org.mastermind.model.scores;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mastermind.core.Core;

public class ScoreSerializer implements Serializable{


	/** Instance du core */
	protected Core core = Core.getInstance(this);

	/** Tableau des scores sous la forme nomDuJoueur =>  {nbrPatiesGagnées ; nbrPatiesPerdues} */
	protected Map<String, int[]> scoreList = new HashMap<String, int[]>();
	
	/** Fichier de score */
	protected File scoreFile = new File(core.config.get("scoreFile"));


	/**
	 * Constructeur
	 */
	public ScoreSerializer(){
		load();
	}

	/**
	 * Chargement du fichier de score
	 */
	public void load(){
		
		// si le fichier score existe
		if(scoreFile.exists() && !scoreFile.isDirectory()) {
			try {
				ObjectInputStream ois = new ObjectInputStream( new FileInputStream(this.scoreFile));
				this.scoreList = (HashMap) ois.readObject();
				ois.close();
			} catch (Exception e) {
				this.core.error(e);
			}
		//Sinon, le creer
		}else {
			try {
				scoreFile.createNewFile();
				save();
			} catch (IOException e) {
				this.core.error(e);
			}
			
		}

	}	

	/**
	 * Sauvegarde du fichier score
	 */
	public void save(){
		try {
			ObjectOutputStream oos = new ObjectOutputStream( new BufferedOutputStream( new FileOutputStream(this.scoreFile)));
			oos.writeObject(this.scoreList);
			oos.close();
		} catch (Exception e) {
			this.core.error(e);
		}

	}


}
