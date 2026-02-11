<?php
class EtudiantNote {
    public ?Etudiant $etudiant;
    public ?string $intituleNote;
    public ?float $moySemestre1;
    public ?float $moySemestre2;
    public ?float $moyAnnee;

    public function __construct(
        Etudiant $etudiant = NULL,
        string $intituleNote = NULL,
        float $moySemestre1 = NULL,
        float $moySemestre2 = NULL,
        float $moyAnnee = NULL
    ) {
        if ($etudiant != NULL) {
            $this->etudiant = $etudiant;
            $this->intituleNote = $intituleNote;
            $this->moySemestre1 = $moySemestre1;
            $this->moySemestre2 = $moySemestre2;
            $this->moyAnnee = $moyAnnee;
        }
    }
}
?>
