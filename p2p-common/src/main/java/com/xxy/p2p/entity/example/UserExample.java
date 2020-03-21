package com.xxy.p2p.entity.example;

import lombok.Data;

import java.util.List;

@Data
public class UserExample {

    private Integer id;

    private String accountNumber;

    private List<Integer> idList;
}
