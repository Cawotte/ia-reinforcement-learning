package pacman.environnementRL;

import pacman.elements.ActionPacman;
import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

import java.util.ArrayList;

/***
 * Des fonctions utiles utilisé pour différencier les états ou
 * comme FeatureFonction du pacman, à partir de StateGamePacmans
 */
public class StateGameFunctions {


    //region features fonction

    /***
     * Renvoie la distance du point le plus proche du pacman. (Euclidienne?)
     * @param stateGame
     * @return
     */
    public static double getClosestDotDistance(StateGamePacman stateGame) {

        StateAgentPacman pacman = stateGame.getPacmanState(0);
        int mapX = stateGame.getMaze().getSizeX();
        int mapY = stateGame.getMaze().getSizeY();

        return stateGame.getClosestDot(pacman) / (double)(mapX + mapY);
    }

    /***
     * Renvoie la distance du fantome le plus proche du pacman. (Euclidienne?)
     * @param stateGame
     * @return
     */
    public static double getClosestGhostDistance(StateGamePacman stateGame) {

        StateAgentPacman pacman = stateGame.getPacmanState(0);

        StateAgentPacman closestGhost = null;
        double minDistanceGhost = Integer.MAX_VALUE;

        for (int i = 0; i < stateGame.getNumberOfGhosts(); i++) {

            StateAgentPacman ghost = stateGame.getGhostState(i);
            double distance = getDistance(pacman, ghost);

            if (closestGhost == null || minDistanceGhost > distance) {
                minDistanceGhost = distance;
                closestGhost = ghost;
            }
        }

        int mapX = stateGame.getMaze().getSizeX();
        int mapY = stateGame.getMaze().getSizeY();

        return minDistanceGhost / (mapX * mapY);
    }

    /**
     * Compte le nombre de fantome à une distance maxDistance ou moins du Pacman.
     * @param stateGame
     * @param maxDistance
     * @return
     */
    public static int getNbGhostAtDistance(StateGamePacman stateGame, int maxDistance) {

        int nbGhost = 0;
        StateAgentPacman pacman = stateGame.getPacmanState(0);
        for (int i = 0; i < stateGame.getNumberOfGhosts(); i++) {
            StateAgentPacman ghost = stateGame.getGhostState(i);

            //If that ghost is at distance maxD or less from pacman, count it.
            if (getManhattanDistance(pacman, ghost) <= maxDistance) {
                nbGhost++;
            }
        }

        return nbGhost;

    }

    /**
     * Renvoie vrai si la nouvelle position
     * @param oldState
     * @param newState
     * @return
     */
    public static boolean nextPositionHasDot(StateGamePacman oldState, StateGamePacman newState) {

        return newState.getFoodEaten() > oldState.getFoodEaten();
    }


    //endregion

    public static int getFoodEaten(StateGamePacman stateGame) {
        return stateGame.getMaze().getNbfood();
    }

    /**
     * Renvoie un hash unique représentant les coordonnées des fantomes de stateGame
     * @param stateGame
     * @return
     */
    public static int getGhostPosHash(StateGamePacman stateGame) {
        int hash = 0;
        for (int i = 0; i < stateGame.getNumberOfGhosts(); i++) {
            StateAgentPacman ghost = stateGame.getGhostState(i);
            int ghost_x = ghost.getX();
            int ghost_y = ghost.getY();

            hash *= 10;
            hash += ghost_x;
            hash *= 10;
            hash += ghost_y;
        }
        return hash;
    }

    /***
     * Renvoie la direction (NORTH, EAST.. ) du Dot le plus proche
     * @param stateGame
     * @return
     */
    public static int getDirectionNextDot(StateGamePacman stateGame) {
        StateAgentPacman pacman = stateGame.getPacmanState(0);
        int currentDotDistance = stateGame.getClosestDot(pacman);
        int bestDir = 4; //STOP

        ArrayList<ActionPacman> validDirections = getValidDirections(stateGame);
        for (ActionPacman action : validDirections) {

            StateGamePacman nextGameState = stateGame.nextStatePacman(action);
            if ( nextGameState.getClosestDot(nextGameState.getPacmanState(0)) <= currentDotDistance ) {
                bestDir = action.getDirection();
                currentDotDistance = nextGameState.getClosestDot(nextGameState.getPacmanState(0));
            }
        }
        return bestDir;
    }

