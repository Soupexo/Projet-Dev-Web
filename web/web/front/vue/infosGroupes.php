<div class="container">
    <h1>Liste des groupes</h1>
    
    <?php if(!empty($groupes)): ?>
        <p class="info">Total : <?= count($groupes) ?> groupe(s)</p>
        
        <table class="table-data">
            <thead>
                <tr>
                    <th>N° Groupe</th>
                    <th>Nom du groupe</th>
                    <th>Promotion</th>
                    <th>Année</th>
                    <th>Capacité max</th>
                    <th>Statut</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($groupes as $groupe): ?>
                <tr>
                    <td><?= htmlspecialchars($groupe['num_g']) ?></td>
                    <td><?= htmlspecialchars($groupe['nom_g']) ?></td>
                    <td><?= htmlspecialchars($groupe['num_p']) ?></td>
                    <td><?= htmlspecialchars($groupe['intitule_a']) ?></td>
                    <td><?= htmlspecialchars($groupe['nombre_etudiant_max_g']) ?> étudiants</td>
                    <td>
                        <?php if($groupe['est_finalise_g']): ?>
                            <span class="badge badge-success"> complet</span>
                        <?php else: ?>
                            <span class="badge badge-warning"> incomplet </span>
                        <?php endif; ?>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert">Aucun groupe trouvé.</p>
    <?php endif; ?>
</div>