package pacman.elements;

import java.util.ArrayList;
import java.util.List;

import environnement.Etat;
import javafx.util.Pair;



/**
 * Cette classe decrit un etat complet du jeu pacman (non modifiable)
 *  et  implemente les regles du jeu (agents bougent, mise a jour labyrinthe, scarred, score, ...).
 * <p>
 * <li> labyrinthe {@link MazePacman}
 * <li>  etats des differents agents {@link StateAgentPacman}
 * <li>  nombre de capsules/dot/fantomes manges
 * <li>  etat de la partie: score, perdu, gagne, ...
 * <li> step : incremente lorsque les fantomes ou les pacmans bougent (tour par tour: un tour par type d'agents)
 * <li> parametres pour calcul du score: point gagne si DOTEATEN, GHOSTEATEN, WIN, LOOSE, STEP...
 * <li> parametres du jeu: TIMEPACMANSCARRED, TIMEGHOSTSCARRED
 * 
* <p>
 * Les regles du jeu:
 * <li> gagne si mange toute les capsules, perd si tous les pacmans mort
 * <li> si pacman et fantome dans meme case: pacman meurt
 * <li> si pacman mange capsule, il est scarred
 * <li> si pacman scared touche fantome: differentes regles possibles, choix dans {@link #updateMaze}
 * <p>
 * V1 ( {@link #updateMaze1}):
 * <li> si pacman scarred touche un fantome, fantome devient scared 
 * <li> si un fantome scarred touche le pacman, il ne le tue pas 
 * <p>
 * V2 ({@link #updateMaze2}):
 * <li> si pacman scarred touche un fantome, fantome revient a sa pose initiale
 * 
 * @author lmatignon
 *
 */
public class StateGamePacman implements Etat//TODO verifier hashcode, equals et clone
{
	public static int TIMEPACMANSCARRED=20;
	public static int TIMEGHOSTSCARRED=20;
	/** points a ajouter au score*/
	public static int DOTEATEN=10;
	public static int GHOSTEATEN=200;
	public static int BIGDOTEATEN=0;
	public static int WIN=500;
	public static int LOOSE=-500;
	public static int STEP=-1;
	
	protected MazePacman maze;
	protected ArrayList<StateAgentPacman> pacmans_states;
	protected ArrayList<StateAgentPacman> ghosts_states;
	protected int foodEaten;
	protected int capsulesEaten;
	protected int ghostsEaten;
	protected boolean win;
	protected boolean lose;
	/** incremente a chaque tour, un tour est le deplacement des pacmans OU des fantomes*/
	protected int step;
	protected int score;
	
	/**
	 * Contructeur d'un etat de jeu initial, à partir d'un labyrinthe
	 * <li> init etats des agents {@link StateAgentPacman} a partir du labyrinthe 
	 * @param maze  {@link MazePacman}
	 */
	public StateGamePacman(MazePacman maze)
	{
		this.maze=maze;
		foodEaten=0;
		capsulesEaten=0;
		ghostsEaten=0;
		win=false;
		lose=false;
		step=0;
		score=0;
		
		pacmans_states=new ArrayList<StateAgentPacman>();
		ghosts_states=new ArrayList<StateAgentPacman>();
		
		for(int i=0;i<maze.getNumberOfPacmans();i++)
		{
			StateAgentPacman a=new StateAgentPacman(maze.getPacmanStartX(i),maze.getPacmanStartY(i));
			pacmans_states.add(a);
		}

		for(int i=0;i<maze.getNumberOfGhosts();i++)
		{
			StateAgentPacman a=new StateAgentPacman(maze.getGhostStartX(i),maze.getGhostStartY(i));
			ghosts_states.add(a);
		}
	}
	
	
	
