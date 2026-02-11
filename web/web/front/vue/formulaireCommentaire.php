<div class="container">
    <h1>Ajouter un commentaire à un groupe</h1>

    <?php if (!empty($_GET['msg'])): ?>
        <div class="alert alert-success">
            <?= htmlspecialchars($_GET['msg']) ?>
        </div>
    <?php endif; ?>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger">
            <?= htmlspecialchars($_GET['error']) ?>
        </div>
    <?php endif; ?>

    <form method="post" action="routeur.php?controleur=controleurEnseignant&action=ajouterCommentaire" class="form-inline">
        <div class="form-group">
            <label for="num_en">ID enseignant (num_en)</label>
            <input
                type="number"
                id="num_en"
                name="num_en"
                class="form-control"
                value="<?= isset($num_en) ? htmlspecialchars((string)$num_en) : '' ?>"
                min="1"
            >
        </div>

        <div class="form-group">
            <label for="nom_g">Nom du groupe</label>
            <input type="text" id="nom_g" name="nom_g" class="form-control" required>
        </div>

        <div class="form-group">
            <label for="commentaire">Commentaire</label>
            <textarea id="commentaire" name="commentaire" class="form-control" rows="4" required></textarea>
        </div>

        <div class="form-group">
            <label for="date_constitution_groupe">Date</label>
            <input type="date" id="date_constitution_groupe" name="date_constitution_groupe" class="form-control">
        </div>

        <button class="btn btn-success">Enregistrer</button>
    </form>

    <hr>

    <p>
        <a href="routeur.php">Retour</a>
    </p>
</div>
