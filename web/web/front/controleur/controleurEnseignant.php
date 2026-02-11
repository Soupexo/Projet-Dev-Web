<?php

require_once __DIR__ . '/../modele/enseignant.php';
require_once __DIR__ . '/../modele/responsabilite.php';
require_once __DIR__ . '/../modele/enseignantGroupe.php';
require_once __DIR__ . '/../modele/utilisateur.php';

class ControleurEnseignant {

    public static function liste() {
        self::listeInfos();
    }

    /**
     * Affiche la liste des enseignants avec leurs responsabilités
     */
    public static function listeInfos() {
        $enseignants = Enseignant::getListeEnseignants();
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/infosEnseignants.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Affiche l'interface pour affecter/supprimer des responsabilités aux enseignants
     */
    public static function afficherAffectations() {
        $enseignants = Enseignant::getListeEnseignants();
        $responsabilites = Responsabilite::getAll();
        // Préparer un mapping num_en => num_r (une seule responsabilité ou null)
        $map = [];
        foreach ($enseignants as $e) {
            $num = $e['num_en'] ?? null;
            if ($num) {
                $rs = Enseignant::getResponsabilitesForEnseignant((int)$num);
                $map[$num] = !empty($rs) ? (int)$rs[0]['num_r'] : null; // prendre la première si plusieurs
            }
        }

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/affecterResponsabilites.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Reçoit POST pour mettre à jour les responsabilités d'un enseignant
     */
    public static function affecter() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $num_en = isset($_POST['num_en']) ? (int)$_POST['num_en'] : null;
        if (!$num_en) { header('Location: routeur.php?controleur=controleurEnseignant&action=afficherAffectations&error='.urlencode('Enseignant manquant')); exit; }

        // On attend une responsabilité unique (ou absence)
        $responsabilite = isset($_POST['responsabilite']) && $_POST['responsabilite'] !== '' ? (int)$_POST['responsabilite'] : null;
        $responsabilites = $responsabilite ? [ $responsabilite ] : [];

        $ok = Enseignant::setResponsabilitesForEnseignant($num_en, $responsabilites);
        if (!$ok) {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherAffectations&error='.urlencode('Erreur lors de la sauvegarde')); exit;
        }

        header('Location: routeur.php?controleur=controleurEnseignant&action=afficherAffectations&msg='.urlencode('Responsabilités mises à jour')); exit;
    }

    /**
     * Crée une nouvelle responsabilité (depuis le formulaire)
     */
    public static function creerResponsabilite() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $intitule = trim($_POST['intitule'] ?? '');
        if ($intitule === '') {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherAffectations&error='.urlencode('Intitulé vide')); exit;
        }
        $id = Responsabilite::create($intitule);
        if ($id === false) {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherAffectations&error='.urlencode('Impossible de créer la responsabilité')); exit;
        }
        header('Location: routeur.php?controleur=controleurEnseignant&action=afficherAffectations&msg='.urlencode('Responsabilité ajoutée')); exit;
    }

    /**
     * Affiche les enseignants par responsabilité
     * URL: index.php?controller=enseignant&action=parResponsabilite&responsabilite=Chef
     */
    public function parResponsabilite() {
        $responsabilite = $_GET['responsabilite'] ?? null;
        
        if (!$responsabilite) {
            $this->redirect('index.php?controller=enseignant&action=liste');
            return;
        }
        
        $enseignants = Enseignant::getEnseignantsParResponsabilite($responsabilite);
        
        $this->render('enseignants/par_responsabilite', [
            'enseignants' => $enseignants,
            'responsabilite' => $responsabilite,
            'titre' => "Enseignants - {$responsabilite}"
        ]);
    }

