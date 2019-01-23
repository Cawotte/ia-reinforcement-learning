package pacman.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import pacman.elements.ActionPacman;
import pacman.elements.StateGamePacman;
import pacman.elements.MazePacman;
import pacman.game.GamePacman;

/**
 * Jpanel pour avoir une vue du jeu pacman.
 * <p>
 * <li> Possede un attribut avec etat complet du jeu pacman {@link StateGamePacman}
 * <li> Est notifie par le jeu de pacman a chaque tour de jeu
 */
public class GamePacmanPanel extends JPanel implements Observer//GameObserver
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected StateGamePacman state;
	//mur: rectangle CYAN avec dedans rectangle bleu
	protected Color wallColor=Color.BLUE;
	protected Color wallColor2=Color.CYAN;
	
	protected double sizePacman=1.1;	
	protected Color pacmansColors[]={Color.yellow,Color.red,Color.blue,Color.magenta,Color.green,Color.orange,Color.white,Color.gray,Color.cyan};
	protected Color pacmanScarredColor=Color.pink;
	
	protected Color ghostsColors[]={Color.magenta,Color.green,Color.orange,Color.white,Color.gray,Color.cyan};
	protected Color ghostScarredColor=Color.DARK_GRAY;
	
	protected double sizeFood=0.3; 
	protected Color colorFood=Color.white; 
	
	protected double sizeCapsule=0.7;
	protected Color colorCapsule=Color.red;
	
	protected int timestep;
	
	/**
	 * action genere par appuie touche (pour KeyboardAgent)
	 */
	public ActionPacman actionKeyboard = new ActionPacman(MazePacman.STOP);
	
	public GamePacmanPanel(StateGamePacman s)
	{
		super();
		setGameState(s);
		timestep=0;
	}
	
	public GamePacmanPanel(StateGamePacman gs, int timestep) {
		super();
		setGameState(gs);
		this.timestep=timestep;
	}

	public void setGameState(StateGamePacman s)
	{
		state=s;
	}
	

	/**
	 * Dessine le labyrinthe (murs, dot, capsule), puis appelle drawPacmand et drawGhost
	 */
	public void paint(Graphics g)
	{
		int dx=getSize().width;
		int dy=getSize().height;
		g.setColor(Color.black);
		g.fillRect(0, 0,dx,dy);
		
		MazePacman m=state.getMaze();
		int sx=m.getSizeX();
		int sy=m.getSizeY();
		double stepx=dx/(double)sx;//largeur une case
		double stepy=dy/(double)sy;//hauteur une case
		double posx=0;
		//parcourt les cases du maze
		for(int x=0;x<sx;x++)
		{
			double posy=0;
			for(int y=0;y<sy;y++)
			{
				if (m.isWall(x, y))
				{	//mur: rectangle CYAN avec dedans rectangle bleu
					g.setColor(wallColor2);
					g.fillRect((int)posx, (int)posy, (int)(stepx+1),(int)(stepy+1));
					
					g.setColor(wallColor);
						double nsx=stepx*0.5;
						double nsy=stepy*0.5;
						double npx=(stepx-nsx)/2.0;
						double npy=(stepy-nsy)/2.0;
					g.fillRect((int)(npx+posx),(int)(npy+posy),(int)(nsx),(int)nsy);						
				}
				if (m.isFood(x, y))
				{
					g.setColor(colorFood);
					
						double nsx=stepx*sizeFood;
						double nsy=stepy*sizeFood;
						double npx=(stepx-nsx)/2.0;
						double npy=(stepy-nsy)/2.0;
					g.fillOval((int)(npx+posx),(int)(npy+posy),(int)(nsx),(int)nsy);
				}
				if (m.isCapsule(x, y))
				{
					g.setColor(colorCapsule);
						double nsx=stepx*sizeCapsule;
						double nsy=stepy*sizeCapsule;
						double npx=(stepx-nsx)/2.0;
						double npy=(stepy-nsy)/2.0;
					g.fillOval((int)(npx+posx),(int)(npy+posy),(int)(nsx),(int)nsy);
				}
				posy+=stepy;				
			}
			posx+=stepx;
		}
		drawPacmans(g);
		drawGhosts(g);
		drawScore(g);
	}
	
	/**
	 * Dessine les pacmans a leur position (calcule positions pour fillArc selon direction)
	 * @param g
	 */
	void drawScore(Graphics g){
		
		int dx=getSize().width;
		int dy=getSize().height;
		MazePacman m=state.getMaze();
		int sx=m.getSizeX();
		int sy=m.getSizeY();
		double stepx=dx/(double)sx;
		double stepy=dy/(double)sy;
		double npx=stepx;
		double npy=stepy/4.0;
		
		g.setFont(new Font(Font.SERIF, Font.BOLD, (int)stepy));
		g.setColor(Color.RED);
		g.drawString("SCORE: "+state.getScore(), (int)npx, (int)(3*npy));
		
	}
	
	
	/**
	 * Dessine les pacmans a leur position (calcule positions pour fillArc selon direction)
	 * @param g
	 */
	void drawPacmans(Graphics g)
	{
		int dx=getSize().width;
		int dy=getSize().height;
		
		MazePacman m=state.getMaze();
		int sx=m.getSizeX();
		int sy=m.getSizeY();
		double stepx=dx/(double)sx;
		double stepy=dy/(double)sy;
		
		for(int i=0;i<state.getNumberOfPacmans();i++)
		{
			int px=state.getPacmanState(i).getX();
			int py=state.getPacmanState(i).getY();
			double posx=px*stepx;
			double posy=py*stepy;
			
			if (state.getPacmanState(i).isDead())
			{
				g.setColor(Color.RED);
				for(double d=0.5;d<1.5;d+=0.1)
				{
					double nsx=stepx*d;
					double nsy=stepy*d;
					double npx=(stepx-nsx)/2.0;
					double npy=(stepy-nsy)/2.0;
					g.drawOval((int)(npx+posx),(int)(npy+posy),(int)(nsx),(int)nsy);
				}
			}
			else
			{
				if (state.getPacmanState(i).isScarred())
					g.setColor(pacmanScarredColor);
				else 
					g.setColor(pacmansColors[i%pacmansColors.length]);
				double nsx=stepx*sizePacman;
				double nsy=stepy*sizePacman;
				double npx=(stepx-nsx)/2.0;
				double npy=(stepy-nsy)/2.0;
				int sa=0;
				int fa=0;
				int d=state.getPacmanState(i).getDirection();
				if (d==MazePacman.NORTH)
				{
					sa=70; fa=-320;
				}
				if (d==MazePacman.SOUTH)
				{
					sa=250; fa=-320;
				}
				if (d==MazePacman.EAST)
				{
					sa=340; fa=-320;				
				}
				if (d==MazePacman.WEST)
				{
					sa=160; fa=-320;
				}
				
				g.fillArc((int)(npx+posx),(int)(npy+posy),(int)(nsx),(int)nsy,sa,fa);
			}
		}
	}
	
	/**
	 * Dessine les pacmans a leur position (calcule positions pour fillArc selon direction)
	 * @param g
	 */
	void drawGhosts(Graphics g)
	{
		int dx=getSize().width;
		int dy=getSize().height;
		
		MazePacman m=state.getMaze();
		int sx=m.getSizeX();
		int sy=m.getSizeY();
		double stepx=dx/(double)sx;
		double stepy=dy/(double)sy;
		
		for(int i=0;i<state.getNumberOfGhosts();i++)
		{
			
			int px=state.getGhostState(i).getX();
			int py=state.getGhostState(i).getY();
			double posx=px*stepx;
			double posy=py*stepy;

			if (state.getGhostState(i).isScarred())
				g.setColor(ghostScarredColor);
			else 
				g.setColor(ghostsColors[i%ghostsColors.length]);
			
			double nsx=stepx*sizePacman;
			double nsy=stepy*sizePacman;
			double npx=(stepx-nsx)/2.0;
			double npy=(stepy-nsy)/2.0;
			int sa=0;
			int fa=0;
			
			g.fillArc((int)(posx+npx),(int)(npy+posy),(int)(nsx),(int)(nsy),0,180);
			g.fillRect((int)(posx+npx),(int)(npy+posy+nsy/2.0-1),(int)(nsx),(int)(nsy*0.666));
			g.setColor(Color.BLACK);
			g.fillOval((int)(posx+npx+nsx/5.0),(int)(npy+posy+nsy/3.0),4,4);
			g.fillOval((int)(posx+npx+3*nsx/5.0),(int)(npy+posy+nsy/3.0),4,4);
			//g.fillArc((int)(npx+posx),(int)(npy+posy),(int)(nsx),(int)nsy,sa,fa);
			
			g.setColor(Color.black);
		}
	}

