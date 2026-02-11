<?php
error_log("DEBUG REPONSE_SONDAGE: Script appelé");

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/etudiantSondage.php';

$method = $_SERVER['REQUEST_METHOD'];

error_log("DEBUG REPONSE_SONDAGE: method=" . $method);

if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {

    case 'GET':
        // Récupérer toutes les réponses aux sondages
        error_log("DEBUG REPONSE_SONDAGE: GET all responses");
        $reponses = EtudiantSondage::getAll();
        echo json_encode($reponses);
        break;

    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);
        error_log("DEBUG DATA RECEIVED: " . print_r($data, true));

        // On récupère les données peu importe le nom de la clé (Java ou PHP style)
        $nomOuIdSondage = $data['nomSondage'] ?? $data['id_sondage'] ?? null;
        $idEtudiant = $data['idEtudiant'] ?? $data['id_etudiant'] ?? null;
        $question = $data['question'] ?? null;
        $reponse = $data['reponse'] ?? null;
        $dateReponse = $data['dateReponse'] ?? $data['date_reponse'] ?? null;

        // Validation
        if (!$nomOuIdSondage || !$idEtudiant || !$question || !$reponse || !$dateReponse) {
            http_response_code(400);
            echo json_encode(["error" => "Champs manquants", "received" => $data]);
            exit; // Arrêt impératif
        }

        // On convertit le nom en ID (ou on garde l'ID s'il est numérique)
        $num_s = EtudiantSondage::fromNameToId($nomOuIdSondage);
        if (!$num_s) {
            http_response_code(404);
            echo json_encode(["error" => "Sondage introuvable: " . $nomOuIdSondage]);
            exit;
        }

        $id = EtudiantSondage::create(
            (int)$num_s,
            (int)$idEtudiant,
            trim($question),
            trim($reponse),
            trim($dateReponse)
        );

        if ($id !== false) {
            http_response_code(201);
            echo json_encode([
                "success" => true,
                "id" => $id,
                "num_s" => (int)$num_s,
                "num_e" => (int)$idEtudiant,
                "date_reponse" => (string)$dateReponse,
                "message" => "Ajouté"
            ]);
        } else {
            http_response_code(500);
            echo json_encode(["error" => EtudiantSondage::getLastError()]);
        }
        exit; // Empêche de passer à la suite du script
        break;

    default:
        http_response_code(405);
        echo json_encode(["error" => "Méthode non autorisée"]);
}
?>
