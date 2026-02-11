<?php
class Promotion {
    public ?int $num;
    public ?int $nbEtudiantCovoiturageMax; // payload: nombre_etudiant_covoiturage_max_p
    public ?int $nbGroupeMax;
    public ?int $nbGroupeTotal;
    public ?int $nbEtudiantMax;
    public ?Annee $annee;

    public function __construct(
        int $num = NULL,
        int $nbEtudiantCovoiturageMax = NULL,
        int $nbGroupeMax = NULL,
        int $nbGroupeTotal = NULL,
        int $nbEtudiantMax = NULL,
        Annee $annee = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->nbEtudiantCovoiturageMax = $nbEtudiantCovoiturageMax;
            $this->nbGroupeMax = $nbGroupeMax;
            $this->nbGroupeTotal = $nbGroupeTotal;
            $this->nbEtudiantMax = $nbEtudiantMax;
            $this->annee = $annee;
        }
    }

    // --- CRUD pour promotions (méthodes basiques avec capture d'erreur) ---
    private static ?string $lastError = null;

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    /**
     * Crée une promotion (seuls les champs "max" sont pris en compte)
     */
    public static function create(?int $nbEtudiantCovoiturageMax = null, ?int $nbGroupeMax = null, ?int $nbGroupeTotal = null, ?int $nbEtudiantMax = null, ?int $num_a = null) {
        try {
            $pdo = connexion::pdo();
            // Le nom de colonne DB attendu pour le champ covo est 'nombre_etudiant_max_covoiturage_p'
            $stmt = $pdo->prepare(
                "INSERT INTO PROMOTION (nombre_etudiant_max_covoiturage_p, nombre_de_groupe_max_p, nombre_de_groupe_total_p, nombre_etudiant_max_p, num_a) VALUES (:nbECovo, :nbGMax, :nbGTotal, :nbEMax, :numA)"
            );
            $res = $stmt->execute([
                ':nbECovo' => $nbEtudiantCovoiturageMax,
                ':nbGMax' => $nbGroupeMax,
                ':nbGTotal' => $nbGroupeTotal,
                ':nbEMax' => $nbEtudiantMax,
                ':numA' => $num_a
            ]);
            if ($res) return (int)$pdo->lastInsertId();
            $err = $stmt->errorInfo();
            self::$lastError = isset($err[2]) ? $err[2] : "Erreur création promotion";
            error_log("Promotion create failed: " . self::$lastError);
            return false;
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur création Promotion: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Récupère toutes les promotions
     */
    public static function getAll() {
        $sql = "SELECT p.*, a.intitule_a FROM PROMOTION p LEFT JOIN ANNEE a USING (num_a)";
        $stmt = connexion::pdo()->query($sql);
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    public static function getById(int $num_p) {
        $sql = "SELECT p.*, a.intitule_a FROM PROMOTION p LEFT JOIN ANNEE a USING (num_a) WHERE p.num_p = :num_p";
        $stmt = connexion::pdo()->prepare($sql);
        $stmt->execute([':num_p' => $num_p]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        return $row ?: null;
    }

    public static function update(int $num_p, array $data) {
        try {
            $fields = [];
            $params = [':num_p' => $num_p];

            if (isset($data['nombre_etudiant_covoiturage_max_p'])) { $fields[] = 'nombre_etudiant_max_covoiturage_p = :nbECovo'; $params[':nbECovo'] = (int)$data['nombre_etudiant_covoiturage_max_p']; }
            // Accept legacy key name if present
            if (isset($data['nombre_etudiant_max_covoiturage_p'])) { $fields[] = 'nombre_etudiant_max_covoiturage_p = :nbECovo'; $params[':nbECovo'] = (int)$data['nombre_etudiant_max_covoiturage_p']; }
            if (isset($data['nombre_groupe_max_p'])) { $fields[] = 'nombre_de_groupe_max_p = :nbGMax'; $params[':nbGMax'] = (int)$data['nombre_groupe_max_p']; }
            if (isset($data['nombre_groupe_total_p'])) { $fields[] = 'nombre_de_groupe_total_p = :nbGTotal'; $params[':nbGTotal'] = (int)$data['nombre_groupe_total_p']; }
            if (isset($data['nombre_etudiant_max_p'])) { $fields[] = 'nombre_etudiant_max_p = :nbEMax'; $params[':nbEMax'] = (int)$data['nombre_etudiant_max_p']; }
            if (isset($data['num_a'])) { $fields[] = 'num_a = :numA'; $params[':numA'] = (int)$data['num_a']; }

            if (empty($fields)) return 0;

            $sql = "UPDATE PROMOTION SET " . implode(', ', $fields) . " WHERE num_p = :num_p";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute($params);
            return $stmt->rowCount();
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur update Promotion: " . $e->getMessage());
            return 0;
        }
    }

    public static function delete(int $num_p) {
        try {
            $stmt = connexion::pdo()->prepare("DELETE FROM PROMOTION WHERE num_p = :num_p");
            $stmt->execute([':num_p' => $num_p]);
            return $stmt->rowCount();
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur delete Promotion: " . $e->getMessage());
            return 0;
        }
    }
}
?>
