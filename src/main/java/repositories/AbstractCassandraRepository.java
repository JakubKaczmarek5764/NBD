package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;

import java.net.InetSocketAddress;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

public class AbstractCassandraRepository implements AutoCloseable {
    private static CqlSession session;

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .withKeyspace(CqlIdentifier.fromCql("rent_a_literature"))
                .build();

        CreateKeyspace keyspace = createKeyspace(CqlIdentifier.fromCql("rent_a_literature"))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    public static CqlSession getSession() {
        return session;
    }

    @Override
    public void close() throws Exception {
        session.close();
    }
}
