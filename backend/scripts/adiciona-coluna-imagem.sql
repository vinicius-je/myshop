-- Adiciona a coluna imagem (URL da foto do produto) em dbo.produtos.
-- Mesmo tipo que o Hibernate gera para @Column(length = 500) String: varchar(500) NULL.
-- Idempotente: não faz nada se a coluna já existir.

IF COL_LENGTH('dbo.produtos', 'imagem') IS NULL
BEGIN
    ALTER TABLE dbo.produtos ADD imagem VARCHAR(500) NULL;
    PRINT 'Coluna dbo.produtos.imagem criada.';
END
ELSE
    PRINT 'Coluna dbo.produtos.imagem já existe.';
