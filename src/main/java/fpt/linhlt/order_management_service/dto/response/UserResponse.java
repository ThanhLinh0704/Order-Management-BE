package fpt.linhlt.order_management_service.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;
    private String email;
    private String fullName;
    private String phone;
    private String status;
    private String roleCode;
}
