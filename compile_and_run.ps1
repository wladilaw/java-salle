# Script pour compiler et exécuter le projet Java sans Maven
Write-Host "🏋️‍♂️ === COMPILATION ET EXÉCUTION DU PROJET JAVA === 🏋️‍♀️" -ForegroundColor Green

# Créer le dossier lib s'il n'existe pas
if (!(Test-Path "lib")) {
    New-Item -ItemType Directory -Name "lib"
    Write-Host "📁 Dossier lib créé" -ForegroundColor Yellow
}

# Télécharger le driver MySQL si nécessaire
$mysqlJar = "lib\mysql-connector-j-8.0.33.jar"
if (!(Test-Path $mysqlJar)) {
    Write-Host "📥 Téléchargement du driver MySQL..." -ForegroundColor Yellow
    try {
        Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar" -OutFile $mysqlJar
        Write-Host "✅ Driver MySQL téléchargé avec succès !" -ForegroundColor Green
    } catch {
        Write-Host "❌ Erreur lors du téléchargement : $_" -ForegroundColor Red
        exit 1
    }
}

# Créer le dossier de compilation
if (!(Test-Path "bin")) {
    New-Item -ItemType Directory -Name "bin"
    Write-Host "📁 Dossier bin créé" -ForegroundColor Yellow
}

Write-Host "🔨 Compilation du projet Java..." -ForegroundColor Yellow

# Compiler tous les fichiers Java
$javaFiles = Get-ChildItem -Path "src" -Filter "*.java" -Recurse
$classPath = "lib\mysql-connector-j-8.0.33.jar"

try {
    # Compilation de tous les fichiers Java
    javac -cp $classPath -d bin $javaFiles.FullName
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilation réussie !" -ForegroundColor Green
        
        Write-Host "🚀 Exécution de l'application complète..." -ForegroundColor Yellow
        
        # Exécution de l'application
        java -cp "bin;$classPath" com.salle.demo.CompleteDemoApp
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "🎉 Application exécutée avec succès !" -ForegroundColor Green
        } else {
            Write-Host "❌ Erreur lors de l'exécution" -ForegroundColor Red
        }
    } else {
        Write-Host "❌ Erreur lors de la compilation" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Erreur : $_" -ForegroundColor Red
}

Write-Host "📋 Pressez une touche pour continuer..." -ForegroundColor Cyan
Read-Host "Appuyez sur Entrée pour continuer" 