package tdtp.modeles;

import java.util.*;

public class algoRepartition {
    /* ========================================================= */
    /* ===================== OUTILS COMMUNS ==================== */
    /* ========================================================= */

    /**
     * Calcule les quotas cibles d'un groupe à partir des ratios globaux.
     * Ordre : [femmes, hommes, apprentis, initiaux]
     */
    private static int[] calculerQuotasCibles(Groupe g) {
        return new int[]{
                (int) Math.round(g.getNombre_etudiant_max_g() * Promotion.ratioFemme),
                (int) Math.round(g.getNombre_etudiant_max_g() * Promotion.ratioHomme),
                (int) Math.round(g.getNombre_etudiant_max_g() * Promotion.ratioApprenti),
                (int) Math.round(g.getNombre_etudiant_max_g() * Promotion.ratioInitial)
        };
    }

    /**
     * Compte les effectifs actuels d'un groupe.
     * Ordre : [femmes, hommes, apprentis, initiaux]
     */
    private static int[] compterEffectifs(Groupe g) {
        int femmes = 0, hommes = 0, apprentis = 0, initiaux = 0;

        for (Etudiant e : g.getSesEtudiants()) {
            if (e.getGenre_e().equals("F")) femmes++;
            else hommes++;

            if (e.getEst_apprenti()) apprentis++;
            else initiaux++;
        }
        return new int[]{femmes, hommes, apprentis, initiaux};
    }

    /**
     * Vérifie si un étudiant peut être ajouté en respectant les quotas restants.
     * Si oui, les quotas sont décrémentés.
     */
    private static boolean peutAjouterEtudiant(int[] quotasRestants, Etudiant e) {

        if (e.getGenre_e().equals("F") && quotasRestants[0] > 0) {
            if (e.getEst_apprenti() && quotasRestants[2] > 0) {
                quotasRestants[0]--;
                quotasRestants[2]--;
                return true;
            } else if (!e.getEst_apprenti() && quotasRestants[3] > 0) {
                quotasRestants[0]--;
                quotasRestants[3]--;
                return true;
            }
        }

        if (e.getGenre_e().equals("M") && quotasRestants[1] > 0) {
            if (e.getEst_apprenti() && quotasRestants[2] > 0) {
                quotasRestants[1]--;
                quotasRestants[2]--;
                return true;
            } else if (!e.getEst_apprenti() && quotasRestants[3] > 0) {
                quotasRestants[1]--;
                quotasRestants[3]--;
                return true;
            }
        }

        return false;
    }

    /* ========================================================= */
    /* ==================== GLOUTON SIMPLE ===================== */
    /* ========================================================= */

    /**
     * Algorithme glouton simple.
     * Les groupes sont remplis séquentiellement.
     *
     * @param etudiants étudiants à répartir
     * @param indexG groupe courant
     */
    public static List<Groupe> glouton(List<Etudiant> etudiants, int indexG, Collection<Groupe> groupes) {

        if (indexG >= groupes.size() || etudiants.isEmpty()) {
            return new ArrayList<>(groupes);
        }

        List<Groupe> groupesList = new ArrayList<>(groupes);
        Groupe g = groupesList.get(indexG);

        int[] quotas = calculerQuotasCibles(g);
        int[] effectifs = compterEffectifs(g);

        int[] restants = {
                quotas[0] - effectifs[0],
                quotas[1] - effectifs[1],
                quotas[2] - effectifs[2],
                quotas[3] - effectifs[3]
        };

        for (int i = etudiants.size() - 1;
             i >= 0 && g.getSesEtudiants().size() < g.getNombre_etudiant_max_g();
             i--) {

            Etudiant e = etudiants.get(i);

            if (peutAjouterEtudiant(restants, e)) {
                g.ajoutEtudiant(e);
                etudiants.remove(i);
            }
        }

        return glouton(etudiants, indexG + 1, groupes);
    }

    /* ========================================================= */
    /* ================= GLOUTON HARMONISÉ ===================== */
    /* ========================================================= */

