<?php
// controllers/BaseController.php
class BaseControleur {
    
    /**
     * Méthode pour rendre une vue avec des données
     * @param string $view Chemin de la vue (sans .php)
     * @param array $data Données à passer à la vue
     */
    protected function render($view, $data = []) {
        // Extrait les variables du tableau pour les rendre accessibles dans la vue
        extract($data);
        
        // Inclut le fichier de vue
        require_once "views/{$view}.php";
    }
    
    /**
     * Redirige vers une URL
     * @param string $url
     */
    protected function redirect($url) {
        header("Location: {$url}");
        exit();
    }
    
    /**
     * Retourne du JSON (pour les API)
     * @param mixed $data
     */
    protected function json($data) {
        header('Content-Type: application/json');
        echo json_encode($data);
        exit();
    }
}
?>