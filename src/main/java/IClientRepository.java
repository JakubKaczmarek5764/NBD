import java.util.List;

public interface IClientRepository {
    void create(Client client);
    List<Client> getAll();
    List<Client> getByFirstName(String firstName);
    List<Client> getByLastName(String lastName);
    Client getByPersonalID(String personalID);
    Client getById(long id);
    void updateClientFirstNameById(long id, String newName);
}
