package simuTP1;

import javax.swing.SwingUtilities;

import vueGridworld.VueGridworldValue;
import agent.planningagent.ValueIterationAgent;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;

public class testValueIterCours {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable(){
				public void run(){
		
					String[][] grid = {{" ","#"},
							{"S","-1"},
							{"1"," "}};
				//	GridworldMDP gmdp = new GridworldMDP(grid);
					
					
					GridworldMDP gmdp = GridworldMDP.getBookGrid();
					GridworldEnvironnement g = new GridworldEnvironnement(gmdp);
					//System.out.println(g.getEtatCourant());
					
					ValueIterationAgent a = new ValueIterationAgent(gmdp);	
					//ValueIterationAgentb a = new ValueIterationAgentb(gmdp);	
					//a.DISPEPISODE = true;
					VueGridworldValue vue = new VueGridworldValue(g,a);
					
									
					vue.setVisible(true);
				}
			});

	}
}
