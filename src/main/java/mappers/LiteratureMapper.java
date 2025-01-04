package mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import dao.LiteratureDao;

@Mapper
public interface LiteratureMapper {
    @DaoFactory
    LiteratureDao literatureDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    LiteratureDao literatureDao();
}
