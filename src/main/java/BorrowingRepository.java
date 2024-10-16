import jakarta.persistence.*;

import java.util.List;

public class BorrowingRepository implements IBorrowingRepository{
    private static ClientRepository clientRepository = new ClientRepository();
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("nbd");
    public void create(Borrowing borrowing){
        EntityManager em =  emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Literature literature = em.find(Literature.class, borrowing.getLiterature().getId(), LockModeType.OPTIMISTIC);

            if (!literature.isBorrowed()) {
                Client client = em.find(Client.class, borrowing.getClient().getId(), LockModeType.OPTIMISTIC);
                if ((client.getCurrentWeight() + literature.getTotalWeight()) <= client.getMaxWeight()) {
                    em.persist(borrowing);
                    client.addCurrentWeight(literature.getTotalWeight());
                    literature.setBorrowed(true);
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

    public List<Borrowing> getAll(){
        return Repository.getAll(Borrowing.class);
    }
    public List<Borrowing> getAllBorrowingsByClientId(long id){
        Client client = clientRepository.getById(id);
        return Repository.getByParam(Borrowing.class, client, "client");
    }

}