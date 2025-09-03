// src/main/java/tw/syuhao/ordersystem/dto/ProfileDTO.java
package tw.syuhao.ordersystem.Ddto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String remark;       // 備註
    private LocalDateTime createdAt;
}
