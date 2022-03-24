package by.tms.model.entity.user;

import by.tms.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue("librarian")
@PrimaryKeyJoinColumn(name = "account_id")
@Table(schema = "library_hibernate")
public class Librarian extends Account {

    @Column(name = "lib_level")
    @Enumerated(EnumType.STRING)
    private Level libLevel;

    @Builder
    public Librarian(String username, String password, String role, UserData userData, List<Order> orders, Level libLevel) {
        super(username, password, role, userData, orders);
        this.libLevel = libLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Librarian librarian = (Librarian) o;
        return id != null && Objects.equals(id, librarian.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
