<?php

require_once __DIR__ . '/../modele/utilisateur.php';

class ControleurUtilisateur {

    /**
     * Affiche le formulaire
     */
    public static function afficherFormulaire() {
        $titre = "Créer un utilisateur";
        require __DIR__ . '/../vue/header.php';
        require __DIR__ . '/../vue/formulaireUtilisateur.php';
        require __DIR__ . '/../vue/footer.php';
    }

    public static function creer() {

    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        http_response_code(405);
        exit;
    }

    $erreurs = [];
    if (empty($_POST["login"])) $erreurs[] = "Le login est obligatoire";
    if (empty($_POST["mdp"])) $erreurs[] = "Le mot de passe est obligatoire";
    if (empty($_POST["nom"])) $erreurs[] = "Le nom est obligatoire";
    if (empty($_POST["prenom"])) $erreurs[] = "Le prénom est obligatoire";
    if (empty($_POST["mail"]) || !filter_var($_POST["mail"], FILTER_VALIDATE_EMAIL)) {
        $erreurs[] = "L'email est invalide";
    }

    if (!empty($erreurs)) {
        $titre = "Erreur de validation";
        require __DIR__ . '/../vue/header.php';
        echo "<div class='alert'>";
        foreach ($erreurs as $erreur) {
            echo "<p>$erreur</p>";
        }
        echo "</div>";
        require __DIR__ . '/../vue/footer.php';
        return;
    }

    $data = [
        "login"  => $_POST["login"],
        "mdp"    => $_POST["mdp"],
        "nom"    => $_POST["nom"],
        "prenom" => $_POST["prenom"],
        "mail"   => $_POST["mail"]
    ];

    $url = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/utilisateur/";

    $ch = curl_init($url);
    curl_setopt_array($ch, [
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_POST           => true,
        CURLOPT_HTTPHEADER     => ["Content-Type: application/json"],
        CURLOPT_POSTFIELDS     => json_encode($data),
        CURLOPT_SSL_VERIFYPEER => false,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_MAXREDIRS      => 3,
        CURLOPT_TIMEOUT        => 30
    ]);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $curlError = curl_error($ch);
    curl_close($ch);

    // ✅ Accepter 200 OU 201 comme succès
    if ($httpCode !== 201 && $httpCode !== 200) {
        $titre = "Erreur création utilisateur";
        require __DIR__ . '/../vue/header.php';
        echo "<div class='alert'>";
        echo "<p>Erreur HTTP $httpCode</p>";
        if ($curlError) {
            echo "<p>Erreur cURL: $curlError</p>";
        }
        if ($response) {
            echo "<pre>Réponse : " . htmlspecialchars($response) . "</pre>";
        }
        echo "</div>";
        require __DIR__ . '/../vue/footer.php';
        return;
    }

    // Vérifier que la création a vraiment réussi
    $responseData = json_decode($response, true);
    if (isset($responseData['success']) && $responseData['success'] === false) {
        $titre = "Erreur création utilisateur";
        require __DIR__ . '/../vue/header.php';
        echo "<p class='alert'>Erreur : " . htmlspecialchars($responseData['error'] ?? "Erreur inconnue") . "</p>";
        require __DIR__ . '/../vue/footer.php';
        return;
    }
    exit;
}

public static function liste() {

    $json = file_get_contents(
        "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api/utilisateur/"
    );

    $utilisateurs = json_decode($json, true);

    $titre = "Liste des utilisateurs";
    require __DIR__ . '/../vue/header.php';
    require __DIR__ . '/../vue/listeUtilisateurs.php';
    require __DIR__ . '/../vue/footer.php';
}

public static function creerFormulaireAuthentification() {
    $titre = "Authentification";
    require __DIR__ . '/../vue/header.php';
    require __DIR__ . '/../vue/formulaireConnexion.php';
    require __DIR__ . '/../vue/footer.php';
}

public static function authentifier(){
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        http_response_code(405);
        exit;
    }
    
    // Récupérer les données du formulaire
    $login = $_POST['login'] ?? '';
    $mdp = $_POST['mdp'] ?? '';
    
    // Créer un objet Utilisateur avec les données du formulaire
    $utilisateur = new Utilisateur($login, $mdp);
    
    // Vérifier l'authentification
    if ($utilisateur->checkMDP()) {
        // Authentification réussie - redirection gérée dans checkMDP()
        exit;
    } else {
        // Authentification échouée
        header("Location: routeur.php?controleur=controleurUtilisateur&action=creerFormulaireAuthentification&erreur=1");
        exit;
    }
}

/**
 * Déconnecte l'utilisateur
 */
public static function logout() {
    Utilisateur::logout();
    header("Location: routeur.php");
    exit;
}

}