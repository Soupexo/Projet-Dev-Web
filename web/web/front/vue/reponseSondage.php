<div class="container">
    <h1>liste des réponses de sondage</h1>
    
    <?php if(!empty($reponses)): ?>
        <p class="info">Total : <?= count($reponses) ?> réponse(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th>Étudiant</th>
                    <th>Numéro groupe</th>
                    <th>Date</th>
                    <th>Question</th>
                    <th>Réponse</th>
                    <th>Type</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($reponses as $reponse): ?>
                <tr>
                    <td><?= htmlspecialchars($reponse['nom_u']) ?> <?= htmlspecialchars($reponse['prenom_u']) ?></td>
                    <td><?= htmlspecialchars($reponse['num_g']) ?></td>
                    <td><?= htmlspecialchars($reponse['date_reponse_sondage']) ?></td>
                    <td><?= htmlspecialchars($reponse['question_sondage']) ?></td>
                    <td><?= htmlspecialchars($reponse['reponse_sondage']) ?></td>
                    <td><?= htmlspecialchars($reponse['type_s']) ?></td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>

    <?php else: ?>
        <p class="alert">Aucune réponse de sondage trouvée.</p>
    <?php endif; ?>
</div>
