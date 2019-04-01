package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import pacman.elements.ActionPacman;
import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	protected int pac_x;
	protected int pac_y;
	protected int ghost_x;
	protected int ghost_y;
	protected int distanceDot;
	protected int directionDot;
	protected int distanceGhost;
	protected int directionGhost;
	protected int foodLeft;
	protected int foodEaten;
	protected int ghostHash;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

		StateAgentPacman pacman = _stategamepacman.getPacmanState(0);

		distanceGhost = 0;
		directionGhost = MazePacman.NORTH; //arbitrary default value
		pac_x = pacman.getX();
		pac_y = pacman.getY();
		ghost_x = _stategamepacman.getGhostState(0).getX();
		ghost_y = _stategamepacman.getGhostState(0).getY();

		//region

		/*
		//Get closest ghost
		for (int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++) {
			StateAgentPacman ghost = _stategamepacman.getGhostState(i);
			ghost_x = ghost.getX();
			ghost_y = ghost.getY();

			int distance = Math.abs(ghost_x - pac_x) + Math.abs(ghost_y - pac_y);
			if ( distance < distanceGhost && distance < 4) {
				distanceGhost = distance;
				directionGhost = getDirection(pac_x, pac_y, ghost_x, ghost_y);
			}
		} */


		//Full Ghost Hash

		/*
		ghostHash = 0;
		distanceGhost = 0;
		for (int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++) {
			StateAgentPacman ghost = _stategamepacman.getGhostState(i);
			ghost_x = ghost.getX();
			ghost_y = ghost.getY();

			int distance = Math.abs(ghost_x - pac_x) + Math.abs(ghost_y - pac_y);
			if ( distance < 3 ) {
				distanceGhost = distance;
				directionGhost = getDirection(pac_x, pac_y, ghost_x, ghost_y);
			}
			else {
				distanceGhost = 0;
				directionGhost = MazePacman.NORTH;
			}
			ghostHash = Objects.hash(ghostHash, distanceGhost, directionGhost);
		}*/

		//endregion

		foodLeft = _stategamepacman.getMaze().getNbfood() - _stategamepacman.getCapsulesEaten();
		foodEaten = _stategamepacman.getCapsulesEaten();

		distanceDot = _stategamepacman.getClosestDot(pacman);
		directionDot = getDirectionNextDot(_stategamepacman);

		StateAgentPacman closestGhost = getClosestGhost(_stategamepacman, 3);

		directionGhost = getDirection(pacman, closestGhost);
		distanceGhost = getDistance(pacman, closestGhost);

		//Coordinate ghost
		ghostHash = getGhostPosHash(_stategamepacman);
	}

	@Override
	public int hashCode() {

        //return Objects.hash(distanceDot, ghost_x, ghost_y);

        return Objects.hash(directionDot, distanceGhost, directionGhost);
		//return Objects.hash(distanceDot, ghostHash, foodLeft);
		//return Objects.hash(distanceDot, ghostHash);

		//return Objects.hash(pac_x, pac_y, distanceGhost, directionGhost);
		//return Objects.hash(distanceDot, distanceGhost, directionGhost);

		//return Objects.hash(distanceDot, ghostHash, foodLeft);
		//return Objects.hash(distanceDot, ghost_x, ghost_y);

	}

	public int getDimensions() {
		return 110;
	}

	private int getGhostPosHash(StateGamePacman stateGame) {
		int hash = 0;
		for (int i = 0; i < stateGame.getNumberOfGhosts(); i++) {
			StateAgentPacman ghost = stateGame.getGhostState(i);
			int ghost_x = ghost.getX();
			int ghost_y = ghost.getY();

			hash *= 10;
			hash += ghost_x;
			hash *= 10;
			hash += ghost_y;
			//hash = Objects.hash(hash, ghost_x, ghost_y);
		}
		return hash;
	}

	private int getDirectionNextDot(StateGamePacman stateGame) {
	    StateAgentPacman pacman = stateGame.getPacmanState(0);
	    int currentDotDistance = stateGame.getClosestDot(pacman);
        int bestDir = 0;
	    //4 dir + map
	    for (int dir = 0; dir < 5; dir++) {

            StateGamePacman nextGameState = stateGame.nextStatePacman(new ActionPacman(dir));
            if ( nextGameState.getClosestDot(nextGameState.getPacmanState(0)) <= currentDotDistance ) {
                bestDir = dir;
                currentDotDistance = nextGameState.getClosestDot(nextGameState.getPacmanState(0));
            }
        }

	    return bestDir;
    }

	private StateAgentPacman getClosestGhost(StateGamePacman stateGame, int maxDistance) {

		StateAgentPacman pacman = stateGame.getPacmanState(0);
		StateAgentPacman closestGhost = null;

		int minDistanceGhost = Integer.MAX_VALUE;

		for (int i = 0; i < stateGame.getNumberOfGhosts(); i++) {
			StateAgentPacman ghost = stateGame.getGhostState(i);
			int distance = getDistance(pacman, ghost);

			if ( minDistanceGhost > distance && distance <= maxDistance) {
				minDistanceGhost = distance;
				closestGhost = ghost;
			}
		}

		return closestGhost;
	}

	private int getDistance(StateAgentPacman pacman, StateAgentPacman ghost) {
		return (ghost != null) ? Math.abs(ghost.getX() - pacman.getX()) + Math.abs(ghost.getY() - pacman.getY()) : 0;
	}

	private int getDirection(StateAgentPacman pacman, StateAgentPacman ghost) {
		return (ghost != null) ? getDirection( pacman.getX(), pacman.getY(), ghost.getX(), ghost.getY()) : 0;
	}

	private int getDirection(int x1, int y1, int x2, int y2) {

		float angle = getAngle(x1, y1, x2, y2);
		if (angle < 45) {
			return MazePacman.EAST;
		}
		else if (angle < 135) {
			return MazePacman.NORTH;
		}
		if (angle < 225 ) {
			return MazePacman.WEST;
		}
		else if (angle < 315){
			return MazePacman.SOUTH;
		}
		else {
			return MazePacman.EAST;
		}
	}

	private float getAngle(int x1, int y1, int x2, int y2) {
		float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

		if(angle < 0){
			angle += 360;
		}

		return angle;
	}



	@Override
	public boolean equals(Object o) {
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
