package br.com.gastrohub.user.entity;

import br.com.gastrohub.address.entity.Address;
import br.com.gastrohub.infra.exception.ValidationException;
import br.com.gastrohub.user.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_login", columnNames = "login")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 36)
    private UUID id;

    @Column(name = "nome", length = 150, nullable = false)
    private String nome;

    @Column(name = "email", length = 200, nullable = false)
    private String email;

    @Column(name = "login", length = 30, nullable = false)
    private String login;

    @Column(name = "senha", length = 255, nullable = false)
    private String senha;

    @Column(name = "data_ultima_alteracao", nullable = false)
    private LocalDateTime dataUltimaAlteracao;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> address = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 30, nullable = false)
    private Role role;

    public User() {
    }

    public User(String nome, String email, String login, String senha, List<Address> address, Role role) {
        validateNome(nome);
        validateEmail(email);
        validateLogin(login);
        validateSenha(senha);

        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.address = address;
        this.role = role;
    }

    public void updateProfile(String nome, String email, String login, Role role) {
        if (nome != null) changeNome(nome);
        if (email != null) changeEmail(email);
        if (login != null) changeLogin(login);
        if (role != null) changeRole(role);
    }


    public void changeNome(String nome) {
        validateNome(nome);
        this.nome = nome;
    }

    public void changeEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public void changeLogin(String login) {
        validateLogin(login);
        this.login = login;
    }

    public void changeSenha(String senhaCriptografada) {
        if (senhaCriptografada == null || senhaCriptografada.isBlank()) {
            throw new ValidationException("Senha não pode ser vazia");
        }
        this.senha = senhaCriptografada;
    }

    public void changeRole(Role role) {
        if (role == null) {
            throw new ValidationException("Role não pode ser nula");
        }
        this.role = role;
    }


    private void validateNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValidationException("Nome não pode ser vazio");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Email inválido");
        }
    }

    private void validateLogin(String login) {
        if (login == null || login.length() < 3) {
            throw new ValidationException("Login deve ter no mínimo 3 caracteres");
        }
    }

    private void validateSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new ValidationException("Senha deve ter no mínimo 6 caracteres");
        }
    }


    @PrePersist
    public void prePersist() {
        if (this.dataUltimaAlteracao == null) {
            this.dataUltimaAlteracao = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dataUltimaAlteracao = LocalDateTime.now();
    }


    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public User setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getSenha() {
        return senha;
    }



    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public User setDataUltimaAlteracao(LocalDateTime dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
        return this;
    }

    public List<Address> getAddress() {
        return address;
    }

    public User setAddress(List<Address> address) {
        this.address = address;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public void addAddress(Address address) {
        address.setUser(this);
        this.address.add(address);
    }

    public void removeAddress(Address address) {
        this.address.remove(address);
        address.setUser(null);
    }



    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", dataUltimaAlteracao=" + dataUltimaAlteracao +
                ", address=" + address +
                ", role=" + role +
                ", nome='" + nome + '\'' +
                '}';
    }
}
