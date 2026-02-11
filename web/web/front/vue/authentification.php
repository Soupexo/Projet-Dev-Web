<main>
    <section class="form-container">
        <h2>Connexion</h2>
        
        <?php if (isset($_GET['erreur']) && $_GET['erreur'] == 1): ?>
            <div class="alert error">
                <p>Login ou mot de passe incorrect</p>
            </div>
        <?php endif; ?>
        
        <form action="../routeur.php?controleur=controleurUtilisateur&action=authentifier" method="post">
            <div class="form-group">
                <label for="login">Login :</label>
                <input type="text" id="login" name="login" required placeholder="Entrez votre login">
            </div>
            
            <div class="form-group">
                <label for="mdp">Mot de passe :</label>
                <input type="password" id="mdp" name="mdp" required placeholder="Entrez votre mot de passe">
            </div>
            
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Se connecter</button>
            </div>
        </form>
        
        <p class="form-links">
            <a href="../routeur.php?controleur=controleurUtilisateur&action=afficherFormulaire">Pas encore de compte ? Créer un compte</a>
        </p>
    </section>
</main>

<style>
.form-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 30px;
    background: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.form-container h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #333;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
    color: #555;
}

.form-group input {
    width: 100%;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 16px;
    box-sizing: border-box;
}

.form-group input:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 5px rgba(0,123,255,0.3);
}

.btn {
    width: 100%;
    padding: 12px;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.btn-primary {
    background-color: #007bff;
    color: white;
}

.btn-primary:hover {
    background-color: #0056b3;
}

.alert {
    padding: 15px;
    margin-bottom: 20px;
    border-radius: 5px;
    border: 1px solid transparent;
}

.alert.error {
    background-color: #f8d7da;
    border-color: #f5c6cb;
    color: #721c24;
}

.form-links {
    text-align: center;
    margin-top: 20px;
}

.form-links a {
    color: #007bff;
    text-decoration: none;
}

.form-links a:hover {
    text-decoration: underline;
}
</style>
