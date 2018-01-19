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

	protected Map<String, int[]> scoresList = new HashMap<String, int[]>();
	protected File scoreFile;



	public ScoreSerializer(String sf){
		this.scoreFile = new File(sf);
		load();
	}

	public void load(){	
		if(scoreFile.exists() && !scoreFile.isDirectory())
			try {
				scoreFile.createNewFile();
				ObjectInputStream ois = new ObjectInputStream( new FileInputStream(this.scoreFile));
				this.scoresList = (HashMap) ois.readObject();
				ois.close();
			} catch (Exception e) {
				this.core.error(e);
			}
		else {
			save();
		}

	}	

	public void save(){
		try {
			ObjectOutputStream oos = new ObjectOutputStream( new BufferedOutputStream( new FileOutputStream(scoreFile)));
			oos.writeObject(scoresList);
			oos.close();

		} catch (Exception e) {
			this.core.error(e);
		}

	}


}
