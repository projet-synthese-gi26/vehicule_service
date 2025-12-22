#!/bin/bash

# Configuration
BASE_URL="http://localhost:8080"
COMPOSE_FILE="bin/compose.yaml"

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting API Integration Tests...${NC}"

# Helper function to extract UUID from JSON response
# Usage: extract_id "json_string" "key_name"
extract_id() {
    local json=$1
    local key=$2
    # Use grep -oP if available, otherwise sed
    if echo "$json" | grep -q "$key"; then
        # Simple sed extraction for "key":"value"
        echo "$json" | sed -n "s/.*\"$key\":\"\([^\"]*\)\".*/\1/p"
    else
        echo ""
    fi
}

# Helper function to make POST request
make_post() {
    local endpoint=$1
    local body=$2
    echo -e "\n${GREEN}POST $endpoint${NC}" >&2
    echo "Body: $body" >&2
    # curl -v sends verbose to stderr (visible), body to stdout (captured)
    response=$(curl -v -X POST "$BASE_URL$endpoint" \
        -H "Content-Type: application/json" \
        -d "$body")
    echo "Response Body: $response" >&2
    echo "$response"
}

# 1. Create FuelType
echo -e "\n--- 1. Creating FuelType ---"
FUEL_RES=$(make_post "/vehicles/lookup/fuel-types" '{"fuelTypeName": "Diesel_Test"}')
FUEL_ID=$(extract_id "$FUEL_RES" "fuelTypeId")

if [ -z "$FUEL_ID" ]; then echo -e "${RED}Failed to create FuelType${NC}"; exit 1; fi
echo "FuelType ID: $FUEL_ID"

# 2. Create Manufacturer
echo -e "\n--- 2. Creating Manufacturer ---"
MAN_RES=$(make_post "/vehicles/lookup/manufacturers" '{"manufacturerName": "Toyota_Test"}')
MAN_ID=$(extract_id "$MAN_RES" "manufacturerId")
echo "Manufacturer ID: $MAN_ID"

# 3. Create TransmissionType
echo -e "\n--- 3. Creating TransmissionType ---"
TRANS_RES=$(make_post "/vehicles/lookup/transmission-types" '{"typeName": "Automatic_Test"}')
TRANS_ID=$(extract_id "$TRANS_RES" "transmissionTypeId")
echo "TransmissionType ID: $TRANS_ID"

# 4. Create VehicleMake
echo -e "\n--- 4. Creating VehicleMake ---"
MAKE_RES=$(make_post "/vehicles/lookup/vehicle-makes" '{"makeName": "Corolla_Test"}')
MAKE_ID=$(extract_id "$MAKE_RES" "vehicleMakeId")
echo "VehicleMake ID: $MAKE_ID"

# 5. Create VehicleModel
echo -e "\n--- 5. Creating VehicleModel ---"
MODEL_RES=$(make_post "/vehicles/lookup/vehicle-models" "{\"vehicleMakeId\": \"$MAKE_ID\", \"modelName\": \"2024_Test\"}")
MODEL_ID=$(extract_id "$MODEL_RES" "vehicleModelId")
echo "VehicleModel ID: $MODEL_ID"

# 6. Create VehicleSize
echo -e "\n--- 6. Creating VehicleSize ---"
SIZE_RES=$(make_post "/vehicles/lookup/vehicle-sizes" '{"sizeName": "Compact_Test"}')
SIZE_ID=$(extract_id "$SIZE_RES" "vehicleSizeId")
echo "VehicleSize ID: $SIZE_ID"

# 7. Create VehicleType
echo -e "\n--- 7. Creating VehicleType ---"
TYPE_RES=$(make_post "/vehicles/lookup/vehicle-types" '{"typeName": "Sedan_Test"}')
TYPE_ID=$(extract_id "$TYPE_RES" "vehicleTypeId")
echo "VehicleType ID: $TYPE_ID"

# 8. Create Vehicle
echo -e "\n--- 8. Creating Vehicle ---"
VEHICLE_BODY=$(cat <<EOF
{
  "vehicleMakeId": "$MAKE_ID",
  "vehicleModelId": "$MODEL_ID",
  "transmissionTypeId": "$TRANS_ID",
  "manufacturerId": "$MAN_ID",
  "vehicleSizeId": "$SIZE_ID",
  "vehicleTypeId": "$TYPE_ID",
  "fuelTypeId": "$FUEL_ID",
  "vehicleSerialNumber": "SN-$(date +%s)",
  "registrationNumber": "REG-$(date +%s)",
  "tankCapacity": 50,
  "luggageMaxCapacity": 400,
  "totalSeatNumber": 5,
  "averageFuelConsumptionPerKm": 0.05,
  "mileageAtStart": 0,
  "mileageSinceCommissioning": 100,
  "vehicleAgeAtStart": 0,
  "brand": "Toyota"
}
EOF
)
VEHICLE_RES=$(make_post "/vehicles" "$VEHICLE_BODY")
VEHICLE_ID=$(extract_id "$VEHICLE_RES" "vehicleId")

if [ -z "$VEHICLE_ID" ]; then echo -e "${RED}Failed to create Vehicle${NC}"; exit 1; fi
echo "Vehicle ID: $VEHICLE_ID"

# 9. Create Party (Direct DB Insert)
echo -e "\n--- 9. Creating Party (Direct DB Insert) ---"
PARTY_ID=$(uuidgen || cat /proc/sys/kernel/random/uuid)
echo "Generated Party ID: $PARTY_ID"

# Check if docker compose is running and insert party
if docker compose -f "$COMPOSE_FILE" ps | grep -q "postgres"; then
    docker compose -f "$COMPOSE_FILE" exec -T postgres psql -U user -d reactivedb -c "INSERT INTO Party (party_id, party_type, display_name) VALUES ('$PARTY_ID', 'FREELANCE_DRIVER', 'Test Driver') ON CONFLICT DO NOTHING;"
    echo "Party inserted into DB."
else
    echo -e "${RED}Postgres container not found. Cannot insert Party. Skipping Ownership test.${NC}"
    PARTY_ID=""
fi

# 10. Create Ownership
if [ -n "$PARTY_ID" ]; then
    echo -e "\n--- 10. Creating VehicleOwnership ---"
    OWNERSHIP_BODY=$(cat <<EOF
{
  "vehicleId": "$VEHICLE_ID",
  "partyId": "$PARTY_ID",
  "usageRole": "OWNER",
  "isPrimary": true,
  "validFrom": "$(date -u +"%Y-%m-%dT%H:%M:%SZ")"
}
EOF
    )
    OWNERSHIP_RES=$(make_post "/vehicles/ownerships" "$OWNERSHIP_BODY")
    OWNERSHIP_ID=$(extract_id "$OWNERSHIP_RES" "vehicleOwnershipId")
    echo "Ownership ID: $OWNERSHIP_ID"
fi

# 11. Get Vehicle Details (Lookup + Ownership)
echo -e "\n--- 11. Getting Vehicle Details ---"
echo -e "${GREEN}GET /vehicles/$VEHICLE_ID/details${NC}"
curl -s -X GET "$BASE_URL/vehicles/$VEHICLE_ID/details" | python3 -m json.tool || echo "Failed to parse JSON"

echo -e "\n\n${GREEN}Test Script Completed.${NC}"
