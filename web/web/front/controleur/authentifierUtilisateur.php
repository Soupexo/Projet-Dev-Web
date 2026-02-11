<!DOCTYPE html>
<html>
    <head>
        <meta charset = "utf-8">
    </head>
    <body>
        <?php
            session_start();
            require_once("../modele/connexion.php");
            require_once("../modele/utilisateur.php");
            connexion::connect();
            echo "<pre>";
            print_r($_POST);
            echo "</pre>";
            $login   = $_POST["login"];
            $mdp     = $_POST["mdp"];
            $utilisateurA = new utilisateur($login, $mdp);
            if ($utilisateurA->checkMDP() == false){
                header("Location: ../vue/authentification.php");
            }
            else{
                $_SESSION["username"] = $login;
                header("Location: ../enseignant/notes.html");
            }
        ?>
    </body>
</html>
