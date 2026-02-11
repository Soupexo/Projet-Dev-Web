<?php
class Critere {
    public ?int $num;
    public ?string $intitule;
    public ?string $type;
    public ?Contrainte $contrainte;

    public function __construct(
        int $num = NULL,
        string $intitule = NULL,
        string $type = NULL,
        Contrainte $contrainte = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->intitule = $intitule;
            $this->type = $type;
            $this->contrainte = $contrainte;
        }
    }
}
?>
