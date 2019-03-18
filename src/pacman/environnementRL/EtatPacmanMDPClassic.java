package pacman.environnementRL;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import environnement.Etat;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	protected int hash;
	protected StateGamePacman stateGame;
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){

		stateGame = _stategamepacman;
		/*
		StateAgentPacman pacman = _stategamepacman.getPacmanState(0);
		StateAgentPacman ghost = _stategamepacman.getGhostState(0);

		hash = 0;
		hash += pacman.getX();
		hash += pacman.getY() * 10;
		hash += ghost.getX() * 10;
		hash += ghost.getY() * 10;*/
	}

	@Override
	public int hashCode() {

		StateAgentPacman pacman = stateGame.getPacmanState(0);
		StateAgentPacman ghost = stateGame.getGhostState(0);
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pacman == null) ? 0 : pacman.hashCode());
		result = prime * result
				+ ((ghost == null) ? 0 : ghost.hashCode());
		return result;
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
