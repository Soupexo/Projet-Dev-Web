<div class="container">
    <h1>Liste des informations des étudiants</h1>
    <?php if(!empty($etudiants)): ?>
            <a href="routeur.php?controleur=controleurEtudiant&action=exporterCSV" class="btn-export" 
           style="background: #28a745; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px; font-weight: bold;">
            📥 Exporter en CSV
        </a>
        <?php endif; ?>
    </div>
    
    <?php if(!empty($etudiants)): ?>
        <p class="info">Total : <?= count($etudiants) ?> étudiant(s)</p>
        
        <table class="table-data">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Groupe</th>
                    <th>Année</th>
                    <th>Apprenti</th>
                    <th>Genre</th>
                    <th>Type Bac</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach($etudiants as $etudiant): ?>
                <tr>
                    <td><?= htmlspecialchars($etudiant['nom_u']) ?></td>
                    <td><?= htmlspecialchars($etudiant['prenom_u']) ?></td>
                    <td><?= htmlspecialchars($etudiant['mail_u']) ?></td>
                    <td><?= htmlspecialchars($etudiant['nom_g']) ?></td>
                    <td><?= htmlspecialchars($etudiant['annee']) ?></td>
                    <td><?= $etudiant['est_apprenti_e'] ? '✓ Oui' : '✗ Non' ?></td>
                    <td><?= htmlspecialchars($etudiant['genre_e'] ?? 'N/A') ?></td>
                    <td><?= htmlspecialchars($etudiant['type_bac_e']) ?></td>
                </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php else: ?>
        <p class="alert">Aucun étudiant trouvé.</p>
    <?php endif; ?>
</div>
