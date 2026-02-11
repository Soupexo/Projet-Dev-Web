<?php

require_once __DIR__ . '/../modele/etudiant.php';
require_once __DIR__ . '/../modele/utilisateur.php';

class controleurEtudiant {

    /**
     * Affiche la liste complète des étudiants avec leurs infos
     */
    public static function listeInfos() {
        $titre = "vueInfosEtudiant";
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant");
        $etudiants = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/infosEtudiants.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Affiche les étudiants non affectés à un groupe
     */
    public static function listeEtudiantsNonAffectes() {
        $titre = "vueEtudiantsNonAffectes";
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/non-affectes");
        $etudiants = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/etudiantsNonAffectes.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function listeEtudiants(){
        $titre = "vueEtudiants";
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/etudiantsPourEtudiant");
        $etudiants = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/etudiantsPourEtudiant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Affiche les notes d'un étudiant
     */
    public static function listeNotes() {
        $titre = "vueNotesEtudiant";
        
        // Récupérer le login de l'utilisateur connecté
        $loginConnecte = Utilisateur::getCurrentLogin();
        
        // Si l'utilisateur est un étudiant, filtrer ses notes uniquement
        if (Utilisateur::estEtudiant($loginConnecte)) {
            // Récupérer toutes les notes puis filtrer
            $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/notes");
            $allNotes = json_decode($json, true);
            
            // Filtrer les notes pour l'étudiant connecté
            $notes = [];
            if (isset($allNotes['data']) && is_array($allNotes['data'])) {
                foreach ($allNotes['data'] as $note) {
                    if (isset($note['login_u']) && $note['login_u'] === $loginConnecte) {
                        $notes[] = $note;
                    }
                }
            }
        } else {
            // Si c'est un admin/enseignant, afficher toutes les notes
            $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/notes");
            $notes = json_decode($json, true);
        }

        // Messages facultatifs (redir from actions)
        $message = $_GET['msg'] ?? null;
        $error = $_GET['error'] ?? null;

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/gestionNotesPourEtudiant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Crée une note pour un étudiant (POST)
     */
    public static function creerNote() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $etudiant = $_POST['etudiant'] ?? null;
        $matiere = $_POST['matiere'] ?? null;
        $note = isset($_POST['note']) ? (float)$_POST['note'] : null;
        $moyPrem = isset($_POST['moy_prem_semestre']) ? (float)$_POST['moy_prem_semestre'] : null;
        $moyDeux = isset($_POST['moy_deux_semestre']) ? (float)$_POST['moy_deux_semestre'] : null;
        $coef = isset($_POST['coef']) ? (int)$_POST['coef'] : 1;

        if (!$etudiant || !$matiere || ($note === null && $moyPrem === null && $moyDeux === null)) {
            header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&error=".urlencode('Champs obligatoires: etudiant, matiere, et au moins une valeur de note'));
            exit;
        }

        $payload = ['etudiant' => $etudiant, 'matiere' => $matiere, 'note' => $note, 'moy_prem_semestre' => $moyPrem, 'moy_deux_semestre' => $moyDeux, 'coef' => $coef];
        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/notes/";

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

        if ($httpCode !== 201 && $httpCode !== 200) {
            $errMsg = $response ? $response : $curlError;
            header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&error=".urlencode('Erreur création note: '.$errMsg));
            exit;
        }

        header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&msg=".urlencode('Note créée/modifiée'));
        exit;
    }

    /**
     * Modifie une note d'un étudiant (POST -> PUT via API)
     */
    public static function modifierNote() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $etudiant = $_POST['etudiant'] ?? null;
        $matiere = $_POST['matiere'] ?? null;
        $note = isset($_POST['note']) ? (float)$_POST['note'] : null;
        $moyPrem = isset($_POST['moy_prem_semestre']) ? (float)$_POST['moy_prem_semestre'] : null;
        $moyDeux = isset($_POST['moy_deux_semestre']) ? (float)$_POST['moy_deux_semestre'] : null;
        $coef = isset($_POST['coef']) ? (int)$_POST['coef'] : 1;

        if (!$etudiant || !$matiere || ($note === null && $moyPrem === null && $moyDeux === null)) {
            header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&error=".urlencode('Champs obligatoires: etudiant, matiere, et au moins une valeur de note'));
            exit;
        }

        $payload = ['etudiant' => $etudiant, 'matiere' => $matiere, 'note' => $note, 'moy_prem_semestre' => $moyPrem, 'moy_deux_semestre' => $moyDeux, 'coef' => $coef];
        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/notes/";

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_CUSTOMREQUEST => 'PUT',
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

        if ($httpCode !== 200) {
            $errMsg = $response ? $response : $curlError;
            header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&error=".urlencode('Erreur modification note: '.$errMsg));
            exit;
        }

        header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&msg=".urlencode('Note modifiée'));
        exit;
    }

    /**
     * Supprime une note d'un étudiant (POST -> DELETE via API)
     */
    public static function supprimerNote() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $etudiant = $_POST['etudiant'] ?? null;
        $matiere = $_POST['matiere'] ?? null;

        if (!$etudiant || !$matiere) {
            header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&error=".urlencode('Champs obligatoires: etudiant, matiere'));
            exit;
        }

        $payload = ['etudiant' => $etudiant, 'matiere' => $matiere];
        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/notes/";

        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_CUSTOMREQUEST => 'DELETE',
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

        if ($httpCode !== 200) {
            $errMsg = $response ? $response : $curlError;
            header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&error=".urlencode('Erreur suppression note: '.$errMsg));
            exit;
        }

        header("Location: routeur.php?controleur=controleurEtudiant&action=listeNotes&msg=".urlencode('Note supprimée'));
        exit;
    }

    /**
     * Affiche les critères disciplinaires (séparations entre étudiants)
     */
    public static function listeCriteresDisciplinaires() {
        $titre = "vueCritèreDisciplinaire";
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/criteres");
        $criteres = json_decode($json, true);
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/etudiantSeparation.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Affiche les groupes de covoiturage
     */
    public static function listeGroupesCovoiturage() {
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/covoiturages");
        $covoiturages = json_decode($json, true);
        $titre = "vueGroupeCovoiturage";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/etudiantCovoiturage.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Affiche les réponses au sondage
     */
    public static function listeReponsesSondage() {
        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/reponses-sondages");
        $reponses = json_decode($json, true);
        $titre = "vueReponseSondage";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/reponseSondage.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireReponseSondage() {
        $titre = "Répondre à un sondage";

        $json = file_get_contents("https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/sondage");
        $sondages = json_decode($json, true);

        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireReponseSondage.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function creerReponseSondage() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }

        $nomSondage = trim($_POST['nomSondage'] ?? '');
        $question = trim($_POST['question'] ?? '');
        $reponse = trim($_POST['reponse'] ?? '');
        $dateReponse = trim($_POST['dateReponse'] ?? '');

        // La colonne BDD est de type DATE, donc on envoie YYYY-MM-DD.
        // Si jamais on reçoit un datetime-local (YYYY-MM-DDTHH:MM[:SS]), on convertit en date.
        if ($dateReponse !== '') {
            if (str_contains($dateReponse, 'T')) {
                $dt = DateTime::createFromFormat('Y-m-d\TH:i', $dateReponse)
                    ?: DateTime::createFromFormat('Y-m-d\TH:i:s', $dateReponse);
                if ($dt instanceof DateTime) {
                    $dateReponse = $dt->format('Y-m-d');
                }
            } else {
                $dt = DateTime::createFromFormat('Y-m-d', $dateReponse);
                if ($dt instanceof DateTime) {
                    $dateReponse = $dt->format('Y-m-d');
                }
            }
        }

        if ($nomSondage === '' || $question === '' || $reponse === '' || $dateReponse === '') {
            header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage&error=' . urlencode('Champs manquants'));
            exit;
        }

        $login = Utilisateur::getCurrentLogin();
        if (!$login || !Utilisateur::estEtudiant($login)) {
            header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage&error=' . urlencode('Vous devez être connecté en tant qu\'étudiant'));
            exit;
        }

        $num_e = Etudiant::getNumByLogin($login);
        if ($num_e === false || (int)$num_e <= 0) {
            header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage&error=' . urlencode('Impossible de récupérer votre num_e'));
            exit;
        }

        $payload = [
            'nomSondage' => $nomSondage,
            'idEtudiant' => (int)$num_e,
            'question' => $question,
            'reponse' => $reponse,
            'dateReponse' => $dateReponse
        ];

        // IMPORTANT: slash final pour éviter certains 301/302 qui transforment le POST en GET
        $url = 'https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/reponse-sondage/';
        $ch = curl_init($url);
        curl_setopt_array($ch, [
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_POST => true,
            CURLOPT_HTTPHEADER => ['Content-Type: application/json'],
            CURLOPT_POSTFIELDS => json_encode($payload),
            CURLOPT_SSL_VERIFYPEER => false,
            CURLOPT_FOLLOWLOCATION => true,
            // Si redirection, conserver le POST (sinon certains serveurs renvoient un GET)
            CURLOPT_POSTREDIR => defined('CURL_REDIR_POST_ALL') ? CURL_REDIR_POST_ALL : 1,
            CURLOPT_MAXREDIRS => 3,
            CURLOPT_TIMEOUT => 30
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $curlError = curl_error($ch);
        curl_close($ch);

        $json = $response ? json_decode($response, true) : null;

        if ($httpCode !== 201 && $httpCode !== 200) {
            $apiErr = is_array($json) ? ($json['error'] ?? ($json['message'] ?? null)) : null;
            $errMsg = $apiErr ?? ($response ?: $curlError);
            header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage&error=' . urlencode('Erreur: ' . $errMsg));
            exit;
        }

        if (!is_array($json) || empty($json['success'])) {
            $raw = is_string($response) ? trim($response) : '';
            $snippet = $raw !== '' ? substr($raw, 0, 300) : '';

            if (is_array($json)) {
                $errMsg = $json['error'] ?? ($json['message'] ?? 'Réponse API inattendue');
            } else {
                $errMsg = 'Réponse API invalide';
            }

            if ($snippet !== '') {
                $errMsg .= " | HTTP=$httpCode | extrait=" . $snippet;
            } else {
                $errMsg .= " | HTTP=$httpCode";
            }

            header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage&error=' . urlencode('Erreur: ' . $errMsg));
            exit;
        }

        $numE = isset($json['num_e']) ? (string)$json['num_e'] : '';
        $numS = isset($json['num_s']) ? (string)$json['num_s'] : '';
        $extra = ($numE !== '' && $numS !== '') ? " (num_e=$numE, num_s=$numS)" : '';

        header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage&msg=' . urlencode('Réponse envoyée' . $extra));
        exit;
    }

    /**
     * Affiche le formulaire de création d'un étudiant
     */
    public static function afficherFormulaire() {
        $titre = "Créer un étudiant";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireEtudiant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Crée un étudiant via l'API
     */
    public static function creer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { 
            http_response_code(405); 
            error_log("DEBUG ETUDIANT: Méthode non POST");
            exit; 
        }

        error_log("DEBUG ETUDIANT: POST reçu - " . print_r($_POST, true));

        $erreurs = [];
        if (empty($_POST['login'])) $erreurs[] = 'Le login est obligatoire';
        if (empty($_POST['mdp'])) $erreurs[] = 'Le mot de passe est obligatoire';
        if (empty($_POST['nom'])) $erreurs[] = 'Le nom est obligatoire';
        if (empty($_POST['prenom'])) $erreurs[] = 'Le prénom est obligatoire';
        if (empty($_POST['mail']) || !filter_var($_POST['mail'], FILTER_VALIDATE_EMAIL)) $erreurs[] = "L'email est invalide";

        if (!empty($erreurs)) {
            error_log("DEBUG ETUDIANT: Erreurs validation - " . implode(", ", $erreurs));
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
            'est_apprenti_e' => isset($_POST['est_apprenti_e']) ? 1 : 0,
            'type_bac_e' => $_POST['type_bac_e'] ?? null,
            'genre_e' => $_POST['genre_e'] ?? null
        ];

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/";

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
            // Handle conflict (login/email already used) with user-friendly messages
            if ($httpCode === 409) {
                $json = json_decode($response, true);
                $apiErr = $json['error'] ?? '';

                if ($apiErr && stripos($apiErr, 'login') !== false) {
                    $friendly = "Un étudiant avec ce login existe déjà. Veuillez choisir un autre login.";
                } elseif ($apiErr && (stripos($apiErr, 'mail') !== false || stripos($apiErr, 'mail') !== false)) {
                    $friendly = "Cet e-mail est déjà utilisé par un autre utilisateur.";
                } else {
                    $friendly = "Un utilisateur avec ces informations existe déjà.";
                }

                $titre = "Conflit : étudiant existant";
                require_once __DIR__ . '/../vue/header.php';
                echo "<div class='alert'><p>".htmlspecialchars($friendly)."</p></div>";
                require_once __DIR__ . '/../vue/footer.php';
                return;
            }

            // Other errors: show readable API error if present
            $json = json_decode($response, true);
            $apiErr = $json['error'] ?? ($json['message'] ?? null);

            $titre = "Erreur création étudiant";
            require_once __DIR__ . '/../vue/header.php';
            echo "<div class='alert'><p>Erreur HTTP $httpCode</p>";
            if ($apiErr) echo "<p>".htmlspecialchars($apiErr)."</p>";
            if ($curlError) echo "<p>Erreur cURL: ".htmlspecialchars($curlError)."</p>";
            if ($response && !$apiErr) echo "<pre>Réponse : ".htmlspecialchars($response)."</pre>";
            echo "</div>";
            require_once __DIR__ . '/../vue/footer.php';
            return;
        }

        header("Location: routeur.php?controleur=controleurEtudiant&action=listeInfos");
        exit;
    }

    /**
     * Modifie un étudiant via l'API (attend POST contenant 'login')
     */
    public static function modifier() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $login = $_POST['login'] ?? null;
        if (!$login) { http_response_code(400); echo 'Login manquant'; return; }

        $data = [];
        $nom = isset($_POST['nom']) ? trim((string)$_POST['nom']) : '';
        $prenom = isset($_POST['prenom']) ? trim((string)$_POST['prenom']) : '';
        $mail = isset($_POST['mail']) ? trim((string)$_POST['mail']) : '';
        $typeBac = isset($_POST['type_bac_e']) ? trim((string)$_POST['type_bac_e']) : '';
        $genre = isset($_POST['genre_e']) ? trim((string)$_POST['genre_e']) : '';

        if ($nom !== '') $data['nom'] = $nom;
        if ($prenom !== '') $data['prenom'] = $prenom;
        if ($mail !== '') $data['mail'] = $mail;
        $data['est_apprenti_e'] = isset($_POST['est_apprenti_e']) ? 1 : 0;
        if ($typeBac !== '') $data['type_bac_e'] = $typeBac;
        if ($genre !== '') $data['genre_e'] = $genre;

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/".urlencode($login)."/";

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
            header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireModification&error=' . urlencode('Erreur modification étudiant (HTTP ' . $httpCode . '): ' . $errMsg));
            exit;
        }

        header('Location: routeur.php?controleur=controleurEtudiant&action=afficherFormulaireModification&msg=' . urlencode('Étudiant modifié'));
        exit;
    }

