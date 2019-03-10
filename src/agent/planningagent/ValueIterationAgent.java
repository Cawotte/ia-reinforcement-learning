package agent.planningagent;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import util.HashMapUtil;

import java.util.HashMap;

import environnement.Action;
import environnement.Etat;
import environnement.IllegalActionException;
import environnement.MDP;
import environnement.Action2D;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;


	/**
	 * Garde une liste des meilleurs actions à effectuer à chaque état.
	 */
	protected HashMap<Etat, List<Action>> bestActions = new HashMap<>();
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		
		this.V = new HashMap<Etat,Double>();
		this.bestActions = new HashMap<Etat, List<Action>>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
			this.bestActions.put(etat, new ArrayList<>());
		}
	}
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}
	
	/**
	 * 
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise la nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//Dans la classe mere, lorsque l'on planifie jusqu'a convergence, on arrete les iterations        
		//lorsque delta < epsilon 
		//Dans cette classe, il  faut juste mettre a jour delta 
		this.delta=0.0;


		//*** VOTRE CODE
		HashMap<Etat,Double> newV = new HashMap<Etat,Double>(V);
		//Pour chaque états
		for (Etat etat : mdp.getEtatsAccessibles() ) {

			this.bestActions.put(etat, new ArrayList<>());

			Double lastBestV;

			//Si état absorbant, on ignore Vk(s), car on sait qu'il n'y aura que Action2D.None.
			if (mdp.estAbsorbant(etat)) {
				lastBestV = 0d;
				continue;
			}
			lastBestV = Double.NEGATIVE_INFINITY;

			//Pour chacun de ses actions possible
			for (Action action : mdp.getActionsPossibles(etat)) {

				//On récupère les états cibles possible de cette action
				Map<Etat, Double> etatsCibles;
				try {
					etatsCibles = mdp.getEtatTransitionProba(etat, action);
				} catch (Exception e) {
					//Exception levé par une action non autorisé dans etat donné
					//N'arrive pas car déjà filtré grâce à GetActionsPossibles.
					e.printStackTrace();
					continue;
				}

				if (etatsCibles.isEmpty()) {
					continue;
				}

				//System.out.println(etat.toString() + " : " + action.toString());
				List<Action> actions;

				//Pour chaque etat cibles, on calcule V
				Double currentV = 0d;
				for (Map.Entry<Etat, Double> entryCible : etatsCibles.entrySet() ) {
					Etat cible = entryCible.getKey();
					Double probaTransition = entryCible.getValue();

					//Calcul de V
					currentV += probaTransition * (mdp.getRecompense(etat, action, cible) + gamma * V.get(cible));

				}

				//System.out.println(etat.toString() + " : " + currentV + "(" + lastBestV + ")");

				//On garde le meilleur V *et son action associé
				if ( currentV > lastBestV ) {
					//We replace the last best with a new one
					lastBestV = currentV;
					actions = new ArrayList<>();
					actions.add(action);
					this.bestActions.put(etat, actions);
				}
				if ( currentV.equals(lastBestV) ) {
					//We add the current actions to the best options known.
					this.bestActions.get(etat).add(action);
				}
			} //end action loop

			//Met à jour l'état avec le V maximum possible.
			newV.put(etat, lastBestV);

		}

		//Calcul de delta
		Double maxDelta = 0d;
		Double diff;
		for (Map.Entry<Etat, Double> entry : V.entrySet() ) {
			Etat etat = entry.getKey();
			Double oldV = entry.getValue();
			diff = Math.abs(newV.get(etat) - oldV);
			if ( diff > maxDelta ) maxDelta = diff;
		}
		this.delta = maxDelta;



		//replace old V map with new one
		V = newV;
		
		//mise a jour de vmax et vmin utilise pour affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s 
		//vmin est la valeur min de V pour tout s
		this.vmax = Double.NEGATIVE_INFINITY;
		this.vmin = Double.POSITIVE_INFINITY;
		for (Etat etat : mdp.getEtatsAccessibles() ) {
			Double v = V.get(etat);
			if (v > vmax ) {
				this.vmax = v;
			}
			if (v < vmin ) {
				this.vmin = v;
			}
		}

		//******************* laisser cette notification a la fin de la methode	
		this.notifyObs();
	}
	
	
	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {

		//*** VOTRE CODE
		List<Action> actions = this.bestActions.get(e);

		if (actions.isEmpty()) {
			return Action2D.NONE;
		}
		else {
			return actions.get(0); //get random instead?
		}

		
	}


	@Override
	public double getValeur(Etat _e) {
		 //Renvoie la valeur de l'Etat _e, c'est juste un getter, ne calcule pas la valeur ici
		 //(la valeur est calculee dans updateV

		//*** VOTRE CODE
		return V.get(_e);

		//return 0.0;
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE

		return bestActions.get(_e);

		// retourne action de meilleure valeur dans _e selon V,
		// retourne liste vide si aucune action legale (etat absorbant)
		
	}
	
	@Override
	public void reset() {
		super.reset();

                //reinitialise les valeurs de V 
		//*** VOTRE CODE

		for (Etat etat : mdp.getEtatsAccessibles() ) {
			V.put(etat, 0d);
			bestActions.put(etat, new ArrayList<>());
		}
		
		this.notifyObs();
	}

	

	

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}
	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}


	
	

	
}