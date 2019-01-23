package pacman.environnementRL;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pacman.agent.GoPacmanAgent;
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
 * Environnement pour RL feature dans pacman, i.e. avec etatcourant = StateGamePacman
 * (pas besoin d'etat specifique issu de StateGamePacman comme pour tabulaire ...)
 * 
 */
public class EnvironnementPacmanFeatureRL extends EnvironnementPacmanRL {


	
	public EnvironnementPacmanFeatureRL(String _filename,boolean _isghostrandom)  {
		super(_filename,_isghostrandom);

		StateGamePacman gamestate;
		try {
		//	gamestate = new StateGamePacman(new MazePacman(filename));
		
			etatcourant = new StateGamePacman(new MazePacman(filename));
			//creation du jeu
			gamepacman=new GamePacman((StateGamePacman) etatcourant);
			//creation agent fantomes
			//for (int i=0; i<((StateGamePacman) etatcourant).getNumberOfGhosts(); i++){
				
			for (int i=0; i<((StateGamePacman) etatcourant).getNumberOfGhosts(); i++){
				if (_isghostrandom)
					gamepacman.addGhostAgent(new RandomPacmanAgent());
				else
					gamepacman.addGhostAgent(new GoPacmanAgent(Action2D.NONE));
				
			}
			
		} catch (MazeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		
	}



	@Override
	public void setEtatCourant(Etat e) {
		etatcourant = (StateGamePacman) e;
		
	}



	@Override
	public Etat doAction(Action _a) {
		//met a jour environnement apres action de agent pacman
		//appele par RLAgent::runEpisode
		int old_score = this.gamepacman.getState().getScore();
		/** In one step, move pacman or ghost and update score**/
		this.gamepacman.oneIteration(new ActionPacman(_a.ordinal()));;//bouge pacman avec _a puis fantomes
		
		this.setEtatCourant(gamepacman.getState());
		int new_score = this.gamepacman.getState().getScore();
		double rwd =new_score - old_score;
		
		//System.out.println("old_score: "+old_score+" new_score "+new_score);
		//System.out.println(this.etatmdpcourant);
		
		notifyObs(rwd);//envi notifie observateur=agent qui update, agent observateur cree dans constructeur de RLAgent 
		gamepacman.notifyObservers();//notify vue --> normalement fait dans onePacmanAction
		return gamepacman.getState();
	}

	@Override
	public void reset() {//reset seulement le labyrinthe et etat initial des agents : fantomes identiques
		try {
			etatcourant = new StateGamePacman(new MazePacman(filename));
			
			//creation du jeu
			gamepacman.setState((StateGamePacman) etatcourant);			
		} catch (MazeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	

}
