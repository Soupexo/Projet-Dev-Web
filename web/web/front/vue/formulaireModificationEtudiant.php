<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modification Étudiant</title>
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
    <h1>Modifier un Étudiant</h1>

    <?php if (isset($_GET['msg'])): ?>
        <p style="color: green;"><?php echo htmlspecialchars($_GET['msg']); ?></p>
    <?php endif; ?>
    <?php if (isset($_GET['error'])): ?>
        <p style="color: red;"><?php echo htmlspecialchars($_GET['error']); ?></p>
    <?php endif; ?>

    <form method="POST" action="routeur.php?controleur=controleurEtudiant&action=modifier">
        <label for="login">Login de l'étudiant à modifier :</label>
        <input type="text" id="login" name="login" required>

        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom">

        <label for="prenom">Prénom :</label>
        <input type="text" id="prenom" name="prenom">

        <label for="mail">Email :</label>
        <input type="email" id="mail" name="mail">

        <label for="est_apprenti_e">Est apprenti :</label>
        <input type="checkbox" id="est_apprenti_e" name="est_apprenti_e" value="1">

        <label for="type_bac_e">Type de bac :</label>
        <input type="text" id="type_bac_e" name="type_bac_e">

        <label for="genre_e">Genre :</label>
        <select id="genre_e" name="genre_e">
            <option value="">--</option>
            <option value="M">Homme</option>
            <option value="F">Femme</option>
            <option value="O">Autre</option>
        </select>

        <button type="submit">Modifier l'étudiant</button>
    </form>

    <br>
    <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaireModification">Rafraîchir</a> |
    <a href="routeur.php">Retour à l'accueil</a>
</body>
</html>
