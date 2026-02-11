<div class="container">
    <h1>Gérer les responsabilités des enseignants</h1>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger"><?= htmlspecialchars($_GET['error']) ?></div>
    <?php endif; ?>
    <?php if (!empty($_GET['msg'])): ?>
        <div class="alert alert-success"><?= htmlspecialchars($_GET['msg']) ?></div>
    <?php endif; ?>

    <div style="margin-bottom:16px;display:flex;gap:12px;align-items:center;">
        <form method="post" action="routeur.php?controleur=controleurEnseignant&action=creerResponsabilite" class="form-inline" style="gap:8px;align-items:center;">
            <div class="form-group">
                <label for="intitule_responsabilite">Nouvelle responsabilité</label>
                <input type="text" id="intitule_responsabilite" name="intitule" placeholder="Ex: Responsable d'année" class="form-control" style="width:360px;" required>
            </div>
            <button class="btn btn-success">Ajouter</button>
        </form>
        <a class="btn btn-secondary" href="routeur.php?controleur=controleurEnseignant&action=listeInfos">Retour à la liste</a>
    </div>

    <?php if (!empty($enseignants)): ?>
        <div class="enseignants-grid" style="display:grid;grid-template-columns:repeat(auto-fill,minmax(360px,1fr));gap:12px;">
            <?php foreach($enseignants as $e): ?>
                <?php $num = $e['num_en'] ?? null; if (!$num) continue; ?>
                <div class="card" style="padding:12px;border:1px solid #ddd;background:#fff;">
                    <h3 style="margin:0 0 8px 0;"><?= htmlspecialchars($e['prenom_u'] . ' ' . $e['nom_u']) ?></h3>
                    <p style="margin:0 0 8px 0;color:#666;"><?= htmlspecialchars($e['mail_u']) ?></p>

                    <form method="post" action="routeur.php?controleur=controleurEnseignant&action=affecter" class="form-inline" style="gap:8px;align-items:center;">
                        <input type="hidden" name="num_en" value="<?= (int)$num ?>">
                        <div class="form-group" style="flex:1;min-width:220px;">
                            <label for="resp-<?= (int)$num ?>" style="display:block;margin-bottom:6px;">Responsabilité</label>
                            <select id="resp-<?= (int)$num ?>" name="responsabilite" class="form-control" style="width:100%;">
                                <option value="">-- Aucune --</option>
                                <?php foreach($responsabilites as $r): ?>
                                    <?php $sel = ((int)$r['num_r'] === ($map[$num] ?? null)) ? 'selected' : ''; ?>
                                    <option value="<?= (int)$r['num_r'] ?>" <?= $sel ?>><?= htmlspecialchars($r['intitule_r']) ?></option>
                                <?php endforeach; ?>
                            </select>
                        </div>

                        <div style="display:flex;gap:8px;align-items:flex-end;">
                            <button class="btn btn-primary" type="submit">Enregistrer</button>
                            <button class="btn btn-link" type="submit" onclick="if(!confirm('Supprimer la responsabilité assignée à cet enseignant ?')) return false; this.form.querySelector('select[name=responsabilite]').value = '';">Supprimer</button>
                        </div>
                    </form>
                </div>
            <?php endforeach; ?>
        </div>
    <?php else: ?>
        <p class="alert">Aucun enseignant trouvé.</p>
    <?php endif; ?>
</div>