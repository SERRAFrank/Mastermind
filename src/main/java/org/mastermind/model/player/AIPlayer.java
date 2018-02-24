package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mastermind.core.Core;

public class AIPlayer extends AbstractPlayer {

	/**
	 * Liste des bornes min et max pour la génération des combinaisons numeriques
	 */
	private List<List<Object>> possibleCombination = new ArrayList<List<Object>>();

	private List<Object> validateCombination = new ArrayList<Object>();

	private Set<Object> excludePossibilities = new HashSet<Object>();

	public AIPlayer(List<Object> acceptedInputList, List<Object> acceptedComparChars, boolean moreLess,
			boolean uniqueValue) {
		super(acceptedInputList, acceptedComparChars, moreLess, uniqueValue);

		/** Instanciation du Core pour le logger */
		Core.getInstance(this);

		// Identifiant
		ID = "AI";

		for (int i = 0; i < this.combinationLenght; i++)
			this.possibleCombination.add(new ArrayList<Object>(this.acceptedInputList));

		for (int i = 0; i < this.combinationLenght; i++)
			validateCombination.add(null);

	}

	/**
	 * Génération d'une clef secrete
	 */
	@Override
	public void genCombination() {
		this.hiddenCombination.clear();
		List<Object> temp = new ArrayList<Object>(this.acceptedInputList);
		int randomIndex;
		for (int i = 0; i < this.combinationLenght; i++) {
			// Tire un nombre aléatoire d'index
			if (this.uniqueValue) {
				randomIndex = (int) (Math.random() * temp.size());
				this.hiddenCombination.add(temp.get(randomIndex));
				temp.remove(randomIndex);
			} else {
				randomIndex = (int) (Math.random() * temp.size());
				this.hiddenCombination.add(temp.get(randomIndex));
			}
		}
		// Affiche la combinaison. Uniquement si le mose DEBUG est actif
		Core.debug("hiddenCombination : " + this.hiddenCombination);
	}

	/**
	 * Création d'une proposition de clef en tirant un nombre aléatoire entre les
	 * bornes min et max
	 */
	@Override
	public void proposCombination() {

		boolean validate = true;
		List<List<Object>> possibilities = new ArrayList<List<Object>>();

		do {
			validate = true;
			this.proposCombination.clear();
			possibilities.clear();

			for (List<Object> p : possibleCombination) {
				List<Object> t = new ArrayList<Object>(p);
				possibilities.add(t);
			}

			int randomIndex;
			for (List<Object> temp : possibilities) {
				if(validate) {
					if(temp.size() > 1) {
						// Si une valeur validée n'existe pas encore
						// Tire un objet aléatoire
						do {
							randomIndex = (int) (Math.random() * temp.size());
						} while ((validateCombination.contains(temp.get(randomIndex))
								|| proposCombination.contains(temp.get(randomIndex))) && this.uniqueValue);

						Object value = temp.get(randomIndex);
						this.proposCombination.add(value);

						// si c'est valeur unique, l'objet est retiré des possibilités
						if (this.uniqueValue) {
							for (List<Object> p : possibilities) {
								p.remove(value);
								if(p.size() == 0)
									validate = false;
							}
						}
					} else if(temp.size() == 1) {
						this.proposCombination.add(temp.get(0)); 
					} else {
						this.proposCombination.add(-1);
					}
				}
			}
		}while(!validate);


	}

	/**
	 * Comparaison d'une proposition de clef avec la clef secrete
	 */
	@Override
	public void comparToHiddenCombination() {

		for (int i = 0; i < combinationLenght; i++) {
			Object hiddenCombinationValue = this.hiddenCombination.get(i);
			Object propositionValue = this.proposCombination.get(i);
			/** Mode plus ou moins */
			if (this.moreLess) {
				if (acceptedInputList.indexOf(hiddenCombinationValue) > acceptedInputList.indexOf(propositionValue)) {
					this.comparCombination.add(acceptedComparChars.get(2));
				} else if (acceptedInputList.indexOf(hiddenCombinationValue) < acceptedInputList
						.indexOf(propositionValue)) {
					this.comparCombination.add(acceptedComparChars.get(0));
				} else {
					this.comparCombination.add(acceptedComparChars.get(1));
					this.wInt++;
				}

				/** Mode mastermin */
			} else {
				if (hiddenCombinationValue == propositionValue) {
					this.comparCombination.add(acceptedComparChars.get(1));
					this.wInt++;
				} else if (this.hiddenCombination.contains(propositionValue)) {
					this.comparCombination.add(acceptedComparChars.get(2));
				} else {
					this.comparCombination.add(acceptedComparChars.get(0));
				}

			}
		}
	}

	/**
	 * Analyse de la reponse de comparaison entre la proposition de clef
	 */
	@Override
	public void comparToProposCombination() {



		int min = 0;
		int max = acceptedInputList.size() - 1;

		for (int i = 0; i < this.combinationLenght; i++) {

			Object props = this.proposCombination.get(i);
			String reponse = this.comparCombination.get(i).toString();

			List<Object> newPossibility = new ArrayList<Object>();

			/** Mode plus ou moins */
			if (this.moreLess) {
				min = acceptedInputList.indexOf(this.possibleCombination.get(i).get(0));
				max = acceptedInputList.indexOf(this.possibleCombination.get(i).get(this.possibleCombination.get(i).size() - 1));
			}

			if (reponse == acceptedComparChars.get(1)) {
				validateCombination.set(i, props);

				if (uniqueValue) {
					for (List<Object> p : possibleCombination)
						p.remove(props);
					excludePossibilities.add(props);
				}


				newPossibility.add(props);
				this.wInt++;

				/** Mode MasterMind */
			} else {
				Object excludeObj = null;

				if (reponse == acceptedComparChars.get(0)) {
					if (this.moreLess) {
						max = (acceptedInputList.indexOf(props) > 0) ? acceptedInputList.indexOf(props) - 1 : 0;
					}else {
						excludePossibilities.add(props);
					}

				} else if (reponse == acceptedComparChars.get(2)) {
					if (this.moreLess) {
						min = (acceptedInputList.indexOf(props) < acceptedInputList.size() - 1) ? acceptedInputList.indexOf(props) + 1 : acceptedInputList.size() - 1;
					}else {
						excludeObj = props;
					}
				}

				if (this.moreLess) {
					for (int j = min; j <= max; j++) {
						Object f = acceptedInputList.get(j);
						newPossibility.add(f);
					}
				}else {
					newPossibility = new ArrayList<Object>(this.possibleCombination.get(i));
				}
				
				if(uniqueValue)
					newPossibility.remove(excludeObj);
			}

			this.possibleCombination.set(i, newPossibility);

		}


		if (this.uniqueValue) {
			for (List<Object> plist : possibleCombination) {
				if(plist.size() == 1)
					excludePossibilities.add(plist.get(0));
			}

			for (List<Object> plist : possibleCombination) {
				if(plist.size() > 1)
					plist.removeAll(excludePossibilities);
			}
		}


	}


	/**
	 * Definit si le joueur est victorieux ou non, et ajouter les points en fonction
	 *
	 * @param w
	 *            true si le joueur est gagnant, sinon false
	 */

	@Override
	public void win(boolean w) {
		// L'IA ne gagne pas de points
	}
}
