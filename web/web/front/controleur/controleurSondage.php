<?php
class ControleurSondage {

    public static function liste() {
        $json = file_get_contents(
            "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/sondage"
        );
        $sondages = json_decode($json, true);

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/sondageCourants.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaire() {
        $titre = "Créer un sondage";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireSondage.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireSuppression(){
        
    }

    public static function creer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') exit;

        $payload = [
            "nom_s" => $_POST['nom_s'] ?? null,
            "type_s" => $_POST['type_s'] ?? null,
            "a_des_reponses_multiples_s" => isset($_POST['reponses_multiples_s']),
            "delai_s" => $_POST['delai_s'] ?? null
        ];

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/sondage/";

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_POST => true,
            CURLOPT_HTTPHEADER => ["Content-Type: application/json"],
            CURLOPT_POSTFIELDS => json_encode($payload),
            CURLOPT_SSL_VERIFYPEER => false
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        curl_close($ch);

        if ($httpCode >= 200 && $httpCode < 300) {
            header('Location: routeur.php?controleur=controleurSondage&action=liste&msg='.urlencode('Sondage créé'));
            exit;
        }

        $err = 'Erreur création sondage';
        if ($response) {
            $decoded = json_decode($response, true);
            if (is_array($decoded) && !empty($decoded['error'])) {
                $err = (string) $decoded['error'];
            }
        }

        header('Location: routeur.php?controleur=controleurSondage&action=afficherFormulaire&error='.urlencode($err));
        exit;
    }

    public static function supprimer() {
        $num = (int)($_POST['num_s'] ?? 0);
        if (!$num) exit;

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/sondage/$num";

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_CUSTOMREQUEST => 'DELETE',
            CURLOPT_SSL_VERIFYPEER => false
        ]);

        curl_exec($ch);
        curl_close($ch);

        header('Location: routeur.php?controleur=controleurSondage&action=liste');
        exit;
    }
}
