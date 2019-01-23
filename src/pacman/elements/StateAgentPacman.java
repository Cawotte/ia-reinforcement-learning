package pacman.elements;

import environnement.Etat;
import environnement.Etat2D;

/**
 * La classe decrit l'etat d'un agent (pacman ou ghost)  dans le jeu pacman 
 * <li> position 2D initiale, actuelle, ancienne
 * <li> direction
 * <li> scarredTimer
 * 
* @author lmatignon
 *
 */
public class StateAgentPacman 
{
	/**Coordonnee x actuelle*/
	protected int x;
	/**Coordonnee y actuelle*/
	protected int y;
	/**Coordonnee x initiale*/
	private int start_x;
	/**Coordonnee y initiale*/
	private int start_y;	
	/**
	 * Temps restant ou agent scared
	 */
	protected int scarredTimer;
	/** Direction de l'agent (MazePacman.NORTH, ...)*/
	protected int direction;
	protected boolean isdead;
	protected int last_x;
	protected int last_y;
	
	/**
	 * Constructeur d'un etat d'un agent (pacman ou ghost): 
	 * <li> position 2D initiale donne en parametre, 
	 * <li> direction NORD par defaut.
	 */
	public StateAgentPacman(int start_x,int start_y)
	{
		this.start_x=start_x;
		this.start_y=start_y;
		this.last_x=-1;
		this.last_y=-1;
		this.x=start_x;
		this.y=start_y;
		scarredTimer=0;
		direction=MazePacman.NORTH;
		isdead=false;
	}
	
	/** 
	 * @return la direction actuelle de l'agent
	 */
	public int getDirection() {return(direction);}
	
	/**
	 * @return la position X initiale
	 */
	public int getStartX() {return(start_x);}
	
	/**
	 * @return  la position Y initiale
	 */
	public int getStartY() {return(start_y);}

	/**
	 * @return  la position X actuelle
	 */
	public int getX() {return(x);}
	
	/**
	 * @return  la position Y actuelle
	 */
	public int getY() {return(y);}
	
	/**
	 * @return la derniere position X avant l'actuelle
	 */
	public int getLastX() {return(last_x);}
	/**
	 * @return la derniere position Y avant l'actuelle
	 */
	public int getLastY() {return(last_y);}
	/**
	 * @return le dernier deplacement de l'agent (MazePacman.NORTH, ...)
	 */
	public int getLastMovement()
	{
		
		if (last_x==x-1) return(MazePacman.EAST);
		if (last_x==x+1) return(MazePacman.WEST);
		if (last_y==y-1) return(MazePacman.SOUTH);
		if (last_y==y+1) return(MazePacman.NORTH);
		return(-1);
	}
	
	
	/**
	 * @return si l'agent est ''effraye''
	 * 
	 */
	public boolean isScarred() {return(scarredTimer>0);}
	
	/**
	 * @return le nombre de coup pendant lequel l'agent restera effraye
	 * 
	 */
	public int getScarredTimer() {return(scarredTimer);}

	/**
	 * @return si l'agent est mort ou non
	 * 
	 */
	public boolean isDead() {return(isdead);}

	public void setX(int x) {this.x=x;}
	public void setY(int y) {this.y=y;}
	public void setLastX(int x) {last_x=x;}
	public void setLastY(int y) {last_y=y;}
	public void setScarredTimer(int t) {this.scarredTimer=t;}
	public void setDirection(int d) {this.direction=d;}
	public void setDead(boolean v) {isdead=v;}
	
	public StateAgentPacman copy()
	{
		StateAgentPacman a=new StateAgentPacman(getStartX(),getStartY());
		a.setX(getX());
		a.setY(getY());
		a.setLastX(getLastX());
		a.setLastY(getLastY());
		a.setScarredTimer(getScarredTimer());
		a.setDirection(direction);
		a.setDead(isdead);
		return(a);
	}
}
