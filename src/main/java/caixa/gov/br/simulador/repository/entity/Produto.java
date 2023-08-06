package caixa.gov.br.simulador.repository.entity;

import caixa.gov.br.simulador.repository.impl.ProdutoImpl;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO")
public class Produto implements ProdutoImpl {

    @Id
    @Column(name = "CO_PRODUTO")
    private int codigoProduto;

    @Column(name = "NO_PRODUTO")
    private String nomeProduto;

    @Column(name = "PC_TAXA_JUROS")
    private BigDecimal taxaJuros;

    @Column(name = "NU_MINIMO_MESES", columnDefinition = "SMALLINT")
    private int minimoMeses;

    @Column(name = "NU_MAXIMO_MESES",  columnDefinition = "SMALLINT")
    private Integer maximoMeses;

    @Column(name = "VR_MINIMO")
    private BigDecimal valorMinimo;

    @Column(name = "VR_MAXIMO")
    private BigDecimal valorMaximo;

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(int codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getTaxaJuros() {
        return taxaJuros;
    }

    public void setTaxaJuros(BigDecimal taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    public int getMinimoMeses() {
        return minimoMeses;
    }

    public void setMinimoMeses(int minimoMeses) {
        this.minimoMeses = minimoMeses;
    }

    public Integer getMaximoMeses() {
        return maximoMeses;
    }

    public void setMaximoMeses(Integer maximoMeses) {
        this.maximoMeses = maximoMeses;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public BigDecimal getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(BigDecimal valorMaximo) {
        this.valorMaximo = valorMaximo;
    }
}