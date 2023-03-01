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

import br.edu.projeto.model.ComponentePlaca;
import br.edu.projeto.model.PlacaEletronica;
import br.edu.projeto.model.TipoComponente;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados
@Stateful
public class PlacaEletronicaDAO implements Serializable{

	@Inject
	//Cria a conexão e controla a transação com o SGBD (usado pelo Hibernate)
    private EntityManager em;
	
	public PlacaEletronica encontrarId(Integer id) {
        return em.find(PlacaEletronica.class, id);
    }
	
	public Boolean uniquePlacaEletronica(String u) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlacaEletronica> criteria = cb.createQuery(PlacaEletronica.class);
        Root<PlacaEletronica> componenteEletronico = criteria.from(PlacaEletronica.class);
        criteria.select(componenteEletronico);
        criteria.where(cb.like(componenteEletronico.get("nome"), u));
        if (em.createQuery(criteria).getResultList().isEmpty())
        	return true;
        return false;
    }
	
	public void deleteDependencies(Integer id) {
		String sql = "Delete From public.componente_placa where cod_placa = ?";
		Query query = em.createNativeQuery(sql, ComponentePlaca.class);
		query.setParameter(1, id);
		query.executeUpdate();
	}
	
	public List<PlacaEletronica> listarTodos() {
		List<PlacaEletronica> r = new ArrayList<PlacaEletronica>();
		List<PlacaEletronica> l = em.createQuery("SELECT a FROM PlacaEletronica a ", PlacaEletronica.class).getResultList();
		for(PlacaEletronica i : l) {
			
			String sql = "Select * From public.componente_placa where cod_placa = ? ";
			Query query = em.createNativeQuery(sql, ComponentePlaca.class);
			query.setParameter(1, i.getCodigo());
			i.setComponentesPlaca(query.getResultList());
			r.add(i);
		}
		return r;
	}
	
	
	
	public void salvar(PlacaEletronica u) {
		em.persist(u);
	}
	
	public void atualizar(PlacaEletronica u) {
		em.merge(u);
	}
	
	public void excluir(PlacaEletronica u) {
		em.remove(em.getReference(PlacaEletronica.class, u.getCodigo()));
	}
	
}