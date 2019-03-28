**Etudiant 1 : ENSUQUE Elie**

# Rapport TP1

## Question 5.1 Brigde Grid
*Donnez les valeurs des paramètres et la justification de ces choix*

Le bon paramètre est un bruit égal à 0.

Les états absorbants négatifs au bord du pont ont une récompense de -100, et vont extrêmement 
vite impacter les valeurs de Vk(s) si il y a du bruit, même avec un gamma très faible, et ainsi "dissuader"
complètement la politique de franchir le pont.
En passant de déplacements stochastique à déterministe, on élimine le risque de tomber dans un
état absorbant très négatif, et l'agent peut franchir le pont sans soucis pour atteindre la récompense +10.

## Question 5.2 Discount Grid
*Donnez les valeurs des paramètres dans chaque cas et la justification de ces choix*

1. Chemin sûr pour l'état absorbant +1 : **Récompense = -2**

Avec une récompense négative à chaque pas, on pousse l'agent à choisir une politique qui va lui faire le
moins de pas possible et atteindre le plus rapidement un état absorbant positif. Il va donc prendre le chemin risqué,
et s'arrêter à l'état absorbantt le plus près, +1, car rejoindre -10 engendrait trop de "pertes".

2. Chemin risqué pour l'état absorbant +10 : **bruit = 0.0**

En passant à un environnement déterministe, on élimine le risque de tomber dans le "fossé"
et la politique va simplement guider l'agent sur le chemin le plus court pour aller à la plus
haute récompense, soit le chemin risqué sur l'état absorbant +10.

3. Chemin sûr pour l'état absorbant +1 : **gamma = 0.3**

Un gamma faible réduit plus rapidement l'influence d'une récompense sur Vk(s) à chaque pas s'éloignant
de son état absorbant. Avec un gamma suffisamment faible, la récompense +10 est suffisamment
atténué pour que la politique favorise l'état absorbant +1 lorsque l'agent va passer à côté.
L'agent continue de prendre le chemin sûr, car à cause du bruit de 0.2 et la falaise, il va continuer à
éviter le chemin risqué. 

4. Eviter états absorbants : **Récompense +5.**

Avec une récompense élevée à chaque pas, il sera plus rentable pour l'agent de tourner en rond et accumuler des points,
que d'atteindre un des états absorbants. 


# Rapport TP2

## Question 1:
*Précisez et justifiez les éléments que vous avez utilisés pour la définition d’un état du MDP pour le jeu du Pacman (partie 2.2)*

Ce problème revient à trouver quels critères vont être utilisé pour différencier les états afin d'avoir un temps de calcul raisonnable,
tout en gardant des résultats cohérents.
Pour chaque tuple de valeurs possibles parmi les critères choisis, il existera un état différent.
On cherche donc un petit nombre de critère pour éviter la profilération d'état,
mais qui ont du sens pour la résolution du problème. On peut le voir comme en répondant à cette question :

"*Qu'elle est le strict minimum d'informations que j'ai besoin de connaître pour gagner une partie?*"

Voici différents critères avec lesquels j'ai expérimenté :

- **La position (x,y) des fantômes** : Des informations sur les fantômes sont impératives, car ils déclenchent la défaite et 
ont un comportement aléatoire, ce qui fait qu'ils ne peuvent pas être indirectement représenté dans la politique. 

- **La distance de Manhattan avec les fantômes** : Une tentative de simplification du critère précédent. Un couple de coordonnées peut prendre de
très nombreuses valeurs différentes, mais beaucoup d'entre elles ont une signification similaire. On cherche ici à condenser l'information et
réduire le nombre d'états générés par les informations des fantômes. Voici des variantes plus précises qui ont été utilisés :
    - **La distance de Manhattan avec le fantôme le plus proche**
    - **La distance de Manhattan avec les fantômes à une distance de Manhattan de moins de X (X étant souvent 3)** : 
    On ignore les fantômes qui sont trop loin pour être considérés une menace, et utilisera une valeur par défaut pour ce cas. 

- **La direction vers laquelle se trouve les fantômes par rapport au Pacman** : En calculant l'angle entre Pacman et les fantômes, il est possible
d'obtenir une direction général de vers laquelle ils se trouve, NORTH, SOUTH, EAST, WEST. C'est un critère utilisé pour donner plus de sens aux calculs de distance,
afin de différencier un fantôme à une distance de 3 au Nord et un fantôme à une distance de 3 au Sud.

- **La distance du Dot le plus proche** : Le but étant de collecter ces points, il est important d'avoir une information permettant de savoir où les trouver. 
Je n'ai hélas pas réussi à calculer la direction du Dot le plus proche, cela aurait surement bien marché.

- **Le nombre de Dot mangés** et **le nombre de Dot restants** : Derrière ce choix ce cache l'espoir qu'au fur et à mesure de l'entraînement, le Pacman
va développer un ordre de ramassage favori des Dot, et ainsi repérer où il en est et vers où se diriger en fonction du nombre de Dot restant ou mangés. 

