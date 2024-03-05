package pl.malcew.publicmentoringmalcew.repo;

import java.util.List;

public interface GenericRepository<T, T1> {
    void create(T entity);
    T read(T1 id);
    List<T> viewAll();
    void update(T entity);
    void delete(T entity);

}
