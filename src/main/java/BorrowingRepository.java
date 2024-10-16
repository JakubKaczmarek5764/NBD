import jakarta.persistence.*;

import java.util.List;

public class BorrowingRepository {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("nbd");
    public static void create(Borrowing borrowing){
        EntityManager em =  emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Literature literature = em.find(Literature.class, borrowing.getLiterature().getId(), LockModeType.OPTIMISTIC);

            if (!literature.isBorrowed()) {
                Client client = em.find(Client.class, borrowing.getClient().getId(), LockModeType.OPTIMISTIC);
                if ((client.getCurrentWeight() + literature.getTotalWeight()) <= client.getMaxWeight()) {
                    em.persist(borrowing);
                    transaction.commit();
                }
            } else {
                throw new WeightExceededException("Can't add borrowing, client exceeded the limit");
            }
        } catch (Exception e){
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }


    }
    public static List<Borrowing> getAll(){
        return Repository.getAll(Borrowing.class);
    }
    public static boolean checkLiteratureById(long id){
        Literature literature = LiteratureRepository.getById(id);
        List<Borrowing> output = Repository.getByParam(Borrowing.class, literature, "literature");
        return !output.isEmpty();
    }
    public static List<Borrowing> getAllBorrowingsByClientId(long id){
        Client client = ClientRepository.getById(id);
        return Repository.getByParam(Borrowing.class, client, "client");
    }

}
