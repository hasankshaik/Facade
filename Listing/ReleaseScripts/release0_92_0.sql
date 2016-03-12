ALTER TABLE courtcenter ADD COLUMN code bigint;
UPDATE courtcenter set code = '404' where centername = 'Birmingham'; 
ALTER TABLE courtcenter ADD UNIQUE (centername);
ALTER TABLE courtcenter ADD UNIQUE (code);