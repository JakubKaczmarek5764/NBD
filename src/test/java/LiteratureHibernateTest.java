import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.*;
// https://docs.jboss.org/hibernate/orm/6.6/introduction/html_single/Hibernate_Introduction.html#hello-hibernate
public class LiteratureHibernateTest {
    // to chyba niepotrzebne bo repo to ogarnia
//    private static EntityManagerFactory factory;
//    @BeforeAll
//    public static void init() {
//        factory = Persistence.createEntityManagerFactory("nbd");
//    }
    @Test
    public void persistLiterature() {
        throw new NotImplementedException();
    }
//    static void inSession(EntityManagerFactory factory, Consumer<EntityManager> work) {
//        var entityManager = factory.createEntityManager();
//        var transaction = entityManager.getTransaction();
//        try {
//            transaction.begin();
//            work.accept(entityManager);
//            transaction.commit();
//        }
//        catch (Exception e) {
//            if (transaction.isActive()) transaction.rollback();
//            throw e;
//        }
//        finally {
//            entityManager.close();
//        }
//    }
}
