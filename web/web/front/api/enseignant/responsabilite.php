<?php
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Authorization");

require_once __DIR__ . '/../../config/connexion.php';
Connexion::connect();
require_once __DIR__ . '/../../modele/enseignant.php';

$method = $_SERVER['REQUEST_METHOD'];

/* CORS */
if ($method === 'OPTIONS') {
    http_response_code(200);
    exit;
}

switch ($method) {
    case 'POST':
        $data = json_decode(file_get_contents('php://input'), true);
        
        if (empty($data['num_en'])) {
            http_response_code(400);
            echo json_encode(["error" => "L'ID de l'enseignant est obligatoire"]);
            break;
        }

        try {
            $num_en = (int)$data['num_en'];
            
            // Gérer la responsabilité (peut être null pour supprimer)
            $responsabilite = isset($data['responsabilite']) && $data['responsabilite'] !== '' ? (int)$data['responsabilite'] : null;
            $responsabilites = $responsabilite ? [$responsabilite] : [];
            
            $ok = Enseignant::setResponsabilitesForEnseignant($num_en, $responsabilites);
            
            if ($ok) {
                http_response_code(200);
                echo json_encode([
                    "success" => true, 
                    "message" => $responsabilite ? "Responsabilité attribuée" : "Responsabilité supprimée"
                ]);
            } else {
                http_response_code(500);
                echo json_encode(["error" => "Erreur lors de la mise à jour"]);
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
