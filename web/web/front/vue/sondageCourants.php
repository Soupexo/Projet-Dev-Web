<div class="container">
    <h1>Liste des sondages en cours</h1>
    
    <?php if(!empty($sondages)): ?>
        <p class="info">Total : <?= count($sondages) ?> sondage(s) actif(s)</p>

        <table class="table-data">
            <thead>
                <tr>
                    <th>N° Sondage</th>
                    <th>Nom du sondage</th>
                    <th>Type</th>
                    <th>Réponses multiples</th>
                    <th>Date limite</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($sondages as $sondage): ?>
                <?php 
                    // Calcul du temps restant
                    $delai = new DateTime($sondage['delai_s']);
                    $maintenant = new DateTime();
                    $diff = $maintenant->diff($delai);
                    $joursRestants = $diff->days;
                    
                    // Classe CSS selon l'urgence
                    $classeUrgence = '';
                    if ($joursRestants <= 2) {
                        $classeUrgence = 'badge-danger';
                        $texteUrgence = 'Urgent';
                    } elseif ($joursRestants <= 7) {
                        $classeUrgence = 'badge-warning';
                        $texteUrgence = 'Bientôt';
                    } else {
                        $classeUrgence = 'badge-success';
                        $texteUrgence = 'Actif';
                    }
                    
                    // Badge pour le type
                    $classeType = '';
                    switch($sondage['type_s']) {
                        case 'Groupe':
                            $classeType = 'badge-info';
                            break;
                        case 'Pédagogique':
                            $classeType = 'badge-pedagogique';
                            break;
                        case 'Orientation':
                            $classeType = 'badge-orientation';
                            break;
                        case 'Autres':
                            $classeType = 'badge-autres';
                            break;
                        default:
                            $classeType = 'badge-info';
                    }
                ?>
                <tr>
                    <td><?= htmlspecialchars($sondage['num_s']) ?></td>
                    <td><strong><?= htmlspecialchars($sondage['nom_s']) ?></strong></td>
                    <td>
                        <span class="badge <?= $classeType ?>">
                            <?= htmlspecialchars($sondage['type_s']) ?>
                        </span>
                    </td>
                    <td>
                        <?php if($sondage['a_des_reponses_multiples_s']): ?>
                            <span class="badge badge-success">✓ Oui</span>
                        <?php else: ?>
                            <span class="badge badge-secondary">✗ Non</span>
                        <?php endif; ?>
                    </td>
                    <td><?= date('d/m/Y', strtotime($sondage['delai_s'])) ?></td>
                    <td>
                        <span class="badge <?= $classeUrgence ?>">
                            <?= $joursRestants ?> jour(s) - <?= $texteUrgence ?>
                        </span>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>

    <?php else: ?>
        <p class="alert alert-info">ℹ️ Aucun sondage en cours actuellement.</p>
    <?php endif; ?>
</div>