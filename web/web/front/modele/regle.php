<?php
class Regle {
    public ?int $num;
    public ?string $intitule;
    public ?string $objectif;
    public ?Critere $critere;

    public function __construct(
        int $num = NULL,
        string $intitule = NULL,
        string $objectif = NULL,
        Critere $critere = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->intitule = $intitule;
            $this->objectif = $objectif;
            $this->critere = $critere;
        }
    }
}
?>
