package cn.itsource.service.impl;

import cn.itsource.domain.*;
import cn.itsource.dto.CourseDto;
import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.mapper.CourseMapper;
import cn.itsource.service.*;
import cn.itsource.util.AssertUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Autowired
    private ITeacherService teacherService;

    @Autowired
    private ICourseResourceService courseResourceService;

    @Autowired
    private ICourseDetailService courseDetailService;

    @Autowired
    private ICourseMarketService courseMarketService;

    @Autowired
    private ICourseSummaryService courseSummaryService;

    @Autowired
    private ICourseTeacherService courseTeacherService;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ICourseTypeService courseTypeService;



    /*
     * @Description: 保存课程
     * @Author: Director
     * @Date: 2022/9/4 16:26
     * @param courseDto: 课程新增dto对象
     * @return: void
     **/
    @Override
    @Transactional
    public void save(CourseDto courseDto) {
        Course course = courseDto.getCourse();
        CourseResource courseResource = courseDto.getCourseResource();
        CourseDetail courseDetail = courseDto.getCourseDetail();
        CourseMarket courseMarket = courseDto.getCourseMarket();
        List<Long> teacherIds = courseDto.getTeacherIds();
        // 1.参数校验，使用JSR303进行校验
        // 2.判断课程是否存在，如果存在不允许添加，抛出异常
        isCourseExisted(course);
        // 3.保存课程 方法传递对象是引用传递，不是值传递，那么引用传递意味着，你这个对象共享地址，那么下面给这个对象设定了id，那么上面也有
        saveCourse(course, teacherIds);
        // 4.保存资源表
        saveCourseResourse(course, courseResource);
        // 5.保存详情
        saveCourseDetail(course, courseDetail);
        // 6.保存课程营销
        saveCourseMarket(course, courseMarket);
        // 7.初始化课程汇总
        initCourseSummary(course);
        // 8.保存课程老师中间表
        saveCourseTeacher(course, teacherIds);

        // 下面这种写法要执行两次SQL语句
        //CourseType courseType = courseTypeService.selectById(course.getCourseTypeId());
        //courseType.setTotalCount(courseType.getTotalCount() + 1);
        //courseTypeService.updateById(courseType);
        // 9.给课程对应类型数量+1
        courseTypeService.updateTotalCountById(course.getCourseTypeId());
    }

    private void saveCourseTeacher(Course course, List<Long> teacherIds) {
        //teacherIds.forEach(id->{
        //    CourseTeacher courseTeacher = new CourseTeacher();
        //    courseTeacher.setCourseId(course.getId());
        //    courseTeacher.setTeacherId(id);
        //    courseTeacherService.insert(courseTeacher);
        //});
        courseMapper.insertCourseTeacher(course.getId(), teacherIds);
    }

    private void initCourseSummary(Course course) {
        CourseSummary courseSummary = new CourseSummary();
        courseSummary.setId(course.getId());
        courseSummaryService.insert(courseSummary);
    }

    private void saveCourseMarket(Course course, CourseMarket courseMarket) {
        courseMarket.setId(course.getId());
        courseMarketService.insert(courseMarket);
    }

    private void saveCourseDetail(Course course, CourseDetail courseDetail) {
        courseDetail.setId(course.getId());
        courseDetailService.insert(courseDetail);
    }

    private void saveCourseResourse(Course course, CourseResource courseResource) {
        courseResource.setCourseId(course.getId());
        courseResourceService.insert(courseResource);
    }

    private void saveCourse(Course course, List<Long> teacherIds) {
        course.setStatus(Course.STATUS_OFFLINE);
        // @TODO 以后做完登录之后过来获取当前登员工
        course.setLoginId(6L);
        course.setLoginUserName("itsource.cn");
        course.setChapterCount(Course.INIT_CHAPTER_COUNT);

        List<Teacher> teachers = teacherService.selectBatchIds(teacherIds);
        //StringBuilder sbr = new StringBuilder();
        //teachers.forEach(e->{
        //    sbr.append(e.getName()).append(",");
        //});
        // map：映射操作，拿到每个你指定的字段值 collect：最终操作，Collectors：数据转换 joining：连接符号
        String teacherNames = teachers.stream().map(Teacher::getName).collect(Collectors.joining(","));
        course.setTeacherNames(teacherNames);
        // 保存
        insert(course);
    }

    /*
     * @Description: 判断课程是否存在
     * @Author: Director
     * @Date: 2022/9/4 16:56
     * @param course: 课程对象
     * @return: void
     **/
    private void isCourseExisted(Course course) {
        Wrapper<Course> wrapper = new EntityWrapper<>();
        wrapper.eq("name", course.getName());
        Course selectCourse = selectOne(wrapper);
        AssertUtil.isNull(selectCourse, GlobalEnumCode.COURSE_EXISTED_ERROR);
    }
}
