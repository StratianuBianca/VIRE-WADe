package info.wade.users.repository;

import info.wade.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>
{
    User findFirstByEmail(String email);
    User findByEmail(String email);

    User findById(Long id);
}
