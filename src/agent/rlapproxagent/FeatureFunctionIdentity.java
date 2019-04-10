package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;

import javax.jws.Oneway;

/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE

	protected HashMap<Etat,HashMap<Action,Integer>> coupleIDs = new HashMap<>();
	protected int id = 0;
	protected int lastID = 0;
	protected double[] values;

	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
		values = new double[_nbEtat * _nbAction];

		for (int i = 0; i < values.length; i++) {
			values[i] = 0d;
		}

	}

	public FeatureFunctionIdentity(){

		values = new double[30];

		for (int i = 0; i < values.length; i++) {
			values[i] = 0d;
		}
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return values.length;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE
		//reinitialize previous id
		values[lastID] = 0d;

		//new id
		lastID = getID(e, a);

		//We increase the size of the values vector if it's not big enough
		if (lastID == values.length) {
			System.out.println("Incremented values !");
			values = new double[id + 10];
			for (int i = 0; i < values.length; i++) values[i] = 0d;
		}

		values[lastID] = 1d;

		return values;
	}


	private int getID(Etat e, Action a) {

		if (!coupleIDs.containsKey(e)) {
			HashMap<Action, Integer> actionID = new HashMap<>();
			actionID.put(a, id++);
			coupleIDs.put(e, actionID);
		}
		else if (!coupleIDs.get(e).containsKey(a)) {
			coupleIDs.get(e).put(a, id++);
		}

		return coupleIDs.get(e).get(a);
	}

	@Override
	public String toString() {
		String str = "";
		str += "Nb poids = " + id + " / " + values.length + " (nb Etats)";
		return str;
	}

}
