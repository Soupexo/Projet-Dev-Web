<?php
class Enseignant {
    public ?int $num;
    public ?Utilisateur $utilisateur;
    // Stocke le dernier message d'erreur SQL/exception pour debug (temporaire)
    private static ?string $lastError = null;

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    public static function getNumByLogin(string $login): int|false {
        try {
            $stmt = connexion::pdo()->prepare("SELECT num_en FROM ENSEIGNANT WHERE login_u = :login LIMIT 1");
            $stmt->execute([':login' => $login]);
            $num = $stmt->fetchColumn();
            if ($num === false) {
                return false;
            }
            return (int)$num;
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur getNumByLogin: " . $e->getMessage());
            return false;
        }
    }

    public function __construct(
        int $num = NULL,
        Utilisateur $utilisateur = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->utilisateur = $utilisateur;
        }
    }

    // Getter générique
    public function __get($property) {
        if(property_exists($this, $property)) {
            return $this->$property;
        }
        return null;
    }

    // Setter générique
    public function __set($property, $value) {
        if(property_exists($this, $property)) {
            $this->$property = $value;
        }
    }

    /**
     * Récupère la liste des enseignants avec leurs responsabilités
     * depuis vueListeEnseignant
     * @return array
     */
    public static function getListeEnseignants() {
        $requete = "SELECT * FROM vueListeEnseignant";
        $resultat = connexion::pdo()->query($requete);
        $enseignants = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $enseignants;
    }

    /**
     * Récupère les enseignants avec une responsabilité spécifique
     * @param string $responsabilite
     * @return array
     */
    public static function getEnseignantsParResponsabilite($responsabilite) {
        $requete = "SELECT * FROM vueListeEnseignant WHERE intitule_r = :responsabilite";
        $stmt = connexion::pdo()->prepare($requete);
        $stmt->execute([':responsabilite' => $responsabilite]);
        $enseignants = $stmt->fetchAll(PDO::FETCH_ASSOC);
        return $enseignants;
    }

    /**
     * CREATE : crée l'utilisateur puis l'enseignant
     */
    /**
     * CREATE : crée l'utilisateur puis l'enseignant
     * Optionnel : $num_r (int) ou $num_rs (array) pour assigner une responsabilité
     * Retourne le num_en inséré (int) ou false en cas d'erreur
     */
    public static function create(string $login, string $mdp, string $nom, string $prenom, string $mail, $num_r = null) {
        $pdo = connexion::pdo();
        try {
            $pdo->beginTransaction();
            $userCreated = Utilisateur::create($login, $mdp, $nom, $prenom, $mail);
            if (!$userCreated) {
                $pdo->rollBack();
                return false;
            }

            $sql = "INSERT INTO ENSEIGNANT (login_u) VALUES (:login)";
            $stmt = $pdo->prepare($sql);
            $result = $stmt->execute([':login' => $login]);

            if ($result !== true) {
                $err = $stmt->errorInfo();
                $msg = "Enseignant insert failed: SQLSTATE=" . ($err[0] ?? '') . ", code=" . ($err[1] ?? '') . ", message=" . ($err[2] ?? '');
                error_log($msg);
                // Stocker le message pour debug via API
                self::$lastError = $msg;
                if ($pdo->inTransaction()) $pdo->rollBack();
                return false;
            }

            // Récupérer num_en inséré (en recherchant par login)
            $stmt2 = $pdo->prepare("SELECT num_en FROM ENSEIGNANT WHERE login_u = :login LIMIT 1");
            $stmt2->execute([':login' => $login]);
            $row = $stmt2->fetch(PDO::FETCH_ASSOC);
            $num_en = $row ? (int)$row['num_en'] : null;

            // Assignation optionnelle de responsabilité
            if ($num_en && $num_r) {
                // vérifier que la responsabilité existe
                $check = connexion::pdo()->prepare("SELECT COUNT(*) as c FROM RESPONSABILITE WHERE num_r = :num_r");
                $check->execute([':num_r' => $num_r]);
                $exists = (int)($check->fetch(PDO::FETCH_ASSOC)['c'] ?? 0);
                if ($exists) {
                    $ins = $pdo->prepare("INSERT INTO ENSEIGNANT_RESPONSABILITE (num_en, num_r) VALUES (:num_en, :num_r)");
                    $ins->execute([':num_en' => $num_en, ':num_r' => $num_r]);
                }
            }

            $pdo->commit();
            return $num_en;
        } catch (PDOException $e) {
            if ($pdo->inTransaction()) $pdo->rollBack();
            $msg = "Erreur création enseignant: " . $e->getMessage();
            error_log($msg);
            // Stocker pour debug
            self::$lastError = $e->getMessage();
            return false;
        }
    }

    /**
     * UPDATE : met à jour l'utilisateur associé (les enseignants n'ont pas de champs propres dans ce modèle)
     */
    public static function update(string $login, array $data): int {
        try {
            $pdo = connexion::pdo();
            $pdo->beginTransaction();

            $rowsUser = 0;
            if (isset($data['nom']) || isset($data['prenom']) || isset($data['email'])) {
                $rowsUser = Utilisateur::update($login, [
                    'nom' => $data['nom'] ?? '',
                    'prenom' => $data['prenom'] ?? '',
                    'mail' => $data['mail'] ?? ''
                ]);
            }

            $pdo->commit();
            return $rowsUser;
        } catch (PDOException $e) {
            if (connexion::pdo()->inTransaction()) connexion::pdo()->rollBack();
            error_log("Erreur mise à jour enseignant: " . $e->getMessage());
            return 0;
        }
    }

    /**
     * DELETE : supprime l'enseignant puis l'utilisateur
     */
    public static function delete(string $login): int {
        try {
            $pdo = connexion::pdo();
            $pdo->beginTransaction();

            $stmt = $pdo->prepare("DELETE FROM ENSEIGNANT WHERE login_u = :login");
            $stmt->execute([':login' => $login]);
            $rowsEns = $stmt->rowCount();

            $rowsUser = Utilisateur::delete($login);

            $pdo->commit();
            return $rowsEns + $rowsUser;
        } catch (PDOException $e) {
            if (connexion::pdo()->inTransaction()) connexion::pdo()->rollBack();
            error_log("Erreur suppression enseignant: " . $e->getMessage());
            return 0;
        }
    }

    /**
     * Récupère les responsabilités (num_r et intitule) assignées à un enseignant
     * @param int $num_en
     * @return array
     */
    public static function getResponsabilitesForEnseignant(int $num_en): array {
        $sql = "SELECT r.num_r, r.intitule_r FROM RESPONSABILITE r
                JOIN ENSEIGNANT_RESPONSABILITE er ON r.num_r = er.num_r
                WHERE er.num_en = :num_en ORDER BY r.intitule_r";
        $stmt = connexion::pdo()->prepare($sql);
        $stmt->execute([':num_en' => $num_en]);
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    /**
     * Remplace la liste des responsabilités d'un enseignant par une nouvelle liste
     * @param int $num_en
     * @param array $num_rs
     * @return bool
     */
    public static function setResponsabilitesForEnseignant(int $num_en, array $num_rs): bool {
        try {
            $pdo = connexion::pdo();
            $pdo->beginTransaction();

            // delete existing
            $del = $pdo->prepare("DELETE FROM ENSEIGNANT_RESPONSABILITE WHERE num_en = :num_en");
            $del->execute([':num_en' => $num_en]);

            if (!empty($num_rs)) {
                $ins = $pdo->prepare("INSERT INTO ENSEIGNANT_RESPONSABILITE (num_en, num_r) VALUES (:num_en, :num_r)");
                foreach ($num_rs as $num_r) {
                    $ins->execute([':num_en' => $num_en, ':num_r' => (int)$num_r]);
                }
            }

            $pdo->commit();
            return true;
        } catch (PDOException $e) {
            if ($pdo->inTransaction()) $pdo->rollBack();
            error_log("Erreur assignation responsabilités: " . $e->getMessage());
            return false;
        }
    }
}
?>
