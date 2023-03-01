package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.ComponentePlaca;
import br.edu.projeto.model.TipoComponente;

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
	
	public Boolean uniqueComponentePlaca(Integer x, Integer y) {
		String sql = "Select * From public.componente_placa where cod_componente = ? and cod_placa = ?";
		Query query = em.createNativeQuery(sql, TipoComponente.class);
		query.setParameter(1, x);
		query.setParameter(2, y);
        if (query.getResultList().isEmpty())
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