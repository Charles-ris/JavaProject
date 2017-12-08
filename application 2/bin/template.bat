@echo OFF
:: ----------------------------------------
:: Script de demarrage de l'application : <nom-application>
:: Author : <auteur>
:: Organization : Universit� de Lorraine
:: date : <date>
:: ---------------------------------------
:: exemple:
:: > <donner un exemple de ligne de commande>

:: -----------------------------------------
:: A modifier
:: -----------------------------------------
:: --->1) dossier d'installation
set PROJ_HOME=%~f0\..\..

:: --->2) dossier de localisation des librairies *.jar
set LIB_DIR=%PROJ_HOME%\lib

:: --->3) dossier de localisation des classes
set CLASSES_DIR=%PROJ_HOME%\build\classes

:: --->4) nom de la classe a demarrer
set CLASS_NAME= <classe main>

:: -->5) parametres de la commande
set PARAMS=

:: -----------------------------------------
:: A ne pas modifier
:: -----------------------------------------
:: ---->Classpath automatique (c'est pas simple en DOS !)
setlocal ENABLEDELAYEDEXPANSION
if errorlevel 1 goto :err1
set PROJ_CP=%CLASSES_DIR%
set PROJ_CP=%PROJ_CP%;%PROJ_HOME%\build\classes
for /F "delims=" %%i in ('dir /B /S "%LIB_DIR%\*.jar"') do set PROJ_CP=!PROJ_CP!;%%i

:: ---->java command
set COMMAND=java -classpath "%PROJ_CP%" %CLASS_NAME% %PARAMS% %1 %2 %3 %4 %5 %6 %7 %8 %9
:: ---->affichage de la commande
echo %COMMAND%
:: ----> execution de la commande
%COMMAND%
goto :end

:err1
echo Impossible de trouver les librairies...
:end
:: ----------------------------------------
::                  END
:: ----------------------------------------

