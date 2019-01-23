package agent.rlapproxagent;

import java.util.List;

import environnement.Action;
import environnement.Etat;

/**
 * Interface pour calcul de l'ensemble des fonctions caracteristiques phi_i(s,a)
 * 
 * 
 *  
 * @author laetitiamatignon
 *
 */
public interface FeatureFunction {
	/** @return le nombre de fonctions caracteristiques (taille du vecteur)*/
	public int getFeatureNb();
	
	
	/** @return vecteur de fonctions caracteristiques pour couple (e,a) */
	public double[] getFeatures(Etat e,Action a);
	


}
