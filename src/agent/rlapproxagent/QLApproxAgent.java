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
		for (int i = 0; i < weights.length; i++) {
			weights[i] = weights[i] + alpha * (reward + (gamma * getValeur(esuivant)) - getQValeur(e, a)) * features.getFeatures(e,a)[i];
		}
		maxValues.clear();

	}
	@Override
	public void endEpisode() {
		System.out.println(features.toString());
		//System.out.println(weightsToString(weights.length - 10));
		super.endEpisode();
	}

	
	@Override
	public void reset() {
		super.reset();
		//this.qvaleurs.clear();
	
		//*** VOTRE CODE
		for (int i = 0; i < this.weights.length; i++)
			this.weights[i] = 0d;
		
		this.episodeNb =0;
		this.notifyObs();
	}

	private String weightsToString(int startIndex) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[\n");
	    for (int i = startIndex; i < weights.length; i++) {
            if (i % 20 == 0 && i != startIndex) {
                sb.append("\n");
            }
            sb.append(weights[i] + ", ");
        }
        sb.append("\n]");

	    return sb.toString();
    }

	private void tryExtendWeights(FeatureFunctionIdentity features) {

	    //If the number of weights increased
		if (features.getFeatureNb() <= weights.length) {
			return;
		}

		double[] newWeights = new double[features.getFeatureNb()];
		//Restore existing weights
		for (int i = 0; i < weights.length; i++) {
			newWeights[i] = weights[i];
		}
		//Initialize new weights
		for (int i = weights.length; i < features.getFeatureNb(); i++) {
			newWeights[i] = 0d;
		}

		this.weights = newWeights;
	}
	
	
}
