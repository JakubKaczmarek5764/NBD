import java.util.List;

public class ClientRepository {
    public static void create(Client client){
        Repository.create(client);
    }
    public static List<Client> getAll() {
        return Repository.getAll(Client.class);
    }
    // tu bedziemy tylko cos takiego pisac
    public static List<Client> getByFirstName(String firstName){

        return Repository.getBy(Client.class, firstName, "firstName");
    }

    public static List<Client> getByLastName(String lastName){
        return Repository.getBy(Client.class, lastName, "lastName");
    }
    // chyba wszystkie parametry by wypadalo napisac
    public static Client getByPersonalID(String personalID){
        return Repository.getBy(Client.class, personalID, "personalID").get(0);
    }
    public static Client getById(long id){
        return Repository.getBy(Client.class, id, "id").get(0);
    }

}
