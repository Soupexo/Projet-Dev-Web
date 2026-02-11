<?php
class Note {
    // Stocke le dernier message d'erreur SQL/exception pour debug (temporaire)
    private static ?string $lastError = null;

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    /**
     * Récupère une note par son intitulé
     * @return array|false Ligne associative ou false
     */
    public static function getByIntitule(string $intitule) {
        try {
            $pdo = connexion::pdo();
            $stmt = $pdo->prepare("SELECT * FROM NOTE WHERE intitule_n = :intitule");
            $stmt->execute([':intitule' => $intitule]);
            return $stmt->fetch(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur getByIntitule Note: " . $e->getMessage());
            return false;
        }
    }

    /**
     * Crée une note et retourne son identifiant (num_n) ou false
     */
    public static function create(string $intitule) {
        try {
            $pdo = connexion::pdo();
            $stmt = $pdo->prepare("INSERT INTO NOTE (intitule_n) VALUES (:intitule)");
            $res = $stmt->execute([':intitule' => $intitule]);
            if ($res) return (int)$pdo->lastInsertId();
            $err = $stmt->errorInfo();
            self::$lastError = isset($err[2]) ? $err[2] : "Erreur création note";
            error_log("Note create failed: " . self::$lastError);
            return false;
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur création Note: " . $e->getMessage());
            return false;
        }
    }
}
?>