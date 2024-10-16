import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.lang.reflect.Field;
import java.util.List;
// tu albo w child repozytoriach musi byc zrobiona jakas sanityzacja danych
class Repository {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("nbd");;

    static <T> void create(T obj) {
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

    static <T> List<T> getAll(Class<T> objClass) {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(objClass);
        Root<T> rootEntry = query.from(objClass);
        query.select(rootEntry);
        return em.createQuery(query).getResultList();
    }
    static <T> List<T> getByParam(Class<T> objClass, Object parameter, String parameterName) {

        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(objClass);
        Root<T> rootEntry = query.from(objClass);
        Predicate predicate = cb.equal(rootEntry.get(parameterName), parameter);
        query.where(predicate);
        return em.createQuery(query).getResultList();
    }
    static <T> void update(Class<T> objClass, Object newValue, String parameterName, Long id){

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T obj = em.find(objClass, id, LockModeType.OPTIMISTIC);
            Field param = objClass.getDeclaredField(parameterName);
            param.setAccessible(true);
            param.set(obj, newValue);
            em.merge(obj);
            transaction.commit();
        } catch (IllegalAccessException | NoSuchFieldException e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
