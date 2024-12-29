package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class AbstractCassandraRepository implements AutoCloseable {
    private static CqlSession session;

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
//                .withKeyspace(CqlIdentifier.fromCql("nbd"))
                .build();
        // chyba trzeba stworzyc tu keyspace
    }
    public static CqlSession getSession() {
        return session;
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}
