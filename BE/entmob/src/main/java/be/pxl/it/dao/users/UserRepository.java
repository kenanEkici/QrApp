package be.pxl.it.dao.users;

import be.pxl.it.model.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByCardNumber(String cardNumber);
}