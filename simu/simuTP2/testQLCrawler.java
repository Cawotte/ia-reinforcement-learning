package simuTP2;

import indicateursJFX.IndicateurSumRwdPerEpisodeOnline;
import indicateursJFX.IndicateursMeanSumRwd;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;

import environnement.Environnement;
import environnement.crawler.CrawlingRobotEnvironnement;
import environnement.gridworld.GridworldEnvironnement;
import environnement.gridworld.GridworldMDP;
import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import vueCrawler.VueCrawlerAbstrait;
import vueCrawler.VueCrawlerManuel;
import vueCrawler.VueCrawlerRL;
import vueGridworld.VueGridworldRL;

public class testQLCrawler  extends Application{
	static Environnement g ;
	static RLAgent a;

	
	private static void createQLAgentCrawler(){
		double gamma=0.9;
		double alpha=0.1;
		int nbEtatBras=4;
		int nbEtatMain=6;
		
		g = new CrawlingRobotEnvironnement(nbEtatBras,nbEtatMain);
		
		 a = new QLearningAgent(alpha,gamma,g);
		
		a.setStratExplorationGreedy(0.1);//sinon strat exploration par defaut est manuelle
		
		a.setMaxnbpasparepisode(100);
		a.DISPEPISODE=true;
		a.DISPRL = false;
		a.DISPTHREAD=false;
		VueCrawlerAbstrait vue = new VueCrawlerRL((CrawlingRobotEnvironnement) g,a);
		vue.setVisible(true);

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)   {
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){		
					createQLAgentCrawler();
					
				}
			});
		  launch( args);//lance start
			
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        //Creates lineChart dans scene 
		primaryStage.setTitle(this.getClass().getName());

		IndicateurSumRwdPerEpisodeOnline lineChart = new IndicateurSumRwdPerEpisodeOnline(new NumberAxis(),new NumberAxis(),a);
		 //Creates a Scene for a specific root Node=lineChart with a specific size and fill.
        Scene scene  = new Scene(lineChart,800,600);
       // lineChart.getData().add(series);
       
        primaryStage.setScene(scene);
		primaryStage.show();
		
	}
}
