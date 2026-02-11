<?php
class Groupe {
    public ?int $num;
    public ?string $nom;
    public ?int $nbEtudiantMax;
    public ?int $nbEtudiant;
    public ?bool $estFinalise;
    public ?Promotion $promotion;

    public function __construct(
        int $num = NULL,
        string $nom = NULL,
        int $nbEtudiantMax = NULL,
        int $nbEtudiant = NULL,
        bool $estFinalise = NULL,
        Promotion $promotion = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->nom = $nom;
            $this->nbEtudiantMax = $nbEtudiantMax;
            $this->nbEtudiant = $nbEtudiant;
            $this->estFinalise = $estFinalise;
            $this->promotion = $promotion;
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
     * Récupère tous les groupes depuis vueListeGroupe
     * @return array Tableau de tous les groupes
     */
    public static function getListeGroupes() {
        $requete = "SELECT * FROM vueListeGroupe";
        $resultat = connexion::pdo()->query($requete);
        $groupes = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $groupes;
    }

    /**
     * Récupère les groupes non finalisés depuis vueGroupeNonFinalise
     * @return array Tableau des groupes avec places restantes
     */
    public static function getGroupesNonFinalises() {
        $requete = "SELECT * FROM vueGroupeNonFinalise";
        $resultat = connexion::pdo()->query($requete);
        $groupes = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $groupes;
    }

    /**
     * Récupère les groupes pour les comptes étudiants
     * @return array Tableau des groupes pour les comptes étudiants
     */
    public static function getGroupesPourEtudiant() {
        $requete = "SELECT nom_g, intitule_a FROM GROUPE INNER JOIN PROMOTION ON GROUPE.num_p = PROMOTION.num_p INNER JOIN ANNEE ON PROMOTION.num_a = ANNEE.num_a ";
        $resultat = connexion::pdo()->query($requete);
        $groupes = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $groupes;
    }

    /**
     * Récupère un groupe spécifique par son numéro avec infos complètes
     * @param int $numGroupe
     * @return array|null
     */
    public static function getGroupeById($numGroupe) {
        $requete = "SELECT * FROM vueListeGroupe WHERE num_g = :numG";
        $stmt = connexion::pdo()->prepare($requete);
        $stmt->execute([':numG' => $numGroupe]);
        $groupe = $stmt->fetch(PDO::FETCH_ASSOC);
        return $groupe ?: null;
    }

    // --- CRUD pour les groupes ---
    private static ?string $lastError = null;

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    /**
 * Crée un groupe et retourne l'id (num_g) ou false
 */
public static function create(string $nom, int $num_p, ?int $nbMax = null, bool $estFinalise = false) {
    try {
        error_log("=== GROUPE CREATE DEBUG ===");
        error_log("Params: nom='$nom', num_p=$num_p, nbMax=$nbMax, estFinalise=" . ($estFinalise ? 'true' : 'false'));
        
        $pdo = connexion::pdo();
        
        // Vérifier que la promotion existe
        $checkPromo = $pdo->prepare("SELECT num_p FROM PROMOTION WHERE num_p = :num_p");
        $checkPromo->execute([':num_p' => $num_p]);
        if (!$checkPromo->fetch()) {
            self::$lastError = "La promotion num_p=$num_p n'existe pas";
            error_log("ERREUR: " . self::$lastError);
            return false;
        }
        error_log("Promotion $num_p existe - OK");
        
        $stmt = $pdo->prepare("INSERT INTO GROUPE (nom_g, num_p, nombre_etudiant_max_g, nombre_etudiant_g, est_finalise_g) VALUES (:nom, :num_p, :nbMax, :nbEtud, :est)");
        
        $params = [
            ':nom' => $nom, 
            ':num_p' => $num_p, 
            ':nbMax' => $nbMax, 
            ':nbEtud' => 0, 
            ':est' => $estFinalise ? 1 : 0  // CORRECTION ICI
        ];
        
        error_log("SQL params: " . print_r($params, true));
        
        $res = $stmt->execute($params);
        
        if ($res) {
            $id = (int)$pdo->lastInsertId();
            error_log("SUCCESS: Groupe créé avec id=$id");
            return $id;
        }
        
        $err = $stmt->errorInfo();
        self::$lastError = isset($err[2]) ? $err[2] : "Erreur création groupe";
        error_log("ERREUR SQL: " . self::$lastError);
        error_log("errorInfo: " . print_r($err, true));
        return false;
        
    } catch (PDOException $e) {
        self::$lastError = $e->getMessage();
        error_log("EXCEPTION PDO: " . $e->getMessage());
        error_log("Stack trace: " . $e->getTraceAsString());
        return false;
    }
}

    /**
     * Met à jour un groupe, retourne le nombre de lignes affectées
     */
    public static function update(int $num_g, array $data) {
        try {
            $fields = [];
            $params = [':num_g' => $num_g];

            if (isset($data['nom_g'])) { $fields[] = 'nom_g = :nom_g'; $params[':nom_g'] = $data['nom_g']; }
            if (isset($data['num_p'])) { $fields[] = 'num_p = :num_p'; $params[':num_p'] = (int)$data['num_p']; }
            if (isset($data['nombre_etudiant_max_g'])) { $fields[] = 'nombre_etudiant_max_g = :nbMax'; $params[':nbMax'] = (int)$data['nombre_etudiant_max_g']; }
            if (isset($data['est_finalise_g'])) { $fields[] = 'est_finalise_g = :est'; $params[':est'] = $data['est_finalise_g'] ? 1 : 0; }

            if (empty($fields)) return 0;

            $sql = "UPDATE GROUPE SET " . implode(', ', $fields) . " WHERE num_g = :num_g";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute($params);
            return $stmt->rowCount();
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur update Groupe: " . $e->getMessage());
            return 0;
        }
    }

    /**
     * Supprime un groupe, retourne le nombre de lignes supprimées
     */
    public static function delete(int $num_g) {
        try {
            $stmt = connexion::pdo()->prepare("DELETE FROM GROUPE WHERE num_g = :num_g");
            $stmt->execute([':num_g' => $num_g]);
            return $stmt->rowCount();
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur delete Groupe: " . $e->getMessage());
            return 0;
        }
    }
}
?>
