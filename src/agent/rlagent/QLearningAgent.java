package agent.rlagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
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
		Double bestQ = Double.NEGATIVE_INFINITY;
		Double currentQ;
		for ( Action a : this.env.getActionsPossibles(e) ) {

			//For each action, we get the Qvaleur.
			currentQ = getQValeur(e, a);
			if ( currentQ > bestQ ) {
				bestQ = currentQ;
				returnactions = new ArrayList<>();
				returnactions.add(a);
			}
			else if ( currentQ.equals(bestQ)) {
				returnactions.add(a);
			}
		}

		return returnactions;
		
		
	}
	
	@Override
	public double getValeur(Etat e) {

		//*** VOTRE CODE
		Double qmax = Double.NEGATIVE_INFINITY;

		if (this.getActionsLegales(e).size() == 0) {
			return 0d;
		}
		//For each action, find q.
		for (Action a : env.getActionsPossibles(e)) {

			if ( qmax < getQValeur(e, a) ) {
				qmax = getQValeur(e, a);
			}
		}
		return qmax;
		
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
		//If the action is not mapped to state, add it
		if ( !qvaleurs.get(e).containsKey(a) ) {
			pair = qvaleurs.get(e);
			pair.put(a, 0d);
			qvaleurs.put(e, pair);
		}
		return qvaleurs.get(e).get(a);
		//return 0;
	}
	
	
	
	@Override
	public void setQValeur(Etat e, Action a, double d) {
		//*** VOTRE CODE

		qvaleurs.get(e).put(a, d);

		if (d < this.vmin) {
			this.vmin = d;
		}
		if (d > this.vmax) {
			this.vmax = d;
		}
		// mise a jour vmax et vmin pour affichage du gradient de couleur:
				//vmax est la valeur de max pour tout s de V
				//vmin est la valeur de min pour tout s de V
				// ...
		
		
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
