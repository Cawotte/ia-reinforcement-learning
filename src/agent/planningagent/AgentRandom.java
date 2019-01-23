package agent.planningagent;

import java.util.ArrayList;
import java.util.List;

import environnement.Action;
import environnement.Etat;
import environnement.MDP;
/**
 * Cet agent choisit une action aleatoire dans chaque etat
 * @author lmatignon
 *
 */
public class AgentRandom extends PlanningValueAgent{
	
	
	public AgentRandom(MDP _m) {
		super(_m);
	}

	@Override
	public Action getAction(Etat e) {
		List<Action> actions = this.getPolitique(e);
		if (actions.size()==0)
				return null;//ActionGridworld.EXIT;
		int r = rand.nextInt(actions.size());//random entre 0 inclu et param exlu
		return actions.get(r);
	}

	
	
	@Override
	public double getValeur(Etat _e) {
		return 0.0;
	}

	

	@Override
	public List<Action> getPolitique(Etat _e) {
		return this.mdp.getActionsPossibles(_e);
	}

	@Override
	public void updateV() {
				System.out.println("agent random mise a jour");
	}

	@Override
	public void setGamma(double parseDouble) {
		// TODO Auto-generated method stub
		
	}





}
