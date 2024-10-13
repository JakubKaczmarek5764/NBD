import java.util.List;

public class BorrowingRepository {
    public static void create(Borrowing borrowing){
        // tu chyba logika biznesowa
        int additionalWeight = borrowing.getLiterature().getTotalWeight();
        Client tmpClient = borrowing.getClient();
        int currentWeight = sumClientWeights(tmpClient.getId());
        if (!checkLiteratureById(borrowing.getLiterature().getId()) && ((additionalWeight + currentWeight) <= tmpClient.getMaxWeight())){
            Repository.create(borrowing);
        } else {
            throw new WeightExceededException("Can't add borrowing, client exceeded the limit");
        }

    }
    public static List<Borrowing> getAll(){
        return Repository.getAll(Borrowing.class);
    }
    public static boolean checkLiteratureById(long id){
        Literature literature = LiteratureRepository.getById(id);
        List<Borrowing> output = Repository.getBy(Borrowing.class, literature, "literature");
        return !output.isEmpty();
    }
    private static int sumClientWeights(long id){
        int sum = 0;
        List<Borrowing> list = getAllBorrowingsByClientId(id);
        for (Borrowing b : list){
            sum += b.getLiterature().getTotalWeight();
        }

        return sum;
    }
    public static List<Borrowing> getAllBorrowingsByClientId(long id){
        Client client = ClientRepository.getById(id);
        return Repository.getBy(Borrowing.class, client, "client");
    }
}
