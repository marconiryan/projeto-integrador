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
import br.edu.projeto.dao.EspecificacaoDAO;
import br.edu.projeto.dao.FornecedorDAO;
import br.edu.projeto.dao.TipoComponenteDAO;
import br.edu.projeto.model.ComponenteEletronico;
import br.edu.projeto.model.Especificacao;
import br.edu.projeto.model.Fornecedor;
import br.edu.projeto.model.TipoComponente;

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
public class CadastroComponenteEletronicoController implements Serializable {


	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
    private ComponenteEletronicoDAO componenteEletronicoDAO;
	
	@Inject
    private FornecedorDAO fornecedorDAO;
	
	@Inject
	private TipoComponenteDAO tipoComponenteDAO;
	@Inject
	private EspecificacaoDAO especificacaoDAO;
	
	private ComponenteEletronico componenteEletronico;
	
	private List<ComponenteEletronico> listaComponenteEletronicos;
	private List<SelectItem> fornecedores;
	private List<SelectItem> tipos;
	private List<SelectItem> especificacoes;
	
	String fornecedorSelecionado;
	Integer tipoSelecionado;
	Integer especificacaoSelecionado;
	
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
    	//Verifica se usuário está autenticado e possui a permissão adequada
    	if (!this.facesContext.getExternalContext().isUserInRole("1")) {
    		try {
				this.facesContext.getExternalContext().redirect("login-error.xhtml");
			} catch (IOException e) {e.printStackTrace();}
    	}
    	//Inicializa elementos importantes
    	
    	this.listaComponenteEletronicos = componenteEletronicoDAO.listarTodos();
    	
    	this.fornecedores = new ArrayList<SelectItem>();
    	for (Fornecedor f: this.fornecedorDAO.listarTodos()) {
    		SelectItem i = new SelectItem(f.getCnpj(), f.getNome());		
    		this.fornecedores.add(i);
    	}
    	
    	this.tipos = new ArrayList<SelectItem>();
    	for (TipoComponente t: this.tipoComponenteDAO.listarTodos()) {
    		SelectItem i = new SelectItem(t.getCodigo(), t.getNome() + " " + t.getEncapsulamento());		
    		this.tipos.add(i);
    	}
    	
    	this.especificacoes = new ArrayList<SelectItem>();
    	for (Especificacao es: this.especificacaoDAO.listarTodos()) {
    		SelectItem i = new SelectItem(es.getCodigo(), es.getValor() + es.getUnidadeMedida());		
    		this.especificacoes.add(i);
    	}
    	
    	
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setComponenteEletronico(new ComponenteEletronico());
    }
	
	//Chamado ao salvar cadastro de usuário (novo ou edição)
	public void salvar() {
        if (this.componenteEletronicoDAO.uniqueComponenteEletronico(this.componenteEletronico.getPn())) {
        	try {
		        if (this.componenteEletronico.getCodigo() == null) {
		        	
		        	
		        	this.componenteEletronico.setEspecificacao(this.especificacaoDAO.encontrarId(this.especificacaoSelecionado));
		        	this.componenteEletronico.setTipoComponente(this.tipoComponenteDAO.encontrarId(this.tipoSelecionado));
		        	this.componenteEletronico.setFornecedor(this.fornecedorDAO.encontrarId(this.fornecedorSelecionado));
		        	this.componenteEletronicoDAO.salvar(this.componenteEletronico);
		        	this.facesContext.addMessage(null, new FacesMessage("Componente Criado"));
		        } else {
		        	
		        	this.componenteEletronico.setEspecificacao(this.especificacaoDAO.encontrarId(this.especificacaoSelecionado));
		        	this.componenteEletronico.setTipoComponente(this.tipoComponenteDAO.encontrarId(this.tipoSelecionado));
		        	this.componenteEletronico.setFornecedor(this.fornecedorDAO.encontrarId(this.fornecedorSelecionado));
		        	this.componenteEletronicoDAO.atualizar(this.componenteEletronico);
		        	this.facesContext.addMessage(null, new FacesMessage("Componente Atualizado"));
		        }
		        this.listaComponenteEletronicos = componenteEletronicoDAO.listarTodos();
		        //Atualiza e executa elementos Javascript na tela assincronamente
			    PrimeFaces.current().executeScript("PF('componenteEletronicoDialog').hide()");
			    PrimeFaces.current().ajax().update("form:messages", "form:dt-componenteEletronicos");
	        } catch (Exception e) {
	            String errorMessage = getMensagemErro(e);
	            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	        }
        }
	}	
	
/*	//Realiza validações adicionais (não relizadas no modelo) e/ou complexas/interdependentes
	private boolean componenteEletronicoValido() {
		if (this.permissoesSelecionadas.isEmpty()) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione ao menos uma permissão para o novo usuário.", null));
			return false;
		}			
		//if (this.componenteEletronico.getCodigo() == null && !this.componenteEletronicoDAO.ehComponenteEletronicoUnico(this.componenteEletronico.getComponenteEletronico())) {
		//	this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Este nome de usuário já está em uso.", null));
		//	return false;
		//}
		return true;
	}
	*/
	//Chamado pelo botão remover da tabela
	public void remover() {
		try {
			this.componenteEletronicoDAO.deleteDependencies(this.componenteEletronico.getCodigo());
			this.componenteEletronicoDAO.excluir(this.componenteEletronico);
			this.listaComponenteEletronicos = componenteEletronicoDAO.listarTodos();
	        this.componenteEletronico = null;
	        this.facesContext.addMessage(null, new FacesMessage("Componente Removido"));
	        PrimeFaces.current().ajax().update("form:messages", "form:dt-componenteEletronicos");
        } catch (Exception e) {
            String errorMessage = getMensagemErro(e);
            this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
        }
	}
	
	//Chamado pelo botão alterar da tabela
	public void alterar() {
		
		this.especificacaoSelecionado= this.componenteEletronico.getEspecificacao().getCodigo();
    	this.tipoSelecionado = this.componenteEletronico.getTipoComponente().getCodigo();
    	this.fornecedorSelecionado = this.componenteEletronico.getFornecedor().getCnpj();
	}
	
	//Captura mensagem de erro das validações do Hibernates
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
	public ComponenteEletronico getComponenteEletronico() {
		return componenteEletronico;
	}

	public void setComponenteEletronico(ComponenteEletronico componenteEletronico) {
		this.componenteEletronico = componenteEletronico;
	}

	public List<ComponenteEletronico> getListaComponenteEletronicos() {
		return listaComponenteEletronicos;
	}

	public void setListaComponenteEletronicos(List<ComponenteEletronico> listaComponenteEletronicos) {
		this.listaComponenteEletronicos = listaComponenteEletronicos;
	}

	public FornecedorDAO getFornecedorDAO() {
		return fornecedorDAO;
	}

	public void setFornecedorDAO(FornecedorDAO fornecedorDAO) {
		this.fornecedorDAO = fornecedorDAO;
	}

	public TipoComponenteDAO getTipoComponenteDAO() {
		return tipoComponenteDAO;
	}

	public void setTipoComponenteDAO(TipoComponenteDAO tipoComponenteDAO) {
		this.tipoComponenteDAO = tipoComponenteDAO;
	}

	public EspecificacaoDAO getEspecificacaoDAO() {
		return especificacaoDAO;
	}

	public void setEspecificacaoDAO(EspecificacaoDAO especificacaoDAO) {
		this.especificacaoDAO = especificacaoDAO;
	}

	public List<SelectItem> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<SelectItem> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public List<SelectItem> getTipos() {
		return tipos;
	}

	public void setTipos(List<SelectItem> tipos) {
		this.tipos = tipos;
	}

	public List<SelectItem> getEspecificacoes() {
		return especificacoes;
	}

	public void setEspecificacoes(List<SelectItem> especificacoes) {
		this.especificacoes = especificacoes;
	}

	public ComponenteEletronicoDAO getComponenteEletronicoDAO() {
		return componenteEletronicoDAO;
	}

	public void setComponenteEletronicoDAO(ComponenteEletronicoDAO componenteEletronicoDAO) {
		this.componenteEletronicoDAO = componenteEletronicoDAO;
	}

	public String getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(String fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	public Integer getTipoSelecionado() {
		return tipoSelecionado;
	}

	public void setTipoSelecionado(Integer tipoSelecionado) {
		this.tipoSelecionado = tipoSelecionado;
	}

	public Integer getEspecificacaoSelecionado() {
		return especificacaoSelecionado;
	}

	public void setEspecificacaoSelecionado(Integer especificacaoSelecionado) {
		this.especificacaoSelecionado = especificacaoSelecionado;
	}
	
	
	
	
}
