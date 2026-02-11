<div class="container">
    <h1>Liste des étudiants</h1>

    <?php if (!empty($etudiants)): ?>
        <p class="info">Total : <?= count($etudiants) ?> étudiant(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Mail</th>
                    <th>Genre</th>
                    <th>Type de bac</th>
                    <th>Apprenti</th>
                    <th>Groupe</th>
                    
                </tr>
            </thead>
            <tbody>
                <?php foreach ($etudiants as $etudiant): ?>
                    <tr>
                        <td><?= htmlspecialchars($etudiant['nom_u']) ?></td>
                        <td><?= htmlspecialchars($etudiant['prenom_u']) ?></td>
                        <td><?= htmlspecialchars($etudiant['mail_u']) ?></td>
                        <td><?= htmlspecialchars($etudiant['genre_e']) ?></td>
                        <td><?= htmlspecialchars($etudiant['type_bac_e']) ?></td>
                        <td>
                            <?php if ((int)$etudiant['est_apprenti_e'] === 1): ?>
                                <span class="badge badge-success">Oui</span>
                            <?php else: ?>
                                <span class="badge badge-secondary">Non</span>
                            <?php endif; ?>
                        </td>
                        <td>
                            <?php if ($etudiant['nom_g'] === null) :?>
                                <span class="badge badge-secondary">Non affecté</span>
                            <?php else: ?>
                                <?= htmlspecialchars($etudiant['nom_g']) ?>
                            <?php endif; ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert alert-warning">Aucun étudiant trouvé</p>
    <?php endif; ?>
</div>
