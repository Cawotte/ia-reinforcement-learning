package agent.rlapproxagent;

import pacman.elements.StateGamePacman;
import pacman.environnementRL.StateGameFunctions;

public enum EnumFeatureFunction {
    BIAS,
    NB_CLOSE_GHOST,
    HAS_DOT,
    DIST_CLOSEST_DOT,
    NB_CLOSE_GHOST_3,
    DIST_CLOSEST_GHOST;

    public double computeFeatureFunction(StateGamePacman oldState, StateGamePacman newState) {
        switch (this) {
            case BIAS:
                return 1d;
            case NB_CLOSE_GHOST:
                return (double)StateGameFunctions.getNbGhostAtDistance(newState, 1);
            case HAS_DOT:
                boolean nextPosHasDot = StateGameFunctions.nextPositionHasDot(oldState, newState);
                return (nextPosHasDot) ? 1d : 0d;
            case DIST_CLOSEST_DOT:
                return StateGameFunctions.getClosestDotDistance(newState);
            case NB_CLOSE_GHOST_3:
                return (double)StateGameFunctions.getNbGhostAtDistance(newState, 3);
            case DIST_CLOSEST_GHOST:
                return StateGameFunctions.getClosestGhostDistance(newState);
            default:
                return 0d;
        }

    }
}
