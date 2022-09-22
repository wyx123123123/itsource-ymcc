package cn.itsource.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author director
 * @since 2022-09-21
 */
@TableName("t_kill_course")
public class KillCourse extends Model<KillCourse> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 课程名字
     */
    @TableField("course_name")
    private String courseName;
    /**
     * 对应的课程ID
     */
    @TableField("course_id")
    private Long courseId;
    @TableField("kill_price")
    private BigDecimal killPrice;
    /**
     * 库存
     */
    @TableField("kill_count")
    private Integer killCount;
    /**
     * 每个人可以秒杀的数量,默认1
     */
    @TableField("kill_limit")
    private Integer killLimit;
    /**
     * 秒杀课程排序
     */
    @TableField("kill_sort")
    private Integer killSort;
    /**
     * 秒杀状态:0待发布，1秒杀中，2秒杀结束
     */
    @TableField("publish_status")
    private Integer publishStatus;
    @TableField("course_pic")
    private String coursePic;
    /**
     * 秒杀开始时间
     */
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    /**
     * 秒杀结束时间
     */
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 发布到Redis的时间
     */
    @TableField("publish_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;
    /**
     * 老师，用逗号隔开
     */
    @TableField("teacher_names")
    private String teacherNames;
    /**
     * 下线时间
     */
    @TableField("offline_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date offlineTime;
    @TableField("activity_id")
    private Long activityId;
    @TableField("time_str")
    private String timeStr;


    /**
     * 返回时间差
     *  如果是未开始：startTime - now
     *  如果是秒杀中：endTime - now
     *
     * @return
     */
    public Long getTimeDiffMill(){
        Date now = new Date();
        if(isKilling()){
            //秒杀中
            return getEndTime().getTime() - now.getTime();
        }
        if(now.before(getStartTime())){
            //秒杀未开始
            return getStartTime().getTime() - now.getTime();
        }
        return null;
    }


    public Boolean isUnbegin(){
        Date now = new Date();
        if(now.after(getStartTime())){
            return true;
        }
        return false;
    }

    /**
     * 秒杀中，返回true
     * @return
     */
    public Boolean isKilling(){
        Date now = new Date();
        if(now.after(getStartTime()) && now.before(getEndTime())){
            return true;
        }
        return false;
    }

    /*
      返回秒杀状态名字
     */
    public String getKillStatusName() {
        Date now = new Date();
        if(now.before(getStartTime())){
            return "未开始";
        }else if(now.after(getEndTime())){
            return "已结束";
        }else{
            return "秒杀中";
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public BigDecimal getKillPrice() {
        return killPrice;
    }

    public void setKillPrice(BigDecimal killPrice) {
        this.killPrice = killPrice;
    }

    public Integer getKillCount() {
        return killCount;
    }

    public void setKillCount(Integer killCount) {
        this.killCount = killCount;
    }

    public Integer getKillLimit() {
        return killLimit;
    }

    public void setKillLimit(Integer killLimit) {
        this.killLimit = killLimit;
    }

    public Integer getKillSort() {
        return killSort;
    }

    public void setKillSort(Integer killSort) {
        this.killSort = killSort;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getCoursePic() {
        return coursePic;
    }

    public void setCoursePic(String coursePic) {
        this.coursePic = coursePic;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getTeacherNames() {
        return teacherNames;
    }

    public void setTeacherNames(String teacherNames) {
        this.teacherNames = teacherNames;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "KillCourse{" +
        ", id=" + id +
        ", courseName=" + courseName +
        ", courseId=" + courseId +
        ", killPrice=" + killPrice +
        ", killCount=" + killCount +
        ", killLimit=" + killLimit +
        ", killSort=" + killSort +
        ", publishStatus=" + publishStatus +
        ", coursePic=" + coursePic +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", createTime=" + createTime +
        ", publishTime=" + publishTime +
        ", teacherNames=" + teacherNames +
        ", offlineTime=" + offlineTime +
        ", activityId=" + activityId +
        ", timeStr=" + timeStr +
        "}";
    }
}
