package agent.planningagent;

import java.util.List;
import java.util.Observable;
import java.util.Random;

import agent.ValueAgent;
import util.HashMapUtil;
import environnement.Action;
import environnement.Etat;
import environnement.MDP;

/**
 * Cet agent planifie off-line pendant nbIterations en utilisant une fonction de valeur
 * @author laetitiamatignon
 *
 */

public abstract class PlanningValueAgent extends ValueAgent implements IPlanningValueAgent{
	protected MDP mdp;

	/**
	 * difference max entre 2 mises a jour de V(s) (utile dans run pour convergence)
	 */
	protected double delta;
	
	
	
	
	
	public PlanningValueAgent(MDP mdp) {
		super();
		this.mdp = mdp;
	//	this.nbIterations = nbIterations;
		this.vmin =Double.MAX_VALUE;
		this.vmax =-Double.MAX_VALUE;
		this.episodeNb = 0;
	}
	/**
	 * nbIterations init a 1
	 * 
	 */
	

	/**
	 * Met a jour sa fonction de valeur en iterant sur nbIterations
	 */
	public void run(int nbIterations){

		for (int i=0;i<nbIterations;i++){
			this.updateV();
		}
	
	}

	/**
	 * Met a jour sa fonction de valeur jusqu'a convergence
	 */
	public void run(){
		int nbIter=0;
		double epsilon = 0.0001;
		do{
			this.updateV();
			nbIter++;
		}
		while(this.getDelta()>epsilon);
		System.out.println("Convergence a "+epsilon+" pres de VI apres "+nbIter+" iterations.");
	}
	
	/**
	 * 
	 * Met a jour sa fonction de valeur: effectue UNE iteration 
	 * 
	 */
	public abstract void updateV();
	
	
	
/*	public int getNbIterations() {
		return nbIterations;
	}
	public void setNbIterations(int nbIterations) {
		this.nbIterations = nbIterations;
	}*/


	public void setDelta(double delta) {
		this.delta = delta;
	}
	public double getDelta() {
		return delta;
	}
	public MDP getMdp() {
		return mdp;
	}
	
	
}