/*	@Override
	public void update(StateGamePacman state) {
		setGameState(state);
		repaint();
		
		if (timestep>0)
			try {
				Thread.sleep(timestep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}*/
	
	/**
	 * mise en place de l'ecouteur d'evenement clavier pour KeyboardAgent  (pour KeyboardAgent)
	 */
	public void setKeyListener(){
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,false),
                "actionOuest");
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0,false),
				"actionNord");
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0,false),
                "actionSud");
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0,false),
                "actionEst");
		this.getActionMap().put("actionOuest",new OuestAction());
		this.getActionMap().put("actionNord",new NordAction());
		this.getActionMap().put("actionSud",new SudAction());
		this.getActionMap().put("actionEst",new EstAction());
	}
	/**
	 * methode qui renvoie action a executer selon touche clavier enfoncee  (pour KeyboardAgent)
	 */
	protected ActionPacman getActionTyped(){
		return actionKeyboard;
	}
	
	protected class OuestAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("action ouest");
			actionKeyboard = new ActionPacman(MazePacman.WEST);

		}
		
	}
	
	protected class NordAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("action nord");
			actionKeyboard = new ActionPacman(MazePacman.NORTH);
			
		}
		
	}
	
	protected class SudAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("action sud");
			actionKeyboard = new ActionPacman(MazePacman.SOUTH);
		
			
		}
		
	}
	
	protected class EstAction extends AbstractAction{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("action est");
			actionKeyboard = new ActionPacman(MazePacman.EAST);
		
			
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {//notifie par GamePacman a chaque tour de jeu
	//	System.out.println("pacman panel notifie");
		if (o instanceof GamePacman){
			GamePacman game = (GamePacman) o;
			setGameState(game.getState());
			repaint();
			
			if (timestep>0)
				try {
					Thread.sleep(timestep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}

