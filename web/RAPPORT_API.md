# Rapport sur l'Architecture API du Projet web

## Introduction

Ce rapport présente l'architecture et l'implémentation de l'API du projet web, qui permet la communication entre l'application Java (client) et le backend PHP (serveur). L'API suit une architecture RESTful et utilise le format JSON pour l'échange de données.

## Architecture Générale

### 1. Structure Backend (PHP)

#### 1.1 Points d'Entrée API
Les API sont accessibles via des points d'entrée spécifiques dans le répertoire `front/api/` :

- `front/api/etudiant/index.php` - Gestion des étudiants
- `front/api/enseignant/index.php` - Gestion des enseignants  
- `front/api/groupe/index.php` - Gestion des groupes
- `front/api/promotion/index.php` - Gestion des promotions
- `front/api/sondage/index.php` - Gestion des sondages

#### 1.2 Modèles de Données
Les modèles PHP se trouvent dans `front/modele/` et implémentent la logique métier :

- `etudiant.php` - Classe Etudiant avec méthodes CRUD
- `enseignant.php` - Classe Enseignant avec méthodes CRUD
- `groupe.php` - Classe Groupe avec méthodes CRUD
- `promotion.php` - Classe Promotion avec méthodes CRUD
- `sondage.php` - Classe Sondage avec méthodes CRUD

#### 1.3 Contrôleurs
Les contrôleurs dans `front/controleur/` gèrent la logique de routage et d'affichage pour l'interface web.

### 2. Structure Client (Java)

#### 2.1 Client API Centralisé
La classe `ApiClient.java` centralise toutes les communications HTTP :

```java
public class ApiClient {
    private static final String BASE_URL = "https://projets.iut-orsay.fr/saes3-obenbou/web/front/api";
    private static final Gson gson = new Gson();
    
    public static String get(String path) // Requêtes GET
    public static String post(String path, Object body) // Requêtes POST
    public static String put(String path, Object body) // Requêtes PUT
    public static String delete(String path) // Requêtes DELETE
}
```

#### 2.2 Classes API Spécifiques
Chaque entité dispose de sa propre classe API :

- `EtudiantApi.java` - Opérations sur les étudiants
- `EnseignantApi.java` - Opérations sur les enseignants
- `GroupeApi.java` - Opérations sur les groupes
- `PromotionApi.java` - Opérations sur les promotions
- `SondageApi.java` - Opérations sur les sondages

## Fonctionnalités Implémentées

### 2. Opérations CRUD

#### 1.1 Création (POST)
```php
// Exemple : Création étudiant
case 'POST':
    $data = json_decode(file_get_contents('php://input'), true);
    $ok = Etudiant::create($data);
    echo json_encode(["success" => $ok]);
```

#### 1.2 Lecture (GET)
```php
// Exemple : Liste des étudiants
case 'GET':
    echo json_encode(Etudiant::getInfosEtudiants());
```

#### 1.3 Mise à Jour (PUT)
```php
// Exemple : Mise à jour enseignant
case 'PUT':
    $data = json_decode(file_get_contents('php://input'), true);
    $ok = Enseignant::update($login, $data);
    echo json_encode(["success" => $ok]);
```

#### 1.4 Suppression (DELETE)
```php
// Exemple : Suppression groupe
case 'DELETE':
    $rows = Groupe::delete($id);
    echo json_encode(["success" => $rows > 0]);
```
```



