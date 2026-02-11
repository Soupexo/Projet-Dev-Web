<?php

class ControleurPromotion {
    public static function liste() {
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/promotion");
        $promotions = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/listePromotions.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaire() {
        $titre = "Créer une promotion";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulairePromotion.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireModification() {
        $titre = "Modifier une promotion";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireModificationPromotion.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    // Debug helper: affiche les derniers logs d'API pour les promotions
    public static function afficherLog() {
        $logPath = __DIR__ . '/../logs/promotion_api.log';
        $log = file_exists($logPath) ? file_get_contents($logPath) : "(aucun log)";
        require_once __DIR__ . '/../vue/header.php';
        echo "<div class=\"container\"><h1>Logs API Promotions</h1><pre style=\"white-space:pre-wrap;max-height:500px;overflow:auto;background:#fff;padding:12px;border:1px solid #ddd;\">" . htmlspecialchars($log) . "</pre><p><a href=\"routeur.php?controleur=controleurPromotion&action=liste\">Retour</a></p></div>";
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function creer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $payload = [
            'nombre_etudiant_covoiturage_max_p' => isset($_POST['nombre_etudiant_covoiturage_max_p']) ? (int)$_POST['nombre_etudiant_covoiturage_max_p'] : (isset($_POST['nombre_etudiant_max_covoiturage_p']) ? (int)$_POST['nombre_etudiant_max_covoiturage_p'] : null),
            'nombre_groupe_max_p' => isset($_POST['nombre_groupe_max_p']) ? (int)$_POST['nombre_groupe_max_p'] : null,
            'nombre_etudiant_max_p' => isset($_POST['nombre_etudiant_max_p']) ? (int)$_POST['nombre_etudiant_max_p'] : null,
            'num_a' => isset($_POST['num_a']) ? (int)$_POST['num_a'] : null
        ];

        // Use trailing slash to avoid 301 redirects from the remote server
        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/promotion/";

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_POST => true,
            CURLOPT_HTTPHEADER => ["Content-Type: application/json"],
            CURLOPT_POSTFIELDS => json_encode($payload),
            CURLOPT_SSL_VERIFYPEER => false,
            // Allow redirects for POST to handle server redirects safely
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_MAXREDIRS => 3,
            CURLOPT_TIMEOUT => 30
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $curlError = curl_error($ch);
        curl_close($ch);

        if ($httpCode !== 201 && $httpCode !== 200) {
            $errMsg = $response ? $response : $curlError;
            header('Location: routeur.php?controleur=controleurPromotion&action=afficherFormulaire&error='.urlencode('Erreur création promotion: '.$errMsg));
            exit;
        }

        // Parse API response and show useful info (debugging): num_p or returned details
        $debugInfo = '';
        if ($response) {
            $json = json_decode($response, true);
            if (is_array($json)) {
                if (isset($json['num_p'])) {
                    $debugInfo = ' (num_p='.$json['num_p'].')';
                } elseif (isset($json[0]) && isset($json[0]['num_p'])) {
                    $debugInfo = ' (num_p='.$json[0]['num_p'].')';
                } elseif (isset($json['error'])) {
                    $err = is_string($json['error']) ? $json['error'] : json_encode($json['error']);
                    $debugInfo = ' (resp error: '.substr($err,0,300).')';
                } else {
                    $debugInfo = ' (resp: '.substr($response,0,300).')';
                }
            } else {
                $debugInfo = ' (resp: '.substr($response,0,300).')';
            }
        }

        header('Location: routeur.php?controleur=controleurPromotion&action=liste&msg='.urlencode('Promotion créée'.$debugInfo));
        exit;
    }

    public static function modifier() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $num_p = isset($_POST['num_p']) ? (int)$_POST['num_p'] : null;
        if (!$num_p) { header('Location: routeur.php?controleur=controleurPromotion&action=liste&error='.urlencode('ID promotion manquant')); exit; }

        $data = [];
        foreach (['nombre_etudiant_covoiturage_max_p','nombre_groupe_max_p','nombre_etudiant_max_p','num_a'] as $f) {
            if (isset($_POST[$f])) $data[$f] = is_numeric($_POST[$f]) ? (int)$_POST[$f] : $_POST[$f];
        }

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/promotion/".$num_p;

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

        if ($httpCode !== 200) {
            $errMsg = $response ? $response : $curlError;
            header('Location: routeur.php?controleur=controleurPromotion&action=liste&error='.urlencode('Erreur modification promotion: '.$errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurPromotion&action=liste&msg='.urlencode('Promotion modifiée'));
        exit;
    }

    public static function supprimer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $num_p = isset($_POST['num_p']) ? (int)$_POST['num_p'] : null;
        if (!$num_p) { header('Location: routeur.php?controleur=controleurPromotion&action=liste&error='.urlencode('ID promotion manquant')); exit; }

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/promotion/".$num_p;

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
            header('Location: routeur.php?controleur=controleurPromotion&action=liste&error='.urlencode('Erreur suppression promotion: '.$errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurPromotion&action=liste&msg='.urlencode('Promotion supprimée'));
        exit;
    }
}
