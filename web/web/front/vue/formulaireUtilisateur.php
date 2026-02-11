<main>
    <section>
   <form action="routeur.php?controleur=controleurUtilisateur&action=creer" method="post">

      <div>
        <label for="login">Login</label>
        <input type="text" name="login" id="login" required>
      </div>

      <div>
        <label for="mdp">Mot de passe</label>
        <input type="password" name="mdp" id="mdp" required>
      </div>

      <div>
        <label for="nom">Nom</label>
        <input type="text" name="nom" id="nom" required>
      </div>

      <div>
        <label for="prenom">Prénom</label>
        <input type="text" name="prenom" id="prenom" required>
      </div>

      <div>
        <label for="mail">Email</label>
        <input type="email" name="mail" id="mail" required>
      </div>
      
      <button type="submit">Créer l'utilisateur</button>
      </form>
</section>
</main>