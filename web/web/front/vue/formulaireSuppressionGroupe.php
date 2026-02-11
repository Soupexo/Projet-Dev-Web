<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Suppression Groupe</title>
    <style>
        body { font-family: Arial, sans-serif; max-width: 720px; margin: 32px auto; padding: 0 16px; }
        form { display: grid; gap: 10px; margin-top: 12px; }
        input { padding: 10px; border: 1px solid #ccc; border-radius: 6px; }
        button { padding: 10px 12px; border: 0; border-radius: 6px; background: #c62828; color: #fff; cursor: pointer; }
        button:hover { background: #b71c1c; }
        a { color: #1565c0; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>Supprimer un Groupe</h1>
    
    <?php if (isset($_GET['msg'])): ?>
        <p style="color: green;">
            <?php echo htmlspecialchars($_GET['msg']); ?>
        </p>
    <?php endif; ?>
    <?php if (isset($_GET['error'])): ?>
        <p style="color: red;">
            <?php echo htmlspecialchars($_GET['error']); ?>
        </p>
    <?php endif; ?>
    
    <form method="POST" action="routeur.php?controleur=controleurGroupe&action=supprimer">
        <label for="num_g">Numéro du groupe à supprimer :</label>
        <input type="number" id="num_g" name="num_g" required>
        <button type="submit">Supprimer le groupe</button>
    </form>
</body>
</html>
