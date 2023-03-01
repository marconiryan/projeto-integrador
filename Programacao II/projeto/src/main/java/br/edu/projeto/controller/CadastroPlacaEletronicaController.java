package br.edu.projeto.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.ComponenteEletronicoDAO;
import br.edu.projeto.dao.ComponentePlacaDAO;
import br.edu.projeto.dao.PlacaEletronicaDAO;
import br.edu.projeto.model.ComponenteEletronico;
import br.edu.projeto.model.ComponentePlaca;
import br.edu.projeto.model.Fornecedor;
import br.edu.projeto.model.PlacaEletronica;

//Escopo do objeto da classe (Bean)
//ApplicationScoped é usado quando o objeto é único na aplicação (compartilhado entre placas), permanece ativo enquanto a aplicação estiver ativa
//SessionScoped é usado quando o objeto é único por placa, permanece ativo enquanto a sessão for ativa
//ViewScoped é usado quando o objeto permanece ativo enquanto não houver um redirect (acesso a nova página)
//RequestScoped é usado quando o objeto só existe naquela requisição específica
//Quanto maior o escopo, maior o uso de memória no lado do servidor (objeto permanece ativo por mais tempo)
//Escopos maiores que Request exigem que classes sejam serializáveis assim como todos os seus atributos (recurso de segurança)
//atributos que não podem ser serializáveis devem ser marcados como transient (eles são novamente criados a cada nova requisição independente do escopo da classe)
@ViewScoped
//Torna classe disponível na camada de visão (html) - são chamados de Beans ou ManagedBeans (gerenciados pelo JSF/EJB)
@Named
public class CadastroPlacaEletronicaController implements Serializable {

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
    private PlacaEletronicaDAO placaEletronicaDAO;
	private PlacaEletronica placaEletronica;
	private List<PlacaEletronica> listaPlacaEletronicas;
	
	
	@Inject
	private ComponentePlacaDAO componentePlacaDAO;
	private ComponentePlaca componentePlaca;
	private List<ComponentePlaca> listaComponentePlaca;
	
	
	@Inject
	private ComponenteEletronicoDAO componenteEletronicoDAO;
	//private ComponenteEletronico componenteEletronico;
	//private List<ComponenteEletronico> listaComponenteEletronico;
	
	List<SelectItem> componentesDropdown;
	Integer codigoComponenteSelecionado;
	Integer quantidadeSelecionada;
	
	
	
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
    	//Verifica se placa está autenticado e possui a permissão adequada
    	if (!this.facesContext.getExternalContext().isUserInRole("1")) {
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	//Inicializa elementos importantes
    	this.listaPlacaEletronicas = placaEletronicaDAO.listarTodos();
    	
    	
    	this.componentesDropdown = new ArrayList<SelectItem>();
    	for (ComponenteEletronico f: this.componenteEletronicoDAO.listarTodos()) {
    		SelectItem i = new SelectItem(f.getCodigo(), f.getPn() + " " + f.getEspecificacao().getValor() + f.getEspecificacao().getUnidadeMedida() + " " + f.getTipoComponente().getNome() + " " + f.getTipoComponente().getEncapsulamento());		
    		this.componentesDropdown.add(i);
    	}
    	

    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setPlacaEletronica(new PlacaEletronica());
    }
	public void novoCadastroComponentePlaca() {
		this.setComponentePlaca(new ComponentePlaca());
		this.componentePlaca.setPlacaEletronica(this.getPlacaEletronica());
		this.setCodigoComponenteSelecionado(null);
    }
	
	//Chamado ao salvar cadastro de placa (novo ou edição)
	public void salvar() {
        if (placaEletronicaValido()) {
        	try {
		        if (this.placaEletronica.getCodigo() == null) {
		        	
		        	
		        	
		        	this.placaEletronicaDAO.salvar(this.placaEletronica);
		        	this.facesContext.addMessage(null, new FacesMessage("Placa Criada"));
		        } else {
		        	this.placaEletronicaDAO.atualizar(this.placaEletronica);
		        	this.facesContext.addMessage(null, new FacesMessage("Placa Atualizada"));
		        }
		        this.listaPlacaEletronicas = placaEletronicaDAO.listarTodos();
		        
		        //Atualiza e executa elementos Javascript na tela assincronamente
			    PrimeFaces.current().executeScript("PF('placaEletronicaDialog').hide()");
			    PrimeFaces.current().ajax().update("form:messages", "form:dt-placaEletronicas");
	        } catch (Exception e) {
	            String errorMessage = getMensagemErro(e);
	            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	        }
        }
	}
	public void salvarComponentePlaca() {
		 if (componentePlacaValido()) { 
			 try {
				 if (this.componentePlaca.getCodigo() == null) {
					 
					 this.componentePlaca.setComponenteEletronico(this.componenteEletronicoDAO.encontrarId(this.getCodigoComponenteSelecionado()));
					 this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, this.componentePlaca.getComponenteEletronico().getPn() + "", null));
					 this.componentePlacaDAO.salvar(this.componentePlaca);
					 this.facesContext.addMessage(null, new FacesMessage("Componente Adicionado"));
				 } else {
					 
					 this.componentePlacaDAO.atualizar(this.componentePlaca);
					 this.facesContext.addMessage(null, new FacesMessage("Relação Atualizada"));
				 }
				 
				 
			 } catch (Exception e) {
				 String errorMessage = getMensagemErro(e);
				 this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
			 }
			 
