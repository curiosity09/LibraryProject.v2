package by.tms.entity.user;

import by.tms.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

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
    public Librarian(String username, String password, UserData userData, List<Order> orders, Level libLevel) {
        super(username, password, userData, orders);
        this.libLevel = libLevel;
    }
}