    public static function listeEtudiantsPourEnseignant() {
        $json = file_get_contents(
            "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/etudiantsPourEnseignant"
        );
        $etudiants = json_decode($json, true);

        $titre = "Liste des étudiants - Enseignant";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/etudiantsPourEnseignant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    /**
     * Supprime un étudiant via l'API (attend POST contenant 'login')
     */
    public static function supprimer() {
        if ($_SERVER['REQUEST_METHOD'] !== 'POST') { http_response_code(405); exit; }
        $login = $_POST['login'] ?? null;
        if (!$login) { http_response_code(400); echo 'Login manquant'; return; }

        $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant/".urlencode($login)."/";
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
        curl_close($ch);

        if ($httpCode !== 200 && $httpCode !== 204) {
            $titre = "Erreur suppression étudiant";
            require_once __DIR__ . '/../vue/header.php';
            echo "<div class='alert'><p>Erreur HTTP $httpCode</p>";
            if ($response) echo "<pre>Réponse : ".htmlspecialchars($response)."</pre>";
            echo "</div>";
            require_once __DIR__ . '/../vue/footer.php';
            return;
        }

        header("Location: routeur.php?controleur=controleurEtudiant&action=listeInfos");
        exit;
    }

    public static function afficherFormulaireSuppression(){
        $titre = "Supprimer un étudiant";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireSuppressionEtudiant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function afficherFormulaireModification(){
        $titre = "Modifier un étudiant";
        require_once __DIR__ . '/../vue/header.php';
        require_once __DIR__ . '/../vue/formulaireModificationEtudiant.php';
        require_once __DIR__ . '/../vue/footer.php';
    }

    public static function exporterCSV() {
    // 1. Récupérer les données depuis l'API (exactement comme listeInfos)
    $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/etudiant";
    $json = file_get_contents($url);
    $etudiants = json_decode($json, true);

    if (empty($etudiants)) {
        header("Location: routeur.php?controleur=controleurEtudiant&action=listeInfos&error=Aucune donnée");
        exit;
    }

    // 2. Préparer le téléchargement
    ob_clean(); // Nettoie la mémoire tampon
    header('Content-Type: text/csv; charset=utf-8');
    header('Content-Disposition: attachment; filename=liste_etudiants_' . date('Y-m-d') . '.csv');

    $output = fopen('php://output', 'w');
    fprintf($output, chr(0xEF).chr(0xBB).chr(0xBF)); // BOM UTF-8

    // 3. Entêtes
    fputcsv($output, ['Nom', 'Prénom', 'Email', 'Groupe', 'Année', 'Apprenti', 'Genre', 'Type Bac'], ';');

    // 4. Données
    foreach ($etudiants as $etudiant) {
        fputcsv($output, [
            $etudiant['nom_u'] ?? '',
            $etudiant['prenom_u'] ?? '',
            $etudiant['mail_u'] ?? '',
            $etudiant['nom_g'] ?? '',
            $etudiant['annee'] ?? '',
            (!empty($etudiant['est_apprenti_e']) ? 'Oui' : 'Non'),
            $etudiant['genre_e'] ?? 'N/A',
            $etudiant['type_bac_e'] ?? ''
        ], ';');
    }

    fclose($output);
    exit;
}
}
?>
