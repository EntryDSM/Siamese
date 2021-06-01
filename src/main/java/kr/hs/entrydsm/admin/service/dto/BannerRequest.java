package kr.hs.entrydsm.admin.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@AllArgsConstructor
public class BannerRequest {
    @PositiveOrZero
    private long imageId;

    @Positive
    private long ttl;
}
