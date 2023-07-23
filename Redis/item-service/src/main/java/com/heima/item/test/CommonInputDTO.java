package com.heima.item.test;


import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CommonInputDTO {

//  @NotBlank
//  @ApiModelProperty(value = "微信:WX_FUNCTION,自助机为ZZJ_ FUNCTION",required = true)
//    private static final String TRANSCODE = "ZZJ_FUNCTION";
    private  String TRANSCODE = "ZZJ_FUNCTION";
//  @NotBlank
//  @ApiModelProperty(value = "方法名称",required = true)
    private String TRANSCODE_TWO;

}