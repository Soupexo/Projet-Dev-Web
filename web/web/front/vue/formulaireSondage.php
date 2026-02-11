<div class="container">
    <h1>Créer un sondage</h1>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger">
            <?= htmlspecialchars($_GET['error']) ?>
        </div>
    <?php endif; ?>

    <form
        method="post"
        action="routeur.php?controleur=controleurSondage&action=creer"
        class="form-inline"
    >

        <div class="form-group">
            <label for="nom_s">Nom du sondage</label>
            <input
                type="text"
                id="nom_s"
                name="nom_s"
                class="form-control"
                required
            >
        </div>

        <div class="form-group">
            <label for="type_s">Type du sondage</label>
            <input
                type="text"
                id="type_s"
                name="type_s"
                class="form-control"
                placeholder="ex : QCM, ouvert, oui/non..."
                required
            >
        </div>

        <div class="form-group">
            <label for="reponses_multiples_s">
                Réponses multiples
            </label>
            <input
                type="checkbox"
                id="reponses_multiples_s"
                name="reponses_multiples_s"
                value="1"
            >
        </div>

        <div class="form-group">
            <label for="delai_s">Date limite</label>
            <input
                type="datetime-local"
                id="delai_s"
                name="delai_s"
                class="form-control"
                required
            >
        </div>

        <button class="btn btn-success">
            Enregistrer
        </button>
    </form>

    <hr>

    <p>
        <a href="routeur.php?controleur=controleurSondage&action=liste">
            Retour à la liste des sondages
        </a>
    </p>
</div>