Je n'ai pas utilisé la position du Pacman comme critère ou d'informations lié à la position du Pacman. Je suis parti du principe que cette information
existe implicitement à travers les connexions entre les états, à la manière du TP1 sur les grilles. Mais maintenant que j'y repense en rédigeant ce rapport, 
je me rends compte que j'ai probablement eu tort. Dans le TP1 chaque état représentait la case précise où se trouvait l'agent et donc ils y a bien eu un ancrage géographique concret.
Ce que j'ai fait est donc bien plus abstrait, et s'apparente plus à une représentation de 'situation', représentant à quel point l'agent est "en danger" ou encore "proche d'un point".

Du coup, j'ai ajouté plus tard une combinaison de critère utilisant la position du pacman pour vérifier si elle était capable de corriger
les défauts de mes précédents essais.


Dans ma quête d'optimisation des résultats et la tentative de franchir un seuil crédible pour la grille de taille moyenne,
voici un tableau résumant les résultats que j'ai obtenus avec différentes combinaisons de critères, où j'inscris le pourcentage de succès moyen
du pacman après entraînement, et le nombre d'états moyen générées. (Mon code générant les états au fur et à mesure de leur 
utilisation, il arrive d'avoir moins d'états que la maximum théorique).


Voici une table de résultats obtenus avec différents critères :

|Critères| % de succès<br>smallGrid2 | % de succès<br>smallGrid2 | % de succès<br>mediumGrid
| -------------------- | ----- | ----- | ---- 
|- Coordonnées des fantômes<br>- Distance Dot le plus proche<br> | 89%<br>(~100 Etats) | 65%<br>(~180 Etats) | <5%<br>(11 000+ Etats) |
|- Coordonnées des fantômes<br>- Distance Dot le plus proche<br>- Nombre de dot restants | 92%<br>(~150 Etats) | 85%<br>(~375 Etats) | <5%<br>(17 000+ Etats) |
|- Distance Dot le plus proche<br>- Distance fantôme le plus proche<br>- Direction fantôme le plus proche | 96%<br>(~130 Etats) | 87%<br>(~140 Etats) | <5%<br>(1 800+ Etats) |
|- Distance Dot le plus proche<br>- Distance fantôme le plus proche (**si d<4**)<br>- Direction fantôme le plus proche (**si d<4**) | 92%<br>(~63 Etats) | 90%<br>(~54 Etats) | <5%<br>(~300 Etats) |
|- Coordonnées Pacman<br>- Distance fantôme le plus proche (**si d<4**)<br>- Direction fantôme le plus proche (**si d<4**) | <5%<br>(~80 Etats) | 93%<br>(~135 Etats) | <5%<br>(1 200+ Etats) |

Au final, je n'aurais pas réussi à avoir un temps raisonnable sur la grille de taille moyenne. Voici par contre plusieurs remarques et 
observations intéressantes que j'ai pu déduire de mes tests, qui sont surtout valables pour la grille moyenne :


**Sur-simplification** : Le revers de l'optimisation. A trop réduire le nombre d'états, il arrive que différentes cases et situations ne sont plus 
assez différenciables et qu'il prennent des décisions similaire là où il devrait faire plus la différence. C'est surtout important pour la grille moyenne, où
le pacman s'est régulièrement trouver à errer dans le même coin de la carte sans explorer le reste. En fait, de nombreuses choses découle de ce problème :


**Représentation pas assez géographique?** : Je pense que mes critères n'ont pas assez représenté où en était l'agent géographiquement, et ne lui ont pas permis
assez d'explorer le terrain pour chercher plus de points, le bloquant dans une zone de la grille car au lieu de voir comment il devait chercher des points,
il voyait comment devenir un...

**Un Dieu de l'Esquive** : Oui, quitte à ne pas savoir collecter tout les points, à travers mes critères, Pacman est devenu extrêmement bon pour survivre
face aux fantômes. A vrai dire le temps de calcul de mes dernières combinaisons de critères sur la grille moyenne était très longue non car il y avait beaucoup d'états,
mais car il passait facilement plus de 1000-2000 pas d'itérations avant de perdre à chaque essai.  (**Record actuel : 10 001**) Si la récompense par pas était positive,
mes choix critères aurait sûrement eu des résultats excellents partout.

**Résultats surprenants avec les coordonnées du Pacman** : Après l'ajout des coordonnées des pacman comme critères pour ma dernière combinaison,
j'ai eu des résultats très surprenant. L'agent est devenu extrêmement mauvais sur la première petite grille, mais
il est aussi devenu un Dieu-Dieu de l'Esquive, touchant systématiquement la limite des 10 000 pas. Toutefois il a gardé de très bon résultats
sur la seconde grille moyenne. 

Pour la première petite grille, ses résultats sembleait chuter et atteindre une limite critique vers la moitié/deux-tiers de
son entraînement, possiblement un bug à l'oeuvre ?

**Conclusion** : C'était un exercice intéressant, mais il semblent effectivement que l'approche tabulaire à ses limites, notamment quand on 
traite des environnements large où qui ne peuvent être représenté simplement. 

## Question 2:
*Précisez et justifiez les fonctions caractéristiques que vous avez choisies pour la classe FeatureFunctionPacman (partie 2.3).*
