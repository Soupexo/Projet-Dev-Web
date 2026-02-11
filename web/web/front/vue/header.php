<?php
require_once __DIR__ . '/../modele/utilisateur.php';
Utilisateur::startSession();
?>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/contact.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/groupes.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/home.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/notes.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/promotions.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/sondages.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/vie-scolaire.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/tableau.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/groupesNonFinalises.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/sondagesCourants.css">
        <link rel="stylesheet" href="/saes3-obenbou/web/front/styles/header.css">
        <script src="/saes3-obenbou/web/front/scripts/script.js" defer></script>
        <script src="../scripts/script.js" defer></script>
        <title><?php echo "{$titre}"; ?></title>
    </head>
    <body>
        <header>
            <div class="header-container">
                <h1><a href="routeur.php">AppSaclay</a></h1>
                <button class="menu-toggle" aria-label="Ouvrir le menu">☰</button>
                <nav class="menu">
                    
                    <?php if (Utilisateur::isConnected()): ?>
                        <?php if (Utilisateur::estEtudiant(Utilisateur::getCurrentLogin())): ?>
                            <!-- Menu étudiant -->
                            <!--<a href="routeur.php?controleur=controleurEtudiant&action=listeInfos">Etudiants affectés</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeEtudiantsNonAffectes">Etudiants non affectés</a>-->
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeNotes">Notes étudiants</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeEtudiants">Liste étudiants</a>
                            <a href="routeur.php?controleur=controleurSondage&action=listeSondageCourants">Sondages courants</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaireReponseSondage">Répondre à un sondage</a>
                       
                        <?php elseif (Utilisateur::estResponsableFormation(Utilisateur::getCurrentLogin())): ?>
                            <a href="routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire">Commenter un groupe</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeInfos">Etudiants affectés</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeEtudiantsNonAffectes">Etudiants non affectés</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeNotes">Notes étudiants</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeCriteresDisciplinaires">Séparations étudiants</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeGroupesCovoiturage">Groupes covoiturage</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=listeReponsesSondage">Réponses sondage</a>
                            <a href="routeur.php?controleur=controleurGroupe&action=listeInfos">Informations groupes</a>
                            <a href="routeur.php?controleur=controleurGroupe&action=listeGroupesNonFinalises">Groupes non finalisés</a>
                            <a href="routeur.php?controleur=controleurPromotion&action=afficherFormulaire">Créer une promotion</a>
                            <a href="routeur.php?controleur=controleurEnseignant&action=listeInfos">Informations enseignants</a>
                            <a href="routeur.php?controleur=controleurSondage&action=listeSondageCourants">Sondages courants</a>
                            <a href="routeur.php?controleur=controleurEnseignant&action=afficherFormulaireCommentaire">Commenter un groupe</a>
                            <a href="routeur.php?controleur=controleurUtilisateur&action=afficherFormulaire">Créer un utilisateur</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaire">Créer un étudiant</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaireModification">Modifier un étudiant</a>
                            <a href="routeur.php?controleur=controleurEnseignant&action=afficherFormulaireModification">Modifier un enseignant</a>
                            <a href="routeur.php?controleur=controleurSondage&action=afficherFormulaire">Créer un sondage</a>
                            <a href="routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification">Modifier un groupe</a>
                            <a href="routeur.php?controleur=controleurEnseignant&action=afficherFormulaire">Créer un enseignant</a>
                            <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaireSuppression">Supprimer un étudiant</a>
                            <a href="routeur.php?controleur=controleurPromotion&action=afficherFormulaireModification">Modifier une promotion</a>
                            
                            <?php elseif (
                            Utilisateur::estResponsableAnnee(Utilisateur::getCurrentLogin())
                            || Utilisateur::estResponsableSemestre(Utilisateur::getCurrentLogin())
                            || Utilisateur::estResponsableFiliere(Utilisateur::getCurrentLogin())
                        ): ?>
                           <a href="routeur.php?controleur=controleurEtudiant&action=listeInfos">Étudiants affectés</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=listeEtudiantsNonAffectes">Étudiants non affectés</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=listeNotes">Notes étudiants</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=listeCriteresDisciplinaires">Séparations étudiants</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=listeGroupesCovoiturage">Groupes covoiturage</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=listeReponsesSondage">Réponses sondage</a>

                           <a href="routeur.php?controleur=controleurGroupe&action=listeInfos">Informations groupes</a>
                           <a href="routeur.php?controleur=controleurGroupe&action=listeGroupesNonFinalises">Groupes non finalisés</a>

                           <a href="routeur.php?controleur=controleurEnseignant&action=listeInfos">Informations enseignants</a>
                           <a href="routeur.php?controleur=controleurSondage&action=listeSondageCourants">Sondages courants</a>
                           <a href="routeur.php?controleur=controleurUtilisateur&action=afficherFormulaire">Créer un utilisateur</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaire">Créer un étudiant</a>

                           <a href="routeur.php?controleur=controleurGroupe&action=afficherFormulaire">Créer un groupe</a>

                           <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaireModification">Modifier un étudiant</a>
                           <a href="routeur.php?controleur=controleurEtudiant&action=afficherFormulaireSuppression">Supprimer un étudiant</a>

                           <a href="routeur.php?controleur=controleurGroupe&action=afficherFormulaireModification">Modifier un groupe</a>

                           <a href="routeur.php?controleur=controleurPromotion&action=afficherFormulaireModification">Modifier une promotion</a>

                           <a href="routeur.php?controleur=controleurSondage&action=afficherFormulaire">Créer un sondage</a>

                            
                            
                            
                            <?php elseif (Utilisateur::estEnseignant(Utilisateur::getCurrentLogin())): ?>
                            <!-- Menu enseignant -->
                             <a href="routeur.php?controleur=controleurEtudiant&action=listeEtudiantsPourEnseignant">Liste étudiants détaillées</a>
                        <?php endif; ?>
                    <?php endif; ?>
                </nav>
                <div class="user-section">
                    <?php if (Utilisateur::isConnected()): ?>
                        <?php Utilisateur::displayUserInfo(); ?>
                    <?php else: ?>
                        <a href="routeur.php?controleur=controleurUtilisateur&action=creerFormulaireAuthentification" class="login-btn">Se connecter</a>
                    <?php endif; ?>
                </div>
            </div>
        </header>