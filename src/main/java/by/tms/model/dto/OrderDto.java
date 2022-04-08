package by.tms.model.dto;

import by.tms.model.dto.user.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
public class OrderDto implements Serializable {

    private Long id;
    private List<BookDto> bookList;
    private AccountDto user;
    @DateTimeFormat()
    @PastOrPresent
    private LocalDateTime rentalTime;
    @DateTimeFormat()
    private LocalDateTime rentalPeriod;
}
