package tw.syuhao.ordersystem.Ddto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String remark;
    private String phone;
    private String address;
}