    /**
     * Glouton harmonisé : ajoute toujours dans le groupe le moins rempli.
     */
    public static List<Groupe> gloutonHarmonise(List<Etudiant> etudiants, Collection<Groupe> groupes) {

        if (etudiants.isEmpty()) {
            return new ArrayList<>(groupes);
        }

        int indexMin = trouveGroupeAvecLeMoinsDetudiant(groupes);
        List<Groupe> groupesList = new ArrayList<>(groupes);
        Groupe g = groupesList.get(indexMin);

        if (g.getNombre_etudiant_max_g() == 0) {
            throw new IllegalArgumentException("Capacité du groupe nulle");
        }

        int[] quotas = calculerQuotasCibles(g);
        int[] effectifs = compterEffectifs(g);

        int[] restants = {
                quotas[0] - effectifs[0],
                quotas[1] - effectifs[1],
                quotas[2] - effectifs[2],
                quotas[3] - effectifs[3]
        };

        boolean ajoute = false;

        for (int i = etudiants.size() - 1;
             i >= 0 && g.getSesEtudiants().size() < g.getNombre_etudiant_max_g();
             i--) {

            Etudiant e = etudiants.get(i);

            if (peutAjouterEtudiant(restants, e)) {
                g.ajoutEtudiant(e);
                etudiants.remove(i);
                ajoute = true;
            }
        }

        if (!ajoute) return new ArrayList<>(groupes);

        return gloutonHarmonise(etudiants, groupes);
    }

    /**
     * Retourne l'indice du groupe le moins rempli.
     */
    public static int trouveGroupeAvecLeMoinsDetudiant(Collection<Groupe> groupes) {

        int min = Integer.MAX_VALUE;
        int index = -1;
        int currentIndex = 0;

        List<Groupe> groupesList = new ArrayList<>(groupes);
        
        for (Groupe g : groupesList) {

            if (g.getSesEtudiants().size() < g.getNombre_etudiant_max_g() &&
                g.getSesEtudiants().size() < min) {
                min = g.getSesEtudiants().size();
                index = currentIndex;
            }
            currentIndex++;
        }

        if (index == -1) {
            throw new IllegalStateException("Tous les groupes sont pleins");
        }

        return index;
    }

    /* ========================================================= */
    /* =================== SOMME DES ÉCARTS ==================== */
    /* ========================================================= */

    /**
     * Calcule la somme des écarts entre les ratios réels et attendus.
     */
    public static double somme_ecarts(Collection<Groupe> groupes) {

        double somme = 0;

        for (Groupe g : groupes) {

            int nb = g.getSesEtudiants().size();
            if (nb == 0) nb = g.getNombre_etudiant_max_g();

            int femmes = 0;
            int apprentis = 0;

            for (Etudiant e : g.getSesEtudiants()) {
                if (e.getGenre_e().equals("F")) femmes++;
                if (e.getEst_apprenti()) apprentis++;
            }

            double rF = (double) femmes / nb;
            double rA = (double) apprentis / nb;

            somme += Math.abs(Promotion.ratioFemme - rF);
            somme += Math.abs(Promotion.ratioApprenti - rA);
        }

        return somme;
    }
    
    /**
     * Variables globales pour sauvegarder la meilleure solution trouvée
     */
    private static List<List<Etudiant>> meilleureSolution = null;
    private static double meilleurScore = Double.MAX_VALUE;

