package com.laptrinhjavaweb.news.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.laptrinhjavaweb.news.dto.ApiResponse;
import com.laptrinhjavaweb.news.dto.request.PermissionRequest;
import com.laptrinhjavaweb.news.dto.response.PermissionResponse;
import com.laptrinhjavaweb.news.service.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.save(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.findAll())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        permissionService.delete(id);
        return ApiResponse.<Void>builder().build();
    }
}