	///////////// MISE A JOUR DU JEU SELON ACTIONS AGENTS /////////////////
	/** Appele {@link #updatePacmans}  avec une seule action */
	public void updatePacman(ActionPacman pacmanAction)
	{
		ArrayList<ActionPacman> p=new ArrayList<ActionPacman>();
		p.add(pacmanAction);		
		updatePacmans(p);
	}
	/**
	 * Bouge chaque pacman selon les actions en parametre, met a jour le labyrinthe (inclut calcul du score), 
	 * decremente pacman scared timer, detecte si gagne ou perd, increment time
	 * @param pacmansActions
	 */
	public void updatePacmans(ArrayList<ActionPacman> pacmansActions)
	{
		assert(getNumberOfPacmans()==pacmansActions.size());
		for(int i=0;i<pacmansActions.size();i++)
		{
			movePacman(i,pacmansActions.get(i));
		}
		
		updateMaze();
		
		//Win ? 
		boolean b=true;
		for(int x=0;x<maze.getSizeX();x++)
			for(int y=0;y<maze.getSizeY();y++)
			{
				if ((maze.isFood(x, y)) || (maze.isCapsule(x, y)))
					b=false;
			}
		win=b;

		for(int i=0;i<pacmansActions.size();i++)
		{
			if (pacmans_states.get(i).isScarred())
			{
				pacmans_states.get(i).setScarredTimer(pacmans_states.get(i).getScarredTimer()-1);
			}
		}
		step++;	

		//Lose ? 
		b=true;
		for(int i=0;i<getNumberOfPacmans();i++)
			if (!pacmans_states.get(i).isDead()) b=false;
		lose=b;	
		

		
	}
	/** Appele {@link #updateGhosts}  avec une seule action */
	public void updateGhost(ActionPacman ga)
	{
		ArrayList<ActionPacman> p=new ArrayList<ActionPacman>();
		p.add(ga);		
		updateGhosts(p);
	}
	
	/**
	 * Bouge chaque fantome selon les actions en parametre, met a jour le labyrinthe (inclut calcul du score), 
	 * decremente fantome scared timer, detecte si gagne ou perd, increment time
	 * @param pacmansActions
	 */
	public void updateGhosts(ArrayList<ActionPacman> ghostsActions)
	{
		assert(getNumberOfGhosts()==ghostsActions.size());
		

		for(int i=0;i<ghostsActions.size();i++)
		{
			moveGhost(i,ghostsActions.get(i));
		}		
		updateMaze();
		
		
		//Win ? 
		boolean b=true;
		for(int x=0;x<maze.getSizeX();x++)
			for(int y=0;y<maze.getSizeY();y++)
			{
				if ((maze.isFood(x, y)) || (maze.isCapsule(x, y)))
					b=false;
			}
		win=b;

		for(int i=0;i<ghostsActions.size();i++)
		{
			if (ghosts_states.get(i).isScarred())
			{
				ghosts_states.get(i).setScarredTimer(ghosts_states.get(i).getScarredTimer()-1);
			}
		}


		//Lose ? 
		b=true;
		for(int i=0;i<getNumberOfPacmans();i++)
			if (!pacmans_states.get(i).isDead()) b=false;
		lose=b;
		
		step++;
		
		if (this.isLose()==true) {
			score+=StateGamePacman.LOOSE;
			return;//si pacman mange dernier dot et fantome le mange au meme moment, considere que LOOSE (et pas LOOSE+WIN=score de 0)
		}
		if (this.isWin()==true) 
			score+=StateGamePacman.WIN;
		
	}
	