    /**
     * Algorithme de force brute avec backtracking complet
     * Place chaque étudiant dans un groupe en testant toutes les combinaisons possibles, Complexité : O(n^m) avec n = nbGroupe et m = nbEtudiant
     * et garde celle qui minimise somme_ecarts
     * Auteur : Otman Benbouziane
     * @param etudiants Liste complète des étudiants à répartir
     * @param index Index de l'étudiant courant à placer (commence à 0)
     * @param groupes Liste des groupes disponibles
     */
    public static void bruteForce(List<Etudiant> etudiants, int index, Collection<Groupe> groupes) {
        // CAS DE BASE : tous les étudiants ont été placés
        if (index == etudiants.size()) {
            double score = somme_ecarts(groupes);
            if (score < meilleurScore) {
                meilleurScore = score;
                meilleureSolution = new ArrayList<>();
                for (Groupe g : groupes) {
                    meilleureSolution.add(new ArrayList<>(g.getSesEtudiants()));
                }
                
                System.out.println("Meilleur Score : " + String.format("%.4f", score));
            }
            return;
        }
        
        // Étudiant actuel à placer
        Etudiant etudiantActuel = etudiants.get(index);
        
        // OPTION 1 : Ne pas prendre cet étudiant maintenant
        bruteForce(etudiants, index + 1, groupes);
        
        // OPTION 2 : ESSAYER DE PLACER CET ÉTUDIANT DANS CHAQUE GROUPE
        for (Groupe groupe : groupes) {
            // Vérifier si le groupe n'est pas plein
            if (groupe.getSesEtudiants().size() < groupe.getNombre_etudiant_max_g()) {
                // Calculer les quotas actuels du groupe
                int femmesActuelles = 0, hommesActuels = 0, apprentisActuels = 0, initiauxActuels = 0;
                for (Etudiant e : groupe.getSesEtudiants()) {
                    if (e.getGenre_e().equals("F")) femmesActuelles++; else hommesActuels++;
                    if (e.getEst_apprenti()) apprentisActuels++; else initiauxActuels++;
                }
                
                // Calculer les quotas cibles
                int nbFemmeCible = (int) Math.round(groupe.getNombre_etudiant_max_g() * Promotion.ratioFemme);
                int nbHommeCible = (int) Math.round(groupe.getNombre_etudiant_max_g() * Promotion.ratioHomme);
                int nbApprentiCible = (int) Math.round(groupe.getNombre_etudiant_max_g() * Promotion.ratioApprenti);
                int nbInitialCible = (int) Math.round(groupe.getNombre_etudiant_max_g() * Promotion.ratioInitial);
                
                boolean peutAjouter = false;
                
                if (etudiantActuel.getGenre_e().equals("F")) {
                    if (femmesActuelles < nbFemmeCible) {
                        if (etudiantActuel.getEst_apprenti() && apprentisActuels < nbApprentiCible) {
                            peutAjouter = true;
                        } else if (!etudiantActuel.getEst_apprenti() && initiauxActuels < nbInitialCible) {
                            peutAjouter = true;
                        }
                    }
                } else { // Homme
                    if (hommesActuels < nbHommeCible) {
                        if (etudiantActuel.getEst_apprenti() && apprentisActuels < nbApprentiCible) {
                            peutAjouter = true;
                        } else if (!etudiantActuel.getEst_apprenti() && initiauxActuels < nbInitialCible) {
                            peutAjouter = true;
                        }
                    }
                }
                
                if (peutAjouter) {
                    // AJOUTER l'étudiant au groupe
                    groupe.ajoutEtudiant(etudiantActuel);
                    
                    // RÉCURSION : placer l'étudiant suivant
                    bruteForce(etudiants, index + 1, groupes);
                    
                    // BACKTRACKING : retirer l'étudiant pour essayer un autre groupe
                    groupe.retireEtudiant(etudiantActuel);
                }
            }
        }
    }

    /**
     * Lance l'algorithme de force brute et applique la meilleure solution trouvée
     * 
     * @param etudiants Liste des étudiants à répartir
     * @param groupes Liste des groupes disponibles
     */
    public static void lancerBruteForce(List<Etudiant> etudiants, Collection<Groupe> groupes) {
        // Réinitialiser les variables globales
        meilleureSolution = null;
        meilleurScore = Double.MAX_VALUE;
        
        // Vider tous les groupes
        for (Groupe g : groupes) {
            g.getSesEtudiants().clear();
            // Réinitialiser le nombre d'étudiants dans le groupe
            // Note: Il faudrait ajouter un setter pour nombre_etudiant_g dans la classe Groupe
            // g.setNombre_etudiant_g(0);
        }
        
        System.out.println("========================================");
        System.out.println("  Lancement du Brute Force");
        System.out.println("========================================");
        System.out.println("Nombre d'étudiants : " + etudiants.size());
        System.out.println("Nombre de groupes : " + groupes.size());
        System.out.println("Configurations à tester : ~" + 
                           (long)Math.pow(groupes.size(), etudiants.size()));
        System.out.println();
        
        long debut = System.currentTimeMillis();
        
        // LANCER LA RECHERCHE EXHAUSTIVE
        bruteForce(etudiants, 0, groupes);
        
        long fin = System.currentTimeMillis();
        
        // APPLIQUER LA MEILLEURE SOLUTION TROUVÉE
        if (meilleureSolution != null) {
            List<Groupe> groupesList = new ArrayList<>(groupes);
            for (int i = 0; i < groupesList.size(); i++) {
                groupesList.get(i).getSesEtudiants().clear();
                groupesList.get(i).getSesEtudiants().addAll(meilleureSolution.get(i));
            }
            
            System.out.println("\n========================================");
            System.out.println("  Résultat du Brute Force");
            System.out.println("========================================");
            System.out.println("Meilleur score (somme_ecarts) : " + String.format("%.4f", meilleurScore));
            System.out.println("Temps d'exécution : " + (fin - debut) + " ms");
            System.out.println("========================================\n");
        } else {
            System.out.println("\nERREUR : Aucune solution trouvée !");
        }
    }

