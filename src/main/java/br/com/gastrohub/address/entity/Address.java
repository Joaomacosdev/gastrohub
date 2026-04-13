package br.com.gastrohub.address.entity;

import br.com.gastrohub.infra.exception.ValidationException;
import br.com.gastrohub.user.entity.User;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String cep;
    private String rua;
    private String numero;
    private String cidade;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address() {
    }


    public Address(String cep, String rua, String numero, String cidade) {
        validateCep(cep);
        validateRua(rua);
        validateNumero(numero);
        validateCidade(cidade);

        this.cep = cep;
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
    }

    public void updateAddress(String cep, String rua, String numero, String cidade) {
        if (cep != null) changeCep(cep);
        if (rua != null) changeRua(rua);
        if (numero != null) changeNumero(numero);
        if (cidade != null) changeCidade(cidade);
    }

    public void changeCep(String cep) {
        validateCep(cep);
        this.cep = cep;
    }

    public void changeRua(String rua) {
        validateRua(rua);
        this.rua = rua;
    }

    public void changeNumero(String numero) {
        validateNumero(numero);
        this.numero = numero;
    }

    public void changeCidade(String cidade) {
        validateCidade(cidade);
        this.cidade = cidade;
    }


    private void validateCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new ValidationException("Cep não pode ser vazio");
        }

        if (!cep.matches("\\d{9}")) {
            throw new ValidationException("CEP deve conter 9 dígitos numéricos");
        }
    }

    private void validateRua(String rua) {
        if (rua == null || rua.isBlank()) {
            throw new ValidationException("Rua não pode ser vazio");
        }
    }

    private void validateNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new ValidationException("numero não pode ser vazio");
        }

        if (numero.length() > 10) {
            throw new ValidationException("Número deve ter no máximo 10 caracteres");
        }
    }

    private void validateCidade(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            throw new ValidationException("Cidade não pode ser vazio");
        }
    }

    public UUID getId() {
        return id;
    }

    public Address setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getCep() {
        return cep;
    }


    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getCidade() {
        return cidade;
    }

    public User getUser() {
        return user;
    }

    public Address setUser(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Address address = (Address) object;
        return Objects.equals(getId(), address.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
