package pl.malcew.publicmentoringmalcew.repo;

import java.util.List;

public interface GenericRepository<T, T1> {
    Long create(T entity);
    T read(T1 id);
    List<T> viewAll();
    T update(T entity);
    Long delete(T entity);

}
