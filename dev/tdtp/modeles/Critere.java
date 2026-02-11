package tdtp.modeles;

public class Critere {

    private Regle saRegle;
    private Contrainte saContrainte;
    private int num_c;
    private String intitule_c;
    private String type_c;

    public Critere(int num_c, String intitule_c, String type_c) {
        this.num_c = num_c;
        this.intitule_c = intitule_c;
        this.type_c = type_c;
    }
}