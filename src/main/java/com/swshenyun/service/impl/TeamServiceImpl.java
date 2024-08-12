package com.swshenyun.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swshenyun.common.ErrorCode;
import com.swshenyun.common.TeamStatus;
import com.swshenyun.constant.PageConstant;
import com.swshenyun.context.BaseContext;
import com.swshenyun.exception.BaseException;
import com.swshenyun.mapper.TeamMapper;
import com.swshenyun.pojo.dto.TeamCreateDTO;
import com.swshenyun.pojo.dto.TeamDTO;
import com.swshenyun.pojo.dto.TeamQueryDTO;
import com.swshenyun.pojo.entity.Team;
import com.swshenyun.pojo.entity.User;
import com.swshenyun.pojo.entity.UserTeam;
import com.swshenyun.pojo.vo.TeamQueryVO;
import com.swshenyun.pojo.vo.UserQueryVO;
import com.swshenyun.service.TeamService;
import com.swshenyun.service.UserService;
import com.swshenyun.service.UserTeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
* @author 神殒魔灭
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-08-04 22:48:38
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {

    @Autowired
    private TeamMapper teamMapper;
    
    @Autowired
    private UserTeamService userTeamService;

    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public Long addTeam(TeamCreateDTO teamCreateDTO) {
        //1.插入team表
        Team team = new Team();
        BeanUtils.copyProperties(teamCreateDTO, team);
        // 校验
        // 队伍最大人数：大于1，小于等于20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BaseException(ErrorCode.PARAMS_ERROR);
        }
        // 超时时间 > 当前时间
        LocalDateTime expireTime = team.getExpireTime();
        if (LocalDateTimeUtil.now().isAfter(expireTime)) {
            throw new BaseException(ErrorCode.PARAMS_ERROR);
        }
        // 状态校验 0 - 公开，1 - 私有，2 - 加密
        Integer status = team.getStatus();
        String password = team.getPassword();
        if (status.equals(TeamStatus.SECRET.getValue()) && password.isBlank()) {
            throw new BaseException(ErrorCode.PARAMS_ERROR);
        }
        // 限制一个用户创建太多队伍，小于5个
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Team::getUserId, currentId);
        Long teamNum = teamMapper.selectCount(wrapper);
        if (teamNum >= 5) {
            throw new BaseException(ErrorCode.ALREADY_LIMITED);
        }

        boolean result = this.save(team);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR);
        }

        //2.插入team_user表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(currentId);
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR);
        }

        return team.getId();
    }

    public boolean deleteTeam(Long teamId) {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Team::getId, teamId)
                .eq(Team::getUserId, currentId);
        boolean result = this.remove(wrapper);
        if (!result) {
            throw new BaseException(ErrorCode.OPERATION_ERROR);
        }
        return result;
    }

    public boolean updateTeam(TeamDTO teamDTO) {
        // 鉴定身份
        Long currentId = BaseContext.getCurrentId();
        Team team = this.getById(teamDTO.getId());
        if (!currentId.equals(team.getUserId())) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR);
        }
        // 如果更改为加密，必须要密码
        Integer status = team.getStatus();
        int secretValue = TeamStatus.SECRET.getValue();
        if (!status.equals(secretValue) && teamDTO.getStatus().equals(secretValue) && teamDTO.getPassword().isBlank()) {
            throw new BaseException(ErrorCode.PARAMS_ERROR);
        }
        LambdaUpdateWrapper<Team> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Team::getId, teamDTO.getId());
        // 如果由加密改为公开，需要删除密码
        if (status.equals(secretValue) && !teamDTO.getStatus().equals(secretValue)) {
            wrapper.set(Team::getPassword, null);
        }

        team = new Team();
        BeanUtils.copyProperties(teamDTO, team);

        return this.update(team, wrapper);
    }

    public Team getSafeTeam(Team team) {
        Team safeTeam = new Team();
        BeanUtils.copyProperties(team, safeTeam);
        safeTeam.setPassword(null);
        return safeTeam;
    }

    public Page<TeamQueryVO> pageTeams(TeamQueryDTO teamQueryDTO) {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper<>();
        // 管理员鉴权
        Boolean isAdmin = userService.isAdmin(currentId);
        //id列表查询
        List<Long> ids = teamQueryDTO.getIds();
        // TODO 不允许1000个查询 or 分批查询
        if (!CollectionUtil.isEmpty(ids)) {
            wrapper.in(Team::getId, ids);
        }
        // 搜索关键词查询
        String searchText = teamQueryDTO.getSearchText();
        if (!StringUtils.isEmpty(searchText)) {
            wrapper.like(Team::getName, searchText);
            wrapper.like(Team::getDescription,searchText);
        }
        // name队名查询
        String name = teamQueryDTO.getName();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like(Team::getName, name);
        }
        // description描述查询
        String description = teamQueryDTO.getDescription();
        if (!StringUtils.isEmpty(description)) {
            wrapper.like(Team::getDescription, description);
        }
        // maxNum最大人数查询
        Integer maxNum = teamQueryDTO.getMaxNum();
        if (maxNum != null && maxNum > 0) {
            wrapper.le(Team::getMaxNum, maxNum);
        }
        // 最小过期时间
        LocalDateTime expireTime = teamQueryDTO.getExpireTime();
        // 不展示过期队伍，null代表永不过期
        if (expireTime != null) {
            wrapper.and(w -> w.gt(Team::getExpireTime, Objects.requireNonNullElseGet(expireTime, Date::new))
                    .or().isNull(Team::getExpireTime));
        }
        // 查询创建用户的队伍
        Long userId = teamQueryDTO.getUserId();
        if (userId != null) {
            wrapper.eq(Team::getUserId, userId);
        }
        // 根据状态查询
        Integer status = teamQueryDTO.getStatus();
        TeamStatus statusEnum;
        if (status == null) {
            statusEnum = TeamStatus.PUBLIC;
        } else {
            statusEnum = TeamStatus.getEnumByValue(status);
        }
        if (!Boolean.TRUE.equals(isAdmin) && statusEnum.equals(TeamStatus.PRIVATE)) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR);
        }
        wrapper.eq(Team::getStatus, status);

        // 查询，封装VO对象，Team和User数据
        if (teamQueryDTO.getPage() == null) {
            throw new BaseException(ErrorCode.PARAMS_ERROR);
        }
        // 将Page<Team>转换成Page<TeamQueryVO>
        Page<Team> page = new Page<>(teamQueryDTO.getPage(), teamQueryDTO.getPageSize());
        Page<Team> teamPage = this.page(page, wrapper);
        List<Team> teamList = teamPage.getRecords();
        Page<TeamQueryVO> teamQueryPage = new Page<>();
        BeanUtils.copyProperties(teamPage, teamQueryPage);

        if (CollectionUtils.isEmpty(teamList)) {
            teamQueryPage.setRecords(new ArrayList<>());
            return teamQueryPage;
        }

        List<TeamQueryVO> teamQueryVOList = new ArrayList<>();
        for (Team team : teamList) {
            Long createUserId = team.getUserId();
            if (createUserId == null) {
                continue;
            }
            TeamQueryVO teamQueryVO = new TeamQueryVO();
            BeanUtils.copyProperties(team, teamQueryVO);

            User user = userService.getById(createUserId);
            if (user != null) {
                UserQueryVO userQueryVO = new UserQueryVO();
                BeanUtils.copyProperties(user,userQueryVO);
                teamQueryVO.setCreateUser(userQueryVO);
            }
            LambdaQueryWrapper<UserTeam> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserTeam::getTeamId, team.getId());
            long count = userTeamService.count(queryWrapper);
            teamQueryVO.setHasJoinNum((int) count);

            queryWrapper.eq(UserTeam::getUserId, team.getUserId());
            boolean exists = userTeamService.exists(queryWrapper);
            teamQueryVO.setHasJoin(exists);

            teamQueryVOList.add(teamQueryVO);
        }
        teamQueryPage.setRecords(teamQueryVOList);
        return teamQueryPage;
    }

    public List<TeamQueryVO> listTeams(TeamQueryDTO teamQueryDTO) {
        // 分页最大数，最大数为int最大值
        teamQueryDTO.setPage(PageConstant.FIRST_PAGE);
        teamQueryDTO.setPageSize(PageConstant.MAX_PAGE_SIZE);
        return this.pageTeams(teamQueryDTO).getRecords();
    }


}




