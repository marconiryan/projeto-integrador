package br.edu.projeto.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "placa_eletronica")
public class PlacaEletronica {
    
	
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private Integer codigo;
	
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "tipo")
    private String tipo;
    
    @OneToMany(mappedBy = "placaEletronica", cascade = CascadeType.ALL)
    List<ComponentePlaca> ComponentesPlaca; 

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

	public List<ComponentePlaca> getComponentesPlaca() {
		return ComponentesPlaca;
	}

	public void setComponentesPlaca(List<ComponentePlaca> componentesPlaca) {
		ComponentesPlaca = componentesPlaca;
	}

	@Override
	public String toString() {
		return "PlacaEletronica [codigo=" + codigo + ", nome=" + nome + ", tipo=" + tipo + ", ComponentesPlaca="
				+ ComponentesPlaca + "]";
	}
	
	
}
