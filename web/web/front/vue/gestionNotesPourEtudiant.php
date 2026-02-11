<div class="container gestion-notes">
    <h1>Gestion des notes</h1>

    <?php if (!empty($message)) : ?>
        <p class="info"><?= htmlspecialchars($message) ?></p>
    <?php endif; ?>
    <?php if (!empty($error)) : ?>
        <p class="alert"><?= htmlspecialchars($error) ?></p>
    <?php endif; ?>

    <hr>

    <h2>Notes existantes</h2>

    <?php if (!empty($notes)) : ?>
        <table class="table-data">
            <thead>
                <tr>
                    <th>Login</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Groupe</th>
                    <th>Matière</th>
                    <th>S1</th>
                    <th>S2</th>
                    <th>Moy. Année</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($notes as $n) : ?>
                    <tr>
                        <td><?= htmlspecialchars($n['login_u']) ?></td>
                        <td><?= htmlspecialchars($n['nom_u']) ?></td>
                        <td><?= htmlspecialchars($n['prenom_u']) ?></td>
                        <td><?= htmlspecialchars($n['nom_g']) ?></td>
                        <td><?= htmlspecialchars($n['intitule_n']) ?></td>
                        <td><?= htmlspecialchars($n['moy_prem_semestre'] ?? '') ?></td>
                        <td><?= htmlspecialchars($n['moy_deux_semestre'] ?? '') ?></td>
                        <td><?= htmlspecialchars($n['moy_annee'] ?? '') ?></td>
                        <td>
                            <!-- Formulaire de modification inline -->
                            <form method="POST" action="routeur.php?controleur=controleurEtudiant&action=modifierNote" class="action-form">
                                <input type="hidden" name="etudiant" value="<?= htmlspecialchars($n['login_u']) ?>">
                                <input type="hidden" name="matiere" value="<?= htmlspecialchars($n['intitule_n']) ?>">
                                <input type="number" name="moy_prem_semestre" step="0.1" min="0" max="20" value="<?= htmlspecialchars($n['moy_prem_semestre'] ?? '') ?>" placeholder="S1" class="small-input">
                                <input type="number" name="moy_deux_semestre" step="0.1" min="0" max="20" value="<?= htmlspecialchars($n['moy_deux_semestre'] ?? '') ?>" placeholder="S2" class="small-input">
                                <input type="number" name="note" step="0.1" min="0" max="20" value="<?= htmlspecialchars($n['moy_annee'] ?? '') ?>" placeholder="Année" class="small-input">
                                <input type="number" name="coef" value="1" class="small-input coef-input">
                                <button type="submit" class="btn-edit">Modifier</button>
                            </form>

                            <!-- Formulaire de suppression inline -->
                            <form method="POST" action="routeur.php?controleur=controleurEtudiant&action=supprimerNote" class="delete-form">
                                <input type="hidden" name="etudiant" value="<?= htmlspecialchars($n['login_u']) ?>">
                                <input type="hidden" name="matiere" value="<?= htmlspecialchars($n['intitule_n']) ?>">
                                <button type="submit" class="btn-delete">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert">Aucune note trouvée.</p>
    <?php endif; ?>
</div>