    public static void afficherResultat(String titre, Collection<Groupe> groupes, double score, long temps) {

        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("                   " + titre);
        System.out.println("════════════════════════════════════════════════════════════");
        System.out.println("Temps : " + temps + " ms");
        System.out.println("Score : " + String.format("%.4f", score));
        System.out.println();

        for (Groupe g : groupes) {

            int nbFemmes = 0, nbHommes = 0, nbApprentis = 0, nbInitiaux = 0;

            for (Etudiant e : g.getSesEtudiants()) {
                if (e.getGenre_e().equals("F")) nbFemmes++; else nbHommes++;
                if (e.getEst_apprenti()) nbApprentis++; else nbInitiaux++;
            }

            int max = g.getNombre_etudiant_max_g();
            int reel = g.getSesEtudiants().size();

            System.out.println("┌─ Groupe " + g.getNom_g() + " (" + reel + "/" + max + ")");
            System.out.println("│ Femmes    : " + nbFemmes + " | Attendu ≈ " + Math.round(max * Promotion.ratioFemme));
            System.out.println("│ Hommes    : " + nbHommes + " | Attendu ≈ " + Math.round(max * Promotion.ratioHomme));
            System.out.println("│ Apprentis : " + nbApprentis + " | Attendu ≈ " + Math.round(max * Promotion.ratioApprenti));
            System.out.println("│ Initiaux  : " + nbInitiaux + " | Attendu ≈ " + Math.round(max * Promotion.ratioInitial));
            System.out.println("│ Étudiants :");

            for (Etudiant e : g.getSesEtudiants()) {
                System.out.println("│  • " + e.getNum_e() +
                        " | " + e.getGenre_e() +
                        " | " + (e.getEst_apprenti() ? "Apprenti" : "Initial"));
            }

            System.out.println("└────────────────────────────────────────────────────────\n");
        }
    }

    /* ========================================================= */
    /* ======================== MAIN =========================== */
    /* ========================================================= */

    public static void main(String[] args) {

        Random rand = new Random();
        List<Etudiant> etudiants = new ArrayList<>();

        int nbEtudiants = 15;
        int nbGroupes = 3;
        int capacite = nbEtudiants / nbGroupes;

        /* ================= GÉNÉRATION DES ÉTUDIANTS ================= */

        for (int i = 1; i <= nbEtudiants; i++) {
            String prenom = "Prenom" + i;
            String nom = "Nom" + i;
            String login = "login" + i;
            String genre = (rand.nextDouble() < 0.25) ? "F" : "M";
            boolean estApprenti = (rand.nextDouble() < 0.15);
            String typeBac = "S";
            String mail = prenom.toLowerCase() + "@mail.com";
            
            Etudiant etudiant = new Etudiant();
            etudiant.setGenre_e(genre);
            etudiant.setEst_apprenti(estApprenti);
            
            etudiants.add(etudiant);

            etudiants.add(etudiant);
        }

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║        COMPARAISON DES 3 ALGORITHMES DE RÉPARTITION        ║");
        System.out.println("║        " + nbEtudiants + " étudiants / " + nbGroupes + " groupes                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        /* ========================================================= */
        /* ===================== GLOUTON SIMPLE =================== */
        /* ========================================================= */

        List<Groupe> groupesGloutonSimple = new ArrayList<>();
        for (int i = 1; i <= nbGroupes; i++) {
            groupesGloutonSimple.add(new Groupe("G" + i, capacite));
        }

        long t1 = System.currentTimeMillis();
        glouton(new ArrayList<>(etudiants), 0, groupesGloutonSimple);
        long t2 = System.currentTimeMillis();

        double scoreGloutonSimple = somme_ecarts(groupesGloutonSimple);

        afficherResultat("GLOUTON SIMPLE", groupesGloutonSimple, scoreGloutonSimple, t2 - t1);

        /* ========================================================= */
        /* ===================== BRUTE FORCE ====================== */
        /* ========================================================= */

        List<Groupe> groupesBruteForce = new ArrayList<>();
        for (int i = 1; i <= nbGroupes; i++) {
            groupesBruteForce.add(new Groupe("G" + i, capacite));
        }

        long t5 = System.currentTimeMillis();
        lancerBruteForce(new ArrayList<>(etudiants), groupesBruteForce);
        long t6 = System.currentTimeMillis();

        double scoreBruteForce = somme_ecarts(groupesBruteForce);

        afficherResultat("BRUTE FORCE", groupesBruteForce, scoreBruteForce, t6 - t5);

        /* ========================================================= */
        /* ================= COMPARAISON FINALE =================== */
        /* ========================================================= */

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                   COMPARAISON FINALE                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        System.out.println("Glouton simple     : " + String.format("%.4f", scoreGloutonSimple));
        System.out.println("Brute force        : " + String.format("%.4f", scoreBruteForce));
    }
}