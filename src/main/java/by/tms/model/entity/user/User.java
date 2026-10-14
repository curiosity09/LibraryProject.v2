package by.tms.model.entity.user;

import by.tms.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString(callSuper = true)
@DiscriminatorValue("user")
@PrimaryKeyJoinColumn(name = "account_id")
@Table(name = "`user`", schema = "library_hibernate")
public class User extends Account {

    @Column(name = "is_banned")
    private boolean isBanned;

    @Builder
    public User(String username, String password, String role, UserData userData, List<Order> orders, boolean isBanned) {
        super(username, password, role, userData, orders);
        this.isBanned = isBanned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
