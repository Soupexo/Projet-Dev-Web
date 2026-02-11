<!DOCTYPE html>
<html>
    <head>
        <meta charset = "utf-8">
    </head>
<body>
    <?php
         require_once("./connexion.php");
         require_once("./utilisateur.php");
            connexion::connect();
            echo "<pre>";
            print_r($_POST);
            echo "</pre>";
            $login   = $_POST["login"];
            $mdp     = $_POST["mdp"];
            $nom     = $_POST["nom"];
            $prenom  = $_POST["prenom"];
            $email   = $_POST["email"];
            $utilisateurA = new utilisateur($login, $mdp, $nom, $prenom, $email);
            $result = utilisateur::addUtilisateur($login, $mdp, $nom, $prenom, $email);
            if (!($result)){
                $message="Erreur lors de l'insertion";
                echo '<script type="text/javascript"> window.alert("ERROR : '.$message.'");</script>';
            }
            echo "<p> $utilisateurA </p>";
        ?>
    </body>
</html>