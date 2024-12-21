package model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import entities.Cliente;

public class ClienteModelCRUD {
	protected EntityManager entityManager;
	private TypedQuery<Cliente> query;
	private String hql;

	public ClienteModelCRUD() {
		entityManager = getEntityManager();
	}

	private EntityManager getEntityManager() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("clienteHibernate");
		if (entityManager == null) {
			entityManager = factory.createEntityManager();
		}
		return entityManager;
	}

	public Cliente getById(final int id) {
		return entityManager.find(Cliente.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> select() {
		return entityManager.createQuery("FROM " + Cliente.class.getName()).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> select(String search) {

		if (search == null || search.isEmpty()) {
			hql = "FROM Cliente";
			query = entityManager.createQuery(hql, Cliente.class);
		} else {
			hql = "FROM Cliente WHERE nome LIKE :search";
			query = entityManager.createQuery(hql, Cliente.class);
			query.setParameter("search", search + "%");
		}

		return query.getResultList();
	}

	public void insert(Cliente cliente) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(cliente);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void update(Cliente cliente) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(cliente);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}

	public void delete(int id) {
		try {
			entityManager.getTransaction().begin();
			Cliente cliente = entityManager.find(Cliente.class, id);
			entityManager.remove(cliente);
			entityManager.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			entityManager.getTransaction().rollback();
		}
	}
}