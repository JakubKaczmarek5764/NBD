import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Repository {
    private static EntityManagerFactory emf;

    private static void emfChecker(){
        if (emf == null){
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


}
