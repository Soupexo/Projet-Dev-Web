<div class="container">
    <h1>Liste des utilisateurs</h1>

    <?php if (!empty($utilisateurs)): ?>
        <p class="info">Total : <?= count($utilisateurs) ?> utilisateur(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th>Login</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($utilisateurs as $utilisateur): ?>
                    <tr>
                        <td><?= htmlspecialchars($utilisateur['login'] ?? '—') ?></td>
                        <td><?= htmlspecialchars($utilisateur['nom'] ?? '—') ?></td>
                        <td><?= htmlspecialchars($utilisateur['prenom'] ?? '—') ?></td>
                        <td><?= htmlspecialchars($utilisateur['mail'] ?? '—') ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>

    <?php else: ?>
        <p class="alert">Aucun utilisateur trouvé.</p>
    <?php endif; ?>
</div>
