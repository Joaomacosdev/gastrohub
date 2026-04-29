package br.com.gastrohub.user.repository;

import br.com.gastrohub.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Page<User> findAll(Pageable pageable);

    Page<User> findByNomeIgnoreCaseContaining(String nome, Pageable pageable);

    Boolean existsByEmail(String email);

    Boolean existsByLogin(String login);

    Optional<User> findByLogin(String login);
}
