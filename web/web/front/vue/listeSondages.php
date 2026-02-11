<?php
require_once __DIR__ . '/../modele/sondage.php';

$sondages = Sondage::getSondagesCourants();
?>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sondages courants - AppSaclay</title>
    <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/sondages.css">
    <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/tableau.css">
    <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/header.css">
</head>
<body>
    <?php require_once __DIR__ . '/header.php'; ?>
    
    <main class="container">
        <section class="sondages-section">
            <h1>Sondages courants</h1>
            
            <?php if (empty($sondages)): ?>
                <div class="no-data">
                    <p>Aucun sondage en cours pour le moment.</p>
                </div>
            <?php else: ?>
                <div class="sondages-grid">
                    <?php foreach ($sondages as $sondage): ?>
                        <div class="sondage-card">
                            <div class="sondage-header">
                                <h3><?php echo htmlspecialchars($sondage['titre_s']); ?></h3>
                                <span class="sondage-date">
                                    Du <?php echo date('d/m/Y', strtotime($sondage['date_debut_s'])); ?>
                                    au <?php echo date('d/m/Y', strtotime($sondage['date_fin_s'])); ?>
                                </span>
                            </div>
                            
                            <div class="sondage-description">
                                <p><?php echo nl2br(htmlspecialchars($sondage['description_s'])); ?></p>
                            </div>
                            
                            <div class="sondage-actions">
                                <?php if (Utilisateur::isConnected()): ?>
                                    <?php if (Utilisateur::estEtudiant(Utilisateur::getCurrentLogin())): ?>
                                        <!-- Pour les étudiants : bouton pour répondre -->
                                        <a href="routeur.php?controleur=controleurSondage&action=repondreSondage&id=<?php echo $sondage['num_s']; ?>" 
                                           class="btn btn-primary">Répondre au sondage</a>
                                    <?php else: ?>
                                        <!-- Pour les admins/responsables : voir les réponses -->
                                        <a href="routeur.php?controleur=controleurSondage&action=voirReponses&id=<?php echo $sondage['num_s']; ?>" 
                                           class="btn btn-secondary">Voir les réponses</a>
                                    <?php endif; ?>
                                <?php else: ?>
                                    <p class="login-required">Veuillez vous connecter pour répondre au sondage</p>
                                <?php endif; ?>
                            </div>
                        </div>
                    <?php endforeach; ?>
                </div>
            <?php endif; ?>
        </section>
    </main>
    
    <?php require_once __DIR__ . '/footer.php'; ?>
</body>
</html>
