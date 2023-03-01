package br.edu.projeto.util;

import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.edu.projeto.dao.FuncionarioDAO;
import br.edu.projeto.dao.TipoPermissaoDAO;
import br.edu.projeto.dao.UsuarioDAO;
import br.edu.projeto.model.Funcionario;
import br.edu.projeto.model.TipoPermissao;
import br.edu.projeto.model.Usuario;

//Executa classe uma única vez ao iniciar a aplicação no servidor
//Recurso usado para criar o primeiro usuário ADMINISTRADOR no sistema, se o mesmo não existir ainda
@WebListener
public class AdminSetup implements ServletContextListener {

	@Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private UsuarioDAO usuarioDAO;
    
    @Inject
    private FuncionarioDAO funcionarioDAO;
    
    @Inject
    private TipoPermissaoDAO tipoPermissaoDAO;

    private Usuario admin;
    private Funcionario func;
    
    public void contextInitialized(ServletContextEvent event) {
        if (usuarioDAO.ehUsuarioUnico("admin")){ 	
	    	admin = new Usuario();
	        admin.setEmail("admin@admin.com");
	        String senhaPadrao = "admin";
	        admin.setSenha(passwordHash.generate(senhaPadrao.toCharArray()));
	        admin.setUsuario("admin");
	        TipoPermissao permissao = tipoPermissaoDAO.encontrarPermissao(Permissao.ADMINISTRADOR.id);
	        permissao.addUsuario(admin);
	        usuarioDAO.salvar(admin);
        }
        if (funcionarioDAO.uniqueFuncionario("teste")){ 	
	    	func = new Funcionario();
	        func.setEmail("teste@teste.com");
	        String pass = "teste";
	        func.setSenha(passwordHash.generate(pass.toCharArray()));
	        func.setNome("teste");
	        func.setTipo(1);
	        func.setTelefone("101010110");
	        funcionarioDAO.salvar(func);
        }
    }
}
