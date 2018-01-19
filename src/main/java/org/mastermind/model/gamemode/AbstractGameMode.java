package  org.mastermind.model.gamemode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mastermind.core.Core;

public abstract class AbstractGameMode {

	/** Instance du core */
	protected Core core = Core.getInstance(this);

	/** Combinaison à trouver */
	protected List<Object> hiddenCombination =  new ArrayList<Object>();
	
	/** Chaine de retour */
	protected List<Object> output =  new ArrayList<Object>();
	
	protected Set<Object> acceptedChar = new HashSet<Object>();
	
	/** Nom du mode */
	public String gameModeName;
	
	/** Mode de vue */
	protected String viewMode = core.config.get("view");
	
	/** Longueur de la combinaison */
	protected int combinationLenght = core.config.getInt("combinationLenght");
	
	/** Type de combinaison */
	protected String combinationType = core.config.get("combinationType");
	
	/** List des couleurs utilisables (combinaison de type couleur/ */
	protected List<String> combinationColors = new ArrayList<String>(Arrays.asList(core.config.get("combinationColors").split(",")));

	
	/** Definit le statut de victoire */
	protected boolean  win = false;
	
	/** Premier joueur */
	protected String firstPlayer;

	
	/** Min et max pour les combinaisons de type chiffre */
	protected int combinationNumbersMin = core.config.getInt("combinationNumbersMin");
	protected int combinationNumbersMax = core.config.getInt("combinationNumbersMax");
	
	/** Initialise les paramettres d'une nouvelle partie */
	public abstract void newGame();
	
	public abstract void Comparaison(List<Object> playerInput);

	public boolean winStatut() {
		return win;
	}
	
	public boolean acceptedChar(Object o) {
		return acceptedChar.contains(o);
	}

	public String getFirstPlayer() {
		return firstPlayer;
	}

	public List<Object> getOutput() {
		return output;
	}
	
	public Set<Object> getAcceptedChar(){
		return acceptedChar;
	}
}

