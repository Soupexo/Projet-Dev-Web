<div class="container">
    <h1><?php echo htmlspecialchars($titre) ?></h1>
    
    <?php if(!empty($covoiturages)): ?>
        <p class="info">Total : <?= count($covoiturages) ?> covoiturage(s)</p>
        
        <div class="covoiturage-list">
            <?php foreach($covoiturages as $cov): ?>
            <div class="covoiturage-card">
                <div class="etudiant-principal">
                    <h3>🚗 Conducteur / Demandeur</h3>
                    <p><strong>Login :</strong> <?= htmlspecialchars($cov['login_u']) ?></p>
                    <p><strong>Nom :</strong> <?= htmlspecialchars($cov['nom_u']) ?> <?= htmlspecialchars($cov['prenom_u']) ?></p>
                    <p><strong>Email :</strong> <?= htmlspecialchars($cov['mail_u']) ?></p>
                    <p><strong>Groupe :</strong> <?= htmlspecialchars($cov['nom_g']) ?></p>
                </div>
                <div class="separator">🤝 COVOITURE AVEC 🤝</div>
                <div class="etudiant-passager">
                    <h3>👥 Passager / Co-voitureur</h3>
                    <p><strong>Login :</strong> <?= htmlspecialchars($cov['login']) ?></p>
                    <p><strong>Nom :</strong> <?= htmlspecialchars($cov['nom']) ?> <?= htmlspecialchars($cov['prenom']) ?></p>
                    <p><strong>Email :</strong> <?= htmlspecialchars($cov['mail']) ?></p>
                </div>
            </div>
            <?php endforeach; ?>
        </div>
    <?php else: ?>
        <p class="alert">Aucun groupe de covoiturage trouvé.</p>
    <?php endif; ?>
</div>