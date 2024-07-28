-- check users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    region VARCHAR(255),
    salary DECIMAL(10, 2),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    account_status VARCHAR(50) DEFAULT 'ENABLED'
);

-- Add ne random users
INSERT INTO users (name, age, region, salary, email, password, account_status)
SELECT
    concat_ws(' ',
        (SELECT word FROM unnest(array['Rafael', 'Atilla', 'Elvin', 'Bahruz', 'Tural', 'Valid', 'Elchin', 'Ismayil', 'Rauf', 'Aziz']) AS word ORDER BY random() LIMIT 1),
        (SELECT word FROM unnest(array['Abdullayev', 'Mammadov', 'Aliyeva', 'Haciyev', 'Orucov', 'Rzayev', 'Jafarov', 'Guliyev', 'Huseynov', 'Sadiqov']) AS word ORDER BY random() LIMIT 1)
    ) AS name,
    (random() * 60 + 18)::INTEGER AS age,
    (SELECT word FROM unnest(array['Shusha', 'Zardab', 'Sumqayıt', 'Zangilan', 'Mingachevir', 'Baku', 'Quba', 'Shamakhi', 'Imishli', 'Hajigabul']) AS word ORDER BY random() LIMIT 1) AS region,
    (random() * 100000 + 20000)::NUMERIC(10, 2) AS salary,
    concat_ws('.',
        (SELECT word FROM unnest(array['administrator', 'manager', 'moderator', 'editor', 'support']) AS word ORDER BY random() LIMIT 1),
        (random() * 10000)::INTEGER || '@example.com'
    ) AS email,

    -- create random password
    encode(gen_random_uuid()::bytea, 'base64') AS password,
    'ENABLED' AS account_status
FROM generate_series(1, 10000);
