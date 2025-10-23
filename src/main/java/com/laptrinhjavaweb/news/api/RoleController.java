package com.laptrinhjavaweb.news.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.news.dto.ApiResponse;
import com.laptrinhjavaweb.news.dto.request.RoleRequest;
import com.laptrinhjavaweb.news.dto.request.RoleUpdateRequest;
import com.laptrinhjavaweb.news.dto.response.RoleResponse;
import com.laptrinhjavaweb.news.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.save(request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ApiResponse.builder().build();
    }

    @PutMapping("/{id}")
    ApiResponse<RoleResponse> update(@PathVariable Long id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.update(id, roleUpdateRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.findAll())
                .build();
    }
}
