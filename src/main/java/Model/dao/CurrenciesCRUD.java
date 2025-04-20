package Model.dao;

import java.util.List;
import java.util.Optional;

public interface CurrenciesCRUD<K, T>{

    List<T> findAll();

    Optional<T> findById(K id);

    void update(T entity);

    T save(T entity);
}