	////////////// RETOURNE ETAT PROCHAIN DU JEU (ETAT ACTUEL NON MODIFIE) //////
	/**
	 * Renvoie l'etat prochain du jeu a partir d'une action de pacman (etat actuel du jeu non modifie)
	 * (on considere ici qu'il n'y a qu'un seul pacman)
	 * <p> appele {@link #updatePacmans}
	 * @param pacmanAction
	 * @return
	 */
	 public StateGamePacman nextStatePacman(ActionPacman pacmanAction)
	 {
		StateGamePacman gw=(StateGamePacman)(copy());
		gw.updatePacman(pacmanAction);
		return(gw);
	 }
		/**
		 * Renvoie l'etat prochain du jeu a partir des actions de plusieurs pacman (etat actuel du jeu non modifie)
		 *  <p> appele {@link #updatePacmans}
		 * @param pacmanActions
		 * @return
		 */
		public StateGamePacman nextStatePacmans(ArrayList<ActionPacman> pacmansActions)
		{
			StateGamePacman gw=(StateGamePacman)(copy());
			gw.updatePacmans(pacmansActions);
			return(gw);
		}
		/**
		 * Renvoie l'etat prochain du jeu a partir d'une action d'un fantome (etat actuel du jeu non modifie)
		 * (on considere ici que seul le premier fantome bouge)
		 * <p> appele {@link #updateGhosts}
		 * @param ghostsAction
		 * @return
		 */
		public StateGamePacman nextStateGhost(ActionPacman ghostsAction)
		{
			StateGamePacman gw=(StateGamePacman)(copy());
			gw.updateGhost(ghostsAction);
			return(gw);
		}
		/**
		 * Renvoie l'etat prochain du jeu a partir des actions de plusieurs fantomes (etat actuel du jeu non modifie)
		 * <p> appele {@link #updateGhosts}
		 * @param ghostsActions
		 * @return
		 */
		public StateGamePacman nextStateGhosts(ArrayList<ActionPacman> ghostsActions)
		{
			StateGamePacman gw=(StateGamePacman)(copy());
			gw.updateGhosts(ghostsActions);
			//fait dans boucle de jeu runUntilEnd, de plus, non utilise par EnviPacmanMDP...
			//if (this.isLose()==true) score+=StateGamePacman.LOOSE;
			//if (this.isWin()==true) score+=StateGamePacman.WIN;
			
			return(gw);
		}		
	
	
	///////////// DEPLACEMENT AGENT /////
	/**
	 * Renvoi le nouvel etat du fantome i s'il fait une action {@link StateAgentPacman}
	 * <p> Simu: ne modifie pas l'etat du fantome
	 * @param i l'indice du fantome a bouger
	 * @param action
	 */
	public StateAgentPacman moveGhostSimu(int i,ActionPacman action)
	{   
		
		StateAgentPacman sghostini=ghosts_states.get(i);
		StateAgentPacman s = sghostini.copy();
		int x=s.getX();
		int y=s.getY();
		if (maze.isWall(x+action.getX(), y+action.getY()))
		{
			//System.out.println("Ghost "+i+" runs into wall");
			s.setLastX(x);
			s.setLastY(y);
		}
		else
		{
			s.setX(x+action.getX());
			s.setY(y+action.getY());
			if (action.getDirection()!=MazePacman.STOP) s.setDirection(action.getDirection());
			s.setLastX(x);
			s.setLastY(y);			
		}
		return s;
	}	
	/**
	 * Renvoi le nouvel etat du pacman i s'il fait une action {@link StateAgentPacman}
	 * <p> Simu: ne modifie pas l'etat du pacman
	 * @param i l'indice du pacman a bouger
	 * @param action
	 */
	public StateAgentPacman movePacmanSimu(int i,ActionPacman action)
	{
		StateAgentPacman spacmaninit=pacmans_states.get(i);
		StateAgentPacman s = spacmaninit.copy();
		int x=s.getX();
		int y=s.getY();
		if (maze.isWall(x+action.getX(), y+action.getY()))
		{			
			//System.out.println("Pacman "+i+" runs into wall");
		}
		else if (pacmans_states.get(i).isDead())
		{		
			s.setLastX(x);
			s.setLastY(y);
		}
		else
		{
			s.setX(x+action.getX());
			s.setY(y+action.getY());
			if (action.getDirection()!=MazePacman.STOP) s.setDirection(action.getDirection());
			s.setLastX(x);
			s.setLastY(y);
		}
		return s;
	}
	/**
	 * Modifie l'etat du pacman i en fonction de l'action realisee  {@link StateAgentPacman}
	 * <p> verifie uniquement les murs
	 * @param i l'indice du pacman a bouger
	 * @param action
	 */
	protected void movePacman(int i,ActionPacman action)
	{
		StateAgentPacman s=pacmans_states.get(i);
		int x=s.getX();
		int y=s.getY();
		if (maze.isWall(x+action.getX(), y+action.getY()))
		{			
			//System.out.println("Pacman "+i+" runs into wall");
		}
		else if (pacmans_states.get(i).isDead())
		{		
			s.setLastX(x);
			s.setLastY(y);
		}
		else
		{
			s.setX(x+action.getX());
			s.setY(y+action.getY());
			if (action.getDirection()!=MazePacman.STOP) s.setDirection(action.getDirection());
			s.setLastX(x);
			s.setLastY(y);
		}
	}
	/**
	 * Modifie l'etat du fantome i en fonction de l'action realisee {@link StateAgentPacman}
	 * <p> verifie uniquement les murs
	 * @param i l'indice du ghost a bouger
	 * @param action
	 */
	protected void moveGhost(int i,ActionPacman action)
	{
		StateAgentPacman s=ghosts_states.get(i);
		int x=s.getX();
		int y=s.getY();
		if (maze.isWall(x+action.getX(), y+action.getY()))
		{
			//System.out.println("Ghost "+i+" runs into wall");
			s.setLastX(x);
			s.setLastY(y);
		}
		else
		{
			s.setX(x+action.getX());
			s.setY(y+action.getY());
			if (action.getDirection()!=MazePacman.STOP) s.setDirection(action.getDirection());
			s.setLastX(x);
			s.setLastY(y);			
		}
	}	
	
