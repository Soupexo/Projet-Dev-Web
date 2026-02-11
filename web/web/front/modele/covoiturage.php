<?php
class Covoiturage {
    public ?Etudiant $etudiant1;
    public ?Etudiant $etudiant2;

    public function __construct(
        Etudiant $etudiant1 = NULL,
        Etudiant $etudiant2 = NULL
    ) {
        if ($etudiant1 != NULL && $etudiant2 != NULL) {
            $this->etudiant1 = $etudiant1;
            $this->etudiant2 = $etudiant2;
        }
    }
}
?>
