package cn.itsource.domain;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@TableName("t_course_chapter")
public class CourseChapter extends Model<CourseChapter> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 章节名称
     */
    private String name;
    /**
     * 第几章
     */
    private Integer number;
    /**
     * 课程ID
     */
    @TableField("course_id")
    private Long courseId;
    /**
     * 课程名
     */
    @TableField("course_name")
    private String courseName;

    //当前这个章节对应的视频，数据来自媒体微服务
    @TableField(exist = false)//数据库中没有这一列，不要去和数据库关联
    private List<MediaFile> mediaFiles = new ArrayList<>();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<MediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CourseChapter{" +
        ", id=" + id +
        ", name=" + name +
        ", number=" + number +
        ", courseId=" + courseId +
        ", courseName=" + courseName +
        "}";
    }
}
