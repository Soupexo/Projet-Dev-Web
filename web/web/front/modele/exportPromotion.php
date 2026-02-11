<?php
// On vérifie si l'utilisateur a cliqué sur le bouton d'export
if (isset($_GET['export']) && $_GET['export'] === 'csv' && !empty($etudiants)) {
    
    // 1. Nettoyage du tampon de sortie pour éviter d'envoyer du HTML accidentellement
    ob_end_clean();
    
    // 2. Headers pour forcer le téléchargement
    header('Content-Type: text/csv; charset=utf-8');
    header('Content-Disposition: attachment; filename=liste_etudiants_' . date('Y-m-d') . '.csv');

    // 3. Ouverture du flux
    $output = fopen('php://output', 'w');

    // BOM UTF-8 pour Excel (gère les accents)
    fprintf($output, chr(0xEF).chr(0xBB).chr(0xBF));

    // 4. Entête du CSV
    fputcsv($output, ['Nom', 'Prénom', 'Email', 'Groupe', 'Année', 'Apprenti', 'Genre', 'Type Bac'], ';');

    // 5. Contenu du CSV
    foreach ($etudiants as $etudiant) {
        fputcsv($output, [
            $etudiant['nom_u'],
            $etudiant['prenom_u'],
            $etudiant['mail_u'],
            $etudiant['nom_g'],
            $etudiant['annee'],
            ($etudiant['est_apprenti_e'] ? 'Oui' : 'Non'),
            ($etudiant['genre_e'] ?? 'N/A'),
            $etudiant['type_bac_e']
        ], ';');
    }

    fclose($output);
    exit; // Stop tout le reste du script pour ne pas afficher le HTML
}
?>