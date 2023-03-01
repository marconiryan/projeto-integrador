package br.edu.projeto.util;

import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.edu.projeto.dao.FuncionarioDAO;

import br.edu.projeto.model.Funcionario;

//Executa classe uma única vez ao iniciar a aplicação no servidor
//Recurso usado para criar o primeiro usuário ADMINISTRADOR no sistema, se o mesmo não existir ainda
@WebListener
public class AdminSetup implements ServletContextListener {

	@Inject
    private Pbkdf2PasswordHash passwordHash;

    
    @Inject
    private FuncionarioDAO funcionarioDAO;

    private Funcionario func;
    
    public void contextInitialized(ServletContextEvent event) {
        if (funcionarioDAO.uniqueFuncionario("admin")){ 	
	    	func = new Funcionario();
	        func.setEmail("admin@admin.com");
	        String pass = "admin";
	        func.setSenha(passwordHash.generate(pass.toCharArray()));
	        func.setNome("admin");
	        func.setTipo(1);
	        func.setTelefone("101010110");
	        funcionarioDAO.salvar(func);
        }
    }
}
