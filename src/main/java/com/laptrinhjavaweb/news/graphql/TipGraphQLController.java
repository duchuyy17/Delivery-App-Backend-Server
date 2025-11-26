package com.laptrinhjavaweb.news.graphql;

import com.laptrinhjavaweb.news.dto.request.mongo.TipInput;
import com.laptrinhjavaweb.news.mongo.TipDocument;
import com.laptrinhjavaweb.news.service.TipService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TipGraphQLController {
    private final TipService tipService;

    @QueryMapping
    TipDocument tips(){
        return tipService.getAllTips().getFirst();
    }
    @MutationMapping
    TipDocument createTip(@Argument TipDocument tip){
        return tipService.createTip(tip);
    }
    @MutationMapping
    public TipDocument editTipping(@Argument TipInput tippingInput) {
        return tipService.editTipping(tippingInput);
    }
}
