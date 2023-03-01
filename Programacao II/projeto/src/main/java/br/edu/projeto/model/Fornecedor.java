package br.edu.projeto.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CNPJ;


@Entity
@Table(name = "fornecedor")
public class Fornecedor {
    
	@NotEmpty()
    @NotNull()
	@Email()
    @Column(name = "email")
    private String email;
    
    @NotEmpty()
    @NotNull()
    @Column(name = "telefone")
    private String telefone;
    
    @NotEmpty()
    @NotNull()
    @Column(name = "nome")
    private String nome;
    
    @Id
    @NotEmpty()
    @NotNull()
    @CNPJ
    @Column(name = "cnpj")
    private String cnpj;
    
    
    @NotEmpty()
    @NotNull()
    @Column(name = "site")
    private String site;

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
