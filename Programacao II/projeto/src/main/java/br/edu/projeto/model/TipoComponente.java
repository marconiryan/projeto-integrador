package br.edu.projeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tipo_componente")
public class TipoComponente {
    
    @Column(name = "nome")
    private String nome;
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private Integer codigo;
    
    @Column(name = "encapsulamento")
    private String encapsulamento;
    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getEncapsulamento() {
        return encapsulamento;
    }

    public void setEncapsulamento(String encapsulamento) {
        this.encapsulamento = encapsulamento;
    }
    
    
}
