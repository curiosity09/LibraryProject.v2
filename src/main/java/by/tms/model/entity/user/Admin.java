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
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("admin")
@PrimaryKeyJoinColumn(name = "account_id")
@Table(schema = "library_hibernate")
public class Admin extends Account {

    @Column(name = "admin_level")
    @Enumerated(EnumType.STRING)
    private Level adminLevel;

    @Builder
    public Admin(String username, String password, String role, UserData userData, List<Order> orders, Level adminLevel) {
        super(username, password, role, userData, orders);
        this.adminLevel = adminLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Admin admin = (Admin) o;
        return id != null && Objects.equals(id, admin.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
