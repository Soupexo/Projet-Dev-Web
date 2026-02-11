<?php
class Etudiant {
    public ?int $num;
    public ?bool $estApprenti;
    public ?string $typeBac;
    public ?string $genre;
    public ?Groupe $groupe;
    public ?Responsabilite $responsabilite;
    public ?Utilisateur $utilisateur;

    // Stocke le dernier message d'erreur SQL/exception pour debug (temporaire)
    private static ?string $lastError = null;

    public static function getLastError(): ?string {
        return self::$lastError;
    }

    public static function getNumByLogin(string $login): int|false {
        try {
            $stmt = connexion::pdo()->prepare("SELECT num_e FROM ETUDIANT WHERE login_u = :login LIMIT 1");
            $stmt->execute([':login' => $login]);
            $num = $stmt->fetchColumn();
            if ($num === false) {
                return false;
            }
            return (int)$num;
        } catch (PDOException $e) {
            self::$lastError = $e->getMessage();
            error_log("Erreur Etudiant::getNumByLogin: " . $e->getMessage());
            return false;
        }
    }
    

    public function __construct(
        int $num = NULL,
        bool $estApprenti = NULL,
        string $typeBac = NULL,
        ?string $genre = NULL,
        ?Groupe $groupe = NULL,
        Responsabilite $responsabilite = NULL,
        Utilisateur $utilisateur = NULL
    ) {
        if ($num != NULL)
            {
            $this->num = $num;
            $this->estApprenti = $estApprenti;
            $this->typeBac = $typeBac;
            $this->genre = $genre;
            $this->groupe = $groupe;
            $this->responsabilite = $responsabilite;
            $this->utilisateur = $utilisateur;
            }
    }

    /**
     * Récupère les infos complètes des étudiants depuis vueInfoEtudiant
     * @return array Tableau d'objets stdClass avec toutes les infos
     */
    public static function getInfosEtudiants() {
        $requete = "SELECT * FROM vueInfoEtudiant";
        $resultat = connexion::pdo()->query($requete);
        $etudiants = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $etudiants;
    }

    /**
     * Récupère les infos complètes des étudiants (affectés et non affectés)
     * @return array Tableau avec tous les étudiants
     */
    public static function getEtudiants() {
        $requete = "SELECT 
                UTILISATEUR.login_u, 
                UTILISATEUR.nom_u, 
                UTILISATEUR.prenom_u, 
                UTILISATEUR.mail_u, 
                ETUDIANT.est_apprenti_e, 
                ETUDIANT.type_bac_e, 
                ETUDIANT.genre_e, 
                GROUPE.nom_g
            FROM ETUDIANT 
            LEFT JOIN UTILISATEUR ON ETUDIANT.login_u = UTILISATEUR.login_u 
            LEFT JOIN GROUPE ON ETUDIANT.num_g = GROUPE.num_g";
        $resultat = connexion::pdo()->query($requete);
        $etudiants = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $etudiants;
    }

    /**
     * Récupère les infos complètes des étudiants (affectés et non affectés)
     * @return array Tableau avec tous les étudiants
     */
    public static function getEtudiantsComplets() {
        $requete = "SELECT 
                UTILISATEUR.login_u, 
                UTILISATEUR.nom_u, 
                UTILISATEUR.prenom_u, 
                UTILISATEUR.mail_u, 
                ETUDIANT.est_apprenti_e, 
                ETUDIANT.type_bac_e, 
                ETUDIANT.genre_e, 
                GROUPE.nom_g,
                A.intitule_a
            FROM ETUDIANT 
            JOIN UTILISATEUR ON ETUDIANT.login_u = UTILISATEUR.login_u 
            JOIN GROUPE ON ETUDIANT.num_g = GROUPE.num_g
            JOIN REDOUBLEMENT R ON R.num_e = ETUDIANT.num_e
            JOIN ANNEE A ON A.num_a = R.num_a";
        $resultat = connexion::pdo()->query($requete);
        $etudiants = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $etudiants;
    }


