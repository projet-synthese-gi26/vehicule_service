# 🛠 Roadmap Refactoring : Fleet & Geofence API

## Étape 1 : Stabilisation de l'Infrastructure
- [x] **Configurer le CORS** : Autoriser les requêtes cross-origin pour permettre au frontend de communiquer avec l'API (Global config).
- [x] **Vérification Schéma SQL** : S'assurer que le fichier `schema.sql` est cohérent avec les nouvelles fonctionnalités attendues (cascades, contraintes).

## Étape 2 : Logique Métier "Smart Creation"
- [x] **Implémenter le pattern "Get or Create"** : Modifier `VehicleSmartCreationService` pour qu'il ne renvoie plus d'erreur si une marque/modèle n'existe pas, mais la crée à la volée.le faire sur uen nouvelle route dediee.
- [x] **Optimisation Réactive** : S'assurer que ces créations en chaîne se font proprement avec Reactor (chaining `switchIfEmpty`).

## Étape 3 : Complétude du CRUD Véhicule
- [ ] **Endpoint PATCH** : Créer la route et la logique pour la mise à jour partielle d'un véhicule (ex: changer juste le kilométrage).
- [ ] **Endpoint GET (Détails)** : Vérifier que le `getById` renvoie bien toutes les infos agrégées (options, images) et pas juste l'objet brut.Et faire le `getById` sur chaque table

## Étape 4 : Gestion des Médias (Images)
- [ ] **Service de Stockage** : Implémenter un service simple pour sauvegarder les fichiers (MultipartFile) sur le disque local.
- [ ] **Endpoint Upload** : Créer la route `POST /vehicles/{id}/images` pour uploader et lier une image à un véhicule.

## Étape 5 : Validation & Tests
- [ ] **Test Swagger** : Valider manuellement chaque endpoint via Swagger UI.
- [ ] **Nettoyage** : Supprimer le code mort ou les TODOs obsolètes.

---
