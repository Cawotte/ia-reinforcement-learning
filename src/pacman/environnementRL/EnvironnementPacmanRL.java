package pacman.environnementRL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pacman.agent.RandomPacmanAgent;
import pacman.elements.ActionPacman;
import pacman.elements.MazeException;
import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.game.GamePacman;
import environnement.Action;
import environnement.Action2D;
import environnement.Environnement;
import environnement.Etat;


/**
 * Classe abstraite avec methode pour Environnement type pacman utilise par RLAgent
 * 
 */
public abstract class EnvironnementPacmanRL extends Environnement {
	/** jeu de pacman : on utilise les fantomes mais pas les agents pacman*/
	protected GamePacman gamepacman;
	/** Etat courant de environnement*/
	protected Etat etatcourant;
	/** Pour reset le jeu, memorise le fichier du labyrinthe*/
	protected String filename;
	protected boolean isghostrandom;
	
	private static int NBACTIONS = 4;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec FeatureFunctionPacman


	/**
	 * Instancie methodes communes a environnement de type pacman, pour etre utilisable par AgentRL 
	 * <li> attribut etat complet du jeu pour MDP, avec des fantomes random
     * <li> le jeu de pacman
	 * @param filename
	 * @param _isghostrandom pour agent fantomes aleatoires, sinon sont static
	 * @throws MazeException
	 */
	public EnvironnementPacmanRL(String _filename,boolean _isghostrandom)  {
		filename = _filename;
		isghostrandom = _isghostrandom;
		
	}

	@Override
	public Etat getEtatCourant() {
		//return etatcourant;//semble fonctionner maintenant
		return (Etat) etatcourant.clone();
	}

	public GamePacman getGamepacman() {
		return gamepacman;
	}


	//--> utilise par RLAgent::getActionsLegales
	@Override
	public List<Action> getActionsPossibles(Etat _e) {
		//renvoi actions qui n'envoient pas dans mur pour _e: action pour unique pacman
				List<Action> aa=new ArrayList<Action>();
					for (int i=0; i<NBACTIONS; i++){//prend en compte action NONE
						StateGamePacman sgp = gamepacman.getState();
						StateAgentPacman sap = gamepacman.getState().getPacmanState(0);
						sgp.isLegalMove(new ActionPacman(i), sap);
						if (gamepacman.getState().isLegalMove(new ActionPacman(i), gamepacman.getState().getPacmanState(0)))
							aa.add(Action2D.values()[i]);
				}
				return aa;
	}
	

	@Override
	public Set<Etat> getEtatSuccesseurs(Etat _e, Action _a) {
		// TODO Auto-generated method stub
		return null;//utile que pour la recherche donc pas a faire ici
	}


	@Override
	public boolean estAbsorbant() {
		return (gamepacman.getState().isLose()) || (gamepacman.getState().isWin());
	}

}
