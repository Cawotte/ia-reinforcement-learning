package simuTP1;



import javax.swing.SwingUtilities;

import vueGridworld.VueGridworldManuel;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;

public class testMoveGridworld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// cree une nouvelle tache, une instance de Runnable, qui est placee a la fin de la file de l'EDT par invokeLater(). 
        SwingUtilities.invokeLater(new Runnable(){
			public void run(){
	
					GridworldMDP gmdp = GridworldMDP.getBookGrid();
 
				GridworldEnvironnement g = new GridworldEnvironnement(gmdp);
	
				VueGridworldManuel vue = new VueGridworldManuel(g);
				vue.setVisible(true);
				
				
				
			}
		});


	}

}
