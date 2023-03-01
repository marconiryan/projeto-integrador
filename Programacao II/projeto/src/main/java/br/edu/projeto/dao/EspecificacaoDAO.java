package br.edu.projeto.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.edu.projeto.model.ComponentePlaca;
import br.edu.projeto.model.Especificacao;
import br.edu.projeto.model.TipoComponente;


//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class EspecificacaoDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public Especificacao encontrarId(Integer id) {
        return em.find(Especificacao.class, id);
    }
	
	public Boolean uniqueEspecificacao(double x, String y) {
		String sql = "Select codigo From public.especificacao where valor = ? and unidade_medida = ?";
		Query query = em.createNativeQuery(sql, Especificacao.class);
		query.setParameter(1, x);
		query.setParameter(2, y);
        if (query.getResultList().isEmpty())
        	return true;
        return false;
    }
	
	public void deleteDependencies(Integer id) {
		
		List<Integer> i = new ArrayList<Integer>();
		
		String sql = "Select codigo From public.componente_eletronico where cod_especificacao = ?";
		Query query = em.createNativeQuery(sql);
		query.setParameter(1, id);
		i = query.getResultList();
			
		for(Integer x : i) {
			sql = "Delete From public.componente_placa where cod_componente = ?";
			query = em.createNativeQuery(sql);
			query.setParameter(1, x);
			query.executeUpdate();
		}
		
		
		sql = "Delete From public.componente_eletronico where cod_especificacao = ?";
		query = em.createNativeQuery(sql);
		query.setParameter(1, id);
		query.executeUpdate();
	}
	
	
	public List<Especificacao> listarTodos() {
	    return em.createQuery("SELECT a FROM Especificacao a ", Especificacao.class).getResultList();      
	}
	
	public void salvar(Especificacao u) {
		em.persist(u);
	}
	
	public void atualizar(Especificacao u) {
		em.merge(u);
	}
	
	public void excluir(Especificacao u) {
		em.remove(em.getReference(Especificacao.class, u.getCodigo()));
	}
	
}