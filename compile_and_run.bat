@echo off
echo === COMPILATION ET EXECUTION DU PROJET JAVA ===

REM Creer le dossier lib s'il n'existe pas
if not exist "lib" mkdir lib

REM Telecharger le driver MySQL si necessaire
if not exist "lib\mysql-connector-j-8.0.33.jar" (
    echo Telechargement du driver MySQL...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar' -OutFile 'lib\mysql-connector-j-8.0.33.jar'"
    if errorlevel 1 (
        echo Erreur lors du telechargement
        pause
        exit /b 1
    )
    echo Driver MySQL telecharge avec succes !
)

REM Creer le dossier de compilation
if not exist "bin" mkdir bin

echo Compilation du projet Java...

REM Compiler tous les fichiers Java
javac -cp lib\mysql-connector-j-8.0.33.jar -d bin src\main\java\com\salle\config\*.java src\main\java\com\salle\interfaces\*.java src\main\java\com\salle\model\*.java src\main\java\com\salle\repository\*.java src\main\java\com\salle\service\*.java src\main\java\com\salle\demo\*.java

if errorlevel 1 (
    echo Erreur lors de la compilation
    pause
    exit /b 1
)

echo Compilation reussie !

echo Execution de l'application complete...

REM Execution de l'application
java -cp "bin;lib\mysql-connector-j-8.0.33.jar" com.salle.demo.CompleteDemoApp

if errorlevel 1 (
    echo Erreur lors de l'execution
) else (
    echo Application executee avec succes !
)

pause 