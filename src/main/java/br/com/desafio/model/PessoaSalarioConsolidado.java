package br.com.desafio.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pessoa_salario_consolidado")
public class PessoaSalarioConsolidado {

    @Id
    @Column(name = "pessoa_id")
    private Integer pessoaId;

    @Column(name = "nome_pessoa")
    private String nomePessoa;

    @Column(name = "nome_cargo")
    private String nomeCargo;

    @Column(name = "salario")
    private BigDecimal salario;

    public PessoaSalarioConsolidado() {
    }

    public Integer getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Integer pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
}
