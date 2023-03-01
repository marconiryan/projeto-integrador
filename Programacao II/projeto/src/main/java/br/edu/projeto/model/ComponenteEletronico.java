package br.edu.projeto.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "componente_eletronico")
public class ComponenteEletronico {
   
    @Column(name = "pn")
    private String pn;
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private Integer codigo;
    
    @Column(name = "quant")
    private int quantidade;
    
    @ManyToOne
    @JoinColumn(name = "cod_especificacao")
    private Especificacao especificacao;
    
    @ManyToOne
    @JoinColumn(name="cod_tipo_componente")
    private TipoComponente tipoComponente;
    
    @ManyToOne
    @JoinColumn(name="cod_fornecedor")
    private Fornecedor fornecedor;
    
    
    @OneToMany(mappedBy = "componenteEletronico", cascade = CascadeType.ALL)
    List<ComponentePlaca> ComponentesPlaca; 

    

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TipoComponente getTipoComponente() {
        return tipoComponente;
    }

	public void setTipoComponente(TipoComponente tipoComponente) {
		this.tipoComponente = tipoComponente;
	}

	public Especificacao getEspecificacao() {
		return especificacao;
	}

	public void setEspecificacao(Especificacao especificacao) {
		this.especificacao = especificacao;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public List<ComponentePlaca> getComponentesPlaca() {
		return ComponentesPlaca;
	}

	public void setComponentesPlaca(List<ComponentePlaca> componentesPlaca) {
		ComponentesPlaca = componentesPlaca;
	}

	
    
    
    
    
    

}
