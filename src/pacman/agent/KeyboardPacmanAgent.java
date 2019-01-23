package pacman.agent;



import pacman.elements.AgentPacman;
import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.graphics.GamePacmanPanel;

/**
 * Renvoi l'action decide au clavier (dans GamePacmanPanel:setKeyListener)
 * @author lmatignon
 *
 */
public class KeyboardPacmanAgent  implements AgentPacman {
	GamePacmanPanel panel;
	
	public KeyboardPacmanAgent(){

	}
	
	public KeyboardPacmanAgent(GamePacmanPanel _panel){
		panel = _panel;
		this.setPanel(panel);
	}
	
	
	
	public GamePacmanPanel getPanel() {
		return panel;
	}

	public void setPanel(GamePacmanPanel panel) {
		System.out.println("seetpanel");
		this.panel = panel;
		this.panel.setKeyListener();
	}

	@Override
	public ActionPacman getAction(StateAgentPacman as, StateGamePacman state) {
		return panel.actionKeyboard;
	}

	

}
