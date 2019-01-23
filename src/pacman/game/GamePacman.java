package pacman.game;

import pacman.elements.AgentPacman;
import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

import java.util.ArrayList;
import java.util.Observable;
/**
 * Gestion de la boucle de jeu en tour par tour 

 * <li> un step = un tour: bouge tous les pacmans OU tous les fantomes
 * <li> une iter = 2 tours: bouge pacmans puis fantomes
 * <li> peut choisir des {@link AgentPacman} qui executeront automatiquement leurs actions,
 *  ou specifier a chaque iter l'action du pacman {@link #oneIteration(ActionPacman)}
 * 
 * @author lmatignon
 *
 */
public class GamePacman extends Observable//extends TimerTask
{
	protected StateGamePacman state;
	protected ArrayList<AgentPacman> pacmansAgents;
	protected ArrayList<AgentPacman> ghostsAgents;
	//protected ArrayList<GameObserver> observers;
	private boolean isPacmanTurn=true;//si vrai, bouge les pacman; si faux, bouge les ghosts: tour par tour

	/** pause entre chaque step=tour en ms*/
	private long step_delay;

	/** Constructeur avec etat du jeu initial et aucun agents*/
	public GamePacman(StateGamePacman state)
	{
		this.state=state;	
		pacmansAgents=new ArrayList<AgentPacman>();
		ghostsAgents=new ArrayList<AgentPacman>();
		//observers=new ArrayList<GameObserver>();
		step_delay = 0;
	}
	
	
	
	public long getStep_delay() {
		return step_delay;
	}



	public void setStep_delay(long time_delay) {
		this.step_delay = time_delay;
	}



	public void setState(StateGamePacman state) {
		this.state = state;
	}



	public void addPacmanAgent(AgentPacman a)
	{
		pacmansAgents.add(a);
	}
	
	public void addGhostAgent(AgentPacman a)
	{
		ghostsAgents.add(a);
	}
	
	/*public void addObserver(GameObserver o)
	{
		observers.add(o);
	}*/
	/**
	 * Un tour de jeu, bouge tous les pacmans ou tous les fantomes,  compute scores
	 * update Observer avec game state 
	 */
	public void oneStep(){
		ArrayList<ActionPacman> pactions=new ArrayList<ActionPacman>();
		ArrayList<ActionPacman> gactions=new ArrayList<ActionPacman>();
		
		if (isPacmanTurn)
		{
			for(int i=0;i<state.getNumberOfPacmans();i++)
			{
				AgentPacman a=pacmansAgents.get(i);
				StateAgentPacman ss=state.getPacmanState(i);
				pactions.add(a.getAction(ss,state));				
			}
			//Bouge chaque pacman, met a jour le labyrinthe: food mange, fantome mange, score, ... 
			//decrement pacman scared timer, detecte si gagne ou perd.
			state.updatePacmans(pactions);//state=state.nextStatePacmans(pactions);
			if (state.getNumberOfGhosts()!=0) isPacmanTurn=false;
		}
		else
		{
			for(int i=0;i<state.getNumberOfGhosts();i++)
			{
				AgentPacman a=ghostsAgents.get(i);
				StateAgentPacman ss=state.getGhostState(i);
			//	AgentAction aa=a.getAction(ss, state);
				gactions.add(a.getAction(ss,state));				
			}			
			
			state.updateGhosts(gactions);//ajoute score win or loose
			isPacmanTurn=true;
		}
		
		//for(GameObserver o:observers)
		//	o.update(state);
		this.setChanged();
		this.notifyObservers();//vue
		
		try {
			Thread.sleep(step_delay);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		state.setScore(state.getScore()+StateGamePacman.STEP);
		
		
	}
	
	/**
	 * Execute 2 steps: une action de pacman,  puis tous les fantomes,  
	 * calcule score,
	 * <li> update Observer avec game state 
	 * <li> fontionne pour un unique pacman
	 */
	public void oneIteration(ActionPacman _a){//utilise par EnviPacmanMDP
		//un seul pacman
		if (state.getNumberOfPacmans()!=1)
			return;			
		
		//Bouge chaque pacman, met a jour le labyrinthe: food mange, fantome mange, score, ... 
		//decrement pacman scared timer, detecte si gagne ou perd.
		isPacmanTurn =true;
		//StateAgentPacman ssp=state.getPacmanState(0);
		state.updatePacman(_a);
		
		//for(GameObserver o:observers)
				//	o.update(state);
		
		state.setScore(state.getScore()+StateGamePacman.STEP);

		this.setChanged();
		this.notifyObservers();//vue
		
		//bouge les fantomes
		//if (state.getNumberOfGhosts()!=0) 
			isPacmanTurn=false;
		this.oneStep();
	}
	
	
	/**
	 * Boucle de jeu, tour par tour: tous les pacmans du jeu bougent, puis tous les fantomes, ...
	 * @return
	 */
	public StateGamePacman runUntilEnd()
	{
		assert((pacmansAgents.size()==state.getNumberOfPacmans()));
		assert((ghostsAgents.size()==state.getNumberOfGhosts()));
		
		try {
			Thread.sleep(step_delay);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}		
		 
		isPacmanTurn=true;//si vrai, bouge les pacman; si faux, bouge les ghosts: tour par tour
		
	//	new Timer().scheduleAtFixedRate(this, 0, time_delay);//ne change pas l'affichage a fqc pas constante
		
		while((state.isLose()==false) && (state.isWin()==false))
		{
			this.oneStep();
		}
		//fait dans updateGhosts
	//	if (state.isLose()==true) state.setScore(state.getScore()+StateGamePacman.LOOSE);
	//	if (state.isWin()==true)  state.setScore(state.getScore()+StateGamePacman.WIN);
		//for(GameObserver o:observers)//update score dans affichage
		//	o.update(state);
		this.setChanged();
		this.notifyObservers();//vue, update score dans affichage
		
		return(state);
	}

	/**
	 * Boucle de jeu, tour par tour: tous les pacmans bougent, puis tous les ghosts, ...
	 * update Observer avec game state a la fin de chq tour
	 * @param nb_turn_max nb de tour max 
	 * @return
	 */
	public StateGamePacman runUntilEnd( int nb_turn_max) {
		assert((pacmansAgents.size()==state.getNumberOfPacmans()));
		assert((ghostsAgents.size()==state.getNumberOfGhosts()));
		
		try {
			Thread.sleep(step_delay);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}		
		
		isPacmanTurn=true;
		int nb_turn=0;
		while((state.isLose()==false) && (state.isWin()==false) && (nb_turn<nb_turn_max))
		{
			this.oneStep();
			nb_turn++;//LAETI modif
		
		}
		//fait dans updateGhosts
//		if (state.isLose()==true) state.setScore(state.getScore()+StateGamePacman.LOOSE);
	//	if (state.isWin()==true)  state.setScore(state.getScore()+StateGamePacman.WIN);
		//for(GameObserver o:observers)//update score dans affichage
				//	o.update(state);
		this.setChanged();
		this.notifyObservers();//vue, update score dans affichage
		return(state);
	}

	public StateGamePacman getState() {
		return state;
	}
	
	public boolean isLose(){
		return this.state.isLose();
	}

	public boolean isWin(){
		return this.state.isWin();
	}

	//pour utiliser TimerTask
/*	@Override
	public void run() {
		if ((state.isLose()==false) && (state.isWin()==false))
			this.oneStep(long time_delay);
		else
			this.cancel();
	}*/
	

}
