package by.tms.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(schema = "library_hibernate")
public class Section extends BaseEntity<Long> {

    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany(mappedBy = "section")
    @ToString.Exclude
    List<Book> books;
}
