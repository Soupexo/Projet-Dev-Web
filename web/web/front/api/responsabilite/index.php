<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/responsabilite.php';

$method = $_SERVER['REQUEST_METHOD'];

/* CORS */
if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {
    case 'GET':
        try {
            $responsabilites = Responsabilite::getAll();
            echo json_encode($responsabilites);
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);
        if (empty($data['intitule'])) {
            http_response_code(400);
            echo json_encode(["error" => "L'intitulé est obligatoire"]);
            break;
        }

        try {
            $id = Responsabilite::create($data['intitule']);
            if ($id !== false) {
                http_response_code(201);
                echo json_encode(["success" => true, "message" => "Responsabilité créée", "num_r" => $id]);
            } else {
                http_response_code(500);
                echo json_encode(["error" => "Erreur lors de la création"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["error" => "Méthode non autorisée"]);
}
?>
