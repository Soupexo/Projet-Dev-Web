<?php
class EnseignantGroupe {
    public ?Enseignant $enseignant;
    public ?Groupe $groupe;
    public ?DateTime $dateConstitution;
    private static ?string $lastError = null;

    public function __construct(
        Enseignant $enseignant = NULL,
        Groupe $groupe = NULL,
        DateTime $dateConstitution = NULL
    ) {
        if ($enseignant != NULL && $groupe != NULL) {
            $this->enseignant = $enseignant;
            $this->groupe = $groupe;
            $this->dateConstitution = $dateConstitution;
        }
    }

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    public static function fromNameToId(string $nom): int|false {
        $sql = "SELECT num_g FROM GROUPE WHERE nom_g = :nom";

        try {
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([':nom' => $nom]);
            $id = $stmt->fetchColumn();
            if ($id === false) {
                return false;
            }
            return (int)$id;
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur EnseignantGroupe::fromNameToId: " . $e->getMessage());
            return false;
        }
    }

    public static function saveCommentaire(int $num_en, int $num_g, string $commentaire, ?string $date_constitution_groupe = null): bool {
        try {
            $pdo = connexion::pdo();
            $date_constitution_groupe = ($date_constitution_groupe !== null && trim($date_constitution_groupe) !== '')
                ? trim($date_constitution_groupe)
                : null;

            if ($date_constitution_groupe !== null) {
                $sql = "UPDATE ENSEIGNANT_GROUPE
                        SET commentaire = :commentaire,
                            date_constitution_groupe = :date
                        WHERE num_en = :num_en AND num_g = :num_g";
                $params = [
                    ':commentaire' => $commentaire,
                    ':date' => $date_constitution_groupe,
                    ':num_en' => $num_en,
                    ':num_g' => $num_g
                ];
            } else {
                $sql = "UPDATE ENSEIGNANT_GROUPE
                        SET commentaire = :commentaire
                        WHERE num_en = :num_en AND num_g = :num_g";
                $params = [
                    ':commentaire' => $commentaire,
                    ':num_en' => $num_en,
                    ':num_g' => $num_g
                ];
            }

            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);

            if ($stmt->rowCount() > 0) {
                return true;
            }

            $check = $pdo->prepare("SELECT COUNT(*) FROM ENSEIGNANT_GROUPE WHERE num_en = :num_en AND num_g = :num_g");
            $check->execute([':num_en' => $num_en, ':num_g' => $num_g]);
            $exists = (int)$check->fetchColumn();
            if ($exists > 0) {
                return true;
            }

            $dateToUse = $date_constitution_groupe ?? date('Y-m-d');
            $ins = $pdo->prepare(
                "INSERT INTO ENSEIGNANT_GROUPE (num_en, num_g, date_constitution_groupe, commentaire)
                 VALUES (:num_en, :num_g, :date, :commentaire)"
            );
            $ins->execute([
                ':num_en' => $num_en,
                ':num_g' => $num_g,
                ':date' => $dateToUse,
                ':commentaire' => $commentaire
            ]);

            return true;
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur EnseignantGroupe::saveCommentaire: " . $e->getMessage());
            return false;
        }
    }
}
?>
