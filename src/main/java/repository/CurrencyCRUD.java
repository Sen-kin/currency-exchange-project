package repository;

import java.util.List;

public interface CurrencyCRUD<K, T> {

    List<T> findAll();

    T find(K code);

    T create(T entity);
}