    /**
     * Affiche le formulaire de création d'un enseignant
     */
    public static function afficherFormulaire() {
        $titre = "Créer un enseignant";
        $responsabilites = Responsabilite::getAll();
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireEnseignant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireCommentaire() {
        $titre = "Ajouter un commentaire";

        Utilisateur::startSession();
        $login = Utilisateur::getCurrentLogin();

        $num_en = null;
        if ($login && Utilisateur::estEnseignant($login)) {
            $tmp = Enseignant::getNumByLogin($login);
            if ($tmp !== false) {
                $num_en = (int)$tmp;
            }
        }

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireCommentaire.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function ajouterCommentaire() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $nom_g = trim($_POST['nom_g'] ?? '');
        $commentaire = trim($_POST['commentaire'] ?? '');
        $date = isset($_POST['date_constitution_groupe']) ? trim($_POST['date_constitution_groupe']) : null;
        $date = ($date !== null && $date !== '') ? $date : null;

        $num_en = (isset($_POST['num_en']) && $_POST['num_en'] !== '') ? (int)$_POST['num_en'] : null;

        if (!$num_en) {
            Utilisateur::startSession();
            $login = Utilisateur::getCurrentLogin();
            if ($login && Utilisateur::estEnseignant($login)) {
                $tmp = Enseignant::getNumByLogin($login);
                if ($tmp !== false) {
                    $num_en = (int)$tmp;
                }
            }
        }

        if (!$num_en) {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire&error=' . urlencode('num_en manquant'));
            exit;
        }

        if ($nom_g === '' || $commentaire === '') {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire&error=' . urlencode('Champs obligatoires: nom_g, commentaire'));
            exit;
        }

        $num_g = EnseignantGroupe::fromNameToId($nom_g);
        if ($num_g === false || (int)$num_g <= 0) {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire&error=' . urlencode('Groupe introuvable (nom_g)'));
            exit;
        }

        $ok = EnseignantGroupe::saveCommentaire((int)$num_en, (int)$num_g, $commentaire, $date);
        if (!$ok) {
            $err = EnseignantGroupe::getLastError() ?? 'Erreur enregistrement commentaire';
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire&error=' . urlencode($err));
            exit;
        }

        header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire&msg=' . urlencode('Commentaire enregistré'));
        exit;
    }

    public static function creer() {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

    $erreurs = [];
    if (empty($_POST['login'])) $erreurs[] = 'Le login est obligatoire';
    if (empty($_POST['mdp'])) $erreurs[] = 'Le mot de passe est obligatoire';
    if (empty($_POST['nom'])) $erreurs[] = 'Le nom est obligatoire';
    if (empty($_POST['prenom'])) $erreurs[] = 'Le prénom est obligatoire';
    if (empty($_POST['mail']) || !filter_var($_POST['mail'], FILTER_VALIDATE_EMAIL)) $erreurs[] = "L'email est invalide";

    if (!empty($erreurs)) {
        $titre = "Erreur de validation";
        require_once __DIR__ . '/../vue/header.php';
        echo "<div class='alert'>";
        foreach ($erreurs as $erreur) echo "<p>".htmlspecialchars($erreur)."</p>";
        echo "</div>";
        require_once __DIR__ . '/../vue/footer.php';
        return;
    }

    $data = [
        'login' => $_POST['login'],
        'mdp' => $_POST['mdp'],
        'nom' => $_POST['nom'],
        'prenom' => $_POST['prenom'],
        'mail' => $_POST['mail'],
        'num_r' => isset($_POST['responsabilite']) && $_POST['responsabilite'] !== '' ? (int)$_POST['responsabilite'] : null
    ];

    // URL corrigée pour POST : pas de login dans l'URL
    $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/enseignant/";

    $ch = curl_init($url);
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_POST => true,
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

    if ($httpCode !== 201 && $httpCode !== 200) {
        $json = json_decode($response, true);
        $apiErr = $json['error'] ?? ($json['message'] ?? null);

        $titre = "Erreur création enseignant";
        require_once __DIR__ . '/../vue/header.php';
        echo "<div class='alert'><p>Erreur HTTP $httpCode</p>";
        if ($apiErr) echo "<p>".htmlspecialchars($apiErr)."</p>";
        if ($curlError) echo "<p>Erreur cURL: ".htmlspecialchars($curlError)."</p>";
        echo "</div>";
        require_once __DIR__ . '/../vue/footer.php';
        return;
    }

    header("Location: routeur.php?controleur=controleurEnseignant&action=listeInfos");
    exit;
}


    /**
     * Modifie un enseignant via l'API (attend POST contenant 'login')
     */
    public static function modifier() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $login = $_POST['login'] ?? null;
        if (!$login) { http_response_code(400); echo 'Login manquant'; return; }

        $data = [];
        $nom = isset($_POST['nom']) ? trim((string)$_POST['nom']) : '';
        $prenom = isset($_POST['prenom']) ? trim((string)$_POST['prenom']) : '';
        $mail = isset($_POST['mail']) ? trim((string)$_POST['mail']) : '';

        if ($nom !== '') $data['nom'] = $nom;
        if ($prenom !== '') $data['prenom'] = $prenom;
        if ($mail !== '') $data['mail'] = $mail;

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/enseignant/".urlencode($login);

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
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireModification&error=' . urlencode('Erreur modification enseignant (HTTP ' . $httpCode . '): ' . $errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireModification&msg=' . urlencode('Enseignant modifié'));
        exit;
    }


    /**
     * Supprime un enseignant via l'API (attend POST contenant 'login')
     */
    public static function supprimer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $login = $_POST['login'] ?? null;
        if (!$login) {
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireSuppression&error='.urlencode('Login manquant'));
            exit;
        }

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/enseignant/".urlencode($login);
        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_CUSTOMREQUEST => 'DELETE',
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_MAXREDIRS => 3,
            CURLOPT_TIMEOUT => 30
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $curlError = curl_error($ch);
        curl_close($ch);

        if ($httpCode !== 200 && $httpCode !== 204) {
            $errMsg = $response ? $response : $curlError;
            header('Location: routeur.php?controleur=controleurEnseignant&action=afficherFormulaireSuppression&error='.urlencode('Erreur suppression enseignant: '.$errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurEnseignant&action=listeInfos&msg='.urlencode('Enseignant supprimé'));
        exit;
    }

    public static function afficherFormulaireSuppression(){
        $titre = "Supprimer un enseignant";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireSuppressionEnseignant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireModification(){
        $titre = "Modifier un enseignant";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireModificationEnseignant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }
}
?>