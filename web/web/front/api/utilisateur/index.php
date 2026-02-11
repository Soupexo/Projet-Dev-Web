<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/utilisateur.php';

$method = $_SERVER['REQUEST_METHOD'];
$url = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$urlParts = explode('/', trim($url, '/'));

$index = array_search('utilisateur', $urlParts);
$login = $urlParts[$index + 1] ?? null;

/* CORS */
if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {

    /* -------- CREATE -------- */
    case 'POST':
        $data = json_decode(file_get_contents("php://input"), true);

        // Validation des données
        if (empty($data['login']) || empty($data['mdp']) || 
            empty($data['nom']) || empty($data['prenom']) || empty($data['mail'])) {
            http_response_code(400);
            echo json_encode(["error" => "Tous les champs sont obligatoires"]);
            break;
        }

        try {
            $success = Utilisateur::create(
                $data['login'],
                password_hash($data['mdp'], PASSWORD_DEFAULT),
                $data['nom'],
                $data['prenom'],
                $data['mail']  
            );

            if ($success) {
                http_response_code(201);
                echo json_encode(["success" => true, "message" => "Utilisateur créé"]);
            } else {
                http_response_code(400);
                echo json_encode(["success" => false, "error" => "Échec de la création"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    /* -------- UPDATE -------- */
    case 'PUT':
        if (!$login) {
            http_response_code(400);
            echo json_encode(["error" => "Login manquant"]);
            break;
        }

        $data = json_decode(file_get_contents("php://input"), true);
        
        try {
            $rows = Utilisateur::update($login, $data);
            
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success" => true, "updated" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error" => "Utilisateur non trouvé"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    /* -------- DELETE -------- */
    case 'DELETE':
        if (!$login) {
            http_response_code(400);
            echo json_encode(["error" => "Login manquant"]);
            break;
        }

        try {
            $rows = Utilisateur::delete($login);
            
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success" => true, "deleted" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error" => "Utilisateur non trouvé"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    /* -------- GET -------- */
    case 'GET':
        try {
            if ($login) {
                // Récupérer un utilisateur spécifique
                $utilisateur = Utilisateur::getByLogin($login);
                if ($utilisateur) {
                    echo json_encode($utilisateur);
                } else {
                    http_response_code(404);
                    echo json_encode(["error" => "Utilisateur non trouvé"]);
                }
            } else {
                // Récupérer tous les utilisateurs
                $utilisateurs = Utilisateur::getAllUtilisateurs();
                echo json_encode($utilisateurs);
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