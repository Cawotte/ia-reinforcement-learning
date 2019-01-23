package simuProjetPacman;

import indicateursJFX.IndicateursMeanSumRwd;

import java.awt.Dimension;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

import javax.swing.JFrame;

import pacman.environnementRL.EnvironnementPacmanFeatureRL;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import pacman.environnementRL.EnvironnementPacmanRL;
import pacman.environnementRL.EtatPacmanMDPClassic;
import pacman.graphics.GamePacmanPanel;
import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import agent.rlapproxagent.FeatureFunction;
import agent.rlapproxagent.FeatureFunctionIdentity;
import agent.rlapproxagent.FeatureFunctionPacman;
import agent.rlapproxagent.QLApproxAgent;
import agent.strategy.StrategyExplorationTest1;

public class testRLPacman extends Application{
	/** type de labyrinthe pour le jeu de pacman*/
	static String mazename = "pacmanlayouts/smallGrid.lay";//smallGrid smallGrid2 mediumGrid

	// parametres RL*/
	static double gamma=0.8;
	static double alpha=0.1;
	static double _epsilon = 0.05;
	
	// parametres experience a lancer, un episode = une partie */
	/** nombre d'experiences a lancer (pour faire une moyenne), une experience est un apprentissage sur plusieurs parties */
	static int nbmean =3;
	/** nombre de parties ou l'agent apprend */
	static int nbepisodelearn = 500;
	/** nombre de partie ou l'agent exploite la politique apprise (epsilon=0) */
	static int nbepisodegreedy = 300;
	/** nombre de parties ou l'on affiche le jeu pacman pour voir le comportement appris  */
	static int nbepisodegreedydisplay=0;

	
	/** pour afficher jeu de pacman en mode greedy */
	static boolean DISPLAYPACMANGAME = true;
	/** pour afficher courbe (somme des rec par episode) a la fin  */
	static boolean DISPLAYCHART = true;
	/** //met un point tous les DELTA_DISPLAY epi */
	static int DELTA_DISPLAY = 5;


	
	static RLAgent rlagent;
	static EnvironnementPacmanRL pacmanmdp ;
	static GamePacmanPanel panel;
	static JFrame frame ;
	/**
	 * Chart pour tracer somme des rec par episode
	 */
	static LineChart<Number,Number> lineChart;
	static Scene scene;
	
	private static void setRLAgent(){
		//QLearning tabulaire classique
		pacmanmdp = new EnvironnementPacmanMDPClassic(mazename,true);
		rlagent = new QLearningAgent(alpha,gamma,pacmanmdp);

		//Qlearning avec fonctions caracteristiques identite
	/*	pacmanmdp = new EnvironnementPacmanMDPClassic(mazename,true);
	    EtatPacmanMDPClassic etatmdp = (EtatPacmanMDPClassic) pacmanmdp.getEtatCourant();
		System.out.println("Dimensions de etatMDP: "+etatmdp.getDimensions());
		FeatureFunction featurefunction = new FeatureFunctionIdentity(etatmdp.getDimensions(),4);
		rlagent = new QLApproxAgent(alpha,gamma,pacmanmdp,featurefunction);
*/
		//QLearning avec approximation lineaire
	/*	pacmanmdp = new EnvironnementPacmanFeatureRL(mazename,true);//smallGrid smallGrid2 mediumGrid
		FeatureFunction featurefunction2 = new FeatureFunctionPacman();
		rlagent = new QLApproxAgent(alpha,gamma,pacmanmdp,featurefunction2);
*/

	}
	
