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
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return 0;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE
		
		return null;
	}
	

}
