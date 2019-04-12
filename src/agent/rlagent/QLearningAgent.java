package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
import pacman.elements.ActionPacman;

/**
 * Renvoi 0 pour valeurs initiales de Q
 * @author laetitiamatignon
 *
 */
public class QLearningAgent extends RLAgent {
	/**
	 *  format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
	 */
	protected HashMap<Etat,HashMap<Action,Double>> qvaleurs;

	//Used for dynamic programming : Avoid calculating a lot the same values at each step.
	protected HashMap<Etat, Double> maxValues = new HashMap<>();
	
	//AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
	//protected HashMap<Pair<Etat,Action>,Double> qvaleurs;

	/**
	 * 
	 * @param alpha
	 * @param gamma
	 * @param _env
	 */
	public QLearningAgent(double alpha, double gamma,
			Environnement _env) {
		super(alpha, gamma,_env);
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();

	
	}

	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
	 *  (plusieurs actions sont renvoyees si valeurs identiques)
	 *  renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
	 */
	@Override
	public List<Action> getPolitique(Etat e) {
		// retourne action de meilleures valeurs dans e selon Q : utiliser getQValeur()
		// retourne liste vide si aucune action legale (etat terminal)

		List<Action> returnactions = new ArrayList<Action>();



		if (this.getActionsLegales(e).size() == 0){//etat  absorbant; impossible de le verifier via environnement
			System.out.println("aucune action legale");
			return new ArrayList<Action>();
			
		}

		//*** VOTRE CODE
		Double bestQ = null;
		Double currentQ = null;
		for ( Action a : this.env.getActionsPossibles(e) ) {

			//For each action, we get the Qvaleur.
			currentQ = getQValeur(e, a);
			//If it's the first element or a better one, make a new list
			if ( bestQ == null || currentQ > bestQ ) {
				bestQ = currentQ;
				returnactions = new ArrayList<>();
				returnactions.add(a);
			}
			//If it's equivalent to our best choice, add it to the list.
			else if ( currentQ.equals(bestQ)) {
				returnactions.add(a);
			}
		}

		return returnactions;
		
		
	}
	
	@Override
	public double getValeur(Etat e) {

		//*** VOTRE CODE
		//If we haven't already calculated it this step,
		if (!maxValues.containsKey(e)) {

			Double qmax = Double.NEGATIVE_INFINITY;

			if (this.getActionsLegales(e).size() == 0) {
				return 0d;
			}
			//For each action possible, calculate QValue, and calculate the best
			for (Action a : env.getActionsPossibles(e)) {
				Double val = getQValeur(e, a);
				if ( qmax < val ) {
					qmax = val;
				}
			}

			//Add it to the maxValues list ( dynamic programming )
			maxValues.put(e, qmax);
			return qmax;
		}

		return maxValues.get(e);

		
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE

		HashMap<Action, Double> pair;

		//If the state is not mapped, add it
		if (!qvaleurs.containsKey(e)) {
			pair = new HashMap<Action, Double>();
			pair.put(a, 0d);
			qvaleurs.put(e, pair);
	}
		//If the action is not mapped to the tate, add it
		if ( !qvaleurs.get(e).containsKey(a) ) {
			pair = qvaleurs.get(e);
			pair.put(a, 0d);
			qvaleurs.put(e, pair);
		}
		return qvaleurs.get(e).get(a);
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE

		qvaleurs.get(e).put(a, d);

		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur de max pour tout s de V
		//vmin est la valeur de min pour tout s de V
		// ...
		Double qval;
		for (Etat etat : qvaleurs.keySet() ) {
			qval = getValeur(etat);
			if (vmin > qval) {
				vmin = qval;
			}
			if (vmax < qval) {
				vmax = qval;
			}
		}
		
		
		this.notifyObs();
		
	}
	
	
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL)
			System.out.println("QL mise a jour etat "+e+" action "+a+" etat' "+esuivant+ " r "+reward);

		//*** VOTRE CODE
		Double qval = (1 - this.alpha) * getQValeur(e, a) + this.alpha * (reward + this.gamma * getValeur(esuivant));
		setQValeur(e, a, qval);

		//max values change at each step, so we reset it.
		maxValues.clear();
	}

	@Override
	public void endEpisode() {
		if (qvaleurs != null && !qvaleurs.isEmpty())
			System.out.println("Nb etats = " + qvaleurs.size());
		super.endEpisode();
	}

	@Override
	public Action getAction(Etat e) {
		this.actionChoisie = this.stratExplorationCourante.getAction(e);
		return this.actionChoisie;
	}

	@Override
	public void reset() {
		super.reset();
		//*** VOTRE CODE
		qvaleurs = new HashMap<Etat,HashMap<Action,Double>>();

		this.episodeNb =0;
		this.notifyObs();
	}












}