			 this.listaPlacaEletronicas = placaEletronicaDAO.listarTodos();
			 PrimeFaces.current().ajax().update("form:messages", "form:dt-placaEletronicas");
		     PrimeFaces.current().ajax().update("form:messages", ":dialogs:componentes-conteudo-dialog");
		     PrimeFaces.current().executeScript("PF('componentePlacaDialog').hide()");
			 
		 }
	}
	
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean placaEletronicaValido() {		
		if (this.placaEletronica.getCodigo() == null && !this.placaEletronicaDAO.uniquePlacaEletronica(this.placaEletronica.getNome())) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este nome de placa já está em uso.", null));
			return false;
		}
		return true;
	}
	private boolean componentePlacaValido() {
		if (this.componentePlaca.getCodigo() == null && !this.componentePlacaDAO.uniqueComponentePlaca(this.getCodigoComponenteSelecionado(), this.componentePlaca.getPlacaEletronica().getCodigo())) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este nome de placa já está em uso.", null));
			return false;
		}
		return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.placaEletronicaDAO.deleteDependencies(this.placaEletronica.getCodigo());
			this.placaEletronicaDAO.excluir(this.placaEletronica);
			this.listaPlacaEletronicas = placaEletronicaDAO.listarTodos();
	        this.placaEletronica = null;
	        this.facesContext.addMessage(null, new FacesMessage("Placa Removida"));
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-placaEletronicas");
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
	}
	
	public void removerComponentePlaca() {
		try {
			this.componentePlacaDAO.excluir(this.componentePlaca);
			this.listaPlacaEletronicas = placaEletronicaDAO.listarTodos();
	        this.componentePlaca = null;
	        this.facesContext.addMessage(null, new FacesMessage("Componente Removido"));
	        
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
		
		PrimeFaces.current().ajax().update("form:messages", "form:dt-placaEletronicas");
	     PrimeFaces.current().ajax().update("form:messages", ":dialogs:componentes-conteudo-dialog");		     
	     PrimeFaces.current().executeScript("PF('componentesDialog').hide()");
	     PrimeFaces.current().executeScript("PF('componentesDialog').show()");
	}
	
	//Chamado pelo botão alterar da tabela
	public void alterar() {
	
	}
	
	public void alterarComponentePlaca() {
		this.setCodigoComponenteSelecionado(this.componentePlaca.getComponenteEletronico().getCodigo());
		this.setQuantidadeSelecionada(this.componentePlaca.getComponenteEletronico().getQuantidade());
	}
	
	//Captura mensagem de erro das validações do Hibernate
	private String getMensagemErro(Exception e) {
        String erro = "Falha no sistema!. Contacte o administrador do sistema.";
        if (e == null) 
            return erro;
        Throwable t = e;
        while (t != null) {
            erro = t.getLocalizedMessage();
            t = t.getCause();
        }
        return erro;
    }
	
	
	public PlacaEletronica getPlacaEletronica() {
		return placaEletronica;
	}

	public void setPlacaEletronica(PlacaEletronica placaEletronica) {
		this.placaEletronica = placaEletronica;
	}

	public List<PlacaEletronica> getListaPlacaEletronicas() {
		return listaPlacaEletronicas;
	}

	public void setListaPlacaEletronicas(List<PlacaEletronica> listaPlacaEletronicas) {
		this.listaPlacaEletronicas = listaPlacaEletronicas;
	}

	public PlacaEletronicaDAO getPlacaEletronicaDAO() {
		return placaEletronicaDAO;
	}

	public void setPlacaEletronicaDAO(PlacaEletronicaDAO placaEletronicaDAO) {
		this.placaEletronicaDAO = placaEletronicaDAO;
	}

	public ComponentePlacaDAO getComponentePlacaDAO() {
		return componentePlacaDAO;
	}

	public void setComponentePlacaDAO(ComponentePlacaDAO componentePlacaDAO) {
		this.componentePlacaDAO = componentePlacaDAO;
	}

	public ComponentePlaca getComponentePlaca() {
		return componentePlaca;	
	}

	public void setComponentePlaca(ComponentePlaca componentePlaca) {
		this.componentePlaca = componentePlaca;
	}

	public List<ComponentePlaca> getListaComponentePlaca() {
		return listaComponentePlaca;
	}

	public void setListaComponentePlaca(List<ComponentePlaca> listaComponentePlaca) {
		this.listaComponentePlaca = listaComponentePlaca;
	}

	public ComponenteEletronicoDAO getComponenteEletronicoDAO() {
		return componenteEletronicoDAO;
	}

	public void setComponenteEletronicoDAO(ComponenteEletronicoDAO componenteEletronicoDAO) {
		this.componenteEletronicoDAO = componenteEletronicoDAO;
	}

	public List<SelectItem> getComponentesDropdown() {
		return componentesDropdown;
	}

	public void setComponentesDropdown(List<SelectItem> componentesDropdown) {
		this.componentesDropdown = componentesDropdown;
	}

	public Integer getCodigoComponenteSelecionado() {
		return codigoComponenteSelecionado;
	}

	public void setCodigoComponenteSelecionado(Integer codigoComponenteSelecionado) {
		this.codigoComponenteSelecionado = codigoComponenteSelecionado;
	}

	public Integer getQuantidadeSelecionada() {
		return quantidadeSelecionada;
	}

	public void setQuantidadeSelecionada(Integer quantidadeSelecionada) {
		this.quantidadeSelecionada = quantidadeSelecionada;
	}

	
	
	
}
