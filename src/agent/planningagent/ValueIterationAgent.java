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


	protected HashMap<Etat, Action> bestActions = new HashMap<Etat, Action>();
	/**
	 * 
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		
		this.V = new HashMap<Etat,Double>();
		this.bestActions = new HashMap<Etat, Action>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
			this.bestActions.put(etat, Action2D.NONE);
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
			//Etat etat = entry.getKey();
			//Double preV = entry.getValue(); //ancienne valeur de V

			this.bestActions.put(etat, Action2D.NONE);

			//Get initial vMaxEtat;
			Double vMaxEtat = V.get(etat);
			
			//Pour chacun de ses actions possible
			for (Action action : mdp.getActionsPossibles(etat)) {

				//On récupère les états cibles possible de cette action
				Map<Etat, Double> etatsCibles;
				try {
					etatsCibles = mdp.getEtatTransitionProba(etat, action);
				} catch (Exception e) {
					//action non autorisé dans etat donné
					e.printStackTrace();
					continue;
				}

				if (etatsCibles.isEmpty()) {
					continue;
				}

				//Pour chaque etat cibles, on calcule V
				Double currentV = 0d;
				for (Map.Entry<Etat, Double> entryCible : etatsCibles.entrySet() ) {
					Etat cible = entryCible.getKey();
					Double probaTransition = entryCible.getValue();


					//System.out.println("Recompense etat : " + cible.toString() + ", " + mdp.getRecompense(etat, action, cible));
					//Calcul de V
					currentV += probaTransition * (mdp.getRecompense(etat, action, cible) + gamma * V.get(cible));

				}

				//On garde le meilleur V *et son action associé
				if ( currentV > vMaxEtat ) {
					vMaxEtat = currentV;
					this.bestActions.put(etat, action);
				}

				//Update Vmin and Vmax
				if ( this.vmin > currentV ) {
					this.vmin = currentV;
				}
				if ( this.vmax < currentV ) {
					this.vmax = currentV;
				}
			}


			//Met à jour l'état avec le V maximum possible.
			newV.put(etat, vMaxEtat);

			//Pour chacun de ses voisins (etats accessibles)


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
		// ...
		
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
		return this.bestActions.get(e);

		//return Action2D.NONE;
		
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
		
		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();
		returnactions.add(getAction(_e));
	
		return returnactions;
		
	}
	
	@Override
	public void reset() {
		super.reset();
                //reinitialise les valeurs de V 
		//*** VOTRE CODE

		for (Etat etat : mdp.getEtatsAccessibles() ) {
			V.put(etat, 0d);
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