<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modification Promotion</title>
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
    <h1>Modifier une Promotion</h1>

    <?php if (isset($_GET['msg'])): ?>
        <p style="color: green;"><?php echo htmlspecialchars($_GET['msg']); ?></p>
    <?php endif; ?>
    <?php if (isset($_GET['error'])): ?>
        <p style="color: red;"><?php echo htmlspecialchars($_GET['error']); ?></p>
    <?php endif; ?>

    <form method="POST" action="routeur.php?controleur=controleurPromotion&action=modifier">
        <label for="num_p">Numéro de la promotion à modifier :</label>
        <input type="number" id="num_p" name="num_p" required>

        <label for="nombre_etudiant_max_p">Nombre étudiants max :</label>
        <input type="number" id="nombre_etudiant_max_p" name="nombre_etudiant_max_p" min="1">

        <label for="nombre_etudiant_covoiturage_max_p">Nombre étudiants covoiturage max :</label>
        <input type="number" id="nombre_etudiant_covoiturage_max_p" name="nombre_etudiant_covoiturage_max_p" min="0">

        <label for="nombre_groupe_max_p">Nombre groupes max :</label>
        <input type="number" id="nombre_groupe_max_p" name="nombre_groupe_max_p" min="1">

        <label for="num_a">Année :</label>
        <input type="number" id="num_a" name="num_a" min="1">

        <button type="submit">Modifier la promotion</button>
    </form>

    <br>
    <a href="routeur.php?controleur=controleurPromotion&action=afficherFormulaireModification">Rafraîchir</a> |
    <a href="routeur.php">Retour à l'accueil</a>
</body>
</html>
