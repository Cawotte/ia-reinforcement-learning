package pacman.elements;

/**
 * Cette Classe decrit un agent pour le jeu de pacman(agent est un pacman ou fantome)
 * @author lmatignon
 *
 */

public interface AgentPacman 
{
	/**
	 * Permet de renvoyer l'action effectuee par l'agent dans un etat du jeu donne
	 * @param as l'etat qui correspond a l'agent (permet de trouver les coordonnees de l'agent)
	 * @param state l'etat courant du jeu
	 * @return l'action a choisir. 
	 */
	public ActionPacman getAction(StateAgentPacman as,StateGamePacman state); 
	
}
