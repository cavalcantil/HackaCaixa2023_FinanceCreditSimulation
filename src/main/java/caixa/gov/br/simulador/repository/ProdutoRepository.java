package caixa.gov.br.simulador.repository;

import caixa.gov.br.simulador.repository.entity.Produto;
import caixa.gov.br.simulador.v1.dto.entrada.EntradaSimulacaoDTO;
import caixa.gov.br.simulador.repository.impl.ProdutoRepositoryImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ProdutoRepository implements ProdutoRepositoryImpl {

    @Inject
    EntityManager entityManager;

    public List<Produto> buscaListProduto(EntradaSimulacaoDTO entrada) {
        BigDecimal valorDesejado = entrada.getValorDesejado();
        int prazo = entrada.getPrazo();

        if (prazo >= 96 && valorDesejado.compareTo(BigDecimal.valueOf(1000000.01)) >= 0) {
            Produto produto4 = entityManager.find(Produto.class, 4);
            if (produto4 != null) {
                return Collections.singletonList(produto4);
            }
        }

        Query query = entityManager.createQuery("SELECT p FROM Produto p " +
                "WHERE (:valorDesejado BETWEEN p.valorMinimo AND p.valorMaximo) " +
                "AND (:prazo BETWEEN p.minimoMeses AND COALESCE(p.maximoMeses, :prazo))");
        query.setParameter("valorDesejado", valorDesejado);
        query.setParameter("prazo", prazo);
        return query.getResultList();
    }
}
