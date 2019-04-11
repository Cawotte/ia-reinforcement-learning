package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;
import pacman.environnementRL.StateGameFunctions;

/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *  
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures ;
	
	private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles

    private final int nbFeatures = 4;

    private double[] means;
    private int nbCalc = 0;
	public FeatureFunctionPacman() {

        vfeatures = new double[nbFeatures];
        means = new double[nbFeatures];
        for (int i = 0; i < means.length; i++)
            means[i] = 0d;
	}

	@Override
	public int getFeatureNb() {
		return vfeatures.length;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		//vfeatures = new double[nbFeatures];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}
	
		StateAgentPacman pacmanstate_next= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));

		 
		//*** VOTRE CODE
		StateGamePacman newStateGame = stategamepacman.nextStatePacman(new ActionPacman(a.ordinal()));


		//biais
		vfeatures[0] = 1d;

		//Nb of ghost on step away
		vfeatures[1] = (double)StateGameFunctions.getNbGhostAtDistance(newStateGame, 2);

		//1 if next position has a Dot, 1 else
		boolean nextPosHasDot = StateGameFunctions.nextPositionHasDot(stategamepacman, newStateGame);
		vfeatures[2] = (nextPosHasDot) ? 1d : 0d;

		//distance with closest Dot on feature position
		vfeatures[3] = (double)newStateGame.getClosestDot(pacmanstate_next);

		for (int i = 0; i < means.length; i++) {
		    means[i] = ( (means[i] * nbCalc) + vfeatures[i] )
                        / (nbCalc + 1);
        }

        nbCalc++;
		
		return vfeatures;
	}

	public String meansToString() {
	    String str = "means : [ ";
        for (int i = 0; i < means.length; i++) {
            str += String.format("%.2f",means[i]);
            if (i != means.length - 1) str += ", ";
        }
        str += "]";
        return str;
    }
	public void reset() {
		vfeatures = new double[4];
		
	}

}
