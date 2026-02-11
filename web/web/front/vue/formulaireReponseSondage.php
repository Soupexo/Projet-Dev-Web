<div class="container">
    <h1>Répondre à un sondage</h1>

    <?php if (!empty($_GET['error'])): ?>
        <div class="alert alert-danger">
            <?= htmlspecialchars($_GET['error']) ?>
        </div>
    <?php endif; ?>

    <?php if (!empty($_GET['msg'])): ?>
        <div class="alert alert-success">
            <?= htmlspecialchars($_GET['msg']) ?>
        </div>
    <?php endif; ?>

    <?php if (empty($sondages)): ?>
        <p class="alert alert-info">Aucun sondage en cours.</p>
    <?php else: ?>
        <form method="post" action="routeur.php?controleur=controleurEtudiant&action=creerReponseSondage" class="form-inline">
            <div class="form-group">
                <label for="nomSondage">Sondage</label>
                <select id="nomSondage" name="nomSondage" class="form-control" required>
                    <?php foreach ($sondages as $s): ?>
                        <option value="<?= htmlspecialchars($s['nom_s']) ?>">
                            <?= htmlspecialchars($s['nom_s']) ?> (<?= htmlspecialchars($s['type_s']) ?>)
                        </option>
                    <?php endforeach; ?>
                </select>
            </div>

            <div class="form-group">
                <label for="question">Question</label>
                <input type="text" id="question" name="question" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="reponse">Réponse</label>
                <textarea id="reponse" name="reponse" class="form-control" rows="4" required></textarea>
            </div>

            <div class="form-group">
                <label for="dateReponse">Date de réponse</label>
                <input type="date" id="dateReponse" name="dateReponse" class="form-control" required>
            </div>

            <button class="btn btn-success">Envoyer</button>
        </form>
    <?php endif; ?>

    <hr>

    <p>
        <a href="routeur.php?controleur=controleurSondage&action=listeSondageCourants">Retour aux sondages</a>
    </p>
</div>
