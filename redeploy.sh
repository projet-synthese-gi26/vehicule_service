# 1. Construire
docker build -t vehicle-service:latest .

# 2. Remplacer le conteneur
docker compose -f docker-compose.prod.yml up -d

# 3. (Optionnel) Nettoyer l'ancienne image pour gagner de la place disque
docker image prune -f