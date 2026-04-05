package br.com.gastrohub.user.repository;

import br.com.gastrohub.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserService extends JpaRepository<User, UUID> {
}
