<?php

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/groupe.php';


$method = $_SERVER['REQUEST_METHOD'];
$url = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$urlParts = explode('/', trim($url, '/'));

$indexGroupe = array_search('groupe', $urlParts);
$action = $urlParts[$indexGroupe + 1] ?? null;

// Debug
error_log("DEBUG API GROUPE: method=$method, url=$url, action=$action");

// CORS préflight
if ($method === 'OPTIONS') {
    header("Access-Control-Allow-Methods: OPTIONS, GET, POST, PUT, DELETE");
    header("Access-Control-Allow-Headers: Content-Type, Authorization");
    http_response_code(200);
    exit;
}

switch ($method) {
    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);
        if (empty($data['nom_g']) || empty($data['num_p'])) {
            http_response_code(400);
            echo json_encode(["error"=>"Champs obligatoires: nom_g, num_p"]);
            break;
        }
        try {
            $id = Groupe::create($data['nom_g'], (int)$data['num_p'], isset($data['nombre_etudiant_max_g']) ? (int)$data['nombre_etudiant_max_g'] : null, isset($data['est_finalise_g']) ? (bool)$data['est_finalise_g'] : false);
            if ($id !== false) {
                http_response_code(201);
                echo json_encode(["success"=>true, "num_g" => $id]);
            } else {
                http_response_code(500);
                echo json_encode(["success"=>false, "error"=>"Échec création groupe", "detail" => Groupe::getLastError()]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'PUT':
        // URL: /api/groupe/{id}
        if (!is_numeric($action)) {
            http_response_code(400);
            echo json_encode(["error"=>"ID groupe manquant"]);
            break;
        }
        $id = (int)$action;
        $data = json_decode(file_get_contents('php://input'), true);
        try {
            $rows = Groupe::update($id, $data);
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success"=>true, "updated" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error"=>"Groupe non trouvé / aucune modification"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error"=> $e->getMessage()]);
        }
        break;

    case 'DELETE':
        // Accepter soit l'ID dans l'URL, soit le nom dans le body
        error_log("DEBUG DELETE GROUPE: action=$action, is_numeric=" . is_numeric($action));
        
        if (!is_numeric($action)) {
            // Si pas d'ID dans l'URL, essayer de récupérer le nom du body
            $data = json_decode(file_get_contents('php://input'), true);
            if (isset($data['nom']) && !empty(trim($data['nom']))) {
                $nomGroupe = trim($data['nom']);
                // Récupérer l'ID par le nom
                $stmt = Connexion::pdo()->prepare("SELECT num_g FROM GROUPE WHERE nom_g = :nom");
                $stmt->execute([':nom' => $nomGroupe]);
                $result = $stmt->fetch(PDO::FETCH_ASSOC);
                if ($result) {
                    $id = (int)$result['num_g'];
                } else {
                    http_response_code(404);
                    echo json_encode(["error"=>"Groupe non trouvé: " . $nomGroupe]);
                    break;
                }
            } else {
                http_response_code(400);
                echo json_encode(["error"=>"ID groupe manquant ou nom non fourni"]);
                break;
            }
        } else {
            $id = (int)$action;
        }
        
        error_log("DEBUG DELETE GROUPE: ID final pour suppression=$id");
        
        try {
            $rows = Groupe::delete($id);
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success"=>true, "deleted" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error"=>"Groupe introuvable"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error"=> $e->getMessage()]);
        }
        break;

    case 'GET':
        if (is_numeric($action)) {
            $groupe = Groupe::getGroupeById((int)$action);
            if ($groupe) echo json_encode($groupe);
            else {
                http_response_code(404);
                echo json_encode(["error"=>"Groupe introuvable"]);
            }
            break;
        }

        switch ($action) {
            case '':
                echo json_encode(Groupe::getListeGroupes());
                break;
            case 'non-finalises':
                echo json_encode(Groupe::getGroupesNonFinalises());
                break;
            case 'etudiant':
                echo json_encode(Groupe::getGroupesPourEtudiant());
                break;
            default:
                http_response_code(404);
                echo json_encode(["error"=>"Ressource inconnue"]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["error"=>"Méthode non autorisée"]);
}
