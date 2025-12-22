/*CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS products CASCADE;

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    price DECIMAL(10, 2),
    status VARCHAR(50)
);*/

-- Active l'extension pour générer des UUIDs, si elle n'est pas déjà active.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Supprime les anciennes tables dans l'ordre inverse de la création pour éviter les erreurs de dépendance.
-- CASCADE supprime automatiquement les contraintes qui dépendent de ces tables.
DROP TABLE IF EXISTS VehicleOwnership CASCADE;
DROP TABLE IF EXISTS VehicleReview CASCADE;
DROP TABLE IF EXISTS VehicleIllustrationImage CASCADE;
DROP TABLE IF EXISTS VehicleCanTransport CASCADE;
DROP TABLE IF EXISTS VehicleKeyword CASCADE;
DROP TABLE IF EXISTS VehicleAmenity CASCADE;
DROP TABLE IF EXISTS Vehicle CASCADE;
DROP TABLE IF EXISTS Party CASCADE;
DROP TABLE IF EXISTS Manufacturer CASCADE;
DROP TABLE IF EXISTS VehicleType CASCADE;
DROP TABLE IF EXISTS FuelType CASCADE;
DROP TABLE IF EXISTS VehicleSize CASCADE;
DROP TABLE IF EXISTS VehicleMake CASCADE;
DROP TABLE IF EXISTS TransmissionType CASCADE;
DROP TABLE IF EXISTS VehicleModel CASCADE;


-- ================================================================================================
-- TABLES DE RÉFÉRENCE (Lookup Tables)
-- Celles-ci doivent être créées en premier car la table Vehicle en dépend.
-- ================================================================================================

CREATE TABLE VehicleModel (
    vehicle_model_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    model_name TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE TransmissionType (
    transmission_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_name TEXT NOT NULL, -- Ex: 'Manual 2WD', 'Automatic 4WD'
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE VehicleMake (
    vehicle_make_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    make_name TEXT NOT NULL -- 'Brand'
);

CREATE TABLE VehicleSize (
    vehicle_size_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    size_name TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE FuelType (
    fuel_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fuel_type_name TEXT NOT NULL -- Ex: 'petrol', 'diesel', 'electric', 'hybrid'
);

CREATE TABLE VehicleType (
    vehicle_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_name TEXT NOT NULL -- Ex: 'commercial', 'sport', 'personal'
);

CREATE TABLE Manufacturer (
    manufacturer_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    manufacturer_name TEXT NOT NULL -- Ex: 'Toyota', 'Renault', 'Mercedes'
);

-- ================================================================================================
-- TABLE POUR LES PROPRIÉTAIRES / UTILISATEURS (Parties)
-- Utilise une approche "Single Table Inheritance" pour les différents types de Party.
-- ================================================================================================

CREATE TABLE Party (
    party_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    party_type TEXT NOT NULL CHECK (party_type IN ('FREELANCE_DRIVER', 'FREELANCE_DELIVERER', 'RENTAL_AGENCY', 'TRAVEL_AGENCY', 'DELIVERY_AGENCY')),
    display_name TEXT NOT NULL,
    phone TEXT,
    email TEXT
);


-- ================================================================================================
-- TABLE PRINCIPALE (Vehicle)
-- Le cœur du domaine.
-- ================================================================================================

CREATE TABLE Vehicle (
    vehicle_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- Clés étrangères vers les tables de référence
    vehicle_make_id UUID REFERENCES VehicleMake(vehicle_make_id),
    vehicle_model_id UUID REFERENCES VehicleModel(vehicle_model_id),
    transmission_type_id UUID REFERENCES TransmissionType(transmission_type_id),
    manufacturer_id UUID REFERENCES Manufacturer(manufacturer_id),
    vehicle_size_id UUID REFERENCES VehicleSize(vehicle_size_id),
    vehicle_type_id UUID REFERENCES VehicleType(vehicle_type_id),
    fuel_type_id UUID REFERENCES FuelType(fuel_type_id),

    -- Informations d'identification du véhicule
    vehicle_serial_number TEXT UNIQUE,
    vehicle_serial_photo TEXT,      -- URL ou chemin vers le fichier
    registration_number TEXT,
    registration_photo TEXT,        -- URL ou chemin vers le fichier
    registration_expiry_date TIMESTAMPTZ,

    -- Caractéristiques techniques
    tank_capacity NUMERIC,
    luggage_max_capacity NUMERIC,
    total_seat_number INTEGER,

    -- Données de consommation et d'usage
    average_fuel_consumption_per_km NUMERIC,
    mileage_at_start NUMERIC,
    mileage_since_commissioning NUMERIC,
    vehicle_age_at_start NUMERIC,

    -- Métadonnées
    brand TEXT, -- Peut être redondant avec VehicleMake, mais conservé du diagramme
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);


-- ================================================================================================
-- TABLES DE DÉTAILS (Join Tables)
-- Lient des informations additionnelles au véhicule.
-- ================================================================================================

CREATE TABLE VehicleAmenity (
    vehicle_amenity_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    amenity_name TEXT NOT NULL,
    UNIQUE (vehicle_id, amenity_name)
);

CREATE TABLE VehicleKeyword (
    vehicle_keyword_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    keyword TEXT NOT NULL,
    UNIQUE (vehicle_id, keyword)
);

CREATE TABLE VehicleCanTransport (
    vehicle_can_transport_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    item TEXT NOT NULL,
    UNIQUE (vehicle_id, item)
);

CREATE TABLE VehicleIllustrationImage (
    vehicle_illustration_image_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    image_path TEXT NOT NULL,
    UNIQUE (vehicle_id, image_path)
);

CREATE TABLE VehicleReview (
    review_id UUID PRIMARY KEY, -- ID unique de la review (provenant d'un service externe)
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE
);

-- ================================================================================================
-- TABLE DE LIEN ENTRE VÉHICULES ET PROPRIÉTAIRES
-- ================================================================================================

CREATE TABLE VehicleOwnership (
    vehicle_ownership_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    -- party_id est l'ID de l'utilisateur du service d'authentification externe
    -- Pas de clé étrangère car géré par un service externe
    party_id UUID NOT NULL,
    usage_role TEXT NOT NULL CHECK (usage_role IN ('DRIVER', 'LOGISTICS', 'FLEET', 'OWNER')),
    is_primary BOOLEAN DEFAULT false,
    valid_from TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    valid_to TIMESTAMPTZ
);

-- Index pour garantir qu'une Party n'a qu'un seul véhicule principal pour un rôle donné
CREATE UNIQUE INDEX idx_unique_primary_vehicle_per_role ON VehicleOwnership (party_id, usage_role) WHERE is_primary = true;
