package pacman.elements;
/**
 * Exception levee dans constructeur de {@link MazePacman} si mauvais formattage du fichier 
 */
public class MazeException extends Exception {

	
	private static final long serialVersionUID = 1L;

	public MazeException(String m)
	{
		super(m);
	}
}
