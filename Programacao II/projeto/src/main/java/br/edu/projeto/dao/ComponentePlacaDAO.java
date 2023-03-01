package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.ComponentePlaca;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class ComponentePlacaDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public ComponentePlaca encontrarId(Integer id) {
        return em.find(ComponentePlaca.class, id);
    }
	
	public Boolean uniqueComponentePlaca(String u) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ComponentePlaca> criteria = cb.createQuery(ComponentePlaca.class);
        Root<ComponentePlaca> componentePlaca = criteria.from(ComponentePlaca.class);
        criteria.select(componentePlaca);
        criteria.where(cb.like(componentePlaca.get("nome"), u));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	
	public List<ComponentePlaca> listarTodos() {
	    return em.createQuery("SELECT a FROM ComponentePlaca a ", ComponentePlaca.class).getResultList();      
	}
	
	public void salvar(ComponentePlaca u) {
		em.persist(u);
	}
	
	public void atualizar(ComponentePlaca u) {
		em.merge(u);
	}
	
	public void excluir(ComponentePlaca u) {
		em.remove(em.getReference(ComponentePlaca.class, u.getCodigo()));
	}
	
}