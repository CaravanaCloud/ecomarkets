-- Category test data
INSERT INTO Category (id, name) VALUES
  (1, 'Bebidas'),
  (2, 'Carnes e Derivados'),
  (3, 'Caseiros'),
  (4, 'Chás e Temperos'),
  (5, 'Frutas'),
  (6, 'Grãos e Cereais'),
  (7, 'Leites e Derivados'),
  (8, 'Mel'),
  (9, 'Outros'),
  (10, 'Processados de Origem Vegetal'),
  (11, 'Verduras e Legumes');

-- Product test data
INSERT INTO Product (cents, measureUnit, price_unit, category_id, id, description, name, productImage_bucket_key, recipe_description)
VALUES
  (0, 3, 4, 1, 2, '1L', 'Água de Côco', null, null),
  (50, 3, 9, 1, 3, '700ml', 'Cachaça Crioula Prata', null, null),
  (75, 3, 7, 2, 4, '500g', 'Banha Suína', null, null),
  (20, 3, 3, 3, 5, '150g', 'Biscoito Casadinho', null, 'Trigo, açucar, maragarina, sal e goiaba'),
  (0, 3, 10, 3, 6, 'Unidade com aproximadamente 600 gramas, bolo tamanho família', 'Bolo de Amendoim', null, null),
  (90, 4, 0, 4, 7, null, 'Babosa', null, null),
  (20, 0, 1, 5, 8, null, 'Banana Nanica', null, null),
  (70, 3, 3, 6, 9, '500g', 'Feijão Vermelho', null, null),
  (70, 3, 4, 7, 10, '1L', 'Leite', null, null),
  (0, 3, 8, 7, 11, '2L', 'Leite', null, null),
  (0, 3, 1, 8, 12, '20ml', 'Extrato de Própolis', null, null),
  (50, 3, 3, 9, 13, null, 'Sabonete Artesanal de Erva-Doce e Mel', null, null),
  (17, 0, 2, 10, 14, null, 'Fubá', null, null),
  (0, 3, 4, 10, 15, '500g. Palmito da Variedade Pupunha, produzido pela Família Schulz Tonoli participante da OCS SABORES E SABERES da REDE BEM VIVER, a família reside na comunidade camponesa da Barra do Tijuco Preto, Domingos Martins - ES', 'Palmito Pupunha', null, null),
  (71, 0, 2, 11, 16, null, 'Batata Inglesa', null, null);

-- Product stock test data
INSERT INTO ProductStock (amount, farmer_id, id, product_id)
VALUES
  (10, 1, 1, 2),
  (6, 3, 2, 3),
  (2, 1, 3, 4), (4, 2, 4, 4),
  (20, 1, 5, 5),
  (2, 2, 6, 6),
  (12, 2, 7, 7), (9, 3, 8, 7),
  (5, 1, 9, 8), (9, 2, 10, 8), (13, 3, 11, 8),
  (5, 3, 12, 9),
  (2, 1, 13, 10), (4, 3, 14, 10),
  (4, 2, 15, 11),
  (14, 1, 16, 12),
  (10, 3, 17, 13),
  (20, 2, 18, 14), (22, 1, 19, 14),
  (27, 2, 20, 15),
  (30, 3, 21, 16), (33, 1, 22, 16);

-- Basket test data
INSERT INTO Basket (creationDate, deliveredDate, id, partner_id, reservedDate)
VALUES 
  ('2024-01-29 14:00:00', '2024-02-01 10:00:00', 1, 4, '2024-01-30 11:00:00'),
  ('2024-01-28 16:00:00', NULL, 2, 5, '2024-01-29 18:00:00'),
  ('2024-01-27 18:00:00', NULL, 3, 6, '2024-01-29 13:32:00'),
  ('2024-02-01 10:00:00', '2024-02-01 15:00:00', 4, 1, '2024-02-01 09:00:00'),
  ('2024-01-31 18:00:00', '2024-02-02 12:00:00', 5, 2, '2024-02-01 10:00:00'),
  ('2024-01-30 20:00:00', NULL, 6, 3, '2024-01-31 15:00:00'),
  ('2024-01-31 12:00:00', NULL, 7, 9, '2024-01-31 22:00:00');