	////////// REGLES JEU /////////
	/**
	 * 
	 * Met a jour le maze selon les etats des agents (apres qu'ils aient bouge) + calcul du score
	 * <p> Choix des regles :
	 * <li> si pacman sur food : mange
	 * <li> si pacman sur capsule : mange et devient scarred
	 * <li> si pacman  et fantome dans meme case: pacman meurt
	 * <li> si pacman scarred et fantome dans meme case: ?
	 * <li> fantome peut etre scarred ?
	 */
	protected void updateMaze()
	{
		//this.updateMaze1();
		this.updateMaze2();		
	}
	/**
	 * 
	 * Choix des regles pour mettre a jour le maze selon etats des agents + calcul du score
	 * (si pacman scarred touche ghost, ?)
	 * <li> si pacman sur food : mange
	 * <li> si pacman sur capsule : mange et devient scarred
	 * <li> si pacman  et fantome dans meme case: pacman meurt
	 * <li> si pacman scarred et fantome dans meme case: ghost attrape revient a sa pose initiale
	 * <li> pas de ghost scarred
	 */
	protected void updateMaze2()
	{
		for(int i=0;i<getNumberOfPacmans();i++)
		{
			StateAgentPacman s=pacmans_states.get(i);
			int x=s.getX();
			int y=s.getY();
			if (maze.isFood(x, y))
			{
				maze.setFood(x, y, false);
				score+=StateGamePacman.DOTEATEN;
				foodEaten++;
			}
			if (maze.isCapsule(x, y))
			{
				maze.setCapsule(x, y, false);
				score+=StateGamePacman.BIGDOTEATEN;
				capsulesEaten++;
				s.setScarredTimer(StateGamePacman.TIMEPACMANSCARRED);
			}
			
			for(int j=0;j<getNumberOfGhosts();j++)
			{
				StateAgentPacman gs=ghosts_states.get(j);
				int gx=gs.getX();
				int gy=gs.getY();
				if ((gx==x) && (gy==y))//ghost et pacman dans meme case
				{
					if (!gs.isScarred())//normalement ghost jamais scarred dans cette version
					{
						if (s.isScarred())//pacman scared touche ghost: ghost reinit
						{
							score+=StateGamePacman.GHOSTEATEN;
							gs.setX(gs.getStartX());
							gs.setY(gs.getStartY());
							ghostsEaten++;
						}
						else
						{
							s.setDead(true);
						}
					}					
				}
			}					
		}
	}
	
