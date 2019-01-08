package ar.com.mobeats.consolidar.backend.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.com.mobeats.consolidar.backend.model.BackEndObject;
import ar.com.mobeats.consolidar.backend.model.Organizacion;
import ar.com.mobeats.consolidar.backend.model.User;

public class UserData extends BackEndObject {

    private static final long serialVersionUID = -5277015751360471015L;

    @JsonProperty("_id")
    private String id;

    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Organizacion organizacion;
    private String impresora;
    private String cliente;

    public UserData() {
    }

    public static UserData fromUser(User user) {
        return new UserData(user);
    }

    public static List<UserData> fromUsers(List<User> users) {
        List<UserData> result = new ArrayList<UserData>();
        for (User user : users) {
            result.add(new UserData(user));
        }
        return result;
    }

    public UserData(User user) {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public String getImpresora() {
        return impresora;
    }

    public void setImpresora(String impresora) {
        this.impresora = impresora;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

}
