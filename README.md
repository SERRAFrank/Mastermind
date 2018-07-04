# Mettez votre logique à l'épreuve
Ceci est le dépôt associé au projet [ Mettez votre logique à l'épreuve ](https://openclassrooms.com/projects/mettez-votre-logique-a-l-epreuve)
sur [_OpenClassrooms_](https://www.openclassrooms.com).

## Introduction

MasterMind est un jeu de réflexion consistant à trouver une combinaison cachée, ou la faire deviner à l'ordinateur, en un nombre limité d'essais.
Il dispose de base de deux modes de jeux:
- Plus ou moins , où il s'agit de retrouver la combinaison à x chiffres de l'adversaire (le défenseur). Pour ce faire, l'attaquant fait une proposition. Le défenseur indique pour chaque chiffre de la combinaison proposée si le chiffre de sa combinaison est plus grand (+), plus petit (-) ou si c'est le bon chiffre (=).
- Masterind , où le  défenseur indique pour chaque proposition de l'attaquant le nombre de chiffre/couleur de la proposition qui apparaissent à la bonne place et à la mauvaise place dans la combinaison secrète.

## Technologies

- Java 8
- Maven 3.3.9
```
http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
```

## Déploiement
### Demarrage

L'application peut se lancer en mode console ou graphique.
Elle supporte plusieurs arguments:
```
java -jar mastermind.jar -h
usage: MasterMind [-h] [-l <lenght>] [-s] [-v <view>]
 -d,--debug             Force debug mode
 -h,--help              Help
 -l,--lenght <lenght>   Combination lenght between 0 and 9
 -s,--save              Save arguments
 -v,--view <view>       View mode : graphic or console
```


### Parametres

L'application est configurable via le fichier resources/config.properties , et par le menu Configuration en mode graphique.
