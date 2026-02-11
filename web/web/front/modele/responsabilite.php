<?php
class Responsabilite {
    public ?int $num;
    public ?string $intitule;

    public function __construct(
        int $num = NULL,
        string $intitule = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->intitule = $intitule;
        }
    }

    /**
     * Récupère toutes les responsabilités
     * @return array
     */
    public static function getAll(): array {
        $stmt = connexion::pdo()->query("SELECT * FROM RESPONSABILITE ORDER BY intitule_r ASC");
        return $stmt->fetchAll(PDO::FETCH_ASSOC);
    }

    /**
     * Crée une responsabilité
     * @param string $intitule
     * @return int|false retourne le num_r inséré ou false
     */
    public static function create(string $intitule) {
        try {
            $pdo = connexion::pdo();
            $stmt = $pdo->prepare("INSERT INTO RESPONSABILITE (intitule_r) VALUES (:intitule)");
            $stmt->execute([':intitule' => $intitule]);
            return (int)$pdo->lastInsertId();
        } catch (PDOException $e) {
            error_log("Erreur création responsabilité: " . $e->getMessage());
            return false;
        }
    }
}
?>
