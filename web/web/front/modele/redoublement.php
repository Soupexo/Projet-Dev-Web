<?php
class Redoublement {
    public ?Etudiant $etudiant;
    public ?Annee $annee;

    public function __construct(
        Etudiant $etudiant = NULL,
        Annee $annee = NULL
    ) {
        if ($etudiant != NULL && $annee != NULL) {
            $this->etudiant = $etudiant;
            $this->annee = $annee;
        }
    }
}
?>
