package br.edu.projeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity

public class PlacasPedido {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "Codigo")
    private int codigo;
    
    @Column(name = "Quantidade")
    private int quantidade;
    
    @Column(name = "codPlaca")
    private int codPlaca;
    
    @Column(name = "codPedido")
    private int codPedido;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getCodPlaca() {
        return codPlaca;
    }

    public void setCodPlaca(int codPlaca) {
        this.codPlaca = codPlaca;
    }

    public int getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(int codPedido) {
        this.codPedido = codPedido;
    }
}
