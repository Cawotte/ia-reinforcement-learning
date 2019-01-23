package pacman.elements;


/**
 * Un agent se deplace selon les directions: Maze.NORTH=0, Maze.SOUTH=1, Maze.EAST=2, Maze.WEST=3, Maze.STOP=4
 *
 * @author lmatignon
 *
 */
public class ActionPacman 
{
	/** Selon la direction, contient la modif (0,1,-1) a effectuer sur la coord x de l'agent */
	private int vx;
	/** Selon la direction, contient la modif (0,1,-1) a effectuer sur la coord y de l'agent */
	private int vy;
	private int direction;
	
	/**
	 * Construit une action a partir d'une direction: Maze.NORTH=0, Maze.SOUTH=1, Maze.EAST=2, Maze.WEST=3, Maze.STOP=4
	 * @param direction la direction
	 */
	public ActionPacman(int direction)
	{
		this.direction=direction;
		if (direction==MazePacman.STOP)
		{
			vx=0;
			vy=0;
		}
		else
		if (direction==MazePacman.NORTH)
		{
			vx=0;
			vy=-1;
		}
		else if (direction==MazePacman.SOUTH)
		{
			vx=0;
			vy=1;			
		}
		else if (direction==MazePacman.EAST)
		{
			vy=0;
			vx=+1;
		}
		else if (direction==MazePacman.WEST)
		{
			vy=0;
			vx=-1;			
		}
		else 
		{
			System.err.println("Unknown direction in AgentAction....");
			this.direction=MazePacman.STOP;
			vx=0;
			vy=0;
		}
	}
	
	/**
	 *  @return  la modif (0,1,-1) a effectuer sur la coord x de l'agent
	 * 
	 */
	public int getX() {return(vx);}	
	
	/**
	 * @return  la modif (0,1,-1) a effectuer sur la coord y de l'agent
	 * 
	 */
	public int getY() {return(vy);}
	
	/**
	 * @return la direction
	 * 
	 */
	public int getDirection() {return(direction);}
}
