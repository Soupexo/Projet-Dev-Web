<?php
error_log("DEBUG SONDAGE: Script appelé");

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/sondage.php';

$method = $_SERVER['REQUEST_METHOD'];
$url = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$parts = explode('/', trim($url, '/'));

$index = array_search('sondage', $parts);
$action = $parts[$index + 1] ?? null;

error_log("DEBUG SONDAGE: method=" . $method . ", action=" . $action);

if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {

    case 'GET':
        if (is_numeric($action)) {
            $s = Sondage::getById((int)$action);
            if ($s) echo json_encode($s);
            else {
                http_response_code(404);
                echo json_encode(["error"=>"Sondage introuvable"]);
            }
        } else {
            echo json_encode(Sondage::getSondagesCourants());
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);
        error_log("DEBUG SONDAGE CREATE: data=" . print_r($data, true));

        if (!isset($data['nom_s'], $data['type_s'], $data['delai_s'])) {
            error_log("DEBUG SONDAGE CREATE: Champs manquants");
            http_response_code(400);
            echo json_encode(["error"=>"Champs requis manquants"]);
            break;
        }

        $id = Sondage::create(
            $data['nom_s'],
            $data['type_s'],
            $data['a_des_reponses_multiples_s'] ?? false,
            $data['delai_s']
        );
        error_log("DEBUG SONDAGE CREATE: id retourné=" . $id);

        if ($id !== false) {
            http_response_code(201);
            echo json_encode(["success"=>true, "num_s"=>$id]);
        } else {
            http_response_code(500);
            echo json_encode(["error"=>Sondage::getLastError()]);
        }
        break;

    case 'PUT':
        if (!is_numeric($action)) {
            http_response_code(400);
            echo json_encode(["error"=>"ID sondage manquant"]);
            break;
        }
        $data = json_decode(file_get_contents('php://input'), true);
        $rows = Sondage::update((int)$action, $data);

        if ($rows > 0) echo json_encode(["success"=>true]);
        else {
            http_response_code(404);
            echo json_encode(["error"=>"Aucune modification"]);
        }
        break;

    case 'DELETE':
        if (!is_numeric($action)) {
            http_response_code(400);
            echo json_encode(["error"=>"ID sondage manquant"]);
            break;
        }

        if (Sondage::delete((int)$action)) {
            echo json_encode(["success"=>true]);
        } else {
            http_response_code(500);
            echo json_encode(["error"=>"Erreur suppression"]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["error"=>"Méthode non autorisée"]);
}
