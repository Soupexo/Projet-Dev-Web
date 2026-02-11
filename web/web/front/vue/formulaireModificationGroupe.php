<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modification Groupe</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 720px; margin: 32px auto; padding: 0 16px; }
        form { display: grid; gap: 10px; margin-top: 12px; }
        input, select { padding: 10px; border: 1px solid #ccc; border-radius: 6px; }
        button { padding: 10px 12px; border: 0; border-radius: 6px; background: #1565c0; color: #fff; cursor: pointer; }
        button:hover { background: #0d47a1; }
        a { color: #1565c0; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>Modifier un Groupe</h1>

    <?php if (isset($_GET['msg'])): ?>
        <p style="color: green;"><?php echo htmlspecialchars($_GET['msg']); ?></p>
    <?php endif; ?>
    <?php if (isset($_GET['error'])): ?>
        <p style="color: red;"><?php echo htmlspecialchars($_GET['error']); ?></p>
    <?php endif; ?>

    <form method="POST" action="routeur.php?controleur=controleurGroupe&action=modifier">
        <label for="num_g">Numéro du groupe à modifier :</label>
        <input type="number" id="num_g" name="num_g" required>

        <label for="nom_g">Nom du groupe :</label>
        <input type="text" id="nom_g" name="nom_g">

        <label for="num_p">Promotion :</label>
        <select id="num_p" name="num_p">
            <option value="">--</option>
            <?php if (isset($promotions) && is_array($promotions)): foreach ($promotions as $p): ?>
                <option value="<?= htmlspecialchars($p['num_p']) ?>"><?= htmlspecialchars($p['num_p'] . ' - ' . ($p['intitule_a'] ?? '')) ?></option>
            <?php endforeach; endif; ?>
        </select>

        <label for="nombre_etudiant_max_g">Capacité max :</label>
        <input type="number" id="nombre_etudiant_max_g" name="nombre_etudiant_max_g" min="1">

        <label for="est_finalise_g">Finalisé :</label>
        <select id="est_finalise_g" name="est_finalise_g">
            <option value="">--</option>
            <option value="0">Non</option>
            <option value="1">Oui</option>
        </select>

        <button type="submit">Modifier le groupe</button>
    </form>

    <br>
    <a href="routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification">Rafraîchir</a> |
    <a href="routeur.php">Retour à l'accueil</a>
</body>
</html>
