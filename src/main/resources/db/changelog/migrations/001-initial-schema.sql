-- Initial schema for Vehicle Service
-- Extracted from schema.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Reference Tables
CREATE TABLE VehicleModel (
    vehicle_model_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    model_name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE TransmissionType (
    transmission_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE VehicleMake (
    vehicle_make_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    make_name TEXT NOT NULL UNIQUE
);

CREATE TABLE VehicleSize (
    vehicle_size_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    size_name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

CREATE TABLE FuelType (
    fuel_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    fuel_type_name TEXT NOT NULL UNIQUE
);

CREATE TABLE VehicleType (
    vehicle_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type_name TEXT NOT NULL UNIQUE
);

CREATE TABLE Manufacturer (
    manufacturer_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    manufacturer_name TEXT NOT NULL UNIQUE
);

-- Parties
CREATE TABLE Party (
    party_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    party_type TEXT NOT NULL CHECK (party_type IN ('FREELANCE_DRIVER', 'FREELANCE_DELIVERER', 'RENTAL_AGENCY', 'TRAVEL_AGENCY', 'DELIVERY_AGENCY')),
    display_name TEXT NOT NULL,
    phone TEXT,
    email TEXT
);

-- Main Vehicle Table
CREATE TABLE Vehicle (
    vehicle_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_make_id UUID REFERENCES VehicleMake(vehicle_make_id),
    vehicle_model_id UUID REFERENCES VehicleModel(vehicle_model_id),
    transmission_type_id UUID REFERENCES TransmissionType(transmission_type_id),
    manufacturer_id UUID REFERENCES Manufacturer(manufacturer_id),
    vehicle_size_id UUID REFERENCES VehicleSize(vehicle_size_id),
    vehicle_type_id UUID REFERENCES VehicleType(vehicle_type_id),
    fuel_type_id UUID REFERENCES FuelType(fuel_type_id),
    vehicle_serial_number TEXT UNIQUE,
    vehicle_serial_photo TEXT,
    registration_number TEXT,
    registration_photo TEXT,
    registration_expiry_date TIMESTAMPTZ,
    tank_capacity NUMERIC,
    luggage_max_capacity NUMERIC,
    total_seat_number INTEGER,
    average_fuel_consumption_per_km NUMERIC,
    mileage_at_start NUMERIC,
    mileage_since_commissioning NUMERIC,
    vehicle_age_at_start NUMERIC,
    brand TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ
);

-- Detail Tables
CREATE TABLE VehicleAmenity (
    vehicle_amenity_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    amenity_name TEXT NOT NULL,
    UNIQUE (vehicle_id, amenity_name)
);

CREATE TABLE VehicleInclusion (
    vehicle_inclusion_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    inclusion_name TEXT NOT NULL,
    is_included BOOLEAN NOT NULL DEFAULT true,
    UNIQUE (vehicle_id, inclusion_name)
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
    review_id UUID PRIMARY KEY,
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE
);

-- Ownership Table
CREATE TABLE VehicleOwnership (
    vehicle_ownership_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vehicle_id UUID NOT NULL REFERENCES Vehicle(vehicle_id) ON DELETE CASCADE,
    party_id UUID NOT NULL,
    usage_role TEXT NOT NULL CHECK (usage_role IN ('DRIVER', 'LOGISTICS', 'FLEET', 'OWNER')),
    is_primary BOOLEAN DEFAULT false,
    valid_from TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    valid_to TIMESTAMPTZ
);

CREATE UNIQUE INDEX idx_unique_primary_vehicle_per_role ON VehicleOwnership (party_id, usage_role) WHERE is_primary = true;
