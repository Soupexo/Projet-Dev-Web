<div class="container">
    <h1>Liste des enseignants</h1>
    
    <?php if(!empty($enseignants)): ?>
        
        <table class="table-data">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Responsabilité</th>
                    <th>Permission</th>
                </tr>
            </thead>
            <tbody>
                <?php 
                // Regroupement des enseignants par email pour éviter les doublons
                $enseignantsGroupes = [];
                foreach($enseignants as $enseignant) {
                    $email = $enseignant['mail_u'];
                    if (!isset($enseignantsGroupes[$email])) {
                        $enseignantsGroupes[$email] = [
                            'nom_u' => $enseignant['nom_u'],
                            'prenom_u' => $enseignant['prenom_u'],
                            'mail_u' => $enseignant['mail_u'],
                            'responsabilites' => [],
                            'permissions' => []
                        ];
                    }
                    // Ajout des responsabilités et permissions (éviter doublons)
                    if (!in_array($enseignant['responsabilite'], $enseignantsGroupes[$email]['responsabilites'])) {
                        $enseignantsGroupes[$email]['responsabilites'][] = $enseignant['responsabilite'];
                    }
                    if (!in_array($enseignant['permission'], $enseignantsGroupes[$email]['permissions'])) {
                        $enseignantsGroupes[$email]['permissions'][] = $enseignant['permission'];
                    }
                }
                ?>
                
                <?php foreach($enseignantsGroupes as $enseignant): ?>
                <tr>
                    <td><?= htmlspecialchars($enseignant['nom_u']) ?></td>
                    <td><?= htmlspecialchars($enseignant['prenom_u']) ?></td>
                    <td><?= htmlspecialchars($enseignant['mail_u']) ?></td>
                    <td>
                        <?php if(count($enseignant['responsabilites']) > 0): ?>
                            <ol class="liste-items">
                                <?php foreach($enseignant['responsabilites'] as $resp): ?>
                                    <li><?= htmlspecialchars($resp) ?></li>
                                <?php endforeach; ?>
                            </ol>
                        <?php else: ?>
                            <span class="text-muted">Aucune</span>
                        <?php endif; ?>
                    </td>
                    <td>
                        <?php if(count($enseignant['permissions']) > 0): ?>
                            <ol class="liste-items">
                                <?php foreach($enseignant['permissions'] as $perm): ?>
                                    <li><?= htmlspecialchars($perm) ?></li>
                                <?php endforeach; ?>
                            </ol>
                        <?php else: ?>
                            <span class="text-muted">Aucune</span>
                        <?php endif; ?>
                    </td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert">Aucun enseignant trouvé.</p>
    <?php endif; ?>
</div>