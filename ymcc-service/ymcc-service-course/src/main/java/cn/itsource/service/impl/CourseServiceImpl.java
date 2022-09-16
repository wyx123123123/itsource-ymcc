package cn.itsource.service.impl;

import cn.itsource.doc.CourseDoc;
import cn.itsource.domain.*;
import cn.itsource.dto.CourseDto;
import cn.itsource.enums.GlobalEnumCode;
import cn.itsource.CourseEsFeignClient;
import cn.itsource.feign.MeidaFileFeignClient;
import cn.itsource.mapper.CourseMapper;
import cn.itsource.result.JsonResult;
import cn.itsource.service.*;
import cn.itsource.util.AssertUtil;
import cn.itsource.vo.CourseInfoVo;
import cn.itsource.vo.OrderInfoVo;
import cn.itsource.vo.OrderItemInfoVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private CourseEsFeignClient courseEsFeignClient;

    @Autowired
    private ICourseChapterService courseChapterService;

    @Autowired
    private MeidaFileFeignClient meidaFileFeignClient;

    @Autowired
    private ICourseUserLearnService courseUserLearnService;



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


    /**
     *
     * 1.参数校验
     * 2.业务校验
     *   课程必须存在
     *   课程状态必须是下架状态
     * 3.修改课程状态（上架）+ 填写上架时间
     * 4.将课程通过Feign 调用service-search保存到Es中
     *   。为service-search服务编写Controller接口，保存数据到Es中
     *   。编写search-api，讲search服务的controller接口暴露成Feign接口
     *   。service-course服务中使用search-api 去调用search服务，完成数据保存到Es
     * @param courseId
     */
    @Override
    @Transactional
    public void onLineCourse(Long courseId) {
        // 1.参数校验
        AssertUtil.isNotNull(courseId,"小子想搞事？？ 哥屋恩！！");
        // 2.业务校验
        Course course = selectById(courseId);
        //   课程必须存在
        AssertUtil.isNotNull(course,"课程不存在！！");
        //   课程状态必须是下架状态
        boolean isOffline = course.getStatus() == Course.STATUS_OFFLINE;
        AssertUtil.isTrue(isOffline,"课程状态非法！！");
        // 3.修改课程状态（上架）+ 填写上架时间
        course.setStatus(Course.STATUS_ONLINE);
        course.setOnlineTime(new Date());
        updateById(course);
        // 4.将课程通过Feign 调用service-search保存到Es中
        //   。为service-search服务编写Controller接口，保存数据到Es中
        //   。编写search-api，讲search服务的controller接口暴露成Feign接口
        //   。service-course服务中使用search-api 去调用search服务，完成数据保存到Es
        CourseDoc courseDoc = new CourseDoc();
        CourseMarket courseMarket = courseMarketService.selectById(courseId);
        CourseSummary courseSummary = courseSummaryService.selectById(courseId);
        BeanUtils.copyProperties(course,courseDoc);
        BeanUtils.copyProperties(courseMarket,courseDoc);
        BeanUtils.copyProperties(courseSummary,courseDoc);
        //处理额外的参数
        courseDoc.setChargeName(courseMarket.getCharge().intValue() == 1 ? "收费":"免费");
        JsonResult jsonResult = courseEsFeignClient.saveCourseDoc(courseDoc);
        AssertUtil.isTrue(jsonResult.isSuccess(),"发布课程失败！！");
    }

    /**
     * 查询课程详情
     * 1.参数校验
     * 2.业务校验
     * 3.查询需要的数据
     *  Course course;// 课程基本信息
     *  CourseDetail courseDetail;//课程详情
     *  CourseSummary courseSummary;//课程统计
     *  CourseMarket courseMarket;//课程销售相关
     *  List<CourseChapter> courseChapters;//课程章节 ----> 一个章节下面还有多个视屏MediaFile
     *  List<Teacher> teachers;// 课程老师
     * 4.封装成 CourseInfoVo 返回给前端
     * @param courseId  课程ID
     * @return
     */
    @Override
    public CourseInfoVo courseInfo(Long courseId) {
        AssertUtil.isNotNull(courseId,"课程Id不能为空！！");
        Course course = selectById(courseId);
        AssertUtil.isNotNull(course,"课程不存在！");
        AssertUtil.isTrue(course.getStatus() == Course.STATUS_ONLINE,"课程状态不是上架状态！！！");

        CourseDetail courseDetail = courseDetailService.selectById(courseId);
        CourseSummary courseSummary = courseSummaryService.selectById(courseId);
        CourseMarket courseMarket = courseMarketService.selectById(courseId);
        //查询老师=======
        Wrapper<CourseTeacher> ctWrapper = new EntityWrapper<>();
        ctWrapper.eq("course_id",courseId);
        List<CourseTeacher> courseTeachers = courseTeacherService.selectList(ctWrapper);
        // List<CourseTeacher>  --> List<Long> ids
        List<Long> ids = courseTeachers.stream().map(CourseTeacher::getTeacherId).collect(Collectors.toList());
        Wrapper<CourseTeacher> teacherWrapper = new EntityWrapper<>();
        List<Teacher> teachers = teacherService.selectBatchIds(ids);//根据多个老师Id，查询出所有老师

        //查询课程章节======
        List<CourseChapter> courseChapters = courseChapterService.listByCourseId(courseId);
        Map<Long, CourseChapter> courseChapterMap = courseChapters.stream()
                .collect(Collectors.toMap(CourseChapter::getId, courseChapter -> courseChapter));

        //1.为media微服务编写controller接口查询课程下视频
        //2.编写api-media,暴露Frign接口
        //3.依赖api-media,使用Feign远程调用查询课程下的视频信息
        JsonResult jsonResult = meidaFileFeignClient.queryMediasByCourserId(courseId);
        AssertUtil.isTrue(jsonResult.isSuccess(),"查询媒体信息失败！！");
        if(jsonResult.getData() != null){//如果数据不为可空我们才处理
            List<MediaFile> mediaFiles = JSON.parseArray(jsonResult.getData().toString(), MediaFile.class);
            for (MediaFile mediaFile : mediaFiles) {
                mediaFile.setFileUrl("");
                CourseChapter courseChapter = courseChapterMap.get(mediaFile.getChapterId());
                if(courseChapter != null){
                    courseChapter.getMediaFiles().add(mediaFile);
                }
            }
        }

        return new CourseInfoVo(
                course,
                courseDetail,
                courseSummary,
                courseMarket,
                courseChapters,
                teachers
        );
    }

    /**
     *
     * 1.参数校验
     * 2.通过Fiegn查询媒体信息
     *   。为media服务编写查询mediaFile的controller接口
     *   。暴露api-midea
     *   。course服务使用api-media，调用media服务查询媒体
     * 3.如果是免费的，就直接返回播放地址
     * 4.根据mediaFile中的courseId查询当前登录人是否购买了课程
     *   。购买了并且在客观看有效期，返回地址
     *   。否则提示：请购买课程后观看
     * @param mediaId
     * @return
     */
    @Override
    public String getForUser(Long mediaId) {
        // 1.参数校验
        AssertUtil.isNotNull(mediaId,"媒体Id不能为空！");
        // 2.通过Fiegn查询媒体信息
        //   。为media服务编写查询mediaFile的controller接口  现成的，代码生成器生成好了
        //   。暴露api-midea
        //   。course服务使用api-media，调用media服务查询媒体
        JsonResult jsonResult = meidaFileFeignClient.get(mediaId);
        AssertUtil.isTrue(jsonResult.isSuccess(),"媒体查询异常！！");
        AssertUtil.isNotNull(jsonResult.getData(),"媒体信息不存在！！");
        MediaFile mediaFile = JSON.parseObject(jsonResult.getData().toString(), MediaFile.class);
        if(mediaFile.getFree()){
            return mediaFile.getFileUrl();//免费的直接返回地址
        }
        // 3.根据mediaFile中的courseId查询当前登录人是否购买了课程 t_course_user_learn
        Long loginId = 3L;
        Wrapper<CourseUserLearn> wrapper = new EntityWrapper<>();
        wrapper.eq("login_id",loginId);
        wrapper.eq("course_id",mediaFile.getCourseId());
        CourseUserLearn courseUserLearn = courseUserLearnService.selectOne(wrapper);
        //   。否则提示：请购买课程后观看
        AssertUtil.isNotNull(courseUserLearn,"请购买后再观看！！");
        //   。购买了并且在客观看有效期，返回地址
        boolean before = new Date().before(courseUserLearn.getEndTime());
        AssertUtil.isTrue(before,"课程的可观看时间已经结束，尊贵的会员,请您为课程续费后方可观看！！");
        return mediaFile.getFileUrl();
    }

    /**
     *
     * 1.参数校验
     * 2.分隔courseIds
     * 3.批量查询Course
     * 4.业务判断
     *   。状态是否合法
     *   。course是否为空
     *   。课程的数量要和传递的课程Id数量一致
     *
     * 5.查找课程对应的销售信息
     * 6.讲课程信息+课程销售信息 封装成小Vo  OrderItemInfo
     * 7.当所有的小VO封装结束，还要封装大VO OrderInfoVo
     * @param courseIds  "3,9"
     * @return
     */
    @Override
    public OrderInfoVo orderInfo(String courseIds) {
        // 1.参数校验
        AssertUtil.isNotEmpty(courseIds,"课程Id不能为空");
        // 2.分隔courseIds
        String[] courseIdArray = courseIds.split(",");
        // 3.批量查询Course
        List<Course> courses = selectBatchIds(Arrays.asList(courseIdArray));
        // 4.业务判断
        //   。课程的数量要和传递的课程Id数量一致
        AssertUtil.isTrue(courseIdArray.length == courses.size(),"课程数量不匹配！！");
        //   。状态是否合法
        // 当所有的小VO封装结束，还要封装大VO OrderInfoVo
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        for (Course course : courses) {
            AssertUtil.isTrue(course.getStatus().intValue() == Course.STATUS_ONLINE,"课程状态非法！！！");
            // 5.查找课程对应的销售信息
            CourseMarket courseMarket = courseMarketService.selectById(course.getId());
            // 6.讲课程信息+课程销售信息 封装成小Vo  OrderItemInfo
            OrderItemInfoVo orderItemInfo = new OrderItemInfoVo(course, courseMarket);
            orderInfoVo.getCourseInfos().add(orderItemInfo);
            orderInfoVo.setTotalAmount(orderInfoVo.getTotalAmount().add(courseMarket.getPrice()));
        }
        return orderInfoVo;
    }


}
