package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.Funcionario;
import br.edu.projeto.model.Usuario;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class FuncionarioDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Funcionario encontrarId(Integer id) {
        return em.find(Funcionario.class, id);
    }
	
	public Boolean uniqueFuncionario(String u) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Funcionario> criteria = cb.createQuery(Funcionario.class);
        Root<Funcionario> funcionario = criteria.from(Funcionario.class);
        criteria.select(funcionario);
        criteria.where(cb.like(funcionario.get("nome"), u));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	
	public List<Funcionario> listarTodos() {
	    return em.createQuery("SELECT a FROM Funcionario a ", Funcionario.class).getResultList();      
	}
	
	public void salvar(Funcionario u) {
		em.persist(u);
	}
	
	public void atualizar(Funcionario u) {
		em.merge(u);
	}
	
	public void excluir(Funcionario u) {
		em.remove(em.getReference(Funcionario.class, u.getCodigo()));
	}
	
}
