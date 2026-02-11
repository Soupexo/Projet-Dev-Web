<div class="container">
    <h1>Listes des séparations étudiantes</h1>
    
    <?php if(!empty($criteres)): ?>
        <p class="info">Total : <?= count($criteres) ?> séparation(s) disciplinaire(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th colspan="4">Étudiant principal</th>
                    <th colspan="3">Étudiant à séparer</th>
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
                <?php foreach($criteres as $critere): ?>
                <tr>
                    <td><?= htmlspecialchars($critere['login_u']) ?></td>
                    <td><?= htmlspecialchars($critere['nom_u']) ?> <?= htmlspecialchars($critere['prenom_u']) ?></td>
                    <td><?= htmlspecialchars($critere['mail_u']) ?></td>
                    <td><?= htmlspecialchars($critere['nom_g']) ?></td>
                    <td><?= htmlspecialchars($critere['login']) ?></td>
                    <td><?= htmlspecialchars($critere['nom']) ?> <?= htmlspecialchars($critere['prenom']) ?></td>
                    <td><?= htmlspecialchars($critere['mail']) ?></td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>

    <?php else: ?>
        <p class="alert">Aucun critère disciplinaire trouvé.</p>
    <?php endif; ?>
</div>
