package com.lee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author libin
 * @date 2020-10-09 22:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysLog implements Serializable {


    private Long id;

    private String username;

    private String operation;

    private Long time;

    private String method;

    private String params;

    private String ip;

    private Date createTime;


    private String location;

}
