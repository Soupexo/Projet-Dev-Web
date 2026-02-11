<div class="container">
    <h1>Liste des groupes non finalisés</h1>
    
    <?php if(!empty($groupes)): ?>
        <p class="info">Total : <?= count($groupes) ?> groupe(s) incomplet(s)</p>
        
        <table class="table-data">
            <thead>
                <tr>
                    <th>N° Groupe</th>
                    <th>Nom du groupe</th>
                    <th>Promotion</th>
                    <th>Année</th>
                    <th>Effectif actuel</th>
                    <th>Capacité max</th>
                    <th>Places restantes</th>
                    <th>Taux</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($groupes as $groupe): ?>
                <?php 
                    $tauxRemplissage = ($groupe['nombre_etudiant_g'] / $groupe['nombre_etudiant_max_g']) * 100;
                    $classeStatut = '';
                    if ($tauxRemplissage >= 80) {
                        $classeStatut = 'badge-warning';
                    } elseif ($tauxRemplissage >= 50) {
                        $classeStatut = 'badge-info';
                    } else {
                        $classeStatut = 'badge-danger';
                    }
                ?>
                <tr>
                    <td><?= htmlspecialchars($groupe['num_g']) ?></td>
                    <td><?= htmlspecialchars($groupe['nom_g']) ?></td>
                    <td><?= htmlspecialchars($groupe['num_p']) ?></td>
                    <td><?= htmlspecialchars($groupe['annee']) ?></td>
                    <td><?= htmlspecialchars($groupe['nombre_etudiant_g']) ?> étudiants</td>
                    <td><?= htmlspecialchars($groupe['nombre_etudiant_max_g']) ?> étudiants</td>
                    <td>
                        <span class="badge <?= $classeStatut ?>">
                            <?= htmlspecialchars($groupe['placesRestantes']) ?> places
                        </span>
                    </td>
                    <td>
                        <div class="progress-container">
                            <div class="progress-bar" style="width: <?= round($tauxRemplissage) ?>%;">
                                <?= round($tauxRemplissage) ?>%
                            </div>
                        </div>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert alert-success">✓ Tous les groupes sont finalisés !</p>
    <?php endif; ?>
</div>