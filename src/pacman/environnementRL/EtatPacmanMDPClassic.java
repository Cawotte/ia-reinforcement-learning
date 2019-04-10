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
		/*
		pac_x = pacman.getX();
		pac_y = pacman.getY();
		ghost_x = _stategamepacman.getGhostState(0).getX();
		ghost_y = _stategamepacman.getGhostState(0).getY(); */

		foodLeft = StateGameFunctions.getFoodLeft(_stategamepacman);
		foodEaten = _stategamepacman.getCapsulesEaten();

		distanceDot = _stategamepacman.getClosestDot(pacman);

		StateAgentPacman closestGhost = StateGameFunctions.getClosestGhost(_stategamepacman, 3);

		//directionGhost = StateGameFunctions.getDirectionClosestGhost(_stategamepacman);

        //good hash
        directionDot = StateGameFunctions.getDirectionNextDot(_stategamepacman);
        directionGhost = StateGameFunctions.getDirectionClosestGhost(_stategamepacman, 4);
        distanceGhost = StateGameFunctions.getDistance(pacman, closestGhost, 4);

        //previously bad hash?
        /*
        distanceDot = _stategamepacman.getClosestDot(pacman);
        directionGhost = StateGameFunctions.getDirection(pacman, closestGhost);
		distanceGhost = StateGameFunctions.getDistance(pacman, closestGhost); */

		//Coordinate ghost
		//ghostHash = StateGameFunctions.getGhostPosHash(_stategamepacman);
	}

	@Override
	public int hashCode() {

        //return Objects.hash(distanceDot, ghost_x, ghost_y);

        //was bad
        //return Objects.hash(distanceDot, distanceGhost, directionGhost);

        return Objects.hash(directionDot, distanceGhost, directionGhost);


		//return Objects.hash(distanceDot, ghostHash, foodLeft);
		//return Objects.hash(distanceDot, ghostHash);

		//return Objects.hash(pac_x, pac_y, distanceGhost, directionGhost);
		//return Objects.hash(distanceDot, distanceGhost, directionGhost);

		//return Objects.hash(distanceDot, ghostHash, foodLeft);
		//return Objects.hash(distanceDot, ghost_x, ghost_y);

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
