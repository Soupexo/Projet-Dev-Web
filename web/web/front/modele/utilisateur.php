<?php
    class Utilisateur{
        private ?string $login;
        private ?string $mdp;
        private ?string $nom;
        private ?string $prenom;
        private ?string $email;

        public function __construct(string $log = NULL, string $password = NULL, string $name = NULL, string $firstname = NULL, string $mail = NULL){
            if (!is_null($log)){
                $this->login = $log; 
                $this->mdp = $password;
                $this->prenom = $firstname;
                $this->nom = $name;
                $this->email = $mail;
            
            }
        }
        public function getMDP(){
            return $this->mdp;
        }
        public static function getAllUtilisateurs(){
            $requete = "SELECT login_u, nom_u, prenom_u, mail_u FROM UTILISATEUR";
            $resultat = connexion::pdo()->query($requete);
            $resultat->setFetchmode(PDO::FETCH_CLASS, "utilisateur");
            $utilisateur = $resultat->fetchAll();
            return $utilisateur;
        }

        public static function getByLogin(string $login) {
            try {
                $sql = "SELECT login_u, nom_u, prenom_u, mail_u FROM UTILISATEUR WHERE login_u = :login";
                $stmt = connexion::pdo()->prepare($sql);
                $stmt->execute([':login' => $login]);
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                return $user ?: null;
            } catch (PDOException $e) {
                error_log("Erreur getByLogin: " . $e->getMessage());
                return null;
            }
        }

        public static function addUtilisateur($login, $mdp, $nom, $prenom, $email) {
            $hashMdp = password_hash($mdp, PASSWORD_DEFAULT);
            $requeteAvecTags = "INSERT INTO UTILISATEUR(`login_u`, `mdp_u`, `nom_u`, `prenom_u`, `mail_u` ) VALUES(:loginU, :mdp, :nom, :prenom, :email);";
            $requetePreparee = connexion::pdo()->prepare($requeteAvecTags);
            $valeurs = array();
            $valeurs["loginU"] = $login;
            $valeurs["mdp"] = $hashMdp;
            $valeurs["nom"] = $nom;
            $valeurs["prenom"] = $prenom;
            $valeurs["email"] = $email;
            try {
            $requetePreparee->execute($valeurs);
            return true;
            } catch (PDOException $e) {
                echo "<pre>";
                echo "ERREUR SQL : " . $e->getMessage();
                echo "</pre>";
                exit;
            }
        }
        public function checkMDP(): bool {
            $sql = "SELECT mdp_u, nom_u, prenom_u, mail_u FROM UTILISATEUR WHERE login_u = :login";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([
                ":login" => $this->login
            ]);

            $row = $stmt->fetch(PDO::FETCH_ASSOC);

            if (!$row) {
                echo "<p>Utilisateur non trouvé</p>";
                return false;
            }
            
            $result = password_verify($this->mdp, $row["mdp_u"]);
            if ($result) {
                // Authentification réussie - créer la session
                session_start();
                $_SESSION['user'] = [
                    'login' => $this->login,
                    'nom' => $row['nom_u'],
                    'prenom' => $row['prenom_u'],
                    'mail' => $row['mail_u'],
                    'role' => $this->getRoleUtilisateur($this->login)
                ];
                header("Location: routeur.php");
                return true;
            } else {
                echo "<p>Mot de passe incorrect</p>";
                return false;
            }
        }

            /**
         * Récupère le rôle de l'utilisateur
         */
    public function getRoleUtilisateur($login): string {
        if (self::estEtudiant($login)) {
            return "étudiant";
        } elseif (self::estEnseignant($login)) {
            if (self::estResponsableAnnee($login)) {
                return "enseignant responsable d'année";
            } elseif (self::estResponsableFormation($login)) {
                return "enseignant responsable de formation";
            } elseif (self::estResponsableFiliere($login)) {
                return "enseignant responsable de filière";
            } elseif (self::estResponsableSemestre($login)) {
                return "enseignant responsable de semestre";
            } else {
                return "enseignant";
            }
        } else {
            return "rôle non défini";
        }
    }


        /**
         * Affiche le rôle de l'utilisateur après authentification
         */
        public function afficherRoleUtilisateur($login): void {
            if (self::estEtudiant($login)) {
                echo "<p>étudiant</p>";
            } elseif (self::estEnseignant($login)) {
                if (self::estResponsableAnnee($login)) {
                    echo "<p>enseignant responsable d'année</p>";
                } elseif (self::estResponsableFormation($login)) {
                    echo "<p>enseignant responsable de formation</p>";
                } elseif (self::estResponsableFiliere($login)) {
                    echo "<p>enseignant responsable de filière</p>";
                } elseif (self::estResponsableSemestre($login)) {
                    echo "<p>enseignant responsable de semestre</p>";
                } else {
                    echo "<p>enseignant</p>";
                }
            } else {
                echo "<p>rôle non défini</p>";
            }
        }

        // DEBUG: stockage du dernier message d'erreur
    private static ?string $lastError = null;

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    public function __toString(){
            return "<p> utilisateur ".$this->login." (".$this->nom."), de email = ".$this->email."</p>";
        }

        
public static function create($login, $mdp, $nom, $prenom, $mail): bool {
    try {
        $sql = "INSERT INTO UTILISATEUR
                (login_u, mdp_u, nom_u, prenom_u, mail_u)
                VALUES (:login, :mdp, :nom, :prenom, :mail)";

        $stmt = connexion::pdo()->prepare($sql);

        $result = $stmt->execute([
            ':login'  => $login,
            ':mdp'    => password_hash($mdp, PASSWORD_DEFAULT),
            ':nom'    => $nom,
            ':prenom' => $prenom,
            ':mail'   => $mail  
        ]);

        if ($result !== true) {
            $err = $stmt->errorInfo();
            $msg = "Utilisateur insert failed: SQLSTATE=" . ($err[0] ?? '') . ", code=" . ($err[1] ?? '') . ", message=" . ($err[2] ?? '');
            error_log($msg);
            self::$lastError = $msg;
            return false;
        }

        return true;

    } catch (PDOException $e) {
        error_log("Erreur création utilisateur: " . $e->getMessage());
        self::$lastError = $e->getMessage();
        return false;
    }
    }

    public static function update(string $login, array $data): int {
        try {
            $sql = "UPDATE UTILISATEUR
                    SET nom_u = :nom,
                        prenom_u = :prenom,
                        mail_u = :email
                    WHERE login_u = :login";

            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([
                ':login'  => $login,
                ':nom'    => $data['nom'],
                ':prenom' => $data['prenom'],
                ':email'  => $data['email']
            ]);

            return $stmt->rowCount();

        } catch (PDOException $e) {
            error_log($e->getMessage());
            return 0;
        }
    }

    public static function updatePassword(string $login, string $newMdp): int {
        try {
            $sql = "UPDATE UTILISATEUR
                    SET mdp_u = :mdp
                    WHERE login_u = :login";

            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([
                ':login' => $login,
                ':mdp'   => password_hash($newMdp, PASSWORD_DEFAULT)
            ]);

            return $stmt->rowCount();

        } catch (PDOException $e) {
            error_log($e->getMessage());
            return 0;
        }
    }

    public static function delete(string $login): int {
        try {
            $sql = "DELETE FROM UTILISATEUR WHERE login_u = :login";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([':login' => $login]);

            return $stmt->rowCount();

        } catch (PDOException $e) {
            error_log($e->getMessage());
            return 0;
        }
    }

    // ============================================
    // Méthodes de vérification des responsabilités
    // ============================================

    /**
     * Vérifie si l'utilisateur est un étudiant
     */
    public static function estEtudiant(string $login_u): bool {
        try {
            $sql = "SELECT estEtudiant(?) AS resultat";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([$login_u]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);
            return $result['resultat'] == 1;
        } catch (PDOException $e) {
            error_log("Erreur estEtudiant: " . $e->getMessage());
            return false;
        }
    }

    public static function fromLoginToNumEnseignant(string $login){
        $sql = "SELECT num_e FROM UTILISATEUR INNER JOIN ENSEIGNANT ON login_u = num_e WHERE login_u = ?";
        $stmt = connexion::pdo()->prepare($sql);
        $stmt->execute([$login]);
        $result = $stmt->fetch(PDO::FETCH_ASSOC);
        return $result['num_e']; 
    }

    /**
     * Vérifie si l'utilisateur est un enseignant
     */
    public static function estEnseignant(string $login_u): bool {
        try {
            $sql = "SELECT estEnseignant(?) AS resultat";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([$login_u]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);
            return $result['resultat'] == 1;
        } catch (PDOException $e) {
            error_log("Erreur estEnseignant: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Vérifie si l'utilisateur est responsable de formation
     */
    public static function estResponsableFormation(string $login_u): bool {
        try {
            $sql = "SELECT estResponsableFormation(?) AS resultat";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([$login_u]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);
            return $result['resultat'] == 1;
        } catch (PDOException $e) {
            error_log("Erreur estResponsableFormation: " . $e->getMessage());
            return false;
        }
    }
    /**
     * Vérifie si l'utilisateur est responsable d'année
     */
    public static function estResponsableAnnee(string $login_u): bool {
        try {
            $sql = "SELECT estResponsableAnnee(?) AS resultat";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([$login_u]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);
            return $result['resultat'] == 1;
        } catch (PDOException $e) {
            error_log("Erreur estResponsableAnnee: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Vérifie si l'utilisateur est responsable de filière
     */
    public static function estResponsableFiliere(string $login_u): bool {
        try {
            $sql = "SELECT estResponsableFiliere(?) AS resultat";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([$login_u]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);
            return $result['resultat'] == 1;
        } catch (PDOException $e) {
            error_log("Erreur estResponsableFiliere: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Vérifie si l'utilisateur est responsable de semestre
     */
    public static function estResponsableSemestre(string $login_u): bool {
        try {
            $sql = "SELECT estResponsableSemestre(?) AS resultat";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([$login_u]);
            $result = $stmt->fetch(PDO::FETCH_ASSOC);
            return $result['resultat'] == 1;
        } catch (PDOException $e) {
            error_log("Erreur estResponsableSemestre: " . $e->getMessage());
            return false;
        }
    }

    // ============================================
    // Méthodes de gestion de session
    // ============================================

    /**
     * Démarre la session si pas déjà démarrée
     */
    public static function startSession(): void {
        if (session_status() == PHP_SESSION_NONE) {
            session_start();
        }
    }

    /**
     * Vérifie si un utilisateur est connecté
     */
    public static function isConnected(): bool {
        self::startSession();
        return isset($_SESSION['user']) && !empty($_SESSION['user']);
    }

    /**
     * Récupère les infos de l'utilisateur connecté
     */
    public static function getCurrentUser(): ?array {
        self::startSession();
        return $_SESSION['user'] ?? null;
    }

    /**
     * Récupère le login de l'utilisateur connecté
     */
    public static function getCurrentLogin(): ?string {
        $user = self::getCurrentUser();
        return $user['login'] ?? null;
    }

    /**
     * Déconnecte l'utilisateur
     */
    public static function logout(): void {
        self::startSession();
        session_destroy();
        unset($_SESSION);
    }

    /**
     * Affiche les infos de l'utilisateur connecté dans le header
     */
    public static function displayUserInfo(): void {
        if (self::isConnected()) {
            $user = self::getCurrentUser();
            echo '<div class="user-info">';
            echo '<span class="user-name">' . htmlspecialchars($user['prenom'] . ' ' . $user['nom']) . '</span>';
            echo '<span class="user-login">(' . htmlspecialchars($user['login']) . ')</span>';
            echo '<span class="user-role">' . htmlspecialchars($user['role']) . '</span>';
            echo '<a href="routeur.php?controleur=controleurUtilisateur&action=logout" class="logout-btn">Déconnexion</a>';
            echo '</div>';
        } else {
            echo '<div class="auth-links">';
            echo '<a href="routeur.php?controleur=controleurUtilisateur&action=creerFormulaireAuthentification">Connexion</a>';
            echo '</div>';
        }
    }

}