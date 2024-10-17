import java.util.List;

public class ClientRepository implements IClientRepository {
    public void create(Client client){
        Repository.create(client);
    }
    public List<Client> getAll() {
        return Repository.getAll(Client.class);
    }
    // tu bedziemy tylko cos takiego pisac
    public List<Client> getByFirstName(String firstName){

        return Repository.getByParam(Client.class, firstName, "firstName");
    }

    public List<Client> getByLastName(String lastName){
        return Repository.getByParam(Client.class, lastName, "lastName");
    }
    // chyba wszystkie parametry by wypadalo napisac
    public Client getByPersonalID(String personalID){
        return Repository.getByParam(Client.class, personalID, "personalID").get(0);
    }
    public Client getById(long id){
        return Repository.getByParam(Client.class, id, "id").get(0);
    }
    public void updateClientFirstNameById(long id, String newName) {
        Repository.update(Client.class, newName, "firstName", id);
    };
    public void delete(long id){
        Repository.delete(Client.class, id);
    }
}
