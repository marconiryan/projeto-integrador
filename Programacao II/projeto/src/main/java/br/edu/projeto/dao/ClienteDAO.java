package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.Cliente;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class ClienteDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Cliente encontrarId(String id) {
        return em.find(Cliente.class, id);
    }
	
	public Boolean uniqueCliente(String u) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Cliente> criteria = cb.createQuery(Cliente.class);
        Root<Cliente> cliente = criteria.from(Cliente.class);
        criteria.select(cliente);
        criteria.where(cb.like(cliente.get("cpfCnpj"), u));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	
	public List<Cliente> listarTodos() {
	    return em.createQuery("SELECT a FROM Cliente a ", Cliente.class).getResultList();      
	}
	
	public void salvar(Cliente u) {
		em.persist(u);
	}
	
	public void atualizar(Cliente u) {
		em.merge(u);
	}
	
	public void excluir(Cliente u) {
		em.remove(em.getReference(Cliente.class, u.getCpfCnpj()));
	}
	
}
