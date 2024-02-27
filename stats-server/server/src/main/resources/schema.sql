--DROP TABLE IF EXISTS stats CASCADE;

CREATE TABLE IF NOT EXISTS stats (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app varchar(50) NOT NULL,
    uri varchar(50) NOT NULL,
    ip varchar(50) NOT NULL,
    timestamp timestamp,
    hits INTEGER
);
