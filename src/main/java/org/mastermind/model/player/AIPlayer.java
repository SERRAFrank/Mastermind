package org.mastermind.model.player;

import java.util.ArrayList;
import java.util.List;
import org.mastermind.core.Core;

public class AIPlayer extends AbstractPlayer {

	/**
	 * Liste des bornes min et max pour la génération des combinaisons numeriques
	 */
	private List<List<Object>> possibleCombination = new ArrayList<List<Object>>();

	private List<Object> validateCombination = new ArrayList<Object>();

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
		this.proposCombination.clear();

		List<List<Object>> possibilities = new ArrayList<List<Object>>();

		for (List<Object> p : possibleCombination) {
			List<Object> t = new ArrayList<Object>(p);
			possibilities.add(t);
		}

		int randomIndex;
		for (int i = 0; i < this.combinationLenght; i++) {

			List<Object> temp = new ArrayList<Object>(possibilities.get(i));

			if (validateCombination.get(i) == null) {

				do {
					randomIndex = (int) (Math.random() * temp.size());
				} while ((validateCombination.contains(temp.get(randomIndex))
						|| proposCombination.contains(temp.get(randomIndex))) && this.uniqueValue);

				this.proposCombination.add(temp.get(randomIndex));

				if (this.uniqueValue) {
					for (List<Object> p : possibilities) {
						p.remove(temp.get(randomIndex));
					}
				}
			} else {
				this.proposCombination.add(validateCombination.get(i));
				if (this.uniqueValue) {
					for (List<Object> t : possibilities)
						t.remove(validateCombination.get(i));
				}
			}

		}
	}

	/**
	 * Comparaison d'une proposition de clef avec la clef secrete
	 */
	@Override
	public void comparToHiddenCombination() {

		for (int i = 0; i < combinationLenght; i++) {
			Object hiddenCombinationValue = this.hiddenCombination.get(i);
			Object propositionValue = this.proposCombination.get(i);
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
		List<Object> excludePossibilities = new ArrayList<Object>();

		int min = 0;
		int max = acceptedInputList.size() - 1;

		int combinationMin = 0;
		int combinationMax = acceptedInputList.size() - 1;

		for (int i = 0; i < this.combinationLenght; i++) {

			Object props = this.proposCombination.get(i);
			String reponse = this.comparCombination.get(i).toString();

			List<Object> newPossibility = new ArrayList<Object>();
			List<Object> possibilities = this.possibleCombination.get(i);

			if (this.moreLess) {
				min = acceptedInputList.indexOf(possibilities.get(0));
				max = acceptedInputList.indexOf(possibilities.get(possibilities.size() - 1));
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
			} else {

				if (reponse == acceptedComparChars.get(0)) {
					if (this.moreLess)
						max = (acceptedInputList.indexOf(props) > 0) ? acceptedInputList.indexOf(props) - 1 : 0;

					if (uniqueValue) {
						for (List<Object> p : possibleCombination)
							p.remove(props);
						excludePossibilities.add(props);
					}

					for (int j = min; j <= max; j++) {
						Object f = acceptedInputList.get(j);
						if (!excludePossibilities.contains(f))
							newPossibility.add(f);
					}

					if (newPossibility.size() == 1)
						validateCombination.set(i, newPossibility.get(0));

				} else if (reponse == acceptedComparChars.get(2)) {
					if (this.moreLess)
						min = (acceptedInputList.indexOf(props) < acceptedInputList.size() - 1)
								? acceptedInputList.indexOf(props) + 1
								: acceptedInputList.size() - 1;

					for (int j = min; j <= max; j++) {
						Object f = acceptedInputList.get(j);
						if (!excludePossibilities.contains(f)) {
							newPossibility.add(f);
						}

					}

					if (uniqueValue)
						newPossibility.remove(props);

					if (newPossibility.size() == 1)
						validateCombination.set(i, newPossibility.get(0));

				}
			}

			this.possibleCombination.set(i, newPossibility);

		}
		for (List l : possibleCombination)
			System.out.println(l);

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
