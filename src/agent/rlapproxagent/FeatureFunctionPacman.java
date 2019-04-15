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

	private EnumFeatureFunction[] functions;

    private double[] means;
    private int nbCalc = 0;

	public FeatureFunctionPacman() {

		/***
		 * Choississez ici les features function à utiliser !
		 * Commenter/Décommenter les features fonctions à tester.
		 */
		functions = new EnumFeatureFunction[]{
				EnumFeatureFunction.BIAS,
				EnumFeatureFunction.DIST_CLOSEST_DOT,
				EnumFeatureFunction.DIST_CLOSEST_GHOST,
				EnumFeatureFunction.NB_CLOSE_GHOST,
				EnumFeatureFunction.NB_CLOSE_GHOST_3,
				EnumFeatureFunction.HAS_DOT,
		};

		int nbFeatures = functions.length;

        vfeatures = new double[nbFeatures];
        means = new double[nbFeatures];

        //init means
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

		//Compute the featuresFunction result using their enum to get the right function.
		for (int i = 0; i < functions.length; i++) {
			vfeatures[i] = functions[i].computeFeatureFunction(stategamepacman, newStateGame);

		}


		//DEBUG : Store the means value of each functions
		for (int i = 0; i < means.length; i++) {
		    means[i] = ( (means[i] * nbCalc) + vfeatures[i] )
                        / (nbCalc + 1);
        }

        nbCalc++;
		
		return vfeatures;
	}

	/**
	 * Get the means values of each functions as a string
	 * @return
	 */
	public String meansToString() {
	    String str = "Means : [";
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
