package com.swshenyun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swshenyun.common.BaseResponse;
import com.swshenyun.pojo.dto.TeamCreateDTO;
import com.swshenyun.pojo.dto.TeamDTO;
import com.swshenyun.pojo.dto.TeamQueryDTO;
import com.swshenyun.pojo.entity.Team;
import com.swshenyun.pojo.vo.TeamQueryVO;
import com.swshenyun.service.TeamService;
import com.swshenyun.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping("/add")
    public BaseResponse<Long> create(@RequestBody TeamCreateDTO teamCreateDTO) {
        log.info("创建新队伍：{}", teamCreateDTO.getName());
        Long teamId = teamService.addTeam(teamCreateDTO);
        return ResultUtils.success(teamId);
    }

    @DeleteMapping("/{teamId}")
    public BaseResponse<Boolean> delete(@PathVariable Long teamId) {
        log.info("删除队伍：{}",teamId);
        boolean result = teamService.deleteTeam(teamId);
        return ResultUtils.success(result);
    }

    @PutMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody TeamDTO teamDTO) {
        log.info("更新队伍信息：{}",teamDTO.toString());
        boolean result = teamService.updateTeam(teamDTO);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(Long id) {
        log.info("根据id获取队伍信息：{}",id);
        Team team = teamService.getById(id);
        team = teamService.getSafeTeam(team);
        return ResultUtils.success(team);
    }

    @GetMapping("/list")
    public BaseResponse<List<TeamQueryVO>> list(TeamQueryDTO teamQueryDTO) {
        log.info("查询队伍信息：{}",teamQueryDTO.toString());
        List<TeamQueryVO> teamList = teamService.listTeams(teamQueryDTO);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/page")
    public BaseResponse<Page<TeamQueryVO>> page(TeamQueryDTO teamQueryDTO) {
        log.info("分页查询队伍信息：{}",teamQueryDTO.toString());
        Page<TeamQueryVO> teamList = teamService.pageTeams(teamQueryDTO);
        return ResultUtils.success(teamList);
    }
}
