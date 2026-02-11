<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/etudiant.php';
require_once __DIR__ . '/../../modele/utilisateur.php';

$method = $_SERVER['REQUEST_METHOD'];
$url = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$urlParts = explode('/', trim($url, '/'));

$indexEtudiants = array_search('etudiant', $urlParts);
action:;
$action = $urlParts[$indexEtudiants + 1] ?? null;

// Known named actions that are not logins
$namedActions = ['non-affectes', 'notes', 'criteres', 'covoiturages', 'reponses-sondages'];

/* CORS preflight */
if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

// Gérer le cas où DELETE est simulé avec POST + X-HTTP-Method-Override
error_log("DEBUG: method original = " . $method);
error_log("DEBUG: X-HTTP-Method-Override = " . ($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] ?? 'not set'));
if ($method === 'DELETE' || (isset($_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE']) && $_SERVER['HTTP_X_HTTP_METHOD_OVERRIDE'] === 'DELETE')) {
    $method = 'DELETE';
    error_log("DEBUG: method changed to DELETE");
}
error_log("DEBUG: final method = " . $method);

switch ($method) {
    case 'POST':
        // Si action spécifique "notes" -> création ou mise à jour d'une note pour un étudiant
        if ($action === 'notes') {
            $data = json_decode(file_get_contents('php://input'), true);
            if (empty($data['etudiant']) || empty($data['matiere']) || (!isset($data['note']) && !isset($data['moy_prem_semestre']) && !isset($data['moy_deux_semestre']))) {
                http_response_code(400);
                echo json_encode(["error" => "Champs obligatoires: etudiant, matiere, et au moins une valeur de note (note, moy_prem_semestre ou moy_deux_semestre)"]);
                break;
            }

            $etudiantLogin = $data['etudiant'];
            $matiere = $data['matiere'];
            $noteVal = isset($data['note']) ? (float)$data['note'] : null;
            $moyPrem = isset($data['moy_prem_semestre']) ? (float)$data['moy_prem_semestre'] : null;
            $moyDeux = isset($data['moy_deux_semestre']) ? (float)$data['moy_deux_semestre'] : null;
            $coef = isset($data['coef']) ? (int)$data['coef'] : 1;

            // Validation basique: valeurs entre 0 et 20
            foreach ([$noteVal, $moyPrem, $moyDeux] as $v) {
                if ($v !== null && ($v < 0 || $v > 20)) {
                    http_response_code(400);
                    echo json_encode(["error" => "Les notes doivent être entre 0 et 20"]);
                    break 2;
                }
            }

            try {
                $ok = Etudiant::upsertNote($etudiantLogin, $matiere, $noteVal, $moyPrem, $moyDeux, $coef);
                if ($ok) {
                    http_response_code(201);
                    echo json_encode(["success" => true, "message" => "Note ajoutée/modifiée"]);
                } else {
                    $detail = Etudiant::getLastError();
                    http_response_code(500);
                    echo json_encode(["success" => false, "error" => "Échec création/modification note", "detail" => $detail]);
                }
            } catch (Exception $e) {
                http_response_code(500);
                echo json_encode(["error" => $e->getMessage()]);
            }
            break;
        }

        // CREATE : création d'un étudiant (comportement précédent)
        $data = json_decode(file_get_contents('php://input'), true);
        error_log("DEBUG ETUDIANT CREATE: data=" . print_r($data, true));

        if (empty($data['login']) || empty($data['mdp']) || empty($data['nom']) || empty($data['prenom']) || empty($data['mail'])) {
            error_log("DEBUG ETUDIANT CREATE: Champs obligatoires manquants");
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

        $estApprenti = !empty($data['est_apprenti_e']) && $data['est_apprenti_e'] ? true : false;
        $typeBac = $data['type_bac_e'] ?? null;
        $genre = $data['genre_e'] ?? null;

        try {
            $success = Etudiant::create($data['login'], $data['mdp'], $data['nom'], $data['prenom'], $data['mail'], $estApprenti, $typeBac, $genre);
            if ($success) {
                http_response_code(201);
                echo json_encode(["success" => true, "message" => "Etudiant créé"]);
            } else {
                $userExists = (bool) Utilisateur::getByLogin($data['login']);
                $detail = Etudiant::getLastError();
                error_log("DEBUG ETUDIANT CREATE: detail=" . $detail);
                http_response_code(500);
                echo json_encode(["success" => false, "error" => ($userExists ? "Utilisateur créé mais échec insertion étudiant (voir logs)" : "Échec de la création"), "detail" => $detail]);
            }
        } catch (Exception $e) {
            error_log("DEBUG ETUDIANT CREATE: exception=" . $e->getMessage());
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'PUT':
        // UPDATE NOTE -> URL: /api/etudiant/notes (body contains etudiant, matiere, note or semester fields)
        if ($action === 'notes') {
            $data = json_decode(file_get_contents('php://input'), true);
            if (empty($data['etudiant']) || empty($data['matiere']) || (!isset($data['note']) && !isset($data['moy_prem_semestre']) && !isset($data['moy_deux_semestre']))) {
                http_response_code(400);
                echo json_encode(["error" => "Champs obligatoires: etudiant, matiere, et au moins une valeur de note (note, moy_prem_semestre ou moy_deux_semestre)"]);
                break;
            }
            $noteVal = isset($data['note']) ? (float)$data['note'] : null;
            $moyPrem = isset($data['moy_prem_semestre']) ? (float)$data['moy_prem_semestre'] : null;
            $moyDeux = isset($data['moy_deux_semestre']) ? (float)$data['moy_deux_semestre'] : null;

            // Validation rapide
            foreach ([$noteVal, $moyPrem, $moyDeux] as $v) {
                if ($v !== null && ($v < 0 || $v > 20)) {
                    http_response_code(400);
                    echo json_encode(["error" => "Les notes doivent être entre 0 et 20"]);
                    break 2;
                }
            }

            $ok = Etudiant::upsertNote($data['etudiant'], $data['matiere'], $noteVal, $moyPrem, $moyDeux, isset($data['coef']) ? (int)$data['coef'] : 1);
            if ($ok) {
                http_response_code(200);
                echo json_encode(["success" => true, "message" => "Note mise à jour"]);
            } else {
                http_response_code(500);
                echo json_encode(["success" => false, "error" => "Échec mise à jour note", "detail" => Etudiant::getLastError()]);
            }
            break;
        }

        // UPDATE -> URL should be /api/etudiant/{login}
        $login = $action;
        
        if (!$login || in_array($login, $namedActions)) {
            http_response_code(400);
            echo json_encode(["error" => "Login manquant ou invalide"]);
            break;
        }

        $data = json_decode(file_get_contents('php://input'), true);
        
        try {
            ob_start(); // Capturer les logs de la méthode update
            $rows = Etudiant::update($login, $data);
            $debugLogs = ob_get_clean();
            
            if ($rows > 0) {
                http_response_code(200);
                // Envoyer les logs dans les headers pour ne pas polluer le JSON
                header('X-Debug-Logs: ' . base64_encode($debugLogs));
                echo json_encode(["success" => true, "updated" => $rows]);
            } else {
                http_response_code(404);
                header('X-Debug-Logs: ' . base64_encode($debugLogs));
                echo json_encode(["error" => "Etudiant non trouvé / aucune modification"]);
            }
        } catch (Exception $e) {
            http_response_code(500);
            echo json_encode(["error" => $e->getMessage()]);
        }
        break;

    case 'DELETE':
        // DELETE NOTE -> URL: /api/etudiant/notes (body: etudiant, matiere)
        if ($action === 'notes') {
            $rawInput = file_get_contents('php://input');
            error_log("DEBUG DELETE: raw input = " . $rawInput);
            $data = json_decode($rawInput, true);
            error_log("DEBUG DELETE: decoded data = " . print_r($data, true));
            
            // Pour DELETE, seul etudiant et matiere sont nécessaires
            if (empty($data['etudiant']) || empty($data['matiere'])) {
                error_log("DEBUG DELETE: champs manquants - etudiant=" . (empty($data['etudiant']) ? 'EMPTY' : $data['etudiant']) . ", matiere=" . (empty($data['matiere']) ? 'EMPTY' : $data['matiere']));
                http_response_code(400);
                echo json_encode(["error" => "Champs obligatoires: etudiant, matiere"]);
                break;
            }
            $ok = Etudiant::deleteNote($data['etudiant'], $data['matiere']);
            if ($ok) {
                http_response_code(200);
                echo json_encode(["success" => true, "message" => "Note supprimée"]);
            } else {
                http_response_code(500);
                echo json_encode(["success" => false, "error" => "Échec suppression note", "detail" => Etudiant::getLastError()]);
            }
            break;
        }

        $login = $action;
        if (!$login || in_array($login, $namedActions)) {
            echo json_encode(["error" => "Login manquant ou invalide"]);
            http_response_code(400);
            break;
        }

        try {
            // Capturer les logs de debug dans un buffer
            ob_start();
            $rows = Etudiant::delete($login);
            $debugLogs = ob_get_clean();
            
            if ($rows > 0) {
                echo json_encode([
                    "success" => true, 
                    "deleted" => $rows,
                    "debug_logs" => $debugLogs
                ]);
                http_response_code(200);
            } else {
                echo json_encode([
                    "error" => "Etudiant non trouvé", 
                    "debug_login" => $login,
                    "debug_logs" => $debugLogs
                ]);
                http_response_code(404);
            }
        } catch (Exception $e) {
            echo json_encode([
                "error" => $e->getMessage(),
                "debug_logs" => ob_get_clean()
            ]);
            http_response_code(500);
        }
        break;

    case 'GET':
        switch ($action) {
            case null:
                echo json_encode(Etudiant::getInfosEtudiants());
                break;

            case 'non-affectes':
                echo json_encode(Etudiant::getEtudiantsNonAffectes());
                break;

            case 'notes':
                echo json_encode(Etudiant::getNotesEtudiant());
                break;

            case 'criteres':
                echo json_encode(Etudiant::getCriteresDisciplinaires());
                break;

            case 'covoiturages':
                echo json_encode(Etudiant::getGroupesCovoiturage());
                break;

            case 'reponses-sondages':
                echo json_encode(Etudiant::getReponsesSondage());
                break;

            case 'etudiantsPourEtudiant':
                echo json_encode(Etudiant::getEtudiants());
                break;
            case 'etudiantsPourEnseignant':
                echo json_encode(Etudiant::getEtudiantsComplets());
                break;
            default:
                http_response_code(404);
                echo json_encode(["error" => "Ressource inconnue"]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["error" => "Méthode non autorisée"]);
}

