package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
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
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return values.length;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE
		/*for (int i = 0; i < values.length; i++) {
			values[i] = 0d;
		}*/
		values[lastID] = 0d;
		lastID = getID(e, a);
		values[lastID] = 1d;

		return values;
	}

	public int getSizeID() {
		return id;
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

		if (id > values.length) {
			System.out.println("Error max id reached");
		}
		return coupleIDs.get(e).get(a);
	}
	

}
