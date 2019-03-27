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

//Parler position Pacman

//Parler overtraining

Voici une table de résultats obtenus avec différents critères :

|Critères| % de succès smallLayout | % de succès smallLayout2 | % de succès MediumLayout
| -------------------- | ----- | ----- | ---- 
|Coordonnées fantômes<br>Distance Dot le plus proche<br> | 10%<br>(20 Etats) | 10%<br>(20 Etats) | 10%<br>(20 Etats) |
|Coordonnées fantômes<br>Critère 2<br>Critère 3 | 10%<br>(20 Etats) | 10%<br>(20 Etats) | 10%<br>(20 Etats) |
|Coordonnées fantômes<br>Critère 2<br>Critère 3 | 10%<br>(20 Etats) | 10%<br>(20 Etats) | 10%<br>(20 Etats) |
|Coordonnées fantômes<br>Critère 2<br>Critère 3 | 10%<br>(20 Etats) | 10%<br>(20 Etats) | 10%<br>(20 Etats) |
|Coordonnées fantômes<br>Critère 2<br>Critère 3 | 10%<br>(20 Etats) | 10%<br>(20 Etats) | 10%<br>(20 Etats) |

//REmarque mega esquive

Le temps d'exécution et efficacité de l'algorithme est lié au nombre d'états :
100+ = Rapide + fiable (90%+)
200+ = Moyen + bon (80%+)


## Question 2:
*Précisez et justifiez les fonctions caractéristiques que vous avez choisies pour la classe FeatureFunctionPacman (partie 2.3).*
