package pacman.agent;
/**
 * Agent qui fait toujours la meme action (celle donnée en paramètre du constructeur)
 */


import environnement.Action2D;
import pacman.elements.ActionPacman;
import pacman.elements.AgentPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

public class GoPacmanAgent implements AgentPacman{
	private Action2D action;
	
	public GoPacmanAgent(Action2D _action){
		action = _action;
	}

	@Override
	public ActionPacman getAction(StateAgentPacman as, StateGamePacman state) {
		/**
		 *  5 possible actions defined in Maze.java:
		 * public static int NORTH=0;
	public static int SOUTH=1;
	public static int EAST=2;
	public static int WEST=3;
	public static int STOP=4;

		 */
		
		return new ActionPacman(action.ordinal());

	}

}
