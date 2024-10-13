import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.*;

import java.util.List;

public class Repository {
    private static EntityManagerFactory emf;

    private static void emfChecker(){
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("nbd");
        }
    }
    public static <T> void create(T obj) {
        emfChecker();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(obj);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    private static <T> List<T> getAll(Class<T> objClass){
        emfChecker();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(objClass);
        Root<T> rootEntry = query.from(objClass);
        query.select(rootEntry);
        return em.createQuery(query).getResultList();
    }
    private static <T> List<T> getBy(Class<T> objClass, Object parameter, String parameterName){
        emfChecker();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(objClass);
        Root<T> rootEntry = query.from(objClass);
        Predicate predicate = cb.equal(rootEntry.get(parameterName), parameter);
        query.where(predicate);
        return em.createQuery(query).getResultList();
    }
    private static <T> void update(Class<T> objClass, Object parameter, Object newValue, String parameterName, Long id){
        emfChecker();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<T> criteriaUpdate = cb.createCriteriaUpdate(objClass);
        Root<T> root = criteriaUpdate.from(objClass);
        criteriaUpdate.set(parameterName, newValue);
        criteriaUpdate.where(cb.equal(root.get("id"), id)); // do sprawdzenia na pewno
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery(criteriaUpdate).executeUpdate();
            transaction.commit();
        } catch (Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
