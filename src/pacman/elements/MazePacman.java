package pacman.elements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



/**
 * Cette classe decrit un labyrinthe de pacman avec positions initiales des agents et des murs, dots, capsules (non modifiable)
 * 
 * <p>
 * x horizontal : [0;size_x[ , y  vertical vers bas :[0;size_y[.
 * <p>
 * On peut charger un labyrinthe depuis un fichier  contenant:
 * <li>'%' mur
 * <li>'.' food ou dot
 * <li>'o' capsule ou bigdot (--> rend pacman invincible, scared)
 * <li>'P' pacman start position
 * <li>'G' ghost start positions
 * 
 * @author lmatignon
 *
 */
public class MazePacman 
{
	/** 
	 * Les differentes directions possibles pour les actions
	 */
	public static int NORTH=0;
	public static int SOUTH=1;
	public static int EAST=2;
	public static int WEST=3;
	public static int STOP=4;
	
	/** Largeur du labyrinthe**/
	protected int size_x;
	/** Hauteur du labyrinthe*/
	protected int size_y;
	protected boolean walls[][];
	protected boolean food[][];
	protected boolean capsules[][];
	/** Nb de food / dot*/
	protected int nbfood;
	/** Nb de capsules / bigdot*/
	protected int nbcapsule;
	/** Nb de murs*/
	protected int nbwall;

	/**
	 * coordonnees initiales des pacmans et des fantomes
	 */
	protected ArrayList<Integer> pacman_start_x;
	protected ArrayList<Integer> pacman_start_y;
	protected ArrayList<Integer> ghosts_start_x;
	protected ArrayList<Integer> ghosts_start_y;
	
	/**
	 * Permet de construire un labyrinthe vide d'une taille	donnee
	 */
	protected MazePacman(int sx,int sy)
	{
		size_x=sx;
		size_y=sy;
		nbfood=0;
		nbcapsule=0;
		nbwall=0;
		walls=new boolean[size_x][size_y];
		food=new boolean[size_x][size_y];
		capsules=new boolean[size_x][size_y];
		ghosts_start_x=new ArrayList<Integer>();
		ghosts_start_y=new ArrayList<Integer>();
		pacman_start_x=new ArrayList<Integer>();
		pacman_start_y=new ArrayList<Integer>();
	}
	
	/**
	 * Permet de construire un labyrinthe a partir d'un fichier
	 * @param filename
	 */
	public MazePacman(String filename) throws MazeException
	{
		try{
			//System.out.println("Layout file is "+filename);
			//Lecture du fichier pour determiner la taille du maze
			InputStream ips=new FileInputStream(filename); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			int nbX=0;
			int nbY=0;
			//Verifie que la fichier layout est bien formate (meme dimension pour chaque ligne)
			while ((ligne=br.readLine())!=null)
			{
				ligne=ligne.trim();
				if (nbX==0) {nbX=ligne.length();}
				else if (nbX!=ligne.length()) throw new MazeException("Wrong Input Format: all lines must have the same size");
				nbY++;
			}			
			br.close(); 
			//System.out.println("### Size of maze is "+nbX+";"+nbY);
			
			//Initialisation du maze
			size_x=nbX;
			size_y=nbY;
			walls=new boolean[size_x][size_y];
			food=new boolean[size_x][size_y];
			capsules=new boolean[size_x][size_y];
			ghosts_start_x=new ArrayList<Integer>();
			ghosts_start_y=new ArrayList<Integer>();
			pacman_start_x=new ArrayList<Integer>();
			pacman_start_y=new ArrayList<Integer>();
			
			//Lecture du fichier pour MAJ du maze (murs, capsules, dot)
			 ips=new FileInputStream(filename); 
			 ipsr=new InputStreamReader(ips);
			 br=new BufferedReader(ipsr);
			 int y=0;
			while ((ligne=br.readLine())!=null)
			{
				ligne=ligne.trim();

				for(int x=0;x<ligne.length();x++)
				{
					if (ligne.charAt(x)=='%') {walls[x][y]=true; nbwall++;} else walls[x][y]=false;
					if (ligne.charAt(x)=='.') {food[x][y]=true; nbfood++;} else food[x][y]=false;
					if (ligne.charAt(x)=='o') {capsules[x][y]=true; nbcapsule++;} else capsules[x][y]=false;
					if (ligne.charAt(x)=='P') {pacman_start_x.add(x); pacman_start_y.add(y);}
					if (ligne.charAt(x)=='G') {ghosts_start_x.add(x); ghosts_start_y.add(y);}
				}
				y++;
			}			
			br.close(); 
			
			if (pacman_start_x.size()==0)throw new MazeException("Wrong input format: must specify a Pacman start");
			
			//On verifie que le labyrinthe est clos			
			for(int x=0;x<size_x;x++) if (!walls[x][0]) throw new MazeException("Wrong input format: the maze must be closed");
			for(int x=0;x<size_x;x++) if (!walls[x][size_y-1]) throw new MazeException("Wrong input format: the maze must be closed");
			for(y=0;y<size_y;y++) if (!walls[0][y]) throw new MazeException("Wrong input format: the maze must be closed");
			for(y=0;y<size_y;y++) if (!walls[size_x-1][y]) throw new MazeException("Wrong input format: the maze must be closed");
			//System.out.println("### Maze loaded.");
			
		}		
		catch (Exception e){
			e.printStackTrace();
			throw new MazeException("Probleme a la lecture du fichier: "+e.getMessage());
		}
	}
	