	/** 
	 * Choix des regles pour mettre a jour le maze selon etats des agents + calcul du score
	 * (si pacman scarred touche ghost, ?)
	 * <li> si pacman sur food : mange
	 * <li> si pacman sur capsule : mange et devient scarred
	 * <li> si pacman  et fantome dans meme case: pacman meurt
	 * <li> si pacman scarred et fantome dans meme case: ghost devient scarred (ne peut pas tuer pacman)
	 */
	protected void updateMaze1()
	{
		for(int i=0;i<getNumberOfPacmans();i++)
		{
			StateAgentPacman s=pacmans_states.get(i);
			int x=s.getX();
			int y=s.getY();
			if (maze.isFood(x, y))
			{
				maze.setFood(x, y, false);
				score+=StateGamePacman.DOTEATEN;
				foodEaten++;
			}
			if (maze.isCapsule(x, y))
			{
				maze.setCapsule(x, y, false);
				score+=StateGamePacman.BIGDOTEATEN;
				capsulesEaten++;
				s.setScarredTimer(StateGamePacman.TIMEPACMANSCARRED);
			}
			
			for(int j=0;j<getNumberOfGhosts();j++)
			{
				StateAgentPacman gs=ghosts_states.get(j);
				int gx=gs.getX();
				int gy=gs.getY();
				if ((gx==x) && (gy==y))
				{
					if (!gs.isScarred())
					{
						if (s.isScarred())
						{
							gs.setScarredTimer(StateGamePacman.TIMEGHOSTSCARRED);
							score+=StateGamePacman.GHOSTEATEN;
							ghostsEaten++;
						}
						else
						{
							s.setDead(true);
						}
					}					
				}
			}					
		}
	}
	
	
	///////////// SETTER //////////////
	/**
	 * Permet de copier l'etat du jeu (cf. aussi clone!)
	 * @return
	 */
	public StateGamePacman copy()
	{
		StateGamePacman g=new StateGamePacman(maze.copy());

		g.setFoodEaten(foodEaten);
		g.setCapsulesEaten(capsulesEaten);
		g.setGhostsEaten(ghostsEaten);
		g.setWin(win);
		g.setLose(lose);	
		g.setStep(step);
		g.setScore(score);
		g.initPacmansStates();
		for(int i=0;i<pacmans_states.size();i++)
			g.addPacmanState(pacmans_states.get(i).copy());

		g.initGhostsStates();
		for(int i=0;i<ghosts_states.size();i++)
			g.addGhostState(ghosts_states.get(i).copy());
		return g;	
	}
	
	public void setFoodEaten(int foodEaten) { 		this.foodEaten = foodEaten;	}
	public void setCapsulesEaten(int capsulesEaten) {		this.capsulesEaten = capsulesEaten;	}
	public void setGhostsEaten(int ghostsEaten) {		this.ghostsEaten = ghostsEaten;	}
	public void setWin(boolean win) {		this.win = win;	}
	public void setLose(boolean lose) {		this.lose = lose;	}
	public void addPacmanState(StateAgentPacman pacmans_state) {		this.pacmans_states.add( pacmans_state);	}
	public void addGhostState(StateAgentPacman ghosts_state) {		this.ghosts_states.add(ghosts_state);	}
	public void initPacmansStates() {pacmans_states=new ArrayList<StateAgentPacman>();}
	public void initGhostsStates() {ghosts_states=new ArrayList<StateAgentPacman>();}
	public void setStep(int t) {step=t;}

	
	
	////////////// GETTER /////////////////
	/**
	 * @return le labyrinthe du jeu
	 */
	public MazePacman getMaze() {return(maze);}
	
	/**
	 * @return le temps ecoule depuis le debut de la partie
	 * <li> time s'incremente lorsque les fantomes ou les pacmans bougent (tour par tour: un tour par type d'agents)
	 */
	public int getStep() {return(step);}

