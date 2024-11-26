package ru.demo.investmentapp.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client clientId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = true)
    private Role roleId;

    public User() {
    }

    public User(String userName, String password, Client clientId, Role roleId) {
        this.userName = userName;
        this.password = password;
        this.clientId = clientId;
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Client getClient() {
        return clientId;
    }

    public void setClient(Client clientId) {
        this.clientId = clientId;
    }

    public Role getRole() {
        return roleId;
    }

    public void setRole(Role roleId) {
        this.roleId = roleId;
    }

    // Метод для получения фамилии и имени клиента
    public String getClientFullName() {
        if (clientId != null)
            return clientId.getFirstName() + " " + clientId.getLastName();
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password) &&
                Objects.equals(clientId, user.clientId) &&
                Objects.equals(roleId, user.roleId);
    }

    @Override
    public int hashCode() {
        final int hashCode = 31 * userName.hashCode() + 17 * password.hashCode() +
                17 * clientId.getClientId().hashCode() + 17 * roleId.getRoleId().hashCode();
        return hashCode;
    }
}
