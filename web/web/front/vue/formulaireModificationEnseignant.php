<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modification Enseignant</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 720px; margin: 32px auto; padding: 0 16px; }
        form { display: grid; gap: 10px; margin-top: 12px; }
        input { padding: 10px; border: 1px solid #ccc; border-radius: 6px; }
        button { padding: 10px 12px; border: 0; border-radius: 6px; background: #1565c0; color: #fff; cursor: pointer; }
        button:hover { background: #0d47a1; }
        a { color: #1565c0; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>Modifier un Enseignant</h1>

    <?php if (isset($_GET['msg'])): ?>
        <p style="color: green;"><?php echo htmlspecialchars($_GET['msg']); ?></p>
    <?php endif; ?>
    <?php if (isset($_GET['error'])): ?>
        <p style="color: red;"><?php echo htmlspecialchars($_GET['error']); ?></p>
    <?php endif; ?>

    <form method="POST" action="routeur.php?controleur=controleurEnseignant&action=modifier">
        <label for="login">Login de l'enseignant à modifier :</label>
        <input type="text" id="login" name="login" required>

        <label for="nom">Nom :</label>
        <input type="text" id="nom" name="nom">

        <label for="prenom">Prénom :</label>
        <input type="text" id="prenom" name="prenom">

        <label for="mail">Email :</label>
        <input type="email" id="mail" name="mail">

        <button type="submit">Modifier l'enseignant</button>
    </form>

    <br>
    <a href="routeur.php?controleur=controleurEnseignant&action=afficherFormulaireModification">Rafraîchir</a> |
    <a href="routeur.php">Retour à l'accueil</a>
</body>
</html>
