package com.myshop.myshop_api.service;

import com.myshop.myshop_api.domain.Produto;
import com.myshop.myshop_api.dto.CriarProdutoRequest;
import com.myshop.myshop_api.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** <b>SRP</b> — regras de produto. */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto criar(CriarProdutoRequest request) {
        return produtoRepository.save(new Produto(request.nome(), request.preco()));
    }
}