	/**
	 * @return le nombre de pacmans du jeu (y compris les morts)
	 * 
	 */
	public int getNumberOfPacmans() {return(pacmans_states.size());}
	
	/**
	 * @return le nombre de fantomes
	 * 
	 */
	public int getNumberOfGhosts() {return(ghosts_states.size());}
	
	/**
	 * @param i le numero du pacman
	 * @return  l'etat du pacman numero i {@link StateAgentPacman}
	 */
	public StateAgentPacman getPacmanState(int i) {return(pacmans_states.get(i));}
	
	/**
	 * @param i le numero du fantome
	 * @return l'etat du fantome i {@link StateAgentPacman}
	 */
	public StateAgentPacman getGhostState(int i) {return(ghosts_states.get(i));}
	
	/**
	 * @return le nombre de nourriture mangee
	 */
	public int getFoodEaten() {	return foodEaten; }
	
	/**
	 * @return le score
	 * 
	 */
	public int getScore() {	return score; }
	
	public void setScore(int s) {score=s;}
	
	/**
	 * @return le nombre de capsules mangees
	 * 
	 */
	public int getCapsulesEaten() {	return capsulesEaten;}
	
	/**
	 * @return le nombre de fantomes manges
	 * 
	 */
	public int getGhostsEaten() {	return ghostsEaten;	}
	
	/**
	 * @return si l'on a gagne la partie
	 * 
	 */
	public boolean isWin() { return win;}
	
	/**
	 * @return si l'on a perdu
	 * 
	 */
	public boolean isLose() { return lose; }
	
	/**
	 * Renvoie true si l'action est valable (n'envoie pas dans un mur)
	 * @param action
	 * @param state
	 * @return
	 */
	public boolean isLegalMove(ActionPacman action,StateAgentPacman state)
	{
		int x=action.getX();
		int y=action.getY();
		if (maze.isWall(state.getX()+x,state.getY()+y)) return(false);
		return(true);
	}
	
	/**
	 *  
	 * @param action action a simuler
	 * @param state etat depuis lequel on simule action
	 * @return la coordonnee atteinte si action dans state (si action envoie  dans un mur, renvoie coord correspondanr a state)
	 */
	public int[] getNextPosition(ActionPacman action,StateAgentPacman state)
	{
		int[] new_s = new int[2];
		if (!isLegalMove(action,state)) {//si mur reste sur place
			new_s[0] = state.getX();
			new_s[1] = state.getY();
			return(new_s);
		}
		else{
			int x=action.getX();
			int y=action.getY();
			new_s[0] = state.getX()+x;
			new_s[1] = state.getY()+y;
			return(new_s);
		}

	}
	/**
	 * 
	 * @param state
	 * @return distance (en nb de cellules) entre coord X,Y de state et plus proche dot/food
	 */
	public int getClosestDot(StateAgentPacman state){
		//list des coord X,Y a tester: init avec coord de depart
		List<Pair<Integer,Integer>> lcoord = new ArrayList<Pair<Integer,Integer>>();
		lcoord.add(new Pair<Integer,Integer>(state.getX(),state.getY()));

		//liste des distances depuis state de chaque paire de lcoord : init avec dist=0 pour coord de depart
		List<Integer> ldist = new ArrayList<Integer>();
		ldist.add(0);		
		//liste des coord testes
		List<Pair<Integer,Integer>> lexpanded = new ArrayList<Pair<Integer,Integer>>();
		
		//evalue chq element de lcoord, si pas de food dans element, etend en regardant position atteignable depuis elemnt.
		while (!lcoord.isEmpty()){
			Pair<Integer,Integer> pair = lcoord.remove(0);
			int dist = ldist.remove(0);
			if (lexpanded.contains(pair)){
				continue;
			}
			lexpanded.add(pair);
			if (this.getMaze().isFood(pair.getKey(), pair.getValue())){
				return dist;
			}
			//ajoute dans list a tester les cellules a voisine a cellule courante
			for (int i=0; i<4; i++){
				int[] new_s = this.getNextPosition(new ActionPacman(i), new StateAgentPacman(pair.getKey(), pair.getValue()));
				lcoord.add(new Pair<Integer,Integer>(new_s[0],new_s[1]));
				ldist.add(dist+1);
			}
		}
		return -1;
	}
	
	
	
