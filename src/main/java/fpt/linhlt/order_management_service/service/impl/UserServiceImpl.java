package fpt.linhlt.order_management_service.service.impl;

import fpt.linhlt.order_management_service.dto.request.CreateUserRequest;
import fpt.linhlt.order_management_service.dto.response.UserResponse;
import fpt.linhlt.order_management_service.entity.Role;
import fpt.linhlt.order_management_service.entity.User;
import fpt.linhlt.order_management_service.mapper.UserMapper;
import fpt.linhlt.order_management_service.repository.RoleRepository;
import fpt.linhlt.order_management_service.repository.UserRepository;
import fpt.linhlt.order_management_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email đã được sử dụng");
        }

        Role role = roleRepository.findByCode(request.getRoleCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Hệ thống chưa cấu hình role đăng ký"));

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(role)
                .status("ACTIVE")
                .build();
        return userMapper.toUserResponse(userRepository.save(user));
    }

}
