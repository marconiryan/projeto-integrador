package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.edu.projeto.model.Fornecedor;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class FornecedorDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Fornecedor encontrarId(String id) {
        return em.find(Fornecedor.class, id);
    }
	
	public Boolean uniqueFornecedor(String u) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Fornecedor> criteria = cb.createQuery(Fornecedor.class);
        Root<Fornecedor> fornecedor = criteria.from(Fornecedor.class);
        criteria.select(fornecedor);
        criteria.where(cb.like(fornecedor.get("cnpj"), u));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	public void deleteDependencies(String id) {
		
		List<Integer> i = new ArrayList<Integer>();
		
		String sql = "Select codigo From public.componente_eletronico where cod_fornecedor = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, id);
		i = query.getResultList();
			
		for(Integer x : i) {
			sql = "Delete From public.componente_placa where cod_componente = ?";
			query = em.createNativeQuery(sql);
			query.setParameter(1, x);
			query.executeUpdate();
		}
		
		
		sql = "Delete From public.componente_eletronico where cod_fornecedor = ?";
		query = em.createNativeQuery(sql);
		query.setParameter(1, id);
		query.executeUpdate();
	}
	
	public List<Fornecedor> listarTodos() {
	    return em.createQuery("SELECT a FROM Fornecedor a ", Fornecedor.class).getResultList();      
	}
	
	public void salvar(Fornecedor u) {
		em.persist(u);
	}
	
	public void atualizar(Fornecedor u) {
		em.merge(u);
	}
	
	public void excluir(Fornecedor u) {
		em.remove(em.getReference(Fornecedor.class, u.getCnpj()));
	}
	
}
