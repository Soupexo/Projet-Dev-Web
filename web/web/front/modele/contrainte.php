<?php
class Contrainte {
    public ?int $num;
    public ?string $intitule;

    public function __construct(
        int $num = NULL,
        string $intitule = NULL
    ) {
        if ($num != NULL) {
            $this->num = $num;
            $this->intitule = $intitule;
        }
    }
}
?>