    public static ArrayList<ActionPacman> getValidDirections(StateGamePacman stateGame) {

        StateAgentPacman pacman = stateGame.getPacmanState(0);
        ArrayList<ActionPacman> validDirections = new ArrayList<>();

        //4 direction (STOP = 4)
        for (int dir = 0; dir < 4; dir ++) {
            ActionPacman move = new ActionPacman(dir);

            //If the next tile in this direction is not a wall
            if (!stateGame.getMaze().isWall(pacman.getX() + move.getX(), pacman.getY() + move.getY())) {
                validDirections.add(move);
            }

        }

        return validDirections;
    }
    /***
     * Renvoie le fantome le plus proche de Pacman
     * @param stateGame
     * @param maxDistance
     * @return
     */
    public static StateAgentPacman getClosestGhost(StateGamePacman stateGame, int maxDistance) {

        StateAgentPacman pacman = stateGame.getPacmanState(0);
        StateAgentPacman closestGhost = null;

        int minDistanceGhost = Integer.MAX_VALUE;

        for (int i = 0; i < stateGame.getNumberOfGhosts(); i++) {
            StateAgentPacman ghost = stateGame.getGhostState(i);
            int distance = getManhattanDistance(pacman, ghost);

            if ( distance < minDistanceGhost  && distance <= maxDistance) {
                minDistanceGhost = distance;
                closestGhost = ghost;
            }
        }

        return closestGhost;
    }

    /**
     * Renvoie la distance de Manhattan entre les deux agents
     * @param pacman
     * @param ghost
     * @return
     */
    public static int getManhattanDistance(StateAgentPacman pacman, StateAgentPacman ghost) {
        return (ghost != null) ? Math.abs(ghost.getX() - pacman.getX()) + Math.abs(ghost.getY() - pacman.getY()) : 0;
    }

    public static int getManhattanDistance(StateAgentPacman pacman, StateAgentPacman ghost, int maxDistance) {
        int distance = (ghost != null) ? Math.abs(ghost.getX() - pacman.getX()) + Math.abs(ghost.getY() - pacman.getY()) : 0;
        return (distance <= maxDistance) ? distance : 0;
    }
    public static double getDistance(StateAgentPacman pacman, StateAgentPacman ghost) {
        if (ghost == null) return 0;

        double deltaX = pacman.getX() - ghost.getX();
        double deltaY = pacman.getY() - ghost.getY();
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY);

    }

    /***
     * Renvoie la direction (NORTH, EAST...) vers laquelle l'agent ghost se trouve par rapport à l'agent pacman.
     * @param pacman
     * @param ghost
     * @return
     */
    public static int getDirection(StateAgentPacman pacman, StateAgentPacman ghost) {
        return (ghost != null) ? getDirection( pacman.getX(), pacman.getY(), ghost.getX(), ghost.getY()) : 0;
    }

    public static int getDirectionClosestGhost(StateGamePacman stateGame, int maxDistance) {
        StateAgentPacman ghost = getClosestGhost(stateGame, maxDistance);

        if (ghost == null)
            return MazePacman.STOP; //default value if ghost too far
        else
            return getDirection(stateGame.getPacmanState(0), ghost);
    }

    public static int getDirectionClosestGhost(StateGamePacman stateGame) {
        return getDirectionClosestGhost(stateGame, 1000);
    }

    public static int getDirection(int x1, int y1, int x2, int y2) {

        float angle = getAngle(x1, y1, x2, y2);
        if (angle < 45) {
            return MazePacman.EAST;
        }
        else if (angle < 135) {
            return MazePacman.NORTH;
        }
        if (angle < 225 ) {
            return MazePacman.WEST;
        }
        else if (angle < 315){
            return MazePacman.SOUTH;
        }
        else {
            return MazePacman.EAST;
        }
    }

    public static float getAngle(int x1, int y1, int x2, int y2) {
        float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}
