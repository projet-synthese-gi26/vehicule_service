#!/bin/bash

# Arrêter le script dès qu'une commande échoue
set -e

echo "🧹 Nettoyage des conteneurs dédiés à vehicle-service (s'ils existent)..."
# On ne touche PAS à fleet-management, on supprime juste les conteneurs de ce projet ci
docker rm -f vehicle-service-db vehicle-service-redis 2>/dev/null || true

echo "🐘 Démarrage de la Base de Données (vehicle-service-db)..."
# Utilisation du port standard 5432 (libre car tes autres conteneurs sont Exited)
docker run -d \
  --name vehicle-service-db \
  -p 5432:5432 \
  -e POSTGRES_USER=user \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=reactivedb \
  postgres:16-alpine

echo "🔴 Démarrage de Redis (vehicle-service-redis)..."
docker run -d \
  --name vehicle-service-redis \
  -p 6379:6379 \
  redis:alpine

echo "⏳ Attente de l'initialisation de la Base de Données (5s)..."
sleep 5

echo "🚀 Lancement de l'application (Sans Kafka)..."
# On exclut l'autoconfiguration Kafka pour ne pas avoir d'erreurs de connexion
chmod +x mvnw
./mvnw spring-boot:run \
  -Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration \
  -Dspring.kafka.bootstrap-servers=localhost:9092