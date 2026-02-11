<div class="container">
    <h1>Ajouter / modifier un groupe</h1>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger"><?= htmlspecialchars($_GET['error']) ?></div>
    <?php endif; ?>
    <?php if (!empty($_GET['msg'])): ?>
        <div class="alert alert-success"><?= htmlspecialchars($_GET['msg']) ?></div>
    <?php endif; ?>

    <form method="post" action="routeur.php?controleur=controleurGroupe&action=creer" class="form-inline">
        <div class="form-group">
            <label for="nom_g">Nom du groupe</label>
            <input type="text" id="nom_g" name="nom_g" class="form-control" placeholder="Nom du groupe">
        </div>

        <div class="form-group">
            <label for="num_p">Promotion</label>
            <select id="num_p" name="num_p" class="form-control">
                <?php foreach ($promotions as $p): ?>
                    <option value="<?= htmlspecialchars($p['num_p']) ?>"><?= htmlspecialchars($p['num_p'] . ' - ' . ($p['intitule_a'] ?? '')) ?></option>
                <?php endforeach; ?>
            </select>
        </div>

        <div class="form-group">
            <label for="nombre_etudiant_max_g">Capacité max</label>
            <input type="number" id="nombre_etudiant_max_g" name="nombre_etudiant_max_g" class="form-control" min="1">
        </div>

        <div class="form-group">
            <small class="help">Un groupe ne peut pas être finalisé lors de sa création. Il sera finalisé plus tard après affectations ou via la modification.</small>
        </div>

        <button class="btn btn-success">Enregistrer</button>
    </form>

    <hr>
    <p><a href="routeur.php?controleur=controleurGroupe&action=listeInfos">Retour à la liste des groupes</a></p>
</div>