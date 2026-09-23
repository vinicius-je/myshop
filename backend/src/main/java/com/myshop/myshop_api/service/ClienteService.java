package com.myshop.myshop_api.service;

import com.myshop.myshop_api.domain.Cliente;
import com.myshop.myshop_api.domain.Produto;
import com.myshop.myshop_api.dto.CriarClienteRequest;
import com.myshop.myshop_api.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/** <b>SRP</b> — regras de cliente. */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * <b>DIP</b> — injeção por construtor. O service recebe a dependência
     * pronta em vez de criá-la com {@code new ClienteRepository()}, o que
     * nem seria possível aqui (é uma interface) e, em geral, impediria
     * substituir a implementação em teste.
     */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente criar(CriarClienteRequest request) {
        return clienteRepository.save(new Cliente(request.nome(), request.email()));
    }
}