	/**
	 * @return coordonnee x a partir d'un indice
	 */
	public int indexToX(int index){
		if (index == this.size_x*this.size_y)
			return -1;

		return (int)(index%size_x);
	}
	/**
	 * @return coordonnee y a partir d'un indice
	 */
	public int indexToY(int index){
		if (index == this.size_x*this.size_y)
			return -1;
		if (size_x==0)
			return -1;
		return index/size_x;
	}
	
	/**
	 * @return la taille X du labyrinthe
	 */
	public int getSizeX() {return(size_x);}

	/**
	 * @return la taille Y du labyrinthe
	 */
	public int getSizeY() {return(size_y);}
	
	/**
	 * @return si il y a un mur en (x,y)
	 */
	public boolean isWall(int x,int y) 
	{
		assert((x>=0) && (x<size_x));
		assert((y>=0) && (y<size_y));
		return(walls[x][y]);
	}
	
	/**
	 * @return si il y a de la nourriture (dot) en (x,y)
	 */
	public boolean isFood(int x,int y) 
	{
		assert((x>=0) && (x<size_x));
		assert((y>=0) && (y<size_y));
		return(food[x][y]);
	}

	/**
	 * @return si il y a une capsule (bigdot) en (x,y)
	 */
	public boolean isCapsule(int x,int y) 
	{
		assert((x>=0) && (x<size_x));
		assert((y>=0) && (y<size_y));
		return(capsules[x][y]);
	}
	
	
	/**
	 * @return le nombre de pacmans
	 * 
	 */
	public int getNumberOfPacmans() 
	{
		return(pacman_start_x.size());	
	}
	
	/**
	 * @return le nombre de fantomes
	 * 
	 */
	public int getNumberOfGhosts() 
	{
		return(ghosts_start_x.size());
	}
	
	
	/**
	 * @return coordonne initiale en X du i-eme agent pacman
	 * 
	 */
	public int getPacmanStartX(int i) 
	{
		return(pacman_start_x.get(i));
	}
	/**
	 * @return coordonne initiale en Y du i-eme agent pacman
	 * 
	 */
	public int getPacmanStartY(int i) 
	{
		return(pacman_start_y.get(i));
	}	
	/**
	 * @return coordonne initiale en X du i-eme agent fantome
	 * 
	 */
	public int getGhostStartX(int i) 
	{
		return(ghosts_start_x.get(i));
	}
	/**
	 * @return coordonne initiale en Y du i-eme agent fantome
	 * 
	 */
	public int getGhostStartY(int i) 
	{
		return(ghosts_start_y.get(i));
	}
	
	public void setFood(int x,int y,boolean b) { 
		if (b && !food[x][y]){ food[x][y]=b; nbfood++;}
		if (!b && food[x][y]){ food[x][y]=b; nbfood--;}
	}
	public void setCapsule(int x,int y,boolean b) {
		if (b && !capsules[x][y]){capsules[x][y]=b; nbcapsule++;}
		if (!b && capsules[x][y]){capsules[x][y]=b; nbcapsule--;}
		
	}
	public void setWall(int x,int y,boolean b) {
		if (b && !walls[x][y]) {walls[x][y]=b; nbwall++;}
		if (!b && walls[x][y]) {walls[x][y]=b; nbwall--;}
		}
	public void setSizeX(int x) {size_x=x;}
	public void setSizeY(int x) {size_x=x;}
	public void addPacmanStart(int x,int y) {pacman_start_x.add(x); pacman_start_y.add(y);}
	public void addGhostStart(int x,int y) {ghosts_start_x.add(x); ghosts_start_x.add(y);}
	
	
	public MazePacman copy()
	{
		MazePacman m=new MazePacman(getSizeX(),getSizeY());
		m.setNbcapsule(this.getNbcapsule());
		m.setNbfood(this.getNbfood());
		m.setNbwall(this.getNbwall());
		for(int x=0;x<size_x;x++)
			for(int y=0;y<size_y;y++)
			{
				m.setCapsule(x, y, capsules[x][y]);
				m.setWall(x, y, walls[x][y]);
				m.setFood(x, y, food[x][y]);
			}
		
		for(int i=0;i<pacman_start_x.size();i++)
			m.addPacmanStart(pacman_start_x.get(i),pacman_start_y.get(i));

		for(int i=0;i<ghosts_start_x.size();i++)
			m.addPacmanStart(ghosts_start_x.get(i),ghosts_start_y.get(i));		
		
		return(m);
	}

	public int getNbfood() {
		return nbfood;
	}

	public void setNbfood(int nbdot) {
		this.nbfood = nbdot;
	}

	public int getNbcapsule() {
		return nbcapsule;
	}

	public void setNbcapsule(int nbbigdot) {
		this.nbcapsule = nbbigdot;
	}

	public int getNbwall() {
		return nbwall;
	}

	public void setNbwall(int nbwall) {
		this.nbwall = nbwall;
	}
	
	
}
