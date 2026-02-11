<?php
// Router central pour l'API

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

// Log pour debug
error_log("API ROUTER: URI = " . $_SERVER['REQUEST_URI'] . " - Method = " . $_SERVER['REQUEST_METHOD']);

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Parser l'URL pour déterminer la ressource
$requestUri = $_SERVER['REQUEST_URI'];
$scriptName = $_SERVER['SCRIPT_NAME'];

// Extraire le chemin après le script
$path = str_replace(dirname($scriptName), '', $requestUri);
$path = trim($path, '/');

error_log("API ROUTER: path = " . $path);

// Router simple basé sur le premier segment
$segments = explode('/', $path);
$resource = $segments[0] ?? '';

error_log("API ROUTER: resource = " . $resource);

switch ($resource) {
    case 'sondage':
        require_once __DIR__ . '/sondage/index.php';
        break;
    case 'promotion':
        require_once __DIR__ . '/promotion/index.php';
        break;
    case 'etudiant':
        require_once __DIR__ . '/etudiant/index.php';
        break;
    default:
        error_log("API ROUTER: Ressource non trouvée: " . $resource);
        http_response_code(404);
        echo json_encode(["error" => "Endpoint not found"]);
        break;
}
