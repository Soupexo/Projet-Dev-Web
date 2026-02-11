<main>
    <section>
   <form action="routeur.php?controleur=controleurEtudiant&action=creer" method="post">

      <input type="hidden" name="role" value="etudiant">

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

      <div>
        <label for="est_apprenti_e">Est apprenti</label>
        <input type="checkbox" name="est_apprenti_e" id="est_apprenti_e" value="1">
      </div>

      <div>
        <label for="type_bac_e">Type de bac</label>
        <input type="text" name="type_bac_e" id="type_bac_e">
      </div>

      <div>
        <label for="genre_e">Genre</label>
        <select name="genre_e" id="genre_e">
          <option value="M">Homme</option>
          <option value="F">Femme</option>
          <option value="O">Autre</option>
        </select>
      </div>
      
      <button type="submit">Créer l'étudiant</button>
      </form>
</section>
</main>