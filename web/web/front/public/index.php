<?php
// index.php dans public/

// Définir le chemin de base (dossier parent de public/)
define('BASE_PATH', dirname(__DIR__) . '/');

// Autoloader
spl_autoload_register(function($class) {
    $paths = [
        BASE_PATH . 'modele/',
        BASE_PATH . 'controleur/'
    ];
    
    foreach($paths as $path) {
        $file = $path . $class . '.php';
        if (file_exists($file)) {
            require_once $file;
            return;
        }
    }
});

// Connexion BDD
require_once BASE_PATH . 'modele/connexion.php';
connexion::connect();

// Récupération des paramètres
$controller = $_GET['controller'] ?? 'etudiant';
$action = $_GET['action'] ?? 'listeInfos';

$controllerMap = [
    'etudiant' => 'EtudiantControleur',  
    'groupe' => 'GroupeControleur'       
];

// Vérifications et exécution
try {
    if (!isset($controllerMap[$controller])) {
        throw new Exception("Contrôleur '{$controller}' non trouvé");
    }

    $controllerClass = $controllerMap[$controller];

    if (!class_exists($controllerClass)) {
        throw new Exception("Classe '{$controllerClass}' non trouvée");
    }

    $controllerInstance = new $controllerClass();

    if (!method_exists($controllerInstance, $action)) {
        throw new Exception("Action '{$action}' non trouvée dans {$controllerClass}");
    }

    $controllerInstance->$action();
// DEBUG - À RETIRER APRÈS
echo "Controller demandé: " . $controller . "<br>";
echo "Classe recherchée: " . ($controllerMap[$controller] ?? 'non trouvé') . "<br>";
echo "Fichier recherché: " . BASE_PATH . 'controleur/' . ($controllerMap[$controller] ?? '') . '.php<br>';
echo "Le fichier existe ? " . (file_exists(BASE_PATH . 'controleur/' . ($controllerMap[$controller] ?? '') . '.php') ? 'OUI' : 'NON') . "<br>";
echo "BASE_PATH = " . BASE_PATH . "<br>";
die(); // Arrêt pour voir le debug
    
} catch (Exception $e) {
    http_response_code(404);
    echo "<!DOCTYPE html>
    <html>
    <head>
        <meta charset='UTF-8'>
        <title>Erreur 404</title>
        <link rel='stylesheet' href='/styles/style.css'>
    </head>
    <body>
        <h1>Erreur 404</h1>
        <p>" . htmlspecialchars($e->getMessage()) . "</p>
        <a href='/'>Retour à l'accueil</a>
    </body>
    </html>";
}
?>


