-- INSERTION DES DONNÉES DE RÉFÉRENCE (Seulement si la table est vide ou pour éviter les doublons)

-- 1. Fuel Types
INSERT INTO FuelType (fuel_type_name) VALUES ('Essence') ON CONFLICT DO NOTHING;
INSERT INTO FuelType (fuel_type_name) VALUES ('Diesel') ON CONFLICT DO NOTHING;
INSERT INTO FuelType (fuel_type_name) VALUES ('Electrique') ON CONFLICT DO NOTHING;
INSERT INTO FuelType (fuel_type_name) VALUES ('Hybride') ON CONFLICT DO NOTHING;

-- 2. Manufacturers
INSERT INTO Manufacturer (manufacturer_name) VALUES ('Toyota Factory') ON CONFLICT DO NOTHING;
INSERT INTO Manufacturer (manufacturer_name) VALUES ('Tesla Gigafactory') ON CONFLICT DO NOTHING;
INSERT INTO Manufacturer (manufacturer_name) VALUES ('BMW Group') ON CONFLICT DO NOTHING;

-- 3. Vehicle Makes (Marques)
INSERT INTO VehicleMake (make_name) VALUES ('Toyota') ON CONFLICT DO NOTHING;
INSERT INTO VehicleMake (make_name) VALUES ('Honda') ON CONFLICT DO NOTHING;
INSERT INTO VehicleMake (make_name) VALUES ('Ford') ON CONFLICT DO NOTHING;
INSERT INTO VehicleMake (make_name) VALUES ('Tesla') ON CONFLICT DO NOTHING;
INSERT INTO VehicleMake (make_name) VALUES ('BMW') ON CONFLICT DO NOTHING;
INSERT INTO VehicleMake (make_name) VALUES ('Mercedes') ON CONFLICT DO NOTHING;

-- 4. Transmission Types
INSERT INTO TransmissionType (type_name) VALUES ('Manuelle') ON CONFLICT DO NOTHING;
INSERT INTO TransmissionType (type_name) VALUES ('Automatique') ON CONFLICT DO NOTHING;
INSERT INTO TransmissionType (type_name) VALUES ('Séquentielle') ON CONFLICT DO NOTHING;

-- 5. Vehicle Sizes
INSERT INTO VehicleSize (size_name) VALUES ('Compacte') ON CONFLICT DO NOTHING;
INSERT INTO VehicleSize (size_name) VALUES ('SUV') ON CONFLICT DO NOTHING;
INSERT INTO VehicleSize (size_name) VALUES ('Berline') ON CONFLICT DO NOTHING;
INSERT INTO VehicleSize (size_name) VALUES ('Utilitaire') ON CONFLICT DO NOTHING;

-- 6. Vehicle Types
INSERT INTO VehicleType (type_name) VALUES ('Personnel') ON CONFLICT DO NOTHING;
INSERT INTO VehicleType (type_name) VALUES ('Commercial') ON CONFLICT DO NOTHING;
INSERT INTO VehicleType (type_name) VALUES ('Transport') ON CONFLICT DO NOTHING;

-- 7. Vehicle Models (Simplifié)
INSERT INTO VehicleModel (model_name) VALUES ('Corolla') ON CONFLICT DO NOTHING;
INSERT INTO VehicleModel (model_name) VALUES ('Yaris') ON CONFLICT DO NOTHING;
INSERT INTO VehicleModel (model_name) VALUES ('Model 3') ON CONFLICT DO NOTHING;
INSERT INTO VehicleModel (model_name) VALUES ('X5') ON CONFLICT DO NOTHING;
INSERT INTO VehicleModel (model_name) VALUES ('Civic') ON CONFLICT DO NOTHING;