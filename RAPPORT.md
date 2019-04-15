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

Pour éviter de multiplier les états, tout les critères ont des valeurs entières, de plus, tout les calculs de distance de cette partie 
sont des distances de Manhattan, pour la même raison.

Voici les différents critères avec lesquels j'ai expérimenté :

- **La position (x,y) des fantômes** : Des informations sur les fantômes sont impératives, car ils provoque une défaite et 
ont un comportement aléatoire, qui ne peut pas être implicitement représenté dans la politique. 

- **La distance de Manhattan avec les fantômes** : Une tentative de simplification du critère précédent. Un couple de coordonnées peut prendre de
très nombreuses valeurs différentes, mais beaucoup d'entre elles ont une signification similaire. On cherche ici à condenser l'information et
réduire le nombre d'états générés par les informations des fantômes. Voici des variantes plus précises qui ont été utilisés :
    - **La distance de Manhattan avec le fantôme le plus proche**
    - **La distance de Manhattan avec les fantômes à une distance de Manhattan de moins de 3** : 
    On ignore les fantômes qui sont trop loin pour être considérés une menace, et on utilise la valeur par défaut 0 dans ce cas.
On utilise une distance de manhattan au lieu d'une distance euclidienne pour garder des résultats entiers, des réels démultiplierait le nombre d'états.

- **La direction vers laquelle se trouve le fantôme le plus proche par rapport au Pacman** : En calculant l'angle entre Pacman et les fantômes, il est possible
d'obtenir une direction général de vers laquelle ils se trouve, NORTH, SOUTH, EAST, WEST. C'est un critère utilisé pour donner plus de sens aux calculs de distance,
afin de différencier un fantôme à une distance de 3 au Nord et un fantôme à une distance de 3 au Sud.
    - **Variante : La direction du fantôme le plus proche à une distance de Manhanttan de 3 ou moins** : On ignore les fantômes trop lointain pour être une menace, on renvoie une valeur par
    défaut si le fantôme est trop loin.

- **La distance du Dot le plus proche** : Le but étant de collecter des points, il est important d'avoir des informations permettant de savoir où les trouver.

- **La direction du Dot le plus proche** : La direction (EAST, WEST, ect...) du point le plus proche à collecter. Utile pour aider à diriger le pacman vers les récompenses.

- **Le nombre de Dot restants** : Derrière ce choix ce cache l'espoir qu'au fur et à mesure de l'entraînement, le Pacman
va développer un ordre de ramassage favori des Dot, et ainsi repérer où il en est et vers où se diriger en fonction du nombre de Dot restant ou mangés. 
C'était un critère surtout utile avant que je réussisse à calculer la direction du dot le plus proche.


Voici un tableau de résumé des critères utilisés :

![descriptionQ](./images/desc_Qlearning.png)

Je n'ai pas utilisé la position du Pacman comme critère ou d'informations lié à la position du Pacman. Je suis parti du principe que les informations
sur les points à collecter et les fantômes à éviter sont les seuls critères vitaux à la réussite de l'agent : Il n'a pas besoin de savoir où il est,
mais de vers où il doit aller et vers où il ne doit pas aller.


Dans ma quête d'optimisation des résultats et de dépasser les 10% de réussite pour la grille de taille moyenne,
voici un tableau résumant les résultats que j'ai obtenus avec différentes combinaisons de critères, où j'inscris le pourcentage de succès moyen
du pacman après entraînement, et le nombre d'états moyen générées. (Mon code générant les états au fur et à mesure de leur 
utilisation, il arrive d'avoir moins d'états que la maximum théorique).

Voici une table de résultats obtenus avec différents critères :


![resultsQ](./images/results_Qlearning.png)

Des notes et observations sur ses résultats :

**Sur les quatres premières lignes** se trouvent mes essais pour diminuer le nombre d'états et arriver à un temps de calcul viable sur MediumGrid. 
J'aurais réussi à baisser le nombre d'états à environ 300, mais en gardant des résultats médiocre sur mediumGrid. 
La faute étant que je n'avais pas encore réussi à calculer la direction du prochain Dot à ce moment-là, 
et qu'il manquait des informations sur les dots restants à récupérer. Mon agent devenait un dieu de l'esquive et arrivait
à éviter les fantômes très longtemps, sans jamais vraiment trouver comment ramasser le prochain dot.

Ensuite, la révélation, j'arrive à calculer la direction du prochain dot ! Le critère le plus fiable pour aider le pacman à ramasser tout les points 
et gagner. **Les quatres lignes restantes l'utilisent toute.**

Sur la cinquième ligne, j'obtiens de très bons résultats avec juste 25 états max (pour toute taille de grille!) en utilisant seulement
la direction du prochain dot et du fantôme le plus proche et pas trop loin, pour donner du sens à la menace. L'agent atteint 50% de réussite sur MediumGrid !

Sixième ligne, identique mais je prends la direction du fantôme le plus proche quel que soit la distance, les résultats sont bien plus bancales car en toujours
la direction d'un fantôme, l'agent ne sais pas à quel point celui-ci le menace vraiment.

**Les meilleurs résultats sont sur l'avant-dernière ligne**, avec la combinaison de direction du fantome proche et de distance du fantome. Les 81% sont atteints sur mediumGrid !

Petit cas étrange sur la dernière ligne, où l'ajout d'information rend les résultats moins bon ! Le nombre supplémentaire d'états rend peut-être l'entraînement trop difficile ?

**En conclusion**, pour entraîner correctement un agent, il est impératif d'avoir des critères permettant de correctement représenter les récompenses, positives et négatives.
Dans notre cas du Pacman, les points à collecter et fantômes à esquiver.
De plus on trouve les limites de l'apprentissage tabulaire avec la durée des calculs : Trop de critères impliquent trop d'états, qui impliquent un temps de calcul très long.
Sur certains problèmes complexes, il n'est pas possible de les représenter assez abstraitement pour n'avoir trop d'états.

**Notes :** Vous pouvez facilement tester différentes combinaisons en modifiant le constructeur de EtatPacmanMDPClassic,
en commentant/décommentant les critères voulus dans la déclaration du tableau ci-dessous. N'oubliez pas de modifier les autres paramètres
de départ dans testRLPacman.

![testQ](./images/criterias_Qlearning.png)

## Question 2:
*Précisez et justifiez les fonctions caractéristiques que vous avez choisies pour la classe FeatureFunctionPacman (partie 2.3).*

Smallgrid : Distance dot, ghost x, ghost y