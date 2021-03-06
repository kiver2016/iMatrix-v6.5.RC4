package com.norteksoft.task.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.norteksoft.product.orm.Page;
import com.norteksoft.product.orm.hibernate.HibernateDao;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.task.base.enumeration.TaskProcessingMode;
import com.norteksoft.task.base.enumeration.TaskProcessingResult;
import com.norteksoft.task.base.enumeration.TaskState;
import com.norteksoft.task.entity.HistoryWorkflowTask;

@Repository
public class HistoryWorkflowTaskDao extends HibernateDao<HistoryWorkflowTask, Long>{

	/**
	 * 根据用户获得自己所有已完成的流程名称
	 * @return
	 */
	public List<Object[]> getAllCompleteTaskGroupNames() {
		String hql="select t.groupName,count(t.groupName) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=? and t.paused=? and t.groupName!=null group by t.groupName";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已取消的流程名称
	 * @return
	 */
	public List<Object[]> getAllCancelTaskGroupNames(){
		String hql="select t.groupName,count(t.groupName) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=?) and t.paused=? and t.groupName!=null group by t.groupName";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已完成的流程名称
	 * @return
	 */
	public List<Object[]> getAllCompleteTaskCustomTypes() {
		String hql="select t.customType,count(t.customType) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=? and t.paused=? and t.customType!=null group by t.customType";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已取消的流程名称
	 * @return
	 */
	public List<Object[]> getAllCancelTaskCustomTypes() {
		String hql="select t.customType,count(t.customType) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=?) and t.paused=? and t.customType!=null group by t.customType";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已完成的任务类型名称
	 * @return
	 */
	public List<Object[]> getAllCompleteTaskTypeInfos() {
		String hql="select t.category,count(t.category) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?  and t.paused=? and t.category!=null group by t.category";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已取消的任务类型名称
	 * @return
	 */
	public List<Object[]> getAllCancelTaskTypeInfos() {
		String hql="select t.category,count(t.category) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and (t.active=? or t.active=?) and t.paused=? and t.category!=null group by t.category";
		return find(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false);
	}

	/**
	 * 根据用户获得自己所有已完成的任务类型名称
	 * @return
	 */
	public Integer getAllCompleteTasksNum() {
		String hql="select count(t) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and t.active=?  and t.paused=?";
		Object obj=createQuery(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(),TaskState.COMPLETED.getIndex(),false).uniqueResult();
		if(obj!=null)return Integer.parseInt(obj.toString());
		return 0;
	}

	/**
	 * 根据用户获得自己所有已取消的任务类型名称
	 * @return
	 */
	public Integer getAllCancelTasksNum() {
		String hql="select count(t) from HistoryWorkflowTask t where t.companyId = ? and t.transactor = ? and t.visible = true and ( t.active=? or t.active=?) and t.paused=?";
		Object obj=createQuery(hql, ContextUtils.getCompanyId(), ContextUtils.getLoginName(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),false).uniqueResult();
		if(obj!=null)return Integer.parseInt(obj.toString());
		return 0;
	}

	/**
	 * 根据流程名称分页查询用户所有已完成任务
	 * @param page
	 * @param typeName
	 */
	public void getCompletedTasksByGroupName(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql=" from HistoryWorkflowTask t where  t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and t.active=?   and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.COMPLETED.getIndex(),false);
		}else{
			hql=" from HistoryWorkflowTask t where   t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and t.active=?  and t.paused=? and t.groupName=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.COMPLETED.getIndex(),false,typeName);
		}
	}

	/**
	 * 根据自定义类型分页查询用户所有已完成任务
	 * @param page
	 * @param typeName
	 */
	public void getCompletedTasksByCustomType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and  t.active=?  and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.COMPLETED.getIndex(),false);
		}else{
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and t.active=?  and t.paused=? and t.customType=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.COMPLETED.getIndex(),false,typeName);
		}
	}

	/**
	 * 分页查询用户已完成任务
	 * @param page
	 * @param typeName
	 */
	public void getCompletedTasksByUserType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and t.active=? and t.paused=? and t.transactDate != null  order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(), TaskState.COMPLETED.getIndex(),false);
		}else{
			hql="from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and t.active=? and t.paused=? and t.category=? and t.transactDate != null  order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(), TaskState.COMPLETED.getIndex(),false,typeName);
		}
	}

	/**
	 * 根据流程名称分页查询用户所有已取消任务
	 * @param page
	 * @param typeName
	 */
	public void getCancelTasksByGroupName(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql=" from HistoryWorkflowTask t where  t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and (t.active=? or t.active=? or t.active=?)   and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
		}else{
			hql=" from HistoryWorkflowTask t where   t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? and t.groupName=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,typeName);
		}
	}

	/**
	 * 根据自定义类型分页查询用户所有已取消任务
	 * @param page
	 * @param typeName
	 */
	public void getCancelTasksByCustomType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and  (t.active=? or t.active=? or t.active=?)  and t.paused=? order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
		}else{
			hql="select t from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? and t.customType=?  order by t.createdTime desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(),TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,typeName);
		}
	}

	/**
	 * 分页查询用户已取消任务
	 * @param page
	 * @param typeName
	 */
	public void getCanceledTasksByUserType(Page<HistoryWorkflowTask> page,String typeName) {
		String hql=null;
		if(StringUtils.isEmpty(typeName)){
			hql="from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
		}else{
			hql="from HistoryWorkflowTask t where t.companyId = ? and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and (t.active=? or t.active=? or t.active=?)  and t.paused=? and t.category=? order by t.transactDate desc";
			this.searchPageByHql(page, hql.toString(),ContextUtils.getCompanyId(), ContextUtils.getLoginName(),ContextUtils.getUserId(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,typeName);
		}
	}
	
	public HistoryWorkflowTask getTask(Long taskId){
		return findUniqueNoCompanyCondition("from HistoryWorkflowTask t where t.id=?", taskId);
	}
	public HistoryWorkflowTask getTaskBySourceTaskId(Long taskId){
		return findUniqueNoCompanyCondition("from HistoryWorkflowTask t where t.sourceTaskId=?", taskId);
	}
	
	public List<String> getCountersignByProcessInstanceId(String processInstanceId,TaskProcessingMode processingMode){
		return find( "select distinct t.name from HistoryWorkflowTask t where t.processInstanceId=? and t.processingMode=? and t.paused=? ", processInstanceId,processingMode,false);
	}
	
	public List<HistoryWorkflowTask> getCountersignByProcessInstanceIdResult(String processInstanceId,String taskName,TaskProcessingResult result){
		return find( "from HistoryWorkflowTask t where t.processInstanceId=?  and t.name=? and t.taskProcessingResult=?  and t.paused=? ", processInstanceId,taskName,result,false);
	}
	/**
	 * 获得审批任务组数
	 * @param processInstanceId
	 * @param taskName
	 * @param result
	 * @return
	 */
	public List<Integer> getGroupNumByTaskName(String processInstanceId,String taskName){
		return find( "select t.groupNum from HistoryWorkflowTask t where t.processInstanceId=?  and t.name=?   and t.paused=? and t.companyId = ? group by t.groupNum", processInstanceId,taskName,false,ContextUtils.getCompanyId());
	}

	public void deleteHistoryTaskByProcessId(String processInstanceId, Long companyId) {
		this.createQuery("delete  from HistoryWorkflowTask t where  t.processInstanceId = ? and t.companyId = ? ", processInstanceId,companyId).executeUpdate();
	}
	
	public Page<HistoryWorkflowTask> getTaskAsTrustee(Long companyId, String loginName, Long userId,Page<HistoryWorkflowTask> page,Boolean isEnd){
		Page<HistoryWorkflowTask> result = new Page<HistoryWorkflowTask>(page.getPageSize(), true);
		result.setPageNo(page.getPageNo());
		String hql = "from HistoryWorkflowTask t where t.companyId=? and((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and t.visible = true and t.effective = true and (t.active=? or t.active=? or t.active=?  or t.active=?) and t.paused=? and t.trustor is not null order by t.createdTime desc";
		if(isEnd){
			this.findPage(result,hql,companyId, loginName,userId, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);			
		}else{
			hql = "from HistoryWorkflowTask t where t.companyId=? and  ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?))  and t.visible = true and t.effective = true and (t.active=? or t.active=? or t.active=? or t.active=?) and t.paused=? and t.trustor is not null order by t.createdTime desc";
			this.findPage(result,hql,companyId, loginName,userId, TaskState.WAIT_TRANSACT.getIndex(), TaskState.WAIT_DESIGNATE_TRANSACTOR.getIndex(),TaskState.DRAW_WAIT.getIndex(),TaskState.WAIT_CHOICE_TACHE.getIndex(),false);
		}
		page = new Page<HistoryWorkflowTask>();
		page.setResult(result.getResult());
		page.setPageNo(result.getPageNo());
		page.setPageSize(result.getPageSize());
		page.setTotalCount(result.getTotalCount());
		return page;
	}
	
	public List<HistoryWorkflowTask> getWorkflowTasks(String instanceId, String taskName) {
		return find("from HistoryWorkflowTask t where t.processInstanceId = ? and t.name = ? and t.paused=?", instanceId, taskName,false);
	}


	public Page<HistoryWorkflowTask> getHistoryDelegateTasksByActive(
			Long companyId, String loginName,Long userId,
			Page<HistoryWorkflowTask> historyTasks) {
		String hql = "from HistoryWorkflowTask t where t.companyId=? and ((t.trustor = ? and t.trustorId is null) or (t.trustorId is not null and t.trustorId=?))  and t.effective = true and (t.active=? or t.active=? or t.active=?  or t.active=?) and t.paused=?  order by t.createdTime desc";
		return this.findPage(historyTasks,hql,companyId, loginName,userId, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false);
	}
	//2014-5-22
	public Page<HistoryWorkflowTask> getHistoryAcceptTasksByActive(
			Page<HistoryWorkflowTask> historyTasks) {
		String hql = "from HistoryWorkflowTask t where t.companyId=? and t.effective = ? and (t.active=? or t.active=? or t.active=? or t.active=?) and t.paused=? and t.transactorId=? and t.transferTaskId is not null order by t.createdTime desc";
		return this.findPage(historyTasks,hql,ContextUtils.getCompanyId(), TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,ContextUtils.getUserId());
	}
	
	/**
	 * 查找公司中所有的超期任务,包括已完成的任务
	 * @param companyId
	 * @return
	 */
	public List<HistoryWorkflowTask> getTotalOverdueTasks(Long companyId) {
		 String hql = "from HistoryWorkflowTask t where t.companyId=? and  t.lastReminderTime is not null  and t.paused=? order by t.createdTime desc";
		return find(hql, companyId,false);
	}
	
	/**
	 * 查找当前办理人所有的超期任务的总数,包括已完成的任务
	 * @param companyId
	 * @param transactorName
	 * @return
	 */
	public Integer getTotalOverdueTasksNumByTransactor(Long companyId,String transactorName) {
		return Integer.parseInt(createQuery(
				"select count(t) from HistoryWorkflowTask t where t.companyId=? and t.transactor = ?  and t.lastReminderTime is not null  and t.paused=?", 
				companyId, transactorName,false).uniqueResult().toString());
	}
	/**
	 * 查找当前办理人所有的超期任务的总数,包括已完成的任务
	 * @param companyId
	 * @param transactorId
	 * @return
	 */
	public Integer getTotalOverdueTasksNumByTransactorId(Long companyId,Long transactorId) {
		return Integer.parseInt(createQuery(
				"select count(t) from HistoryWorkflowTask t where t.companyId=? and (t.transactorId is not null and t.transactorId=?) and t.lastReminderTime is not null  and t.paused=?", 
				companyId, transactorId,false).uniqueResult().toString());
	}
	
	/**
	 * 获得所有办理人除当前任务名称的办理人
	 * @param task
	 * @return
	 */
	public List<String> getTransactorsExceptTask(HistoryWorkflowTask task){
		String hql="select distinct t.transactor from HistoryWorkflowTask t where t.name!=? and t.companyId=? and t.processInstanceId=? and t.active=?  and t.paused=?";
		return this.find(hql, task.getName(),task.getCompanyId(),task.getProcessInstanceId(),TaskState.COMPLETED.getIndex(),false);
	}
	
	public List<String> getHandledTransactors(String workflowId) {
		String hql = "select t.transactor from HistoryWorkflowTask t where t.processInstanceId=? and t.active=? and t.effective=?  and t.paused=?";
		return find(hql, workflowId,TaskState.COMPLETED.getIndex(),true,false);
	}
	public List<Long> getHandledTransactorIds(String workflowId) {
		String hql = "select t.transactorId from HistoryWorkflowTask t where t.processInstanceId=? and t.active=? and t.effective=?  and t.paused=?";
		return find(hql, workflowId,TaskState.COMPLETED.getIndex(),true,false);
	}
	//获得流程所有办理人
	public List<String> getAllHandleTransactors(String workflowId) {
		String hql = "select t.transactor from HistoryWorkflowTask t where t.processInstanceId=? and t.effective=? and t.paused=?";
		return find(hql, workflowId,true,false);
	}
	//获得流程所有办理人
	public List<Long> getAllHandleTransactorIds(String workflowId) {
		String hql = "select t.transactorId from HistoryWorkflowTask t where t.processInstanceId=? and t.effective=? and t.paused=?";
		return find(hql, workflowId,true,false);
	}
	public Integer getTrusteeTasksNum(Long companyId, String loginName,Long userId){
		String hql = "select count(t) from HistoryWorkflowTask t where t.companyId=?  and t.effective = true and ((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) and (t.active=? or t.active=?  or t.active=?  or t.active=?) and t.paused=? and t.trustor is not null";
		Object o =  createQuery(hql, companyId, loginName,userId, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false).uniqueResult();
		return Integer.valueOf(o.toString());
	}
	
	public Integer getDelegateTasksNum(Long companyId, String loginName,Long userId){
		String hql = "select count(t) from HistoryWorkflowTask t where t.companyId=? and t.effective = true and ((t.trustor = ? and t.trustorId is null) or (t.trustorId is not null and t.trustorId=?)) and (t.active=? or t.active=?  or t.active=?  or t.active=?) and t.paused=?";
		Object o = createQuery(hql, companyId, loginName,userId, TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false).uniqueResult();
		return Integer.valueOf(o.toString());
	}

	public List<HistoryWorkflowTask> getHistoryTasksByInstanceId(
			String processInstanceId) {
		return this.find("from HistoryWorkflowTask t where t.companyId = ? and t.processInstanceId = ? ", 
				ContextUtils.getCompanyId(), processInstanceId);
	}
	
	/**
	 * 根据办理人查询当前实例中的任务
	 * @param transactor 办理人登录名
	 * @param workflowId 实例id
	 * @return 
	 */
	public List<HistoryWorkflowTask> getTaskByTransactor(String transactor, String workflowId){
		return this.find("from HistoryWorkflowTask t where  t.processInstanceId = ?  and t.visible=? and (((t.transactor = ? and t.transactorId is null) or (t.transactorId is not null and t.transactorId=?)) or ((t.trustor = ? and t.trustorId is null) or (t.trustorId is not null and t.trustorId=?))) and (t.active<>? and  t.active<>? and  t.active<>?)  and t.distributable=? and t.effective=?  and t.paused=? and t.specialTask=? order by t.createdTime DESC", 
				workflowId,false,transactor,transactor,TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,true,false,false);
	}
	/**
	 * 根据办理人查询当前实例中的任务
	 * @param transactor 办理人登录名
	 * @param workflowId 实例id
	 * @return 
	 */
	public List<HistoryWorkflowTask> getTaskByTransactor(Long userId,String workflowId){
		return this.find("from HistoryWorkflowTask t where  t.processInstanceId = ?  and t.visible=? and ((t.transactorId is not null and t.transactorId=?) or (t.trustorId is not null and t.trustorId=?) ) and (t.active<>? and  t.active<>? and  t.active<>?)  and t.distributable=? and t.effective=?  and t.paused=? and t.specialTask=? order by t.createdTime DESC", 
				workflowId,false,userId,userId,TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,true,false,false);
	}
	
	public List<String> getAllTaskTransactors(String workflowId){
		return this.find("select t.transactor from HistoryWorkflowTask t where  t.processInstanceId = ?   and t.distributable=? and t.effective=?  and t.paused=? and t.specialTask=?  order by t.createdTime DESC", 
				workflowId,false,true,false,false);
		
	}
	public List<String> getAllTaskTrustors(String workflowId){
		return this.find("select t.trustor from HistoryWorkflowTask t where  t.processInstanceId = ?   and t.distributable=? and t.effective=?  and t.paused=? and t.specialTask=? and t.trustor is not null order by t.createdTime DESC", 
				workflowId,false,true,false,false);
		
	}
	
	/**
	 * 查询当前环节的办理人
	 * @param workflowId 实例id
	 * @param taskName 任务名称
	 * @return 办理人列表
	 */
	public List<String> getTransactorsByName(String workflowId,String taskName) {
		return this.find("select t.transactor from HistoryWorkflowTask t where  t.processInstanceId = ?  and t.distributable=? and t.effective=?  and t.paused=? and t.name=?", 
				workflowId,false,true,false,taskName);
	}
	/**
	 * 查询当前环节的委托人登录名
	 * @param workflowId 实例id
	 * @param taskName 任务名称
	 * @return 委托人列表
	 */
	public List<String> getTrustorsByName(String workflowId,String taskName) {
		return this.find("select t.trustor from HistoryWorkflowTask t where  t.processInstanceId = ?  and t.distributable=? and t.effective=?  and t.paused=? and t.name=? and t.trustor is not null", 
				workflowId,false,true,false,taskName);
	}
	
	
	public List<Long> getAllTaskTransactorIds(String workflowId){
		return this.find("select t.transactorId from HistoryWorkflowTask t where  t.processInstanceId = ?   and t.distributable=? and t.effective=?  and t.paused=? and t.specialTask=? and t.transactorId is not null  order by t.createdTime DESC", 
				workflowId,false,true,false,false);
		
	}
	public List<Long> getAllTaskTrustorIds(String workflowId){
		return this.find("select t.trustorId from HistoryWorkflowTask t where  t.processInstanceId = ?   and t.distributable=? and t.effective=?  and t.paused=? and t.specialTask=? and t.trustorId is not null order by t.createdTime DESC", 
				workflowId,false,true,false,false);
		
	}
	
	/**
	 * 查询当前环节的办理人
	 * @param workflowId 实例id
	 * @param taskName 任务名称
	 * @return 办理人列表
	 */
	public List<Long> getTransactorIdsByName(String workflowId,String taskName) {
		return this.find("select t.transactorId from HistoryWorkflowTask t where  t.processInstanceId = ?  and t.distributable=? and t.effective=?  and t.paused=? and t.name=? and t.transactorId is not null", 
				workflowId,false,true,false,taskName);
	}
	/**
	 * 查询当前环节的委托人登录名
	 * @param workflowId 实例id
	 * @param taskName 任务名称
	 * @return 委托人列表
	 */
	public List<Long> getTrustorIdsByName(String workflowId,String taskName) {
		return this.find("select t.trustorId from HistoryWorkflowTask t where  t.processInstanceId = ?  and t.distributable=? and t.effective=?  and t.paused=? and t.name=? and t.trustorId is not null", 
				workflowId,false,true,false,taskName);
	}
	/**
	 * 查询所有超期任务
	 * @param workflowId 实例id
	 * @param taskName 任务名称
	 * @return 委托人列表
	 */
	public void getOverdueTasks(Page<HistoryWorkflowTask> page) {
		this.searchPageByHql(page,"from HistoryWorkflowTask t where   t.distributable=? and t.effective=?  and t.paused=? and t.lastTransactTime is not null and (t.transactDate is not null and t.lastTransactTime < t.transactDate and (t.active=? or t.active=?)) and t.companyId=? order by t.lastTransactTime desc", 
				false,true,false,TaskState.COMPLETED.getIndex(),TaskState.CANCELLED.getIndex(),ContextUtils.getCompanyId());
	}
	/**
	 * 查询办理人的所有超期任务
	 */
	public void getOverdueTaskDetails(Page<HistoryWorkflowTask> page,Long transactorId,String transactor,String transactorName,Date lastTransactTimeStart,Date lastTransactTimeEnd) {
		StringBuilder hql = new StringBuilder("from HistoryWorkflowTask t where   t.distributable=? and t.effective=?  and t.paused=? and t.lastTransactTime is not null and (t.transactDate is not null and t.lastTransactTime < t.transactDate and (t.active=? or t.active=?))  and ((t.transactorId is not null and t.transactorId=?) or (t.transactorId is null and t.transactor=?)) and t.companyId=? ");
		int num = getObjectNum(transactorName, lastTransactTimeStart, lastTransactTimeEnd);
		Object[] objs = new Object[8+num];
		objs[0] = false;
		objs[1] = true;
		objs[2] = false;
		objs[3]= TaskState.COMPLETED.getIndex();
		objs[4]= TaskState.CANCELLED.getIndex();
		objs[5] = transactorId;
		objs[6] = transactor;
		objs[7] = ContextUtils.getCompanyId();
		int i=8;
		searchParma(hql,objs,transactorName,lastTransactTimeStart,lastTransactTimeEnd,i,false);
		hql.append(" order by t.lastTransactTime desc");
		this.searchPageByHql(page,hql.toString(), objs);
	}
	private void searchParma(StringBuilder hql,Object[] objs,String transactorName,Date lastTransactTimeStart,Date lastTransactTimeEnd,int i,boolean sqlable){
		if(StringUtils.isNotEmpty(transactorName)){
			if(sqlable)hql.append(" and pt.transactor_name like ?");
			else hql.append(" and t.transactorName like ?");
			objs[i] = "%"+transactorName+"%";
			i++;
		}
		if(lastTransactTimeStart!=null){
			if(sqlable)hql.append(" and pt.last_transact_time >= ?");
			else hql.append(" and t.lastTransactTime >=?");
			objs[i] = lastTransactTimeStart;
			i++;
		}
		if(lastTransactTimeEnd!=null){
			if(sqlable)hql.append(" and pt.last_transact_time <= ?");
			else hql.append(" and t.lastTransactTime <=?");
			objs[i] = lastTransactTimeEnd;
			i++;
		}
	}
	
	private int getObjectNum(String transactorName,Date lastTransactTimeStart,Date lastTransactTimeEnd){
		int num = 0;
		if(StringUtils.isEmpty(transactorName)&&lastTransactTimeStart==null&&lastTransactTimeEnd==null){
			return 0;
		}
		if(StringUtils.isNotEmpty(transactorName)&&lastTransactTimeStart==null&&lastTransactTimeEnd==null){
			return 1;
		}
		if(StringUtils.isEmpty(transactorName)&&lastTransactTimeStart!=null&&lastTransactTimeEnd==null){
			return 1;
		}
		if(StringUtils.isEmpty(transactorName)&&lastTransactTimeStart==null&&lastTransactTimeEnd!=null){
			return 1;
		}
		if(StringUtils.isNotEmpty(transactorName)&&lastTransactTimeStart!=null&&lastTransactTimeEnd==null){
			return 2;
		}
		if(StringUtils.isNotEmpty(transactorName)&&lastTransactTimeStart==null&&lastTransactTimeEnd!=null){
			return 2;
		}
		if(StringUtils.isEmpty(transactorName)&&lastTransactTimeStart!=null&&lastTransactTimeEnd!=null){
			return 2;
		}
		if(StringUtils.isNotEmpty(transactorName)&&lastTransactTimeStart!=null&&lastTransactTimeEnd!=null){
			return 3;
		}
		return num;
	}
	/**
	 * 查询所有超期任务的办理人、委托人、办理人个数
	 */
	public void getOverdueTaskTransactors(Page<Object> page,String transactorName,Date lastTransactTimeStart,Date lastTransactTimeEnd) {
		StringBuilder hql = new StringBuilder("select pt.transactor_name,count(pt.transactor_id) as countNum,pt.transactor_id,pt.transactor from HISTORY_WORKFLOW_TASK pt where   pt.paused=? and pt.last_transact_time is not null and (pt.transact_date is not null and pt.last_transact_time < pt.transact_date and (pt.active=? or pt.active=?)) and pt.company_id=? and pt.distributable=? and pt.effective=?");
		int num = getObjectNum(transactorName, lastTransactTimeStart, lastTransactTimeEnd);
		Object[] objs = new Object[6+num];
		objs[0] = false;
		objs[1]= TaskState.COMPLETED.getIndex();
		objs[2]= TaskState.CANCELLED.getIndex();
		objs[3] = ContextUtils.getCompanyId();
		objs[4] = false;
		objs[5] = true;
		int i=6;
		searchParma(hql,objs,transactorName,lastTransactTimeStart,lastTransactTimeEnd,i,true);
		hql.append(" group by pt.transactor_name,pt.transactor_id,pt.transactor order by pt.transactor");
		this.searchPageBySql(page,hql.toString(), objs);
	}
	
	/**
	 * 根据taskId获得移交任务列表
	 * @param taskId
	 * @param page
	 */
	public void getTransferTasksByTaskId(Long taskId,Page<HistoryWorkflowTask> page){
		this.searchPageByHql(page,"from HistoryWorkflowTask t where  t.transferTaskId=? and  t.distributable=? and t.effective=?  and t.paused=?  and t.companyId=? order by t.lastTransactTime desc", 
				taskId,false,true,false,ContextUtils.getCompanyId());
	}
	
	/**
	 * 获得办理人所有移交的任务个数
	 * @param transactorId
	 * @return
	 */
	public Integer getTransferTaskNumByTransactorId(Long transactorId,String transactorName,Date lastTransactTimeStart,Date lastTransactTimeEnd){
		StringBuilder hql = new StringBuilder("select count(t.id) from HistoryWorkflowTask t where  t.transferabled=? and t.transactorId=? and t.paused=? and t.lastTransactTime is not null and ((t.transactDate is not null and t.lastTransactTime < t.transactDate and (t.active=? or t.active=?)) )  and t.companyId=?  and t.distributable=? and t.effective=?");
		int num = getObjectNum(transactorName, lastTransactTimeStart, lastTransactTimeEnd);
		Object[] objs = new Object[8+num];
		objs[0] = true;
		objs[1]= transactorId;
		objs[2] = false;
		objs[3]= TaskState.COMPLETED.getIndex();
		objs[4]= TaskState.CANCELLED.getIndex();
		objs[5] = ContextUtils.getCompanyId();
		objs[6] = false;
		objs[7] = true;
		int i=8;
		searchParma(hql,objs,transactorName,lastTransactTimeStart,lastTransactTimeEnd,i,false);
		
		Object transferTaskNum  = createQuery(hql.toString(), objs).uniqueResult();
		if(transferTaskNum!=null)return Integer.valueOf(transferTaskNum.toString());
		return 0;
	}
	/**
	 * 获得办理人所有移交的任务
	 * @param transactorId
	 * @return
	 */
	public void getTransferTaskDetails(Page<HistoryWorkflowTask> page,
			Long transactorId,String transactorName,Date lastTransactTimeStart,Date lastTransactTimeEnd){
		StringBuilder hql = new StringBuilder("from HistoryWorkflowTask t where  t.transferabled=? and t.transactorId=? and t.paused=? and t.lastTransactTime is not null and (t.transactDate is not null and t.lastTransactTime < t.transactDate and (t.active=? or t.active=?))  and t.companyId=?  and t.distributable=? and t.effective=?");
		int num = getObjectNum(transactorName, lastTransactTimeStart, lastTransactTimeEnd);
		Object[] objs = new Object[8+num];
		objs[0] = true;
		objs[1]= transactorId;
		objs[2] = false;
		objs[3]= TaskState.COMPLETED.getIndex();
		objs[4]= TaskState.CANCELLED.getIndex();
		objs[5] = ContextUtils.getCompanyId();
		objs[6] = false;
		objs[7] = true;
		int i=8;
		searchParma(hql,objs,transactorName,lastTransactTimeStart,lastTransactTimeEnd,i,false);
		
		this.searchPageByHql(page,hql.toString(), objs);
	}
	
	/**
	 * 获得办理人移交任务的数量
	 * @param transactorId
	 * @return
	 */
	public Integer getTransferTasksNum(Long transactorId){
		String hql = "select count(t) from HistoryWorkflowTask t where t.companyId=? and t.effective = true and (t.active=? or t.active=?  or t.active=?  or t.active=?) and t.paused=? and t.transferId=? ";
		Object o = createQuery(hql, ContextUtils.getCompanyId(), TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,transactorId).uniqueResult();
		return Integer.valueOf(o.toString());
	}

	/**
	 * 移交任务-已完成历史任务
	 * @param historyTasks
	 * @param userId
	 */
	public void getHistoryTransferTasksByActive(Page<HistoryWorkflowTask> historyTasks, Long userId) {
		String hql = "from HistoryWorkflowTask t where t.companyId=? and t.effective = true and (t.active=? or t.active=? or t.active=?  or t.active=?) and t.paused=? and t.transferId=? order by t.createdTime desc";
		this.searchPageByHql(historyTasks,hql,ContextUtils.getCompanyId(),TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,userId);
	}
	
	//2014-5-22
	public Page<HistoryWorkflowTask> getTaskAsAccept(Page<HistoryWorkflowTask> page,Boolean isEnd){
		String hql = "from HistoryWorkflowTask t where t.companyId=? and t.effective = ? and (t.active=? or t.active=? or t.active=? or t.active=?) and t.paused=? and t.transactorId=?  and t.transferTaskId is not null order by t.createdTime desc";
		this.searchPageByHql(page,hql,ContextUtils.getCompanyId(), true,TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,ContextUtils.getUserId());
		return page;
	}
	
	//2014-5-22
	public Integer getAcceptTasksNum(){
		Object num = 0;
		String hql = "select count(t) from HistoryWorkflowTask t where t.companyId=? and t.effective = ? and (t.active=? or t.active=? or t.active=? or t.active=?) and t.paused=? and t.transactorId=?  and t.transferTaskId is not null";
		num=createQuery(hql,ContextUtils.getCompanyId(), true,TaskState.COMPLETED.getIndex(), TaskState.CANCELLED.getIndex(),TaskState.ASSIGNED.getIndex(),TaskState.HAS_DRAW_OTHER.getIndex(),false,ContextUtils.getUserId()).uniqueResult();
		if(num!=null)return Integer.parseInt(num.toString());
		return 0;
	}
}
