package agent.strategy;

import java.util.List;
import java.util.Random;

import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Action2D;
import environnement.Etat;

/**
 * Strategie de test: pour etre sure de ce que va faire l'agent
  * @author lmatignon
 *
 */
public class StrategyExplorationTest1 extends StrategyExploration{
	static int nbappel=0;
	
	
	
	public StrategyExplorationTest1(RLAgent agent) {
		super(agent);
	}

	@Override
	public Action getAction(Etat _e) {//renvoi null si _e absorbant
		nbappel++;
		switch(nbappel)
		{
			case 1:
				return Action2D.EST;
			case 2:
				return Action2D.OUEST;
			case 3:
				return Action2D.EST;
			case 4:
				return Action2D.OUEST;
			default:
				return Action2D.OUEST;
		}
		
	
	}

	

}
