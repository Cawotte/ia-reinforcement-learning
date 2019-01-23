package simuTP2;

import javax.swing.SwingUtilities;

import vueGridworld.VueGridworldRL;
import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;

public class testQLAgentGridworld {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable(){
				public void run(){

					GridworldMDP gmdp = GridworldMDP.getBookGrid();
					//GridworldMDP gmdp = GridworldMDP.getCliffGrid();
					
					GridworldEnvironnement g = new GridworldEnvironnement(gmdp);
					gmdp.setProba(0.1);
					double gamma=0.9;
					double alpha=0.1;
					int nbA=5;
					//nombre total d'etats qui comprend aussi les obstacles dans le labyrinthe: pour un tableau de Q valeurs avec etats indices
					int nbS = gmdp.getNbEtats();
					
					
					RLAgent a = new QLearningAgent(alpha,gamma,g);
					a.DISPRL = false;
					a.DISPEPISODE=true;
					a.DISPTHREAD = false;
					VueGridworldRL vue = new VueGridworldRL(g,a);			
									
					vue.setVisible(true);
				
				}
			});

	}
}
