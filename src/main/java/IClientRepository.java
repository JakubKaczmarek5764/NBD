import java.util.List;

public interface IClientRepository extends Repository<Client> {
    void updateFirstName(Client client, String firstName);
}
