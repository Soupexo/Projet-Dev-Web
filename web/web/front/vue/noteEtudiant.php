<div class="container">
    <h1>Liste des notes des étudiants</h1>
    
    <?php if(!empty($notes)): ?>
        <p class="info">Total : <?= count($notes) ?> note(s)</p>
        
        <table class="table-data">
            <thead>
                <tr>
                    <th>Login</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Groupe</th>
                    <th>Matière</th>
                    <th>Moy. S1</th>
                    <th>Moy. S2</th>
                    <th>Moy. Année</th>
                    <th>Validé</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($notes as $note): ?>
                <tr>
                    <td><?= htmlspecialchars($note['login_u']) ?></td>
                    <td><?= htmlspecialchars($note['nom_u']) ?></td>
                    <td><?= htmlspecialchars($note['prenom_u']) ?></td>
                    <td><?= htmlspecialchars($note['nom_g']) ?></td>
                    <td><?= htmlspecialchars($note['intitule_n']) ?></td>
                    <td><?= htmlspecialchars($note['moy_prem_semestre']) ?></td>
                    <td><?= htmlspecialchars($note['moy_deux_semestre']) ?></td>
                    <td><?= htmlspecialchars($note['moy_annee']) ?></td>
                    <td><?= $note['aValideMatiere'] ? '✓ Validé' : '✗ Non validé' ?></td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert">Aucune note trouvée.</p>
    <?php endif; ?>
</div>