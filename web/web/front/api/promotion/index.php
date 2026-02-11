<?php

// Log immédiat pour voir si le script est appelé
error_log("DEBUG PROMOTION: Script appelé - URI = " . $_SERVER['REQUEST_URI'] . " - Method = " . $_SERVER['REQUEST_METHOD']);

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/promotion.php';

$method = $_SERVER['REQUEST_METHOD'];
$url = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$urlParts = explode('/', trim($url, '/'));

error_log("DEBUG PROMOTION: full URL = " . $url);
error_log("DEBUG PROMOTION: urlParts = " . print_r($urlParts, true));

$index = array_search('promotion', $urlParts);
$action = $urlParts[$index + 1] ?? null;

error_log("DEBUG PROMOTION: index = " . $index);
error_log("DEBUG PROMOTION: action = " . $action);

if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {
    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);

        // Log incoming payload
        @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | POST payload: " . json_encode($data) . PHP_EOL, FILE_APPEND);
        try {
            $nbECovo = isset($data['nombre_etudiant_covoiturage_max_p']) ? (int)$data['nombre_etudiant_covoiturage_max_p'] : (isset($data['nombre_etudiant_max_covoiturage_p']) ? (int)$data['nombre_etudiant_max_covoiturage_p'] : null);
            $nbGMax = isset($data['nombre_groupe_max_p']) ? (int)$data['nombre_groupe_max_p'] : null;
            $nbGTotal = isset($data['nombre_groupe_total_p']) ? (int)$data['nombre_groupe_total_p'] : null;
            $nbEMax = isset($data['nombre_etudiant_max_p']) ? (int)$data['nombre_etudiant_max_p'] : null;
            $numA = isset($data['num_a']) ? (int)$data['num_a'] : null;

            // Validation: require student max and year
            if ($nbEMax === null || $numA === null) {
                http_response_code(400);
                $err = ["success"=>false, "error" => "Champs requis manquants : nombre_etudiant_max_p et num_a sont nécessaires"];
                echo json_encode($err);
                @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | POST error: " . json_encode($err) . PHP_EOL, FILE_APPEND);
                break;
            }

            $id = Promotion::create($nbECovo, $nbGMax, $nbGTotal, $nbEMax, $numA);
            @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | create id: " . var_export($id, true) . " lastError: " . var_export(Promotion::getLastError(), true) . PHP_EOL, FILE_APPEND);

            if ($id !== false) {
                // Fetch created row to confirm
                $created = Promotion::getById((int)$id);
                @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | created row: " . json_encode($created) . PHP_EOL, FILE_APPEND);
                http_response_code(201);
                echo json_encode(["success" => true, "num_p" => $id, "data" => $created]);
            } else {
                $last = Promotion::getLastError();
                // Specific friendly message when num_p has no default (SQLSTATE 1364)
                if ($last && (strpos($last, "doesn't have a default value") !== false || strpos($last, '1364') !== false)) {
                    $msg = "Le champ 'num_p' n'a pas de valeur par défaut sur la table PROMOTION. Exécutez la migration 'migrations/002-promotion-auto-increment.sql' pour rendre 'num_p' AUTO_INCREMENT.";
                    http_response_code(500);
                    echo json_encode(["success"=>false, "error"=>"Échec création promotion", "detail" => $msg]);
                    @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | create failed: missing default num_p - " . var_export($last, true) . PHP_EOL, FILE_APPEND);
                } else {
                    http_response_code(500);
                    echo json_encode(["success"=>false, "error"=>"Échec création promotion", "detail" => $last]);
                }
            }
        } catch (Exception $e) {
            http_response_code(500);
            $err = ["error" => $e->getMessage()];
            echo json_encode($err);
            @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | exception: " . $e->getMessage() . PHP_EOL, FILE_APPEND);
        }
        break;

    case 'PUT':
        if (!is_numeric($action)) {
            http_response_code(400);
            echo json_encode(["error"=>"ID promotion manquant"]);
            break;
        }
        $id = (int)$action;
        $data = json_decode(file_get_contents('php://input'), true);
        try {
            $rows = Promotion::update($id, $data);
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success"=>true, "updated" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error"=>"Promotion non trouvée / aucune modification"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error"=> $e->getMessage()]);
        }
        break;

    case 'DELETE':
        // Deleting promotions is not permitted via API (business rule)
        http_response_code(403);
        echo json_encode(["error"=>"Suppression de promotions non autorisée"]);
        @file_put_contents(__DIR__ . '/../../logs/promotion_api.log', date('c') . " | DELETE attempted for action=" . var_export($action, true) . PHP_EOL, FILE_APPEND);
        break;

    case 'GET':
        if (is_numeric($action)) {
            $promo = Promotion::getById((int)$action);
            if ($promo) echo json_encode($promo);
            else {
                http_response_code(404);
                echo json_encode(["error"=>"Promotion introuvable"]);
            }
            break;
        }

        echo json_encode(Promotion::getAll());
        break;

    default:
        http_response_code(405);
        echo json_encode(["error"=>"Méthode non autorisée"]);
}