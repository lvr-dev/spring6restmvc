package guru.springframework.spring6restmvc.models;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Customer {
  private UUID id;
  private String customerName;
  private String version;
  private LocalDateTime createdDate;
  private LocalDateTime updateDate;
}
