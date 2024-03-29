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

    /**
     * Associe un index différent du tableau valeur à chaque couples (action, etat)
     */
	protected HashMap<Etat,HashMap<Action,Integer>> coupleIDs = new HashMap<>();
	protected int id = 0;
	protected int lastID = 0;
	protected double[] values;

	/***
	 * DECREPATED! The size is now dynamic, use FeatureFunctionIdentity()
	 * @param _nbEtat
	 * @param _nbAction
	 */
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
		values = new double[_nbEtat * _nbAction];

		for (int i = 0; i < values.length; i++) {
			values[i] = 0d;
		}

	}

	public FeatureFunctionIdentity(){

		//We initialize a small array because the size will be updated to fit the need.
		values = new double[30];

		for (int i = 0; i < values.length; i++) {
			values[i] = 0d;
		}
	}
	
	@Override
	public int getFeatureNb() {
		return values.length;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){

		//reinitialize previous id
		values[lastID] = 0d;

		//new id
		lastID = getID(e, a);

		//We increase the size of the values vector if it's not big enough anymore
		if (lastID == values.length) {
			System.out.println("Features size increased ! " + id + " ==> " + (id + 10));
			values = new double[id + 10];
			for (int i = 0; i < values.length; i++) values[i] = 0d;
		}

		values[lastID] = 1d;

		return values;
	}


	private int getID(Etat e, Action a) {

		//If this current state/action has no ID, we assign a new one to it and register it.

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
		str += "Nombre de poids = " + id + " / " + values.length + " (array size)";
		return str;
	}

}
