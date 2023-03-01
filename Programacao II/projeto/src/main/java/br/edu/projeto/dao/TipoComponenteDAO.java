package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.projeto.model.ComponentePlaca;
import br.edu.projeto.model.TipoComponente;


//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class TipoComponenteDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public TipoComponente encontrarId(Integer id) {
        return em.find(TipoComponente.class, id);
    }
	
	public Boolean uniqueTipoComponente(String x, String y) {
		String sql = "Select * From public.tipo_componente where nome = ? and encapsulamento = ?";
		Query query = em.createNativeQuery(sql, TipoComponente.class);
		query.setParameter(1, x);
		query.setParameter(2, y);
        if (query.getResultList().isEmpty())
        	return true;
        return false;
    }
	
	public void deleteDependencies(Integer id) {
		
		List<Integer> i = new ArrayList<Integer>();
		
		String sql = "Select codigo From public.componente_eletronico where cod_tipo_componente = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, id);
		i = query.getResultList();
			
		for(Integer x : i) {
			sql = "Delete From public.componente_placa where cod_componente = ?";
			query = em.createNativeQuery(sql, ComponentePlaca.class);
			query.setParameter(1, x);
			query.executeUpdate();
		}
		
		
		sql = "Delete From public.componente_eletronico where cod_tipo_componente = ?";
		query = em.createNativeQuery(sql, TipoComponente.class);
		query.setParameter(1, id);
		query.executeUpdate();
	}
	
	
	public List<TipoComponente> listarTodos() {
	    return em.createQuery("SELECT a FROM TipoComponente a ", TipoComponente.class).getResultList();      
	}
	
	public void salvar(TipoComponente u) {
		em.persist(u);
	}
	
	public void atualizar(TipoComponente u) {
		em.merge(u);
	}
	
	public void excluir(TipoComponente u) {
		em.remove(em.getReference(TipoComponente.class, u.getCodigo()));
	}
	
}