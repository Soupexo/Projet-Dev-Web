<?php
class EtudiantSondage {
    public ?int $num_e;
    public ?int $num_s;
    public ?string $reponse_sondage;
    public ?string $question_sondage;
    public ?DateTime $date_reponse_sondage;

    public function __construct(
        int $num_e = NULL,
        int $num_s = NULL,
        string $reponse_sondage = NULL,
        string $question_sondage = NULL,
        DateTime $date_reponse_sondage = NULL
    ) {
        if ($num_e != NULL && $num_s != NULL) {
            $this->num_e = $num_e;
            $this->num_s = $num_s;
            $this->reponse_sondage = $reponse_sondage;
            $this->question_sondage = $question_sondage;
            $this->date_reponse_sondage = $date_reponse_sondage;
        }
    }

    /**
     * Récupère toutes les réponses aux sondages
     */
    public static function getAll(): array {

        $sql = "SELECT es.num_e, es.num_s, es.reponse_sondage, es.question_sondage, es.date_reponse_sondage,
                       u.nom_u, u.prenom_u, s.nom_s
                FROM ETUDIANT_SONDAGE es
                JOIN ETUDIANT e ON es.num_e = e.num_e
                JOIN UTILISATEUR u ON e.login_u = u.login_u 
                JOIN SONDAGE s ON es.num_s = s.num_s
                ORDER BY es.date_reponse_sondage DESC";
        
        $stmt = Connexion::pdo()->prepare($sql);
        $stmt->execute();
        
        $reponses = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $reponses[] = [
                'num_e' => $row['num_e'],
                'num_s' => $row['num_s'],
                'reponse_sondage' => $row['reponse_sondage'],
                'question_sondage' => $row['question_sondage'],
                'date_reponse_sondage' => $row['date_reponse_sondage'],
                // On utilise les colonnes _u récupérées
                'nom_etudiant' => $row['nom_u'] . ' ' . $row['prenom_u'],
                'nom_sondage' => $row['nom_s']
            ];
        }
        
        return $reponses;
    }

    public static function fromNameToId($nomOuId) {
    // Si c'est déjà un nombre (comme le "13" envoyé par ton Java), on le renvoie directement
    if (is_numeric($nomOuId)) {
        return (int)$nomOuId;
    }

    // Sinon, on cherche l'ID correspondant au nom
    $sql = "SELECT num_s FROM SONDAGE WHERE nom_s = :nom";
    $stmt = Connexion::pdo()->prepare($sql);
    $stmt->execute([':nom' => $nomOuId]);
    $res = $stmt->fetchColumn();
    
    return $res;
}

    public static function create(int $numS, int $numE, string $questionSondage, string $reponseSondage, string $dateReponseSondage): int|false {
    // Assurez-vous que les noms des colonnes ci-dessous (num_s, num_e, etc.) 
    // correspondent EXACTEMENT à votre table SQL ETUDIANT_SONDAGE
    $sql = "INSERT INTO ETUDIANT_SONDAGE (num_s, num_e, reponse_sondage, question_sondage, date_reponse_sondage) 
            VALUES (:num_s, :num_e, :reponse_sondage, :question_sondage, :date_reponse_sondage)";
    
    try {
        $pdo = Connexion::pdo();
        $stmt = $pdo->prepare($sql);
        $success = $stmt->execute([
            ':num_s' => $numS,
            ':num_e' => $numE,
            ':reponse_sondage' => $reponseSondage,
            ':question_sondage' => $questionSondage,
            ':date_reponse_sondage' => $dateReponseSondage
        ]);
        
        if (!$success || $stmt->rowCount() <= 0) {
            return false;
        }

        $check = $pdo->prepare("SELECT COUNT(*) FROM ETUDIANT_SONDAGE WHERE num_s = :num_s AND num_e = :num_e");
        $check->execute([':num_s' => $numS, ':num_e' => $numE]);
        $exists = (int)$check->fetchColumn();
        if ($exists <= 0) {
            return false;
        }

        return 1;
    } catch (PDOException $e) {
        self::$lastError = $e->getMessage();
        error_log("SQL ERROR: " . $e->getMessage());
        return false;
    }
}

    private static string $lastError = '';
    
    public static function getLastError(): string {
        return self::$lastError;
    }
}
?>