    /**
     * Récupère les notes d'un étudiant depuis vueNoteEtudiant
     * @param string $login Login de l'étudiant
     * @return array Tableau des notes
     */
    public static function getNotesEtudiant() {
        $requete = "SELECT * FROM vueNoteEtudiant";
        $resultat = connexion::pdo()->query($requete);
        $notes = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $notes;
    }

    /**
     * Crée ou met à jour la note d'un étudiant pour une matière.
     * Si l'intitulé de la matière n'existe pas dans la table NOTE, il est créé.
     * Accepte soit une note annuelle (`moy_annee`), soit des moyennes par semestre
     * (`moy_prem_semestre` et/ou `moy_deux_semestre`). Si les deux semestres sont fournis,
     * la colonne `moy_annee` est calculée automatiquement comme la moyenne des deux.
     * @param string $login Login de l'étudiant
     * @param string $matiere Intitulé de la matière
     * @param float|null $moyAnnee Note annuelle (optionnelle)
     * @param float|null $moyPremiere Moyenne premier semestre (optionnelle)
     * @param float|null $moyDeuxieme Moyenne second semestre (optionnelle)
     * @param int $coef Coefficient (non persisté pour l'instant)
     * @return bool True si succès, false sinon (getLastError() pour détails)
     */
    public static function upsertNote(string $login, string $matiere, ?float $moyAnnee = null, ?float $moyPremiere = null, ?float $moyDeuxieme = null, int $coef = 1): bool {
        $pdo = connexion::pdo();
        try {
            $pdo->beginTransaction();

            // Récupérer num_e
            $stmt = $pdo->prepare("SELECT num_e FROM ETUDIANT WHERE login_u = :login");
            $stmt->execute([':login' => $login]);
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if (!$row) {
                self::$lastError = "Étudiant introuvable (login: $login)";
                if ($pdo->inTransaction()) $pdo->rollBack();
                return false;
            }
            $num_e = (int)$row['num_e'];

            // Récupérer ou créer la note (TABLE NOTE)
            require_once __DIR__ . '/note.php';
            $noteRow = Note::getByIntitule($matiere);
            if ($noteRow && isset($noteRow['num_n'])) {
                $num_n = (int)$noteRow['num_n'];
            } else {
                $newId = Note::create($matiere);
                if ($newId === false) {
                    self::$lastError = Note::getLastError() ?? "Échec création matière";
                    if ($pdo->inTransaction()) $pdo->rollBack();
                    return false;
                }
                $num_n = (int)$newId;
            }

            // Normaliser les valeurs : null ou float
            $moy_prem = is_numeric($moyPremiere) ? (float)$moyPremiere : null;
            $moy_deux = is_numeric($moyDeuxieme) ? (float)$moyDeuxieme : null;
            $moy_ann = is_numeric($moyAnnee) ? (float)$moyAnnee : null;

            // Si les deux semestres fournis, calculer la moyenne annuelle automatique
            if ($moy_prem !== null && $moy_deux !== null) {
                $moy_ann = round(($moy_prem + $moy_deux) / 2, 2);
            }

            // Vérifier si ligne ETUDIANT_NOTE existe
            $stmt = $pdo->prepare("SELECT 1 FROM ETUDIANT_NOTE WHERE num_e = :num_e AND num_n = :num_n");
            $stmt->execute([':num_e' => $num_e, ':num_n' => $num_n]);

            if ($stmt->fetch()) {
                // UPDATE : écrire les 3 colonnes (peuvent être NULL)
                $stmt2 = $pdo->prepare("UPDATE ETUDIANT_NOTE
                                        SET moy_prem_semestre = :moy_prem,
                                            moy_deux_semestre = :moy_deux,
                                            moy_annee = :moy_ann
                                        WHERE num_e = :num_e AND num_n = :num_n");
                $stmt2->execute([
                    ':moy_prem' => $moy_prem,
                    ':moy_deux' => $moy_deux,
                    ':moy_ann' => $moy_ann,
                    ':num_e' => $num_e,
                    ':num_n' => $num_n
                ]);
            } else {
                // INSERT
                $stmt2 = $pdo->prepare("INSERT INTO ETUDIANT_NOTE (num_e, num_n, moy_prem_semestre, moy_deux_semestre, moy_annee) VALUES (:num_e, :num_n, :moy_prem, :moy_deux, :moy_ann)");
                $stmt2->execute([
                    ':num_e' => $num_e,
                    ':num_n' => $num_n,
                    ':moy_prem' => $moy_prem,
                    ':moy_deux' => $moy_deux,
                    ':moy_ann' => $moy_ann
                ]);
            }

            $pdo->commit();
            return true;
        } catch (PDOException $e) {
            if ($pdo->inTransaction()) $pdo->rollBack();
            $msg = "Erreur upsert note: " . $e->getMessage();
            error_log($msg);
            self::$lastError = $e->getMessage();
            return false;
        }
    }

    /**
     * Supprime la note d'un étudiant pour une matière (ligne ETUDIANT_NOTE)
     * @return bool True si supprimé ou ligne inexistante; false si erreur
     */
    public static function deleteNote(string $login, string $matiere): bool {
        $pdo = connexion::pdo();
        try {
            $pdo->beginTransaction();

            $stmt = $pdo->prepare("SELECT num_e FROM ETUDIANT WHERE login_u = :login");
            $stmt->execute([':login' => $login]);
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            if (!$row) {
                self::$lastError = "Étudiant introuvable (login: $login)";
                if ($pdo->inTransaction()) $pdo->rollBack();
                return false;
            }
            $num_e = (int)$row['num_e'];

            // Récupérer num_n
            require_once __DIR__ . '/note.php';
            $noteRow = Note::getByIntitule($matiere);
            if (!$noteRow || !isset($noteRow['num_n'])) {
                // Rien à supprimer
                if ($pdo->inTransaction()) $pdo->commit();
                return true;
            }
            $num_n = (int)$noteRow['num_n'];

            $stmtDel = $pdo->prepare("DELETE FROM ETUDIANT_NOTE WHERE num_e = :num_e AND num_n = :num_n");
            $stmtDel->execute([':num_e' => $num_e, ':num_n' => $num_n]);

            $pdo->commit();
            return true;
        } catch (PDOException $e) {
            if ($pdo->inTransaction()) $pdo->rollBack();
            $msg = "Erreur suppression note: " . $e->getMessage();
            error_log($msg);
            self::$lastError = $e->getMessage();
            return false;
        }
    }

    /**
     * Récupère tous les étudiants non affectés depuis vueEtudiantNonAffecte
     * @return array Tableau d'étudiants non affectés
     */
    public static function getEtudiantsNonAffectes() {
        $requete = "SELECT * FROM vueEtudiantNonAffecte";
        $resultat = connexion::pdo()->query($requete);
        $etudiants = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $etudiants;
    }

    /**
     * Récupère les critères disciplinaires depuis vueCritereDisciplinaire
     * @return array
     */
    public static function getCriteresDisciplinaires() {
        $requete = "SELECT * FROM vueCritereDisciplinaire";
        $resultat = connexion::pdo()->query($requete);
        $criteres = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $criteres;
    }

    /**
     * Récupère les groupes de covoiturage depuis vueGroupeCovoiturage
     * @return array
     */
    public static function getGroupesCovoiturage() {
        $requete = "SELECT * FROM vueGroupeCovoiturage";
        $resultat = connexion::pdo()->query($requete);
        $groupes = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $groupes;
    }

    /**
     * Récupère les réponses au sondage depuis vueReponseSondage
     * @return array
     */
    public static function getReponsesSondage() {
        $requete = "SELECT * FROM vueReponseSondage";
        $resultat = connexion::pdo()->query($requete);
        $reponses = $resultat->fetchAll(PDO::FETCH_ASSOC);
        return $reponses;
    }

    /**
     * CREATE : crée l'utilisateur puis l'étudiant
     */
    public static function create(string $login, string $mdp, string $nom, string $prenom, string $mail, bool $estApprenti = false, ?string $typeBac = null, ?string $genre = null): bool {
        $pdo = connexion::pdo();
        try {
            $pdo->beginTransaction();
            $userCreated = Utilisateur::create($login, $mdp, $nom, $prenom, $mail);
            if (!$userCreated) {
                $pdo->rollBack();
                return false;
            }

            $sql = "INSERT INTO ETUDIANT (login_u, est_apprenti_e, type_bac_e, genre_e) VALUES (:login, :est_apprenti, :type_bac, :genre)";
            $stmt = $pdo->prepare($sql);
            $result = $stmt->execute([
                ':login' => $login,
                ':est_apprenti' => $estApprenti ? 1 : 0,
                ':type_bac' => $typeBac,
                ':genre' => $genre
            ]);

            if ($result !== true) {
                $err = $stmt->errorInfo();
                $msg = "Etudiant insert failed: SQLSTATE=" . ($err[0] ?? '') . ", code=" . ($err[1] ?? '') . ", message=" . ($err[2] ?? '');
                error_log($msg);
                // Stocker le message pour debug via API
                self::$lastError = $msg;
                if ($pdo->inTransaction()) $pdo->rollBack();
                return false;
            }

            $pdo->commit();
            // Retourner true si l'exécution a réussi
            return true;
        } catch (PDOException $e) {
            if ($pdo->inTransaction()) $pdo->rollBack();
            $msg = "Erreur création étudiant: " . $e->getMessage();
            error_log($msg);
            // Stocker pour debug
            self::$lastError = $e->getMessage();
            return false;
        }
    }

    /**
     * UPDATE : met à jour utilisateur et étudiant
     */
    public static function update(string $login, array $data): int {
        try {
            $pdo = connexion::pdo();
            $pdo->beginTransaction();

            echo "DEBUG PHP UPDATE ETUDIANT: Début update pour login='$login'\n";
            echo "DEBUG PHP UPDATE ETUDIANT: data reçue=" . json_encode($data) . "\n";

            $rowsUser = 0;
            if (isset($data['nom']) || isset($data['prenom']) || isset($data['mail'])) {
                echo "DEBUG PHP UPDATE ETUDIANT: Mise à jour UTILISATEUR\n";
                $rowsUser = Utilisateur::update($login, [
                    'nom' => $data['nom'] ?? '',
                    'prenom' => $data['prenom'] ?? '',
                    'email' => $data['mail'] ?? ''
                ]);
                echo "DEBUG PHP UPDATE ETUDIANT: lignes UTILISATEUR modifiées=$rowsUser\n";
            }

            $sql = "UPDATE ETUDIANT
                    SET est_apprenti_e = :est_apprenti,
                        type_bac_e = :type_bac,
                        genre_e = :genre
                    WHERE login_u = :login";
            $stmt = $pdo->prepare($sql);
            
            $estApprenti = isset($data['est_apprenti_e']) && $data['est_apprenti_e'] ? 1 : 0;
            $typeBac = $data['type_bac_e'] ?? null;
            $genre = $data['genre_e'] ?? null;
            
            echo "DEBUG PHP UPDATE ETUDIANT: est_apprenti=$estApprenti, type_bac='$typeBac', genre='$genre'\n";
            
            $stmt->execute([
                ':login' => $login,
                ':est_apprenti' => $estApprenti,
                ':type_bac' => $typeBac,
                ':genre' => $genre
            ]);
            $rowsEtudiant = $stmt->rowCount();
            
            echo "DEBUG PHP UPDATE ETUDIANT: lignes ETUDIANT modifiées=$rowsEtudiant\n";

            $pdo->commit();
            $total = ($rowsUser ?? 0) + $rowsEtudiant;
            echo "DEBUG PHP UPDATE ETUDIANT: Transaction commitée, total=$total\n";
            return $total;
        } catch (PDOException $e) {
            if (connexion::pdo()->inTransaction()) connexion::pdo()->rollBack();
            echo "DEBUG PHP UPDATE ETUDIANT: Exception - " . $e->getMessage() . "\n";
            return 0;
        }
    }

    public static function delete(string $login): int {
    try {
        $pdo = connexion::pdo();
        $pdo->beginTransaction();

        echo "DEBUG DELETE: Début suppression pour login='$login'\n";

        // Vérifier si l'étudiant existe
        $check = $pdo->prepare("SELECT login_u FROM ETUDIANT WHERE login_u = :login");
        $check->execute([':login' => $login]);
        $exists = $check->fetch();
        
        echo "DEBUG DELETE: Existence vérifiée=" . ($exists ? 'OUI' : 'NON') . "\n";
        
        if (!$exists) {
            echo "DEBUG DELETE: Étudiant non trouvé, rollback\n";
            $pdo->rollBack();
            return 0;
        }

        // Récupérer le num_e de l'étudiant
        $getNum = $pdo->prepare("SELECT num_e FROM ETUDIANT WHERE login_u = :login");
        $getNum->execute([':login' => $login]);
        $numE = $getNum->fetchColumn();
        echo "DEBUG DELETE: num_e = $numE\n";

        // Supprimer les enregistrements dans COVOITURAGE (les deux colonnes)
        $covoitStmt = $pdo->prepare("DELETE FROM COVOITURAGE WHERE num_e = :num_e OR num_e_1 = :num_e");
        $covoitResult = $covoitStmt->execute([':num_e' => $numE]);
        $covoitRows = $covoitStmt->rowCount();
        echo "DEBUG DELETE: Lignes supprimées COVOITURAGE = $covoitRows\n";

        // Supprimer les enregistrements dans ETUDIANT_NOTE
        $noteStmt = $pdo->prepare("DELETE FROM ETUDIANT_NOTE WHERE num_e = :num_e");
        $noteResult = $noteStmt->execute([':num_e' => $numE]);
        $noteRows = $noteStmt->rowCount();
        echo "DEBUG DELETE: Lignes supprimées ETUDIANT_NOTE = $noteRows\n";

        // Supprimer les enregistrements dans ETUDIANT_SEPARATION (les deux colonnes)
        $sepStmt = $pdo->prepare("DELETE FROM ETUDIANT_SEPARATION WHERE num_e = :num_e OR num_e_1 = :num_e");
        $sepResult = $sepStmt->execute([':num_e' => $numE]);
        $sepRows = $sepStmt->rowCount();
        echo "DEBUG DELETE: Lignes supprimées ETUDIANT_SEPARATION = $sepRows\n";

        // Supprimer les enregistrements dans ETUDIANT_SONDAGE
        $sondageStmt = $pdo->prepare("DELETE FROM ETUDIANT_SONDAGE WHERE num_e = :num_e");
        $sondageResult = $sondageStmt->execute([':num_e' => $numE]);
        $sondageRows = $sondageStmt->rowCount();
        echo "DEBUG DELETE: Lignes supprimées ETUDIANT_SONDAGE = $sondageRows\n";

        // Supprimer les enregistrements dans REDOUBLEMENT
        $redoublementStmt = $pdo->prepare("DELETE FROM REDOUBLEMENT WHERE num_e = :num_e");
        $redoublementResult = $redoublementStmt->execute([':num_e' => $numE]);
        $redoublementRows = $redoublementStmt->rowCount();
        echo "DEBUG DELETE: Lignes supprimées REDOUBLEMENT = $redoublementRows\n";

        // Supprimer l'étudiant
        $stmt = $pdo->prepare("DELETE FROM ETUDIANT WHERE login_u = :login");
        $result = $stmt->execute([':login' => $login]);
        $rowsEt = $stmt->rowCount();
        
        echo "DEBUG DELETE: Suppression ETUDIANT result=" . ($result ? 'true' : 'false') . ", rows=$rowsEt\n";
        
        if ($rowsEt === 0) {
            echo "DEBUG DELETE: Aucune ligne supprimée, rollback\n";
            $pdo->rollBack();
            return 0;
        }

        // Supprimer l'utilisateur associé
        $rowsUser = Utilisateur::delete($login);
        echo "DEBUG DELETE: Suppression UTILISATEUR rows=$rowsUser\n";

        $pdo->commit();
        $total = $rowsEt + $rowsUser + $covoitRows + $noteRows + $sepRows + $sondageRows + $redoublementRows;
        echo "DEBUG DELETE: Transaction commitée, total=$total\n";
        return $total;
    } catch (PDOException $e) {
        if (connexion::pdo()->inTransaction()) connexion::pdo()->rollBack();
        echo "DEBUG DELETE: Exception: " . $e->getMessage() . "\n";
        return 0;
    }
}
}
?>
