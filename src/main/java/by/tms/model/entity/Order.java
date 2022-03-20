package by.tms.model.entity;

import by.tms.model.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "`order`", schema = "library_hibernate")
public class Order extends BaseEntity<Long> {

    @ManyToMany
    @JoinTable(
            name = "order_book",
            schema = "library_hibernate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> book;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private User account;
    @Column(name = "rental_time", nullable = false)
    private LocalDateTime rentalTime;
    @Column(name = "rental_period", nullable = false)
    private LocalDateTime rentalPeriod;
}
