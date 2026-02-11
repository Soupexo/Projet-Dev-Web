<div class="container">
    <h1><?php echo htmlspecialchars($titre) ?></h1>
    
    <?php if(!empty($reponses)): ?>
        <p class="info">Total : <?= count($reponses) ?> réponse(s)</p>
        
        <div class="reponses-list">
            <?php foreach($reponses as $reponse): ?>
            <div class="reponse-card">
                <div class="reponse-header">
                    <strong><?= htmlspecialchars($reponse['nom_u']) ?> <?= htmlspecialchars($reponse['prenom_u']) ?></strong>
                    <span class="badge">Groupe <?= htmlspecialchars($reponse['num_g']) ?></span>
                    <span class="date"><?= htmlspecialchars($reponse['date_reponse_sondage']) ?></span>
                </div>
                <div class="reponse-body">
                    <p class="question"><strong>Question :</strong> <?= htmlspecialchars($reponse['question_sondage']) ?></p>
                    <p class="reponse"><strong>Réponse :</strong> <?= htmlspecialchars($reponse['reponse_sondage']) ?></p>
                    <p class="type-sondage"><em>Type : <?= htmlspecialchars($reponse['type_s']) ?></em></p>
                </div>
            </div>
            <?php endforeach; ?>
        </div>
    <?php else: ?>
        <p class="alert">Aucune réponse de sondage trouvée.</p>
    <?php endif; ?>
</div>