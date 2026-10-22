-- Um banco e um usuário por serviço: cada serviço só enxerga o próprio banco.
-- Em produção seriam instâncias separadas; aqui usamos uma só por economia de recursos.

CREATE USER keycloak WITH PASSWORD 'keycloak';
CREATE DATABASE keycloak OWNER keycloak;

CREATE USER catalog WITH PASSWORD 'catalog';
CREATE DATABASE catalog_db OWNER catalog;

CREATE USER orders WITH PASSWORD 'orders';
CREATE DATABASE order_db OWNER orders;

CREATE USER inventory WITH PASSWORD 'inventory';
CREATE DATABASE inventory_db OWNER inventory;

CREATE USER payment WITH PASSWORD 'payment';
CREATE DATABASE payment_db OWNER payment;
