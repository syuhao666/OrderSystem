// src/main/java/tw/syuhao/ordersystem/Ddto/ChangePasswordRequest.java
package tw.syuhao.ordersystem.Ddto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
