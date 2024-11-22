package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dao.WorkScheduleRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkScheduleRepository workScheduleRepository;


}
