#!/bin/sh 
# ----------------------------------------
# Script de démarrage de l'application : <nom-appli>
# Author : <auteur>
# Organization : Université de Lorraine
# date : <date>
# ---------------------------------------
# exemple:
# > <donner ici un exemple de ligne de commande>

# -----------------------------------------
# A modifier
# -----------------------------------------
# --->1) répertoire d'installation
PROJ_HOME="`dirname $0`/.."

# --->2) répertoire où se trouvent les *.jar
LIB_DIR="$PROJ_HOME/lib"

# --->3) répertoire où se trouvent les *.class
CLASSES_DIR="$PROJ_HOME/build/classes"


# --->4) nom de la classe à démarrer
CLASS_NAME=<nom classe main>

# -->5) paramètres de la commande
PARAMS=""

# -----------------------------------------
# A ne pas modifier
# -----------------------------------------

# ---->Classpath automatique 
PROJ_CP="$PROJ_HOME/build/classes"
for x in `ls "$LIB_DIR"`
do
PROJ_CP="$PROJ_CP:$LIB_DIR/$x"
done

# ---->java command
COMMAND="java -classpath \"$PROJ_CP\" $CLASS_NAME $PARAMS $@"
#echo $COMMAND
eval $COMMAND

# ----------------------------------------
#                  END
# ----------------------------------------
