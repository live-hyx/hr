package org.hr.service;

import org.hr.mapper.PerformanceMapper;
import org.hr.model.Performance;
import org.hr.model.PerformanceFlow;
import org.hr.model.User;
import org.hr.modelOv.PerformanceOV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PerformanceService {
    @Autowired
    PerformanceMapper performanceMapper;

    /**
     * 新增签到
     */
    public void signIn(PerformanceFlow performanceFlow) {
        performanceMapper.signIn(performanceFlow);
    }

    /**
     * 签退，更新个人当前签到记录
     */
    public void signUp(PerformanceFlow performanceFlow) {
        performanceMapper.signUp(performanceFlow);
    }

    /**
     * 管理员查看所有人签到记录
     */
    public List<PerformanceFlow> getUsersAllSigns(PerformanceOV perform){
        return performanceMapper.getUsersAllSigns(perform);
    }
    /**
     * 普通用户查看个人所有签到记录
     */
    public List<PerformanceFlow> getUserAllSigns(String username) {
        return performanceMapper.getUserAllSigns(username);
    }

    /**
     * 辅助函数，查看个人当天签到记录
     */
    public PerformanceFlow getTodaySign(String username) {
        return performanceMapper.getTodaySign(username);
    }

    /**
     * 增加一条个人当月当日工作时长记录
     */
    public void addSign(Performance performance) {
        performanceMapper.addSign(performance);
    }

    /**
     * 更新 正常签到但未签退的 记录
     */
    public void updatePerformanceFlowStateAndType() {
        performanceMapper.updateStateAndType();
    }

    /**
     * 更新 今日的签到记录为 非今日
     */
    public void updateState() {
        performanceMapper.updateState();
    }

    public Integer getUserTotalTime(Performance performance) {
        return performanceMapper.getUserTotalTime(performance);
    }
}
