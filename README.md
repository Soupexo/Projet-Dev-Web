# Projet-Dev-Web

Projet consistant en l’implémentation d’un progiciel Java (Swing et AWT) de gestion complète consommant une API REST en PHP, accompagné d’un site web de gestion développé en PHP.
1. Le dossier **dev** contient les fichiers sources de l'application ainsi que certaines ressources complémentaires (script de téléchargement de la ressource gson.jar, README local ...)
2. Le dossier **web**, quant à lui, contient le site web en PHP avec son style, son API ...

## Téléchargement des dépendances

Si vous avez perdu le dossier `lib` et son JAR, téléchargez le fichier nécessaire :

- **Gson 2.8.9** :  
  [https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.9/gson-2.8.9.jar)

Copiez-le dans le dossier `lib` de votre projet.

---

## Construction du projet dev

1. **Ajout du code source**  
   Copiez le dossier `tdtp` dans le dossier `src` de votre projet Eclipse.

2. **Création du dossier lib**  
   Dans Eclipse : `New > Folder` → nommez-le `lib`.

3. **Ajout de la librairie Gson**  
   Copiez `gson-2.8.9.jar` dans le dossier `lib` (du projet eclipse).

4. **Configuration du Build Path dans Eclipse**  
   - Clic droit sur le projet → `Build Path` → `Configure Build Path`  
   - Onglet `Libraries` → `Add JARs...`  
   - Sélectionnez `lib/gson-2.8.9.jar` et validez.

5. **Lancement de l’application**  
   Exécutez la classe principale : ./src/tdtp/ApplicationTPTD.java

---

## Accès au projet web

### 1. Premier contact
Pour un premier accès au site web, vous pouvez vous diriger directement vers `routeur.php`.

### 2. Test de la plateforme
Les informations suivantes permettent de se connecter avec différents profils. Chaque couple login/mdp correspond à une responsabilité dotée de permissions différentes :

- login : `adminRespFormation` - mdp : `admin` (Responsabilité : enseignant responsable de formation, équivalent admin)  
- login : `enseignant` - mdp : `enseignant` (Responsabilité : enseignant classique)  
- login : `enseignantRespAnneeTest` - mdp : `enseignantRespAnneeTest` (Responsabilité : enseignant responsable d'année)  
- login : `enseignantRespFiliereTest` - mdp : `enseignantRespFiliereTest` (Responsabilité : enseignant responsable de filière)  
- login : `enseignantRespSemestreTest` - mdp : `enseignantRespSemestreTest` (Responsabilité : enseignant responsable de semestre)  
- login : `etudiantRespTest` - mdp : `etudiantRespTest` (Responsabilité : étudiant)

### 3. Précisions
- La vue principale (accueil `routeur.php`) affiche tous les étudiants affectés à un groupe.  
- La vue *Liste des étudiants détaillée* (pour les enseignants classiques) affiche les étudiants ayant au moins un redoublement et affectés à un groupe.

