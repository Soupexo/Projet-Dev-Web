<div class="container">
    <h1>Groupes disponibles pour les étudiants</h1>

    <?php if (!empty($groupes)): ?>
        <p class="info">Total : <?= count($groupes) ?> groupe(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th>Nom du groupe</th>
                    <th>Année universitaire</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($groupes as $groupe): ?>
                    <tr>
                        <td><?= htmlspecialchars($groupe['nom_g']) ?></td>
                        <td><?= htmlspecialchars($groupe['intitule_a']) ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert alert-warning">Aucun groupe disponible</p>
    <?php endif; ?>
</div>
