<div class="container promotion-page">
    <h1>Liste des promotions</h1>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger"><?= htmlspecialchars($_GET['error']) ?></div>
    <?php endif; ?>
    <?php if (!empty($_GET['msg'])): ?>
        <div class="alert alert-success"><?= htmlspecialchars($_GET['msg']) ?></div>
    <?php endif; ?>

    <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;">
        <p class="info">Total : <?= count($promotions) ?> promotion(s)</p>
        <p style="display:flex;gap:8px;">
            <a class="btn btn-primary" href="routeur.php?controleur=controleurPromotion&action=afficherFormulaire">Créer une promotion</a>
    </div>

    <?php if (!empty($promotions)): ?>
        <p class="help" style="color:#555;margin-bottom:8px;">Les noms entre parenthèses sont les noms de colonnes en base (pour repérer les champs).</p>
        <table class="table-data">
            <thead>
                <tr>
                    <th>ID <br></th>
                    <th>Année <br></th>
                    <th>Nb groupes max <br></th>
                    <th>Nb étudiants max <br></th>
                    <th>Covoiturage max <br></th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($promotions as $p): ?>
                <tr>
                    <td><?= htmlspecialchars($p['num_p'] ?? $p['num']) ?></td>
                    <td><?= htmlspecialchars($p['intitule_a'] ?? (isset($p['num_a']) ? 'Année #'.(int)$p['num_a'] : '')) ?></td>
                    <td><?= htmlspecialchars($p['nombre_de_groupe_max_p'] ?? $p['nombre_groupe_max_p'] ?? '') ?></td>
                    <td><strong><?= htmlspecialchars($p['nombre_etudiant_max_p'] ?? $p['nombre_etudiant_p'] ?? '') ?></strong></td>
                    <td>
                        <?php $covo = $p['nombre_etudiant_max_covoiturage_p'] ?? ($p['nombre_etudiant_covoiturage_max_p'] ?? ($p['nombre_etudiant_max_covoiturage'] ?? null)); ?>
                        <?= $covo ? htmlspecialchars($covo) : '&mdash;' ?>
                    </td>
                    <td>—</td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert">Aucune promotion trouvée.</p>
    <?php endif; ?>
</div>