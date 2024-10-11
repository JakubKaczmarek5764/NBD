//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//// https://docs.jboss.org/hibernate/orm/6.6/introduction/html_single/Hibernate_Introduction.html#hello-hibernate
//public class LiteratureHibernateTest {
//    private static EntityManagerFactory factory;
//    @BeforeAll
//    public static void init() {
//        factory = Persistence.createEntityManagerFactory("example");
//    }
//    @Test
//    public void persistLiterature() {
//
//    }
////    static void inSession(EntityManagerFactory factory, Consumer<EntityManager> work) {
////        var entityManager = factory.createEntityManager();
////        var transaction = entityManager.getTransaction();
////        try {
////            transaction.begin();
////            work.accept(entityManager);
////            transaction.commit();
////        }
////        catch (Exception e) {
////            if (transaction.isActive()) transaction.rollback();
////            throw e;
////        }
////        finally {
////            entityManager.close();
////        }
////    }
//}
