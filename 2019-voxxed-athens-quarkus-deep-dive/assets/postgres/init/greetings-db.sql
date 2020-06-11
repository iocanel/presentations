DROP TABLE IF EXISTS greeting;
CREATE TABLE greeting (id SERIAL PRIMARY KEY, lang TEXT NOT NULL, greeting TEXT NOT NULL);
INSERT INTO greeting (lang, greeting) VALUES ('es', 'hola');
INSERT INTO greeting (lang, greeting) VALUES ('en', 'hi');
INSERT INTO greeting (lang, greeting) VALUES ('it', 'ciao');
INSERT INTO greeting (lang, greeting) VALUES ('fr', 'bonjour');
