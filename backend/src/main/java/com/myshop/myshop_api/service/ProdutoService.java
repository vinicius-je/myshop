package com.myshop.myshop_api.service;

import com.myshop.myshop_api.domain.Produto;
import com.myshop.myshop_api.dto.CriarProdutoRequest;
import com.myshop.myshop_api.exception.RecursoNaoEncontradoException;
import com.myshop.myshop_api.repository.ProdutoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** <b>SRP</b> — regras de produto. */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto criar(CriarProdutoRequest request) {
        return produtoRepository.save(new Produto(request.nome(), request.preco(), request.imagem()));
    }

    /** Catálogo ordenado por nome, para a vitrine. */
    @Transactional(readOnly = true)
    public List<Produto> listar() {
        return produtoRepository.findAll(Sort.by("nome"));
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }
}
