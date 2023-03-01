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
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.EspecificacaoDAO;
import br.edu.projeto.model.Especificacao;

//Escopo do objeto da classe (Bean)
//ApplicationScoped é usado quando o objeto é único na aplicação (compartilhado entre usuários), permanece ativo enquanto a aplicação estiver ativa
//SessionScoped é usado quando o objeto é único por usuário, permanece ativo enquanto a sessão for ativa
//ViewScoped é usado quando o objeto permanece ativo enquanto não houver um redirect (acesso a nova página)
//RequestScoped é usado quando o objeto só existe naquela requisição específica
//Quanto maior o escopo, maior o uso de memória no lado do servidor (objeto permanece ativo por mais tempo)
//Escopos maiores que Request exigem que classes sejam serializáveis assim como todos os seus atributos (recurso de segurança)
//atributos que não podem ser serializáveis devem ser marcados como transient (eles são novamente criados a cada nova requisição independente do escopo da classe)
@ViewScoped
//Torna classe disponível na camada de visão (html) - são chamados de Beans ou ManagedBeans (gerenciados pelo JSF/EJB)
@Named
public class CadastroEspecificacaoController implements Serializable {

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;

	@Inject
    private EspecificacaoDAO especificacaoDAO;
	
	private List<SelectItem> unidades = new ArrayList<SelectItem>();
	
	private Especificacao especificacao;
	
	public List<SelectItem> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<SelectItem> unidades) {
		this.unidades = unidades;
	}

	private List<Especificacao> listaEspecificacaos;
	
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
    	this.unidades.add(new SelectItem("KV", "KV"));
    	this.unidades.add(new SelectItem("V", "V"));
    	
    	this.unidades.add(new SelectItem("MΩ", "MΩ"));
    	this.unidades.add(new SelectItem("KΩ", "KΩ"));
    	this.unidades.add(new SelectItem("Ω", "Ω"));
    	
    	this.unidades.add(new SelectItem("A", "A"));
    	this.unidades.add(new SelectItem("mA", "mA"));
    	this.unidades.add(new SelectItem("µA", "µA"));
    	
    	this.unidades.add(new SelectItem("KW", "KW"));
    	this.unidades.add(new SelectItem("W", "W"));
    	this.unidades.add(new SelectItem("mW", "mW"));
    	this.unidades.add(new SelectItem("µW", "µW"));
    	
    	
    	this.unidades.add(new SelectItem("F", "F"));
    	this.unidades.add(new SelectItem("mF", "mF"));
    	this.unidades.add(new SelectItem("µF", "µF"));
    	this.unidades.add(new SelectItem("nF", "nF"));
    	this.unidades.add(new SelectItem("pF", "pF"));
    	
    	this.unidades.add(new SelectItem("H", "H"));
    	this.unidades.add(new SelectItem("mH", "mH"));
    	this.unidades.add(new SelectItem("µH", "µH"));
    	this.unidades.add(new SelectItem("nH", "nH"));
    	this.unidades.add(new SelectItem("pH", "pH"));
    	
    	this.unidades.add(new SelectItem("GHz", "GHz"));
    	this.unidades.add(new SelectItem("MHz", "MHz"));
    	this.unidades.add(new SelectItem("KHz", "KHz"));
    	this.unidades.add(new SelectItem("Hz", "Hz"));


    	if (!this.facesContext.getExternalContext().isUserInRole("1")) {
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	this.listaEspecificacaos = especificacaoDAO.listarTodos();
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setEspecificacao(new Especificacao());
    }
	
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
        if (EspecificacaoValido()) {
        	try {
		        if (this.especificacao.getCodigo() == null) {
		        	this.especificacaoDAO.salvar(this.especificacao);
		        	this.facesContext.addMessage(null, new FacesMessage("Especificação Criada"));
		        } else {
		        	this.especificacaoDAO.atualizar(this.especificacao);
		        	this.facesContext.addMessage(null, new FacesMessage("Especificação Atualizada"));
		        }
		        this.listaEspecificacaos = especificacaoDAO.listarTodos();
		        //Atualiza e executa elementos Javascript na tela assincronamente
			    PrimeFaces.current().executeScript("PF('especificacaoDialog').hide()");
			    PrimeFaces.current().ajax().update("form:messages", "form:dt-especificacaos");
			    
	        } catch (Exception e) {
	            String errorMessage = getMensagemErro(e);
	            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	        }
        }
	}	
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean EspecificacaoValido() {			
		if (this.especificacao.getCodigo() == null && !this.especificacaoDAO.uniqueEspecificacao(this.especificacao.getValor(),this.especificacao.getUnidadeMedida() )) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Especificação Já Existente", null));
			return false;
		}
		return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.especificacaoDAO.excluir(this.especificacao);
			this.listaEspecificacaos = especificacaoDAO.listarTodos();
	        this.especificacao = null;
	        this.facesContext.addMessage(null, new FacesMessage("Usuário Removido"));
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
		PrimeFaces.current().ajax().update("form:messages", "form:dt-especificacaos");
	}
	
	//Chamado pelo botão alterar da tabela
	public void alterar() {
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
	
	//GETs e SETs
	public Especificacao getEspecificacao() {
		return especificacao;
	}

	public void setEspecificacao(Especificacao Especificacao) {
		this.especificacao = Especificacao;
	}

	public List<Especificacao> getListaEspecificacaos() {
		return listaEspecificacaos;
	}

	public void setListaEspecificacaos(List<Especificacao> listaEspecificacaos) {
		this.listaEspecificacaos = listaEspecificacaos;
	}


}