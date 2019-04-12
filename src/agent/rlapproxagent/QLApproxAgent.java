package agent.rlapproxagent;


import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{

	private FeatureFunction features;
	private double[] weights;

	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);

		//*** VOTRE CODE

		this.features = _featurefunction;
		this.weights = new double[_featurefunction.getFeatureNb()];

		for (int i = 0; i < this.weights.length; i++)
			this.weights[i] = 0d;
		
	}

	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE


		double qVal = 0d;
		double[] qValues = features.getFeatures(e, a);

		if (features instanceof FeatureFunctionIdentity) {
			tryExtendWeights((FeatureFunctionIdentity)features);
		}

		for (int i = 0; i < qValues.length; i++) {
			qVal += weights[i] * qValues[i];
		}

		return qVal;

	}

	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant	
		
		//*** VOTRE CODE
		//Update weights
		double[] featuresValues = features.getFeatures(e,a);
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weights[i] + alpha * (reward + (gamma * getValeur(esuivant)) - getQValeur(e, a)) * featuresValues[i];
		}
		maxValues.clear();

	}
	@Override
	public void endEpisode() {
		if (features instanceof FeatureFunctionIdentity) {
			//Affiche le nombre de poids utilisés par l'épisode (variable)
			System.out.println(features.toString());
		}
		else {
			//On affiche les poids de chaque fonctions, et leur moyennes
			System.out.println(weightsToString(0));
			System.out.println(((FeatureFunctionPacman)features).meansToString());
		}
		super.endEpisode();
	}

	
	@Override
	public void reset() {
		super.reset();
	
		//*** VOTRE CODE
		for (int i = 0; i < this.weights.length; i++)
			this.weights[i] = 0d;
		
		this.episodeNb =0;
		this.notifyObs();
	}

	/***
	 * Renvoie un string avec les poids de l'agent, en partant de l'index donné.
	 * @param startIndex
	 * @return
	 */
	private String weightsToString(int startIndex) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Weights : [");
	    for (int i = startIndex; i < weights.length; i++) {
            sb.append(String.format("%2.2e",weights[i]));
            if (i != weights.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

	    return sb.toString();
    }

	/**
	 * Increase the size of the weights array if the features size increased. (Meaning FeatureIdentity has a bigger array
	 * than the current one.)
	 * @param features
	 */
	private void tryExtendWeights(FeatureFunctionIdentity features) {

		if (features.getFeatureNb() <= weights.length) {
			return;
		}
		//If the number of weights increased

		//Increase the weights array sizes
		double[] newWeights = new double[features.getFeatureNb()];
		//Restore the existing weights values
		for (int i = 0; i < weights.length; i++) {
			newWeights[i] = weights[i];
		}
		//Initialize new weights to 0
		for (int i = weights.length; i < features.getFeatureNb(); i++) {
			newWeights[i] = 0d;
		}

		this.weights = newWeights;
	}
	
	
}
