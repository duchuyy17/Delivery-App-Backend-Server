package com.laptrinhjavaweb.news.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.news.dto.ApiResponse;
import com.laptrinhjavaweb.news.dto.request.UserCreationRequest;
import com.laptrinhjavaweb.news.dto.request.UserUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.PagedResponse;
import com.laptrinhjavaweb.news.dto.response.UserResponse;
import com.laptrinhjavaweb.news.exception.AppException;
import com.laptrinhjavaweb.news.exception.ErrorCode;
import com.laptrinhjavaweb.news.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest user) {
        return userService.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ApiResponse<UserResponse> userResponse = userService.findById(userId);
        if (userResponse.getResult().getUserName().equals(username)) {
            return userResponse;
        } else throw new AppException(ErrorCode.ACCESS_DENIED);
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateRequest user) {
        return userService.update(userId, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<PagedResponse<UserResponse>> getAllUser(
            @RequestParam int page,
            @RequestParam int limit,
            @RequestParam(required = false) String sortName,
            @RequestParam(required = false) String sortBy) {
        PageRequest pageable = sortName.equals("desc")
                ? PageRequest.of(page - 1, limit, Sort.Direction.DESC, sortBy)
                : PageRequest.of(page - 1, limit, Sort.Direction.ASC, sortBy);
        List<UserResponse> userResponses = userService.findAll(pageable);
        Integer totalItem = userService.count();
        int totalPage = (int) Math.ceil((double) totalItem / limit);

        return ApiResponse.<PagedResponse<UserResponse>>builder()
                .message("Found")
                .result(PagedResponse.<UserResponse>builder()
                        .data(userResponses)
                        .currentPage(page)
                        .totalPages(totalPage)
                        .limit(limit)
                        .totalElements(totalItem)
                        .build())
                .build();
    }

    @PostAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ApiResponse<?> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ApiResponse.builder().build();
    }

    @GetMapping("/current-user")
    public ApiResponse<UserResponse> getCurrentUser() {

        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
}
