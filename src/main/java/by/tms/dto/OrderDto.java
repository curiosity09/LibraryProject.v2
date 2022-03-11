package by.tms.dto;

import by.tms.dto.user.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OrderDto implements Serializable {

    private Long id;
    private List<BookDto> bookList;
    private AccountDto user;
    private LocalDateTime rentalTime;
    private LocalDateTime rentalPeriod;
}
