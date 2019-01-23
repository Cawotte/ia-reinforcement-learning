package pacman.agent;

import java.util.ArrayList;

import pacman.elements.AgentPacman;
import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

public class RandomPacmanAgent implements AgentPacman
{

	@Override
	public ActionPacman getAction(StateAgentPacman as,StateGamePacman state) 
	{
		/**
		 *  5 possible actions defined in Maze.java:
		 *  public static int STOP=0;
			public static int NORTH=1;
			public static int SOUTH=2;
			public static int EAST=3;
			public static int WEST=4;
		 */
		ArrayList<ActionPacman> aa=new ArrayList<ActionPacman>();
		for(int i=0;i<4;i++)
		{
			if (state.isLegalMove(new ActionPacman(i),as))//si essaie d'aller dans mur, reste sur place
				aa.add(new ActionPacman(i));
		}		
		return(aa.get((int)(Math.random()*aa.size())));
	}

}
