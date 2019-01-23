package simuTP1;

import javax.swing.SwingUtilities;


import vueGridworld.VueGridworldValue;
import agent.planningagent.ValueIterationAgent;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;

public class testValueIterationBridge {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable(){
				public void run(){
		
					GridworldMDP gmdp = GridworldMDP.getBridgeGrid();
					GridworldEnvironnement g = new GridworldEnvironnement(gmdp);

				
					ValueIterationAgent a = new ValueIterationAgent(gmdp);
					
					VueGridworldValue vue = new VueGridworldValue(g,a);
					
									
					vue.setVisible(true);
			
				}
			});

	}
}