--Basket items test data
INSERT INTO Basket_items (amount, Basket_id, creationDate, product_id)
VALUES
  (2, 1, null, 2), (12, 1, null, 9),
  (30, 2, null, 8), (19, 2, null, 3),
  (6, 3, null, 14),
  (10, 4, null, 7),
  (7, 5, null, 10), (23, 5, null, 15), (1, 5, null, 5),
  (14, 6, null, 4),
  (30, 7, null, 6), (3, 7, null, 11);

-- Farmer test data
INSERT INTO Farmer (houseNumber, postCode, id, addOn, areaCode, city, country, email, name, number, reference, state)
VALUES
  (150, 45654321, 1, 'Apto 304', '27', 'Linhares', 'Brasil', 'joao.agricultor@email.com', 'João Silva', '27984567890', 'Próximo à Praça Central', 'ES'),
  (225, 12345987, 2, null, '27', 'Domingo Martins', 'Brasil', 'maria.fazenda@email.com', 'Maria Santos', '27912345678', 'Esquina com a Rua das Oliveiras', 'ES'),
  (380, 85274196, 3, 'Fundos', '27', 'Aracruz', 'Brasil', 'carlos.produtor@email.com', 'Carlos Oliveira', '27956784321', 'Atrás da Escola Municipal', 'ES');

-- Partner test data
INSERT INTO Partner (birthDate, houseNumber, postCode, id, addOn, areaCode, city, country, cpf, email, name, number, reference, state)
VALUES
  ('1992-05-18', 254, 54010400, 1, 'Bloco A', '62', 'Goiânia', 'Brasil', '45678912354', 'ana.silva@email.com', 'Ana Silva', '62987654321', 'Próximo ao Shopping', 'GO'),
  ('1987-11-21', 876, 40010001, 2, 'Apto 102', '81', 'Recife', 'Brasil', '12345678900', 'joao.pereira@email.com', 'João Pereira', '81999998888', 'Em frente ao Parque', 'PE'),
  ('1975-03-09', 112, 30140020, 3, '', '31', 'Belo Horizonte', 'Brasil', '98765432100', 'marcos.alves@email.com', 'Marcos Alves', '31988887777', 'Esquina com a Rua das Flores', 'MG'),
  ('1990-01-25', 123, 55010100, 4, 'Apto 405', '81', 'Recife', 'Brasil', '12345678900', 'example@email.com', 'John Doe', '81999999999', 'Próximo ao Parque', 'PE'),
  ('1980-07-14', 567, 22020040, 5, 'Casa', '21', 'Rio de Janeiro', 'Brasil', '78945612398', 'bruno.barbosa@email.com', 'Bruno Barbosa', '21976543210', 'Próximo à Praia de Copacabana', 'RJ'),
  ('1968-09-25', 345, 80040130, 6, 'Fundos', '41', 'Curitiba', 'Brasil', '65432198700', 'claudia.martins@email.com', 'Claudia Martins', '41965432109', 'Atrás do Parque Barigui', 'PR'),
  ('2000-01-01', 987, 51011090, 7, 'Apto 201', '81', 'Recife', 'Brasil', '32165498700', 'daniel.silva@email.com', 'Daniel Silva', '81954321098', 'Próximo à Praia de Boa Viagem', 'PE'),
  ('1995-05-12', 123, 74003010, 8, 'Sobrado', '62', 'Goiânia', 'Brasil', '21987654300', 'fernanda.souza@email.com', 'Fernanda Souza', '62943210987', 'Esquina com a Avenida Goiás', 'GO'),
  ('2005-10-31', 456, 69005000, 9, '', '92', 'Manaus', 'Brasil', '54321987600', 'gabriel.costa@email.com', 'Gabriel Costa', '92932109876', 'Próximo ao Teatro Amazonas', 'AM');
