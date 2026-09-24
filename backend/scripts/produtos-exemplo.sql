INSERT INTO dbo.produtos (nome, preco, imagem)
SELECT v.nome, v.preco, v.imagem
FROM (VALUES
    (
        N'Teclado mecânico',
        349.90,
        N'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&q=80'
    ),
    (
        N'Mouse sem fio',
        129.90,
        N'https://images.unsplash.com/photo-1527814050087-3793815479db?w=800&q=80'
    ),
    (
        N'Monitor 27"',
        1599.00,
        N'https://unsplash.com/photos/x2Z0uNj-Quo/download?force=true'
    ),
    (
        N'Headset USB',
        259.50,
        N'https://unsplash.com/photos/LSNJ-pltdu8/download?force=true'
    ),
    (
        N'Webcam Full HD',
        219.99,
        N'https://images.unsplash.com/photo-1588196749597-9ff075ee6b5b?w=800&q=80'
    ),
    (
        N'Hub USB-C',
        189.90,
        N'https://images.unsplash.com/photo-1763161786687-43d0c9babdf0?w=800&q=80'
    ),
    (
        N'Notebook 15"',
        4299.00,
        N'https://unsplash.com/photos/1SAnrIxw5OY/download?force=true'
    ),
    (
        N'SSD 1TB NVMe',
        449.90,
        N'https://unsplash.com/photos/ZhFoeRUDXhI/download?force=true'
    ),
    (
        N'Cadeira ergonômica',
        1249.00,
        N'https://unsplash.com/photos/Pvse_0mSm6Y/download?force=true'
    ),
    (
        N'Mousepad grande',
        79.90,
        N'https://unsplash.com/photos/xxL1FavYOh0/download?force=true'
    ),
    (
        N'Caixa de som Bluetooth',
        299.00,
        N'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=800&q=80'
    ),
    (
        N'Suporte para notebook',
        149.90,
        N'https://unsplash.com/photos/o9srZw5wDaE/download?force=true'
    )
) AS v (nome, preco, imagem)
WHERE NOT EXISTS (
    SELECT 1
    FROM dbo.produtos p
    WHERE p.nome = v.nome
);