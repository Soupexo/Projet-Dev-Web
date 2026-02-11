<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/enseignant.php';
require_once __DIR__ . '/../../modele/utilisateur.php';

$method = $_SERVER['REQUEST_METHOD'];
$url = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$urlParts = explode('/', trim($url, '/'));

$index = array_search('enseignant', $urlParts);
$login = $urlParts[$index + 1] ?? null;

/* CORS */
if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {
    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);
        if (empty($data['login']) || empty($data['mdp']) || empty($data['nom']) || empty($data['prenom']) || empty($data['mail'])) {
            http_response_code(400);
            echo json_encode(["error" => "Tous les champs obligatoires: login, mdp, nom, prenom, mail"]);
            break;
        }

        // Vérifier si le login existe déjà
        if (Utilisateur::getByLogin($data['login'])) {
            http_response_code(409);
            echo json_encode(["success" => false, "error" => "Login déjà utilisé"]);
            break;
        }

        try {
            // Accept optional responsibility id in creation payload (num_r)
            $num_r = isset($data['num_r']) ? (int)$data['num_r'] : null;
            $numEn = Enseignant::create($data['login'], $data['mdp'], $data['nom'], $data['prenom'], $data['mail'], $num_r);
            if ($numEn !== false && $numEn !== null) {
                http_response_code(201);
                echo json_encode(["success" => true, "message" => "Enseignant créé", "num_en" => $numEn]);
            } else {
                // Diagnostic supplémentaire pour debug
                $userExists = (bool) Utilisateur::getByLogin($data['login']);

                // Existe-t-il déjà une ligne ENSEIGNANT pour ce login ?
                try {
                    $stmtCheck = connexion::pdo()->prepare('SELECT COUNT(*) as c FROM ENSEIGNANT WHERE login_u = :login');
                    $stmtCheck->execute([':login' => $data['login']]);
                    $row = $stmtCheck->fetch(PDO::FETCH_ASSOC);
                    $enseignantCount = (int) ($row['c'] ?? 0);
                } catch (Exception $e) {
                    $enseignantCount = null;
                }

                // Structure de la table ENSEIGNANT (colonnes)
                try {
                    $colsStmt = connexion::pdo()->query('SHOW COLUMNS FROM ENSEIGNANT');
                    $columns = $colsStmt->fetchAll(PDO::FETCH_ASSOC);
                } catch (Exception $e) {
                    $columns = null;
                }

                $detailEnseignant = Enseignant::getLastError();
                $detailUtilisateur = Utilisateur::getLastError();

                http_response_code(500);
                echo json_encode([
                    "success" => false,
                    "error" => ($userExists ? "Utilisateur créé mais échec insertion enseignant (voir logs)" : "Échec de la création (vérifier les logs)"),
                    "detailEnseignant" => $detailEnseignant,
                    "detailUtilisateur" => $detailUtilisateur,
                    "userExists" => $userExists,
                    "enseignantCount" => $enseignantCount,
                    "enseignantColumns" => $columns
                ]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'PUT':
        if (!$login) {
            http_response_code(400);
            echo json_encode(["error" => "Login manquant"]);
            break;
        }

        $data = json_decode(file_get_contents('php://input'), true);
        try {
            $rows = Enseignant::update($login, $data);
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success" => true, "updated" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error" => "Enseignant non trouvé / aucune modification"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'DELETE':
        if (!$login) {
            http_response_code(400);
            echo json_encode(["error" => "Login manquant"]);
            break;
        }

        try {
            $rows = Enseignant::delete($login);
            if ($rows > 0) {
                http_response_code(200);
                echo json_encode(["success" => true, "deleted" => $rows]);
            } else {
                http_response_code(404);
                echo json_encode(["error" => "Enseignant non trouvé"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'GET':
        // Pour l'instant on expose uniquement la vue (liste)
        $result = Enseignant::getListeEnseignants();
        echo json_encode($result);
        break;

    default:
        http_response_code(405);
        echo json_encode(["error" => "Méthode non autorisée"]);
}
?>
