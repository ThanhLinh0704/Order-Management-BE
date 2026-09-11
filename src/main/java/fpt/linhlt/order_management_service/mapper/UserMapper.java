package fpt.linhlt.order_management_service.mapper;

import fpt.linhlt.order_management_service.dto.response.UserResponse;
import fpt.linhlt.order_management_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.code", target = "roleCode")
    UserResponse toUserResponse(User user);
}
