import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.lang.reflect.Field;
import java.util.List;
// tu albo w child repozytoriach musi byc zrobiona jakas sanityzacja danych
class Repository {
    private static EntityManagerFactory emf;

    private static void emfChecker(){
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("nbd");
        }
    }
    static <T> void create(T obj) {
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
    static <T> List<T> getAll(Class<T> objClass) throws NotFoundException {
        emfChecker();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(objClass);
        List<T> output = em.createQuery(query).setLockMode(LockModeType.OPTIMISTIC).getResultList();
        if (output.isEmpty()){ throw new NotFoundException();}
        return output;
    }
    static <T> List<T> getBy(Class<T> objClass, Object parameter, String parameterName) throws NotFoundException {
        emfChecker();
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(objClass);
        Root<T> rootEntry = query.from(objClass);
        Predicate predicate = cb.equal(rootEntry.get(parameterName), parameter);
        query.where(predicate);
        List<T> output = em.createQuery(query).setLockMode(LockModeType.OPTIMISTIC).getResultList();
        if (output.isEmpty()){ throw new NotFoundException();}
        return output;
    }
    static <T> void update(Class<T> objClass, Object newValue, String parameterName, Long id){
        emfChecker();
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T obj = em.find(objClass, id, LockModeType.OPTIMISTIC);
            if (obj == null){
                throw new NotFoundException();
            }
            Field param = objClass.getDeclaredField(parameterName);
            param.set(obj, newValue);
            em.merge(obj);
            transaction.commit();
        } catch (NotFoundException | IllegalAccessException | NoSuchFieldException e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
