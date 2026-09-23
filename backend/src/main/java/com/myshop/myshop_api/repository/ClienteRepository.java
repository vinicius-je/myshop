package com.myshop.myshop_api.repository;

import com.myshop.myshop_api.domain.Cliente;
import com.myshop.myshop_api.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>SRP</b> — persistência de cliente e nada mais.
 *
 * <p><b>DIP</b> — {@code JpaRepository} é uma interface. Os services dependem
 * dela, não de {@code EntityManager} nem de SQL. A implementação concreta é
 * gerada pelo Spring Data em tempo de execução, e trocar o mecanismo de
 * persistência não obriga a mexer no service.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
