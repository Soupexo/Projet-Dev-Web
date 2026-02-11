<?php
// index.php - Point d'entrée de l'application

// Inclusion des fichiers nécessaires
require_once '../modele/connexion.php';
require_once '../controleur/BaseControleur.php';

// Autoloader simple pour charger les classes
spl_autoload_register(function($class) {
    $paths = [
        'models/',
        'controllers/'
    ];
    
    foreach($paths as $path) {
        $file = $path . $class . '.php';
        if (file_exists($file)) {
            require_once $file;
            return;
        }
    }
});

// Récupération des paramètres de l'URL
$controller = $_GET['controller'] ?? 'etudiant';
$action = $_GET['action'] ?? 'listeInfos';

// Mapping des contrôleurs
$controllerMap = [
    'etudiant' => 'EtudiantController',
    'groupe' => 'GroupeController',
    'enseignant' => 'EnseignantController',
    'sondage' => 'SondageController'
];

// Vérification que le contrôleur existe
if (!isset($controllerMap[$controller])) {
    die("Contrôleur non trouvé");
}

$controllerClass = $controllerMap[$controller];

// Vérification que la classe existe
if (!class_exists($controllerClass)) {
    die("Classe de contrôleur non trouvée");
}

// Instanciation du contrôleur
$controllerInstance = new $controllerClass();

// Vérification que la méthode existe
if (!method_exists($controllerInstance, $action)) {
    die("Action non trouvée");
}

// Exécution de l'action
$controllerInstance->$action();
?>

