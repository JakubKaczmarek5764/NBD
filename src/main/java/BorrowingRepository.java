import jakarta.persistence.*;

import java.util.GregorianCalendar;
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
                    client.addCurrentWeight(literature.getTotalWeight());
                    System.out.println("BR: "+client.getCurrentWeight());
                    literature.setBorrowed(true);
                    em.persist(borrowing);
                    transaction.commit();
                    borrowing.getClient().addCurrentWeight(literature.getTotalWeight());
                    borrowing.getLiterature().setBorrowed(true);
                    Client client2 = em.find(Client.class, borrowing.getClient().getId());
                    Literature literature2 = em.find(Literature.class, borrowing.getLiterature().getId());
                    System.out.println("BR klient "+client2.getCurrentWeight()+"literatura "+literature2.isBorrowed());
                } else {
                    throw new WeightExceededException("Can't add borrowing, client exceeded the limit");
                }
            }
        } catch (WeightExceededException e){
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw new WeightExceededException(e.getMessage());
        }   catch (Exception e){
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
        finally {
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

    public void endBorrowing(Borrowing borrowing) {
        EntityManager em =  emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Literature literature = em.find(Literature.class, borrowing.getLiterature().getId(), LockModeType.OPTIMISTIC);
            literature.setBorrowed(false);
            Client client = em.find(Client.class, borrowing.getClient().getId(), LockModeType.OPTIMISTIC);
            client.substractCurrentWeight(literature.getTotalWeight());
            borrowing.endBorrowing(new GregorianCalendar());
            transaction.commit();
        } catch (Exception e) {

        }
        finally {
            em.close();
        }
    }

}