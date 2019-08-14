package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
//表示文件上传的VO对象
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIFile {
	private Integer error=0;  
	//如果文件上传没错则返回0,有错返回1
	private String  url;
	private Integer width;
	private Integer height;
}
