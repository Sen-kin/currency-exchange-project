package Model.dao;

import java.util.Optional;

public interface CurrencyCRUD<K, T>{

    Optional<T> findByCode(K code);
}
