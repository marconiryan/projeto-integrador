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

import br.edu.projeto.dao.FuncionarioDAO;
import br.edu.projeto.model.Funcionario;

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
public class CadastroFuncionarioController implements Serializable {

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
    transient private Pbkdf2PasswordHash passwordHash;
	
	@Inject
    private FuncionarioDAO funcionarioDAO;
	
	
	private Funcionario funcionario;
	
	private List<Funcionario> listaFuncionarios;
	
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
       	if (!this.facesContext.getExternalContext().isUserInRole("1")) {
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	this.listaFuncionarios = funcionarioDAO.listarTodos();
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setFuncionario(new Funcionario());
    }
	
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
        if (FuncionarioValido()) {
        	try {
        		this.funcionario.setSenha(this.passwordHash.generate(this.funcionario.getSenha().toCharArray()));
		        if (this.funcionario.getCodigo() == null) {
		        	this.funcionarioDAO.salvar(this.funcionario);
		        	this.facesContext.addMessage(null, new FacesMessage("Funcionario Criado"));
		        } else {
		        	this.funcionarioDAO.atualizar(this.funcionario);
		        	this.facesContext.addMessage(null, new FacesMessage("Funcionario Atualizado"));
		        }
		        this.listaFuncionarios = funcionarioDAO.listarTodos();
		        //Atualiza e executa elementos Javascript na tela assincronamente
			    PrimeFaces.current().executeScript("PF('funcionarioDialog').hide()");
			    PrimeFaces.current().ajax().update("form:messages", "form:dt-funcionarios");
			    
	        } catch (Exception e) {
	            String errorMessage = getMensagemErro(e);
	            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	        }
        }
	}	
	
	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean FuncionarioValido() {			
		if (this.funcionario.getCodigo() == null && !this.funcionarioDAO.uniqueFuncionario(this.funcionario.getNome())) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este nome de usuário já está em uso.", null));
			return false;
		}
		return true;
	}
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.funcionarioDAO.excluir(this.funcionario);
			this.listaFuncionarios = funcionarioDAO.listarTodos();
	        this.funcionario = null;
	        this.facesContext.addMessage(null, new FacesMessage("Usuário Removido"));
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-Funcionarios");
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
	}
	
	//Chamado pelo botão alterar da tabela
	public void alterar() {
		this.funcionario.setSenha("");
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
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario Funcionario) {
		this.funcionario = Funcionario;
	}

	public List<Funcionario> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(List<Funcionario> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}


}