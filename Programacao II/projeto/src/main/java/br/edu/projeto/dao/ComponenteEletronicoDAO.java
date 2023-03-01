package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.ComponenteEletronico;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class ComponenteEletronicoDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public ComponenteEletronico encontrarId(String id) {
        return em.find(ComponenteEletronico.class, id);
    }
	
	public Boolean uniqueComponenteEletronico(String u) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ComponenteEletronico> criteria = cb.createQuery(ComponenteEletronico.class);
        Root<ComponenteEletronico> componenteEletronico = criteria.from(ComponenteEletronico.class);
        criteria.select(componenteEletronico);
        criteria.where(cb.like(componenteEletronico.get("pn"), u));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	
	public List<ComponenteEletronico> listarTodos() {
	    return em.createQuery("SELECT a FROM ComponenteEletronico a ", ComponenteEletronico.class).getResultList();      
	}
	
	public void salvar(ComponenteEletronico u) {
		em.persist(u);
	}
	
	public void atualizar(ComponenteEletronico u) {
		em.merge(u);
	}
	
	public void excluir(ComponenteEletronico u) {
		em.remove(em.getReference(ComponenteEletronico.class, u.getCodigo()));
	}
	
}