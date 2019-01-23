package simuProjetPacman;

import java.awt.Dimension;

import javax.swing.JFrame;

import pacman.agent.KeyboardPacmanAgent;
import pacman.agent.RandomPacmanAgent;
import pacman.elements.MazeException;
import pacman.elements.MazePacman;
import pacman.elements.StateGamePacman;
import pacman.game.GamePacman;
import pacman.graphics.GamePacmanPanel;

public class PacmanGame {
	public static void main(String args[]) throws MazeException
	{
		//lecture du fichier layout et remplissage des donnees dans Maze
		//originalClassic
		MazePacman maze=new MazePacman("pacmanlayouts/smallGrid2.lay");
		//mis a jour etat du jeu
		StateGamePacman state=new StateGamePacman(maze);
		//creation du jeu
		GamePacman game=new GamePacman(state);
	
		//creation fenetre d'affichage
		GamePacmanPanel panel=new GamePacmanPanel(state);
		game.addObserver(panel);
		
		JFrame frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(640,480));
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		//ajoute les agents au jeu
		for(int i=0;i<state.getNumberOfPacmans();i++)
			game.addPacmanAgent(new KeyboardPacmanAgent(panel));
		for(int i=0;i<state.getNumberOfGhosts();i++)
			game.addGhostAgent(new RandomPacmanAgent());
		

		//lancement boucle de jeu
		game.setStep_delay(300);
		game.runUntilEnd();
		
		if (state.isLose())			
			System.out.println("******* Lose !!");
		else 
			System.out.println("******* Win !!");
		System.out.println("Score = "+state.getScore());
		System.out.println("Nb Food = "+state.getFoodEaten());
		System.out.println("Nb Capsule = "+state.getCapsulesEaten());
		System.out.println("Nb Ghost = "+state.getGhostsEaten());
		System.out.println("Duration = "+state.getStep());

		
	}
}
