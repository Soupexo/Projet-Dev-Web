<div class="container">
    <h1>Liste des binômages de covoiturages</h1>
    
    <?php if(!empty($covoiturages)): ?>
        <p class="info">Total : <?= count($covoiturages) ?> covoiturage(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th colspan="4">🚗 Conducteur / Demandeur</th>
                    <th colspan="3">👥 Passager / Co-voitureur</th>
                </tr>
                <tr>
                    <th>Login</th>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Groupe</th>
                    <th>Login</th>
                    <th>Nom</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($covoiturages as $cov): ?>
                <tr>
                    <td><?= htmlspecialchars($cov['login_u']) ?></td>
                    <td><?= htmlspecialchars($cov['nom_u']) ?> <?= htmlspecialchars($cov['prenom_u']) ?></td>
                    <td><?= htmlspecialchars($cov['mail_u']) ?></td>
                    <td><?= htmlspecialchars($cov['nom_g']) ?></td>
                    <td><?= htmlspecialchars($cov['login']) ?></td>
                    <td><?= htmlspecialchars($cov['nom']) ?> <?= htmlspecialchars($cov['prenom']) ?></td>
                    <td><?= htmlspecialchars($cov['mail']) ?></td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>

    <?php else: ?>
        <p class="alert">Aucun groupe de covoiturage trouvé.</p>
    <?php endif; ?>
</div>
