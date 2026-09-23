package com.myshop.myshop_api.config;

import com.myshop.myshop_api.domain.Cliente;
import com.myshop.myshop_api.domain.Produto;
import com.myshop.myshop_api.repository.ClienteRepository;
import com.myshop.myshop_api.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Popula o H2 do profile {@code local} com um catálogo e clientes de exemplo,
 * para o frontend ter o que exibir logo após subir a API.
 */
@Profile("local")
@Component
public class DadosIniciais implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    public DadosIniciais(ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (produtoRepository.count() == 0) {
            produtoRepository.saveAll(List.of(
                    new Produto("Teclado mecânico", new BigDecimal("349.90"), imagem("teclado")),
                    new Produto("Mouse sem fio", new BigDecimal("129.90"), imagem("mouse")),
                    new Produto("Monitor 27\"", new BigDecimal("1599.00"), imagem("monitor")),
                    new Produto("Headset USB", new BigDecimal("259.50"), imagem("headset")),
                    new Produto("Webcam Full HD", new BigDecimal("219.99"), imagem("webcam")),
                    new Produto("Hub USB-C", new BigDecimal("189.90"), imagem("hub"))));
        }
        if (clienteRepository.count() == 0) {
            clienteRepository.saveAll(List.of(
                    new Cliente("Ana Souza", "ana@example.com"),
                    new Cliente("Bruno Lima", "bruno@example.com")));
        }
    }

    /** Imagem de exemplo com o nome do produto (placeholder público). */
    private static String imagem(String texto) {
        return "https://placehold.co/400x300?text=" + texto;
    }
}