	/**
	 * Renvoie vrai s'il y a un fantome en x,y
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isGhost(int x,int y)
	{
		for(StateAgentPacman ss:ghosts_states)
		{
			if ((ss.getX()==x) && (ss.getY()==y))
					return(true);			
		}
		return(false);
	}
	
	

	/**
	 * Permet de transformer un etat en chaine de caractere: affiche Maze avec P si pacman, G si ghost, @ si les 2, 
	 * . si dot, o si bigdot, % si wall.
	 */
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		for(int y=0;y<maze.getSizeY();y++)
		{
			for(int x=0;x<maze.getSizeX();x++)
			{
				boolean isp=false;
				for(int i=0;i<pacmans_states.size();i++)
				{
					if ((pacmans_states.get(i).getX()==x) && (pacmans_states.get(i).getY()==y))
					{
						isp=true;
					}
				}boolean isg=false;
				for(int i=0;i<ghosts_states.size();i++)
				{
					if ((ghosts_states.get(i).getX()==x) && (ghosts_states.get(i).getY()==y))
					{
						isg=true;
					}
				}
				if (isp && isg) sb.append("@");
				else if (isp) sb.append("P");
				else if (isg) sb.append("G");
				else
				if (maze.isFood(x, y)) sb.append(".");
				else if (maze.isCapsule(x, y)) sb.append("o");
				else if (maze.isWall(x, y)) sb.append("%");
				else sb.append(" ");
			}
			sb.append("\n");
		}
		return(sb.toString());
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + capsulesEaten;
		result = prime * result + foodEaten;
		result = prime * result + ghostsEaten;
		result = prime * result
				+ ((ghosts_states == null) ? 0 : ghosts_states.hashCode());
		result = prime * result + (lose ? 1231 : 1237);
		result = prime * result + ((maze == null) ? 0 : maze.hashCode());
		result = prime * result
				+ ((pacmans_states == null) ? 0 : pacmans_states.hashCode());
		result = prime * result + score;
		result = prime * result + step;
		result = prime * result + (win ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateGamePacman other = (StateGamePacman) obj;
		if (capsulesEaten != other.capsulesEaten)
			return false;
		if (foodEaten != other.foodEaten)
			return false;
		if (ghostsEaten != other.ghostsEaten)
			return false;
		if (ghosts_states == null) {
			if (other.ghosts_states != null)
				return false;
		} else if (!ghosts_states.equals(other.ghosts_states))
			return false;
		if (lose != other.lose)
			return false;
		if (maze == null) {
			if (other.maze != null)
				return false;
		} else if (!maze.equals(other.maze))
			return false;
		if (pacmans_states == null) {
			if (other.pacmans_states != null)
				return false;
		} else if (!pacmans_states.equals(other.pacmans_states))
			return false;
		if (score != other.score)
			return false;
		if (step != other.step)
			return false;
		if (win != other.win)
			return false;
		return true;
	}
	public Object clone() {
		StateGamePacman clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (StateGamePacman) super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		//on clone l'attribut de type MazePacman non immuable
		clone.maze = this.maze.copy();
		
		clone.pacmans_states=new ArrayList<StateAgentPacman>();
		for (StateAgentPacman s:this.pacmans_states){
			clone.pacmans_states.add(s.copy());
		}
	
		clone.ghosts_states=new ArrayList<StateAgentPacman>();
		for (StateAgentPacman s:this.ghosts_states){
			clone.ghosts_states.add(s.copy());
		}
		// on renvoie le clone
		return clone;
	}

}
