package com.findPartner.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.findPartner.domain.entity.Team;
import com.findPartner.mapper.TeamMapper;
import com.findPartner.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * @author 47607
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2023-02-22 11:35:54
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

}




