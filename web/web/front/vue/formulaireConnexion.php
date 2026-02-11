<main>
            <section>
                <form action="routeur.php?controleur=controleurUtilisateur&action=authentifier" method="post">
                    <legend>Connectez-vous</legend>
                    <input type="text" id="last-name" name="login" placeholder="login" />
                    <input type="password" id="mail" name="mdp" placeholder="mot de passe" />
                    <input type="submit" id="envoyer" name="validation" value="Envoyer"/>
                </form>
            </section>
        </main>