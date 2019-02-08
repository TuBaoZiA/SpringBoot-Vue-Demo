package com.ycj.demo.web.admin.vo;


import com.ycj.demo.domain.TestInnoDB;
import lombok.Data;

@Data
public class TestVo extends TestInnoDB {

    private String birthdayStart;

    private String birthdayEnd;

}
