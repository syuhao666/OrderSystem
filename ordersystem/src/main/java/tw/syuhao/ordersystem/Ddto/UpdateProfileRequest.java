package tw.syuhao.ordersystem.Ddto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String email;
    private String remark;
    private String phone;
    private String address;
}
