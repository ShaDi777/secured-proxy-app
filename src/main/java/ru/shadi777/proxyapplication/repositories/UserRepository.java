package ru.shadi777.proxyapplication.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shadi777.proxyapplication.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    User findByUsername(String username);

    @Override
    void delete(User user);
}