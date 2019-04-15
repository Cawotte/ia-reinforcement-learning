package pacman.environnementRL;

import java.util.Arrays;

import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	protected int distanceDot;
	protected int directionDot;
	protected int distanceGhost;
	protected int directionGhost;
	protected int foodEaten;
	protected int ghostHash;

	protected Criterias[] criterias;
	protected int[] hashCriterias;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

        /***
         * Sélectionnez les critères voulus ici !
         */
	    criterias = new Criterias[]{
                Criterias.DISTANCE_DOT,
                //Criterias.DIRECTION_CLOSEST_DOT,
                //Criterias.DISTANCE_CLOSEST_GHOST,
                //Criterias.DISTANCE_CLOSEST_GHOST_DIST3,
                //Criterias.DIRECTION_CLOSEST_GHOST,
                //Criterias.DIRECTION_CLOSEST_GHOST_DIST3,
                //Criterias.FOOD_EATEN,
                Criterias.GHOST_COORDINATES,
        };

	    hashCriterias = new int[criterias.length];
	    for (int i = 0; i < criterias.length; i++) {
            hashCriterias[i] = criterias[i].CriteriaHash(_stategamepacman);
        }

	}

	private enum Criterias {
        DISTANCE_DOT,
        DIRECTION_CLOSEST_DOT,
        DISTANCE_CLOSEST_GHOST,
        DISTANCE_CLOSEST_GHOST_DIST3,
        DIRECTION_CLOSEST_GHOST,
        DIRECTION_CLOSEST_GHOST_DIST3,
        FOOD_EATEN,
        GHOST_COORDINATES;

        public int CriteriaHash(StateGamePacman stateGame) {

            StateAgentPacman pacman = stateGame.getPacmanState(0);
            switch (this) {
                case DISTANCE_DOT:
                    return stateGame.getClosestDot(pacman);
                case DIRECTION_CLOSEST_DOT:
                    return StateGameFunctions.getDirectionNextDot(stateGame);
                case DISTANCE_CLOSEST_GHOST:
                    StateAgentPacman closestGhost = StateGameFunctions.getClosestGhost(stateGame, 1000);
                    return StateGameFunctions.getManhattanDistance(pacman, closestGhost); //if ghost == null, return 0
                case DISTANCE_CLOSEST_GHOST_DIST3:
                    StateAgentPacman closestGhost3 = StateGameFunctions.getClosestGhost(stateGame, 3);
                    return StateGameFunctions.getManhattanDistance(pacman, closestGhost3); //if ghost == null, return 0
                case DIRECTION_CLOSEST_GHOST:
                    return StateGameFunctions.getDirectionClosestGhost(stateGame);
                case DIRECTION_CLOSEST_GHOST_DIST3:
                    return StateGameFunctions.getDirectionClosestGhost(stateGame, 3);
                case FOOD_EATEN:
                    return StateGameFunctions.getFoodEaten(stateGame);
                case GHOST_COORDINATES:
                    return StateGameFunctions.getGhostPosHash(stateGame);
                default:
                    return 0;
            }
        }
    }

	@Override
	public int hashCode() {

        return Arrays.hashCode(hashCriterias);

	}



	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		else if (this.getClass() != o.getClass()) {
			return false;
		}

		return (hashCode() == o.hashCode());
	}
	
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		


		// on renvoie le clone
		return clone;
	}



	

}
