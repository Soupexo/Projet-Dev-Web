<div class="container">
    <h1><?php echo htmlspecialchars($titre) ?></h1>
    
    <?php if(!empty($separations)): ?>
        <p class="info">Total : <?= count($separations) ?> séparation(s) disciplinaire(s)</p>
        
        <div class="separations-list">
            <?php foreach($separations as $separation): ?>
            <div class="separation-card">
                <div class="etudiant-principal">
                    <h3>Étudiant principal</h3>
                    <p><strong>Login :</strong> <?= htmlspecialchars($separation['login_u']) ?></p>
                    <p><strong>Nom :</strong> <?= htmlspecialchars($separation['nom_u']) ?> <?= htmlspecialchars($separation['prenom_u']) ?></p>
                    <p><strong>Email :</strong> <?= htmlspecialchars($separation['mail_u']) ?></p>
                    <p><strong>Groupe :</strong> <?= htmlspecialchars($separation['nom_g']) ?></p>
                </div>
                <div class="separator">⚠️ NE DOIT PAS ÊTRE AVEC ⚠️</div>
                <div class="etudiant-separe">
                    <h3>Étudiant à séparer</h3>
                    <p><strong>Login :</strong> <?= htmlspecialchars($separation['login']) ?></p>
                    <p><strong>Nom :</strong> <?= htmlspecialchars($separation['nom']) ?> <?= htmlspecialchars($separation['prenom']) ?></p>
                    <p><strong>Email :</strong> <?= htmlspecialchars($separation['mail']) ?></p>
                </div>
            </div>
            <?php endforeach; ?>
        </div>
    <?php else: ?>
        <p class="alert">Aucun critère disciplinaire trouvé.</p>
    <?php endif; ?>
</div>