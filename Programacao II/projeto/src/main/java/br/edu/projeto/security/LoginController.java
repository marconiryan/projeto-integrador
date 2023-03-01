package br.edu.projeto.security;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.projeto.model.Funcionario;

//Controlador da página de Login
@Named 
@RequestScoped
public class LoginController {

	@Inject
    private FacesContext facesContext;

    @Inject
    private SecurityContext securityContext;

    private Funcionario funcionario;

    @PostConstruct
    public void inicializarFuncionario() {
        funcionario = new Funcionario();
    }

    public void login() throws IOException{
    	if (facesContext.getExternalContext().getAuthType() != null) {
    		try {
    			throw new Exception();       	
            } catch (Exception e) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Existe um usuário autenticado! Use a opção logout primeiro.", ""));
            }
    	} else {
	    	Credential credential = new UsernamePasswordCredential(funcionario.getNome(), new Password(funcionario.getSenha()));
	    	AuthenticationStatus status = securityContext.authenticate(
	    	        	(HttpServletRequest)facesContext.getExternalContext().getRequest(),
	    	        	(HttpServletResponse)facesContext.getExternalContext().getResponse(),
	    	            AuthenticationParameters.withParams().credential(credential));
	    	if (status.equals(AuthenticationStatus.SUCCESS))
	    		facesContext.getExternalContext().redirect("cadastro_placa.xhtml");
	    	else if (status.equals(AuthenticationStatus.SEND_FAILURE)) {
	    		funcionario = new Funcionario();
	    		try {
	            	throw new Exception();       	
	            } catch (Exception e) {
	                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Inválido!", "Usuário ou senha incorretos."));
	            }
	    	}
    	}
    }
    
    public void logout() throws IOException {
    	facesContext.getExternalContext().invalidateSession();
    	facesContext.getExternalContext().redirect("logout.xhtml");
    }
    
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
