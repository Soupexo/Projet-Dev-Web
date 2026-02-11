<?php
class Sondage {
    public ?int $num;
    public ?string $nom;
    public ?string $type;
    public ?bool $reponsesMultiples;
    public ?DateTime $delai;

    private static ?string $lastError = null;

    public static function getLastError() {
        return self::$lastError;
    }

    public static function getAll() {
        $sql = "SELECT * FROM SONDAGE";
        return connexion::pdo()->query($sql)->fetchAll(PDO::FETCH_ASSOC);
    }

    public static function getById(int $num) {
        $sql = "SELECT * FROM SONDAGE WHERE num_s = ?";
        $stmt = connexion::pdo()->prepare($sql);
        $stmt->execute([$num]);
        return $stmt->fetch(PDO::FETCH_ASSOC);
    }

    public static function getSondagesCourants() {
        $sql = "SELECT * FROM vueSondageCourant";
        return connexion::pdo()->query($sql)->fetchAll(PDO::FETCH_ASSOC);
    }

    public static function create($nom, $type, $repMultiples, $delai) {
        try {
            $sql = "
                INSERT INTO SONDAGE (nom_s, type_s, a_des_reponses_multiples_s, delai_s)
                VALUES (?, ?, ?, ?)
            ";
            $stmt = connexion::pdo()->prepare($sql);
            $stmt->execute([
                $nom,
                $type,
                $repMultiples ? 1 : 0,
                $delai
            ]);
            return connexion::pdo()->lastInsertId();
        } catch (Exception $e) {
            self::$lastError = $e->getMessage();
            return false;
        }
    }

    public static function update(int $num, array $data) {
        $fields = [];
        $values = [];

        foreach ($data as $k => $v) {
            // Mapping des champs API vers les colonnes BDD
            if ($k === 'a_des_reponses_multiples_s') {
                $fields[] = "a_des_reponses_multiples_s = ?";
            } else {
                $fields[] = "$k = ?";
            }
            $values[] = $v;
        }

        if (empty($fields)) return 0;

        $sql = "UPDATE SONDAGE SET " . implode(', ', $fields) . " WHERE num_s = ?";
        $values[] = $num;

        $stmt = connexion::pdo()->prepare($sql);
        $stmt->execute($values);
        return $stmt->rowCount();
    }

    public static function delete(int $num) {
        $sql = "DELETE FROM SONDAGE WHERE num_s = ?";
        $stmt = connexion::pdo()->prepare($sql);
        return $stmt->execute([$num]);
    }
}
