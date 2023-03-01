package br.edu.projeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;


@Entity
@Table(name = "componente_placa")
public class ComponentePlaca {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private Integer codigo;
	
    @Column(name = "quantidade")
    private int quantidade;
    
    @ManyToOne
    @JoinColumn(name = "cod_placa")
    private PlacaEletronica placaEletronica;
    
    @ManyToOne
    @JoinColumn(name = "cod_componente")
    private ComponenteEletronico componenteEletronico;
   
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    //
    
	public PlacaEletronica getPlacaEletronica() {
		return placaEletronica;
	}

	public void setPlacaEletronica(PlacaEletronica placaEletronica) {
		this.placaEletronica = placaEletronica;
	}
	
	//
	
	public ComponenteEletronico getComponenteEletronico() {
		return componenteEletronico;
	}

	public void setComponenteEletronico(ComponenteEletronico componenteEletronico) {
		this.componenteEletronico = componenteEletronico;
	}
	
	//
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public Integer getCodigo() {
		return this.codigo;
	}
    
}
