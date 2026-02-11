<?php

require_once __DIR__ . '/../modele/groupe.php';

class ControleurGroupe {


    public static function listeInfos() {
        $titre = "vueListeGroupes";

        $json = file_get_contents(
            "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe"
        );
        $groupes = json_decode($json, true);

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/infosGroupes.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function listeGroupesNonFinalises() {
    $titre = "vueListeGroupesNonFinalises";

    $json = file_get_contents(
        "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe/non-finalises"
    );

    $groupes = json_decode($json, true);

    require_once __DIR__ . '/../vue/header.php';
    require_once __DIR__ . '/../vue/groupesNonFinalises.php';
    require_once __DIR__ . '/../vue/footer.php';
}
public static function listeGroupesPourEtudiant() {
    $titre = "vueListeGroupesNonFinalises";

    $json = file_get_contents(
        "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe/etudiant"
    );

    $groupes = json_decode($json, true);

    require_once __DIR__ . '/../vue/header.php';
    require_once __DIR__ . '/../vue/groupesPourEtudiant.php';
    require_once __DIR__ . '/../vue/footer.php';
}

    public static function detail() {
        $numGroupe = $_GET['id'] ?? null;

        if (!$numGroupe) {
            header('Location: index.php?controller=groupe&action=liste');
            exit;
        }

        $json = file_get_contents(
            "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe/" . $numGroupe
        );
        $groupe = json_decode($json, true);

        if (!$groupe) {
            header('Location: index.php?controller=groupe&action=liste');
            exit;
        }

        $titre = "Détails du groupe " . $groupe['nom_g'];

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/detailGroupe.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaire() {
        $titre = "Créer un groupe";
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/promotion");
        $promotions = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireGroupe.php';
        require_once __DIR__ . '/../vue/footer.php';
    }


public static function creer() {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

    $nom = $_POST['nom_g'] ?? null;
    $num_p = isset($_POST['num_p']) ? (int)$_POST['num_p'] : null;
    $nbMax = isset($_POST['nombre_etudiant_max_g']) ? (int)$_POST['nombre_etudiant_max_g'] : null;

    error_log("DEBUG GROUPE CREER: nom_g=$nom, num_p=$num_p, nbMax=$nbMax");
    error_log("DEBUG GROUPE CREER: POST data = " . print_r($_POST, true));

    if (!$nom || !$num_p) {
        header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaire&error='.urlencode('Champs obligatoires: nom_g, num_p'));
        exit;
    }
    $payload = [
        'nom_g' => $nom, 
        'num_p' => $num_p, 
        'nombre_etudiant_max_g' => $nbMax,
        'est_finalise_g' => 0,
        'nombre_etudiant_g' => 0
    ];
    
    error_log("DEBUG GROUPE CREER: payload = " . json_encode($payload));
    
    $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe";

    $ch = curl_init($url);
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_POST => true,
        CURLOPT_HTTPHEADER => ["Content-Type: application/json"],
        CURLOPT_POSTFIELDS => json_encode($payload),
        CURLOPT_SSL_VERIFYPEER => false,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_MAXREDIRS => 3,
        CURLOPT_TIMEOUT => 30
    ]);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $curlError = curl_error($ch);
    curl_close($ch);

    // DEBUG: Afficher la réponse de l'API
    error_log("DEBUG GROUPE CREER: HTTP code = $httpCode");
    error_log("DEBUG GROUPE CREER: Response = $response");
    error_log("DEBUG GROUPE CREER: cURL error = $curlError");

    if ($httpCode !== 201 && $httpCode !== 200) {
        $errMsg = $response ? $response : $curlError;
        header("Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaire&error=".urlencode('Erreur création groupe: '.$errMsg));
        exit;
    }

    $responseData = json_decode($response, true);
    error_log("DEBUG GROUPE CREER: Response decoded = " . print_r($responseData, true));

    header('Location: routeur.php?controleur=controleurGroupe&action=listeInfos&msg='.urlencode('Groupe créé'));
    exit;
}

    public static function supprimer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $num_g = isset($_POST['num_g']) ? (int)$_POST['num_g'] : null;
        if (!$num_g) { header('Location: routeur.php?controleur=controleurGroupe&action=listeInfos&error='.urlencode('ID groupe manquant')); exit; }

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe/".$num_g;

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_CUSTOMREQUEST => 'DELETE',
            CURLOPT_HTTPHEADER => ["Content-Type: application/json"],
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_MAXREDIRS => 3,
            CURLOPT_TIMEOUT => 30
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $curlError = curl_error($ch);
        curl_close($ch);

        if ($httpCode !== 200) {
            $errMsg = $response ? $response : $curlError;
            header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaireSuppression&error='.urlencode('Erreur suppression groupe: '.$errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaireSuppression&msg='.urlencode('Groupe supprimé'));
        exit;
    }

    public static function afficherFormulaireSuppression(){
        $titre = "Supprimer un groupe";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireSuppressionGroupe.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireModification(){
        $titre = "Modifier un groupe";
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/promotion");
        $promotions = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireModificationGroupe.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function modifier() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $num_g = isset($_POST['num_g']) ? (int)$_POST['num_g'] : null;
        if (!$num_g) {
            header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification&error=' . urlencode('ID groupe manquant'));
            exit;
        }

        $data = [];
        $nom = isset($_POST['nom_g']) ? trim((string)$_POST['nom_g']) : '';
        if ($nom !== '') $data['nom_g'] = $nom;

        if (isset($_POST['num_p']) && $_POST['num_p'] !== '') {
            $data['num_p'] = (int)$_POST['num_p'];
        }

        if (isset($_POST['nombre_etudiant_max_g']) && $_POST['nombre_etudiant_max_g'] !== '') {
            $data['nombre_etudiant_max_g'] = (int)$_POST['nombre_etudiant_max_g'];
        }

        if (isset($_POST['est_finalise_g']) && $_POST['est_finalise_g'] !== '') {
            $data['est_finalise_g'] = ((string)$_POST['est_finalise_g'] === '1');
        }

        if (empty($data)) {
            header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification&error=' . urlencode('Aucune modification fournie'));
            exit;
        }

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/groupe/" . $num_g;

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_CUSTOMREQUEST => 'PUT',
            CURLOPT_HTTPHEADER => ["Content-Type: application/json"],
            CURLOPT_POSTFIELDS => json_encode($data),
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_MAXREDIRS => 3,
            CURLOPT_TIMEOUT => 30
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $curlError = curl_error($ch);
        curl_close($ch);

        if ($httpCode !== 200 && $httpCode !== 201) {
            $errMsg = $response ? $response : $curlError;
            header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification&error=' . urlencode('Erreur modification groupe (HTTP ' . $httpCode . '): ' . $errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification&msg=' . urlencode('Groupe modifié'));
        exit;
    }
}
