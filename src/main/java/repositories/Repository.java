package repositories;

import mappers.MongoUniqueId;

import java.util.List;


interface Repository<T> {

    void create(T obj);
    List<T> getAll();
    T getById(MongoUniqueId id);
    void delete(T obj);
    void update(T obj);
    void emptyCollection();

}
