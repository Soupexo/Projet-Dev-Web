<?php

require_once 'config/connexion.php';
Connexion::connect();

$tabControleurs = [
    'controleurEtudiant'    => 'ControleurEtudiant',
    'controleurGroupe'      => 'ControleurGroupe',
    'controleurEnseignant'  => 'ControleurEnseignant',
    'controleurSondage'     => 'ControleurSondage',
    'controleurUtilisateur' => 'ControleurUtilisateur', 
    'controleurPromotion'   => 'ControleurPromotion'
];

// Actions par défaut pour chaque contrôleur
$actionsParDefaut = [
    'controleurEtudiant'    => 'listeInfos',
    'controleurGroupe'      => 'liste',
    'controleurEnseignant'  => 'listeInfos',
    'controleurSondage'     => 'liste',
    'controleurUtilisateur' => 'liste',
    'controleurPromotion'   => 'liste'
];

// Contrôleur par défaut
$controleurParam = 'controleurEtudiant';

// Récupérer le contrôleur depuis l'URL
if (isset($_GET['controleur']) && array_key_exists($_GET['controleur'], $tabControleurs)) {
    $controleurParam = $_GET['controleur'];
}

// Action par défaut selon le contrôleur
$action = $actionsParDefaut[$controleurParam];

$controleurClasse = $tabControleurs[$controleurParam];

require_once "controleur/$controleurParam.php";

if (!class_exists($controleurClasse)) {
    die("Erreur : La classe '$controleurClasse' n'existe pas.");
}

$methodes = get_class_methods($controleurClasse);

// Récupérer l'action depuis l'URL si elle existe et est valide
if (isset($_GET['action']) && in_array($_GET['action'], $methodes)) {
    $action = $_GET['action'];
}

// Vérifier que l'action existe avant de l'exécuter
if (in_array($action, $methodes)) {
    $controleurClasse::$action();
} else {
    die("Erreur : La méthode '$action' n'existe pas dans '$controleurClasse'.");
}