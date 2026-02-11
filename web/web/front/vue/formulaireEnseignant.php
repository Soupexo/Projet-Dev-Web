<main>
    <section>
   <form action="routeur.php?controleur=controleurEnseignant&action=creer" method="post">

      <input type="hidden" name="role" value="enseignant">

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
        <label for="responsabilite">Responsabilité (optionnel)</label>
        <select name="responsabilite" id="responsabilite">
            <option value="">-- Aucune --</option>
            <?php if (!empty($responsabilites)): foreach ($responsabilites as $r): ?>
                <option value="<?= (int)$r['num_r'] ?>"><?= htmlspecialchars($r['intitule_r']) ?></option>
            <?php endforeach; endif; ?>
        </select>
      </div>
      
      <button type="submit">Créer l'enseignant</button>
      </form>
</section>
</main>