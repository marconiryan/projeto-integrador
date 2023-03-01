package br.edu.projeto.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.FornecedorDAO;
import br.edu.projeto.model.Fornecedor;

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
public class CadastroFornecedorController implements Serializable {

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;

	@Inject
    private FornecedorDAO fornecedorDAO;
	
	private Fornecedor fornecedor;

	private List<Fornecedor> listaFornecedors;
	
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {


    	if (!this.facesContext.getExternalContext().isUserInRole("1")) {
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	this.listaFornecedors = fornecedorDAO.listarTodos();
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setFornecedor(new Fornecedor());
    }
	
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
		Boolean a = this.fornecedorDAO.uniqueFornecedor(this.fornecedor.getCnpj());
		
		if(a) {
			this.facesContext.addMessage(null, new FacesMessage("Verdadeiro"));
		}
		else {
			this.facesContext.addMessage(null, new FacesMessage("Falso"));
		}
		
    	try {
	        if (this.fornecedorDAO.uniqueFornecedor(this.fornecedor.getCnpj())) {
	        	this.fornecedorDAO.salvar(this.fornecedor);
	        	this.facesContext.addMessage(null, new FacesMessage("Especificação Criada"));
	        } else {
	        	this.fornecedorDAO.atualizar(this.fornecedor);
	        	this.facesContext.addMessage(null, new FacesMessage("Especificação Atualizada"));
	        }
	        this.listaFornecedors = fornecedorDAO.listarTodos();
	        //Atualiza e executa elementos Javascript na tela assincronamente
		    PrimeFaces.current().executeScript("PF('fornecedorDialog').hide()");
		    PrimeFaces.current().ajax().update("form:messages", "form:dt-fornecedors");
		    
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
	}	
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.fornecedorDAO.excluir(this.fornecedor);
			this.listaFornecedors = fornecedorDAO.listarTodos();
	        this.fornecedor = null;
	        this.facesContext.addMessage(null, new FacesMessage("Usuário Removido"));
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
		PrimeFaces.current().ajax().update("form:messages", "form:dt-fornecedors");
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
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor Fornecedor) {
		this.fornecedor = Fornecedor;
	}

	public List<Fornecedor> getListaFornecedors() {
		return listaFornecedors;
	}

	public void setListaFornecedors(List<Fornecedor> listaFornecedors) {
		this.listaFornecedors = listaFornecedors;
	}


}