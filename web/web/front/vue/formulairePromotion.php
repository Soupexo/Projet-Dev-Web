<div class="container">
    <h1>Créer une promotion</h1>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger"><?= htmlspecialchars($_GET['error']) ?></div>
    <?php endif; ?>

    <form method="post" action="routeur.php?controleur=controleurPromotion&action=creer" class="form-inline">
        <div class="form-group">
            <label for="nombre_etudiant_max_p">Nombre étudiants max</label>
            <input type="number" id="nombre_etudiant_max_p" name="nombre_etudiant_max_p" class="form-control" min="1">
        </div>

        <div class="form-group">
            <label for="nombre_etudiant_covoiturage_max_p">Nombre étudiants covoiturage max</label>
            <input type="number" id="nombre_etudiant_covoiturage_max_p" name="nombre_etudiant_covoiturage_max_p" class="form-control" min="0">
        </div>

        <div class="form-group">
            <label for="nombre_groupe_max_p">Nombre groupes max</label>
            <input type="number" id="nombre_groupe_max_p" name="nombre_groupe_max_p" class="form-control" min="1">
        </div>

        <div class="form-group">
            <label for="num_a">Année</label>
            <input type="number" id="num_a" name="num_a" class="form-control" min="1">
        </div>

        <button class="btn btn-success">Enregistrer</button>
    </form>

    <hr>
    <p><a href="routeur.php?controleur=controleurPromotion&action=liste">Retour à la liste des promotions</a></p>
</div>