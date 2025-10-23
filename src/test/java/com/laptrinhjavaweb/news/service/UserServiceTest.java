package com.laptrinhjavaweb.news.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.entity.RoleEntity;
import com.laptrinhjavaweb.news.entity.UserEntity;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.mapper.UserMapper;
import com.laptrinhjavaweb.news.repository.RoleRepository;
import com.laptrinhjavaweb.news.repository.UserRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private UserEntity userEntity;

    @Autowired
    private UserService userService;

    private RoleEntity roleUserEntity;
    private RoleEntity roleAdminEntity;
    private ArrayList<Long> roles;

    @BeforeEach
    void init() {
        LocalDate dob = LocalDate.of(2003, 11, 17);
        roles = new ArrayList<>(List.of(46L, 47L));
        request = UserCreationRequest.builder()
                .userName("john")
                .phone("0352542203")
                .email("lhuy123hl@gmail.com")
                .fullName("Đàm Đức Huy")
                .dateOfBirth(dob)
                .password("12345678")
                .roleCode(roles)
                .build();
        userResponse = UserResponse.builder()
                .id(12)
                .userName("john")
                .phone("0352542203")
                .email("lhuy123hl@gmail.com")
                .fullName("Đàm Đức Huy")
                .dateOfBirth(dob)
                .build();
        roleUserEntity =
                RoleEntity.builder().id(46).code("USER").name("Người dùng").build();
        roleAdminEntity =
                RoleEntity.builder().id(47).code("ADMIN").name("Quản trị").build();
        userEntity = UserEntity.builder()
                .id(12)
                .userName("john")
                .phone("0352542203")
                .email("lhuy123hl@gmail.com")
                .fullName("Đàm Đức Huy")
                .dateOfBirth(dob)
                .roles(new HashSet<>(List.of(roleUserEntity, roleAdminEntity)))
                .build();
        userResponse = userMapper.toUserResponse(userEntity);
    }
    //    @Test
    //    void CreateUser_validRequest_success(){
    //        Mockito.when(userRepository.existsByUserName(anyString())).thenReturn(false);
    //        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
    //
    //        when(roleRepository.findAllById(anyList())).thenReturn(new
    // ArrayList<>(List.of(roleAdminEntity,roleUserEntity)));
    //
    //        var response = userService.save(request);
    //
    //        assertThat(response.getResult().getId()).isEqualTo(12L);
    //        assertThat(response.getResult().getUserName()).isEqualTo("john");
    //
    //        assertThat(response.getResult().getRoles()).isEqualTo(userResponse.getRoles());
    //    }
    //
    //    @Test
    //    void CreateUser_userExisted_fail(){
    //        Mockito.when(userRepository.existsByUserName(anyString())).thenReturn(true);
    //
    //        var exception = assertThrows(AppException.class,()-> userService.save(request));
    //
    //        assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    //    }

    @Test
    @WithMockUser(username = "john")
    void GetInfo_valid_success() {
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(Optional.ofNullable(userEntity));

        var user = userService.getMyInfo();

        assertThat(user.getUserName()).isEqualTo("john");
        assertThat(user.getId()).isEqualTo(12L);
    }

    @Test
    @WithMockUser(username = "john")
    void GetInfo_invalid_fail() {
        Mockito.when(userRepository.findByUserName(anyString())).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1006);
    }
}
