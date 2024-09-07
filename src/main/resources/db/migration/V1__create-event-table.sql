CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE event (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  title varchar(100) NOT NULL,
  description varchar(250) NOT NULL,
  image_url varchar(100) NOT NULL,
  event_url varchar(100) NOT NULL,
  date timestamp NOT NULL,
  remote boolean NOT NULL
)