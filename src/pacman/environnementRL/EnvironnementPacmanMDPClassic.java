package pacman.environnementRL;


import pacman.agent.GoPacmanAgent;
import pacman.agent.RandomPacmanAgent;
import pacman.elements.ActionPacman;
import pacman.elements.AgentPacman;
import pacman.elements.MazeException;
import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.game.GamePacman;
import environnement.*;


/**
 * Environnement pour RL classic dans pacman, i.e. avec etatcourant = etat complet du MDP (EtatPacmanMDPClassic): 
 * 
 * 
 */
public class EnvironnementPacmanMDPClassic extends EnvironnementPacmanRL {

	
	public EnvironnementPacmanMDPClassic(String _filename,boolean _isghostrandom)  {
		super(_filename,_isghostrandom);

		StateGamePacman gamestate;
		try {
			gamestate = new StateGamePacman(new MazePacman(filename));
			//creation du jeu
			gamepacman=new GamePacman(gamestate);
			//creation agent fantomes
			for (int i=0; i<gamestate.getNumberOfGhosts(); i++){
				if (_isghostrandom)
					gamepacman.addGhostAgent(new RandomPacmanAgent());
				else
					gamepacman.addGhostAgent(new GoPacmanAgent(Action2D.NONE));
				
			}
			
			this.etatcourant = new EtatPacmanMDPClassic(gamepacman.getState());
		} catch (MazeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		
	}

	


	@Override
	public void setEtatCourant(Etat e) {
		this.etatcourant = (EtatPacmanMDPClassic) e;
		
	}



	@Override
	public Etat doAction(Action _a) {
		//met a jour environnement apres action de agent pacman
		//appele par RLAgent::runEpisode
		int old_score = this.gamepacman.getState().getScore();
		/** In one step, move pacman or ghost and update score**/
		this.gamepacman.oneIteration(new ActionPacman(_a.ordinal()));;//bouge pacman avec _a puis fantomes
		
		EtatPacmanMDPClassic emdp = new EtatPacmanMDPClassic(gamepacman.getState());
		this.setEtatCourant(emdp);
		int new_score = this.gamepacman.getState().getScore();
		double rwd =new_score - old_score;
		
		//System.out.println("old_score: "+old_score+" new_score "+new_score);
		//System.out.println(this.etatmdpcourant);
		
		notifyObs(rwd);//envi notifie observateur=agent qui update, agent observateur cree dans constructeur de RLAgent !
		gamepacman.notifyObservers();//notify vue --> normalement fait dans onePacmanAction
		return emdp;
	}

	@Override
	public void reset() {//reset seulement le labyrinthe et etat initial des agents : fantomes identiques
		StateGamePacman gamestate;
		try {
			gamestate = new StateGamePacman(new MazePacman(filename));
			
			//creation du jeu
			gamepacman.setState(gamestate);			
			this.etatcourant = new EtatPacmanMDPClassic(gamepacman.getState());
		} catch (MazeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


}
