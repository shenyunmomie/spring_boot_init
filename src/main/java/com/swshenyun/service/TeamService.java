package com.swshenyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swshenyun.pojo.dto.TeamCreateDTO;
import com.swshenyun.pojo.dto.TeamDTO;
import com.swshenyun.pojo.dto.TeamQueryDTO;
import com.swshenyun.pojo.entity.Team;
import com.swshenyun.pojo.vo.TeamVO;
import com.swshenyun.pojo.vo.UserVO;

import java.util.List;


/**
* @author 神殒魔灭
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-08-04 22:48:38
*/
public interface TeamService extends IService<Team> {

    Long addTeam(TeamCreateDTO teamCreateDTO);

    boolean deleteTeam(Long teamId);

    boolean updateTeam(TeamDTO teamDTO);

    Team getSafeTeam(Team team);

    Page<TeamVO> pageTeams(TeamQueryDTO teamQueryDTO);

    List<TeamVO> listTeams(TeamQueryDTO teamQueryDTO);

    Boolean joinTeam(Long teamId, String password);

    Boolean exitTeam(Long teamId);

    Boolean changeLeader(Long currentId, Long newUserId, Long teamId);

    Boolean kickOut(Long userId, Long teamId);

}
