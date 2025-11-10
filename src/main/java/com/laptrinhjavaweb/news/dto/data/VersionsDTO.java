package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionsDTO {
    private AppVersionDTO customerAppVersion;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppVersionDTO {
        private String android;
        private String ios;
    }
}
