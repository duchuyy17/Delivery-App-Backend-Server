package com.laptrinhjavaweb.news.dto.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatchWeight {
    double wDistance = 0.55;
    double wAcceptance = 0.10;
    double wCompletion = 0.10;
    double wAvgSpeed = 0.05;
    double wTraffic = 0.20;
}
