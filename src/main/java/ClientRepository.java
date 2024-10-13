import java.util.List;

public class ClientRepository extends Repository {
    public void create(Client client){
        Repository.create(client);
    }
    public List<Client> getAll(){
        return Repository.getAll(Client.class);
    }
    // tu bedziemy tylko cos takiego pisac
    public List<Client> getByName(String name){
        return Repository.getBy(Client.class, name, "name");

    }
}