	/**
	 * creation jeu pacman et RLAgent 
	 */
	private static void common(){
		testRLPacman.setRLAgent();
		rlagent.setGamma(gamma);
		rlagent.DISPRL = false;
		rlagent.DISPEPISODE = true;
		rlagent.setMaxnbpasparepisode(10000);

		if (DISPLAYPACMANGAME){
		  	pacmanmdp.getGamepacman().setStep_delay(0);
			//creation fenetre d'affichage
			panel=new GamePacmanPanel(pacmanmdp.getGamepacman().getState());
			pacmanmdp.getGamepacman().addObserver(panel);
			
			frame = new JFrame("FrameDemo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setPreferredSize(new Dimension(640,480));
			frame.add(panel);
			frame.pack();
			frame.setVisible(false);
		}
		

	}

	/** trace courbe moyenne sur plusieurs expe, affiche jeu pacman sur derniers episodes greedy de derniere expe */
	private static void apprentissage(){
		//nb d'episode greedy ou gagne / nbepisodegreedy
		int nbwin=0;
			
		//si affiche courbe a la fin de l'apprentissage
		if (DISPLAYCHART){
			lineChart = new IndicateursMeanSumRwd(new NumberAxis(),new NumberAxis(),nbepisodelearn+nbepisodegreedy+nbepisodegreedydisplay,nbmean,rlagent);
			((IndicateursMeanSumRwd) lineChart).setDeltapoints(DELTA_DISPLAY);
		}
		
		for (int i=0;i<nbmean;i++){
			System.out.println("Expe "+i);
			rlagent.setStratExplorationGreedy(_epsilon);
			rlagent.setEpisodeNb(0);
			rlagent.runEpisode(nbepisodelearn);//runEpisode {while !etatabsorbant}
	
				
			rlagent.setStratExplorationGreedy(0.0);
			for (int nbepi=0; nbepi<nbepisodegreedy; nbepi++){
				rlagent.runEpisode(1);//runEpisode {while !etatabsorbant}
				if(pacmanmdp.getGamepacman().isWin()) nbwin++;
			}
			
		}
	    
	
	   //calcule moyenne des donnees et inscrit dans chart
		double[] data ;
		if (DISPLAYCHART){
			data = ((IndicateursMeanSumRwd) lineChart).computeResults();
		}

	  /* 	for (int i=0;i<nbepisode;i++){
	   		System.out.print(data[i]+" ");
	   	}
*/
		System.out.println("PACMAN  greedy gagne "+nbwin+ " fois sur "+nbmean*nbepisodegreedy+" : "+ (nbwin*100/(nbmean*nbepisodegreedy))+"%");
		if (DISPLAYPACMANGAME){
			//pour voir jeu pacman en mode greedy
			pacmanmdp.getGamepacman().setStep_delay(300);
			frame.setVisible(true);
			rlagent.DISPEPISODE = false;
			rlagent.runEpisode(nbepisodegreedydisplay);//runEpisode {while !etatabsorbant}	
		}

	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testPacmanClassicRL.testCalculQValeur();
		
		testRLPacman.common();
		testRLPacman.apprentissage();
		 

		  if (DISPLAYCHART){
			  launch( args);//lance start, affichage du graph
		  }
	
		
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
   		start1(primaryStage);
	}
	
	public void start1(Stage primaryStage){
	        //Creates lineChart dans scene 
			primaryStage.setTitle(this.getClass().getName());
			//Creates a Scene for a specific root Node=lineChart with a specific size and fill.
			
			Scene scene  = new Scene(lineChart,800,600);

	        primaryStage.setScene(scene);
			primaryStage.show();


	}
	
	
	/** test avec action predefinie a chq pas pour verifier mise à jour de Q	 */
	private static void testCalculQValeur(){
		pacmanmdp = new EnvironnementPacmanMDPClassic("pacmanlayouts/smallGrid.lay",true);//smallGrid smallGrid2 mediumGrid
		double gamma=0.2;
		double alpha=0.1;
  
		rlagent = new QLearningAgent(alpha,gamma,pacmanmdp);
		rlagent.setGamma(0.8);

		rlagent.setStratExploration(new StrategyExplorationTest1(rlagent));
		rlagent.DISPRL = true;
		rlagent.DISPEPISODE = true;
		
		System.out.println("########## GAME:\n"+pacmanmdp.getGamepacman().getState());
		Scanner sc = new Scanner(System.in);
		System.out.println("Attend frappe clavier :");
		String str = sc.nextLine();
//		if (rlagent instanceof QLearningAgent)
//			System.out.println("Q: "+((QLearningAgent) rlagent).getQvaleurs());
		
		while (!pacmanmdp.estAbsorbant())
		{
			rlagent.runOneStep();
			System.out.println("########## GAME:\n"+pacmanmdp.getGamepacman().getState());
	//		System.out.println("Q: "+((QLearningAgent) rlagent).getQvaleurs());
			System.out.println("Attend frappe clavier :");
			str = sc.nextLine();
		}
	

	}

}



