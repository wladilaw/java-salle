@echo off
echo === COMPILATION ET EXECUTION AVEC H2 DATABASE ===

REM Creer le dossier lib s'il n'existe pas
if not exist "lib" mkdir lib

REM Telecharger H2 Database si necessaire
if not exist "lib\h2-2.2.224.jar" (
    echo Telechargement de H2 Database...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar' -OutFile 'lib\h2-2.2.224.jar'"
    if errorlevel 1 (
        echo Erreur lors du telechargement de H2
        pause
        exit /b 1
    )
    echo H2 Database telecharge avec succes !
)

REM Creer le dossier de compilation
if not exist "bin" mkdir bin

echo Compilation du projet Java avec H2...

REM Compiler tous les fichiers Java
javac -cp lib\h2-2.2.224.jar -d bin src\main\java\com\salle\config\*.java src\main\java\com\salle\interfaces\*.java src\main\java\com\salle\model\*.java src\main\java\com\salle\repository\*.java src\main\java\com\salle\service\*.java src\main\java\com\salle\demo\*.java

if errorlevel 1 (
    echo Erreur lors de la compilation
    pause
    exit /b 1
)

echo Compilation reussie !

echo Execution de l'application avec base de donnees H2 en memoire...

REM Execution de l'application
java -cp "bin;lib\h2-2.2.224.jar" com.salle.demo.CompleteDemoApp

if errorlevel 1 (
    echo Erreur lors de l'execution
) else (
    echo Application executee avec succes !
)

pause 