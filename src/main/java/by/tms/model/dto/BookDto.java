package by.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class BookDto implements Serializable {

    private Long id;
    private String name;
    private AuthorDto author;
    private GenreDto genre;
    private SectionDto section;
    private int quantity;
    private int publicationYear;
}
