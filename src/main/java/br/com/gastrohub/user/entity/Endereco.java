package br.com.gastrohub.user.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Endereco {

    private String cep;
    private String rua;
    private String numero;
    private String cidade;

}
