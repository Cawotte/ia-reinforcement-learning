package agent.rlagent;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import agent.ValueAgent;
import agent.strategy.StrategyExploration;
import agent.strategy.StrategyGreedy;
import agent.strategy.StrategyManuel;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Cet agent evalue les couples etat-action, et observe l'environnement 
 * pour savoir quand une transition est realisee et etre notifie de la recompense recue.
 * 
 * Cet agent est observe (Agent classe mere observable) par indicateur
 * @author lmatignon
 *
 */
public abstract class RLAgent extends ValueAgent implements IRLAgent,Observer{
	/**
	 * coefficient d'apprentissage
	 */
	protected double alpha;
	/**
	 * discount facteur
	 */
	protected double gamma;
	/**
	 * sauvegarde etat precedent pour mise a jour
	 */
	protected Etat etatPrec;
	/**
	 * sauvegarde action choisie
	 */
	protected Action actionChoisie;
	/**
	 * 
	 */
	public Environnement env;//necessaire pour connaitre les actions possibles, l'etat courant, et realiser des actions dans l'environnement
	/**
	 * somme des recompenses par episode
	 */
	protected double sumrecepisode = 0.0;
	
	public static boolean DISPRL = false;
	/**
	 * Strategie d'exploration choisie pour les episodes d'apprentissages 
	 */
	protected StrategyExploration stratExplorationCourante;
	/**
	 * Une strategie possible : manuelle (clavier)
	 */
	protected StrategyManuel stratExplorationManuel;
	/**
	 * Une strategie possible : manuelle (clavier)
	 */
	protected StrategyGreedy stratExplorationGreedy;
	
	/**
	 * Initialise les paramètres, la strategie d'exploration a manuelle,
	 * ajoute l'environnement comme observe
	 * @param alpha
	 * @param gamma
	 * @param _env
	 */
	public RLAgent(double alpha, double gamma,Environnement _env) {
		super();
		this.alpha = alpha;
		this.gamma = gamma;
		
		this.stratExplorationManuel = new StrategyManuel(this);
	//	RLAgent.stratExplorationManuel.setAgent(this);
		this.stratExplorationGreedy = new StrategyGreedy(this,0.1);
	//	RLAgent.stratExplorationGreedy.setAgent(this);
				
		this.stratExplorationCourante = stratExplorationManuel;
		
		this.env=_env;
		this.env.addObserver(this);
		this.etatPrec = env.getEtatCourant();
		this.actionChoisie = null;

	}
	/**
	 * initialise les parametres pour un episode d'apprentissage: reset l'etat courant de l'environnement
	 */
	@Override
	public void startEpisode(){
		env.reset();
		if (DISPEPISODE)
			System.out.println("\n\n Episode "+this.episodeNb);
		this.etatPrec = env.getEtatCourant();
		this.actionChoisie = null;
		this.nbpasparepisode = 0;
		this.sumrecepisode = 0.0;
	}
	@Override 
	public void endEpisode(){
		super.endEpisode();
		if (DISPEPISODE)
			System.out.println("somme recompenses de l'episode "+this.getSumrecepisode());
		this.notifyObs(this.getSumrecepisode());//notifie observer IndicateurSumRwd
	}
	/**
	 * @return liste des actions possibles dans etat _e. Renvoi liste vide si etat absorbant.
	 */
	public List<Action> getActionsLegales(Etat e){
		return env.getActionsPossibles(e);
	}
	/**
	 * lance nbe episode, chqaue episode
	 * <li> dure un nombre max d'episode ou jusqu'a etat absorbant
	 * <li> appele startEpisode et endEpisode
	 * <li> execute action de this sur etat courant de l'environnement {@link Environnement#doAction}
	 * 
	 * @param nbe
	 */
	public void runEpisode(int nbe){
		for (int i=0; i<nbe ; i++){
			startEpisode();//sumrec remis a 0
			
			while (!env.estAbsorbant() && this.nbpasparepisode<=this.maxnbpasparepisode){
				this.runOneStep();				
			}
			
			this.endEpisode();
			
		}
		
	}
	/**
	 * lance un pas de l'episode, sans verification sur etat absorbant, ...
	 *  <li> execute action de this sur etat courant de l'environnement {@link Environnement#doAction}
	 */
	public void runOneStep(){
		this.nbpasparepisode ++;
		Etat etat= env.getEtatCourant();
		Action act = this.getAction(etat);	//modif de etat courant de environnement
		env.doAction(act) ; //env notifie son observateur (agent) de la recompense et fait alors update dans lequel endStep
	}

	/**
	 * 
	 * @param e
	 * @param a
	 * @return valeur du couple (e,a)
	 */
	public abstract double getQValeur(Etat e,Action a);
	
	/**
	 * Met a jour la valeur du couple (e,a) avec la valeur d
	 * @param e
	 * @param a
	 * @param d
	 */
	public abstract void setQValeur(Etat e,Action a,double d);
	/**
	 * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
	 * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
	 * @param e
	 * @param a
	 * @param esuivant
	 * @param reward
	 */
	public abstract void endStep(Etat e, Action a, Etat esuivant, double reward);
	

	
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
		System.out.println("alpha:"+alpha);
	}
	public double getGamma() {
		return gamma;
	}
	public void setGamma(double gamma) {
		this.gamma = gamma;

		System.out.println("gamma:"+gamma);
	}
	public StrategyExploration getStratExploration() {
		return stratExplorationCourante;
	}
	
	public void setStratExplorationCourante(StrategyExploration strat) {
		stratExplorationCourante = strat;
	}
	
	public void setStratExplorationGreedy(double _epsilon) {
		stratExplorationGreedy.setEpsilon(_epsilon);
		this.stratExplorationCourante = stratExplorationGreedy;
		
	}
	
	public void setStratExplorationManuel() {
		this.stratExplorationCourante = stratExplorationManuel;
		
	}
	public void setStratExploration(StrategyExploration s) {
		this.stratExplorationCourante = s;
		
	}
	public void setStratExplorationSoftmax(double tau) {
		;
		
	}
	
	
	
	public double getSumrecepisode() {
		return sumrecepisode;
	}
	public void setSumrecepisode(double sumrecepisode) {
		this.sumrecepisode = sumrecepisode;
	}
	/**
	 * L'environnement notifie l'agent RL lorsque doAction(a) est appele par l'agent.
	 * L'agent est notifie avec la recompense recue pour la transition.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg0 instanceof Environnement && arg1!=null){
			double reward = (Double)(arg1);
			this.sumrecepisode += reward;
			Etat etatnouveau = ((Environnement)(arg0)).getEtatCourant();
			this.endStep(this.etatPrec, this.actionChoisie, etatnouveau, reward);
			this.etatPrec =etatnouveau  ;
		}
	}
	
	@Override
	public Environnement getEnv() {
		return this.env;
	}

}
