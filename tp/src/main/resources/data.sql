-- Données initiales pour le profil DEV (H2)
-- Produits
INSERT INTO products (name, price, description) VALUES
                                                    ('Laptop Dell XPS 15', 1299.99, 'Ordinateur portable haute performance, 15 pouces, i7'),
                                                    ('Souris Logitech MX Master', 89.99, 'Souris ergonomique sans fil pour professionnels'),
                                                    ('Clavier Mécanique Keychron K2', 119.99, 'Clavier mécanique compact avec switches Blue'),
                                                    ('Écran Samsung 27"', 349.99, 'Moniteur IPS 4K, 60Hz, HDMI/DisplayPort'),
                                                    ('Casque Sony WH-1000XM5', 279.99, 'Casque Bluetooth avec réduction de bruit active');

-- Articles
INSERT INTO articles (title, content, created_at) VALUES
                                                      ('Introduction à Spring Boot', 'Spring Boot facilite la création d''applications Java en réduisant la configuration nécessaire. Il utilise le principe "convention over configuration" pour accélérer le développement.', NOW()),
                                                      ('Les avantages de JPA avec Hibernate', 'JPA (Java Persistence API) permet de mapper les objets Java vers des tables de base de données. Hibernate est l''implémentation la plus populaire de JPA.', NOW()),
                                                      ('REST API Best Practices', 'Une bonne API REST doit être stateless, utiliser les verbes HTTP correctement (GET, POST, PUT, DELETE), et retourner des codes de statut appropriés.', NOW());

-- Commentaires
INSERT INTO comments (text, author, created_at, article_id) VALUES
                                                                ('Excellent article, très clair !', 'Ahmed', NOW(), 1),
                                                                ('Merci pour cette explication détaillée.', 'Sara', NOW(), 1),
                                                                ('JPA m''a vraiment simplifié la vie dans mes projets.', 'Mohamed', NOW(), 2),
                                                                ('N''oubliez pas de mentionner la validation des données !', 'Fatima', NOW(), 3);