package cn.itsource.service.impl;

import cn.itsource.constants.RedisCacheConstant;
import cn.itsource.domain.CourseType;
import cn.itsource.mapper.CourseTypeMapper;
import cn.itsource.service.ICourseTypeService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程目录 服务实现类
 * </p>
 *
 * @author director
 * @since 2022-09-02
 */
@Service
@Slf4j
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private CourseTypeMapper courseTypeMapper;

    // 本地缓存如果使用map会有线程安全问题，但是这种方式效率是最高的，因为他是基于内存的
    // private static HashMap<String, Object> map = new HashMap<>();

    ///*
    // * @Description: 查询课程分类树数据--瓦代码
    // * @Author: Director
    // * @Date: 2022/9/2 10:32
    // * @return: java.util.List<cn.itsource.domain.CourseType>
    // **/
    //@Override
    //public List<CourseType> treeData() {
    //    // 1.查询所有分类数据
    //    List<CourseType> courseTypes = selectList(null);
    //    // 2.创建一个空的集合数据
    //    List<CourseType> treeData = new ArrayList<>();
    //    // 3.遍历集合数据
    //    for (CourseType courseType : courseTypes) {
    //        // 1.判断父ID是不是空的，如果是空的就放入到外面的空集合里面
    //        if (courseType.getPid() == null || courseType.getPid().intValue() == 0){
    //            treeData.add(courseType);
    //        }else {// 2.如果不是空的，再次遍历数据，找爹，放入到爹的children中
    //            for (CourseType parent : courseTypes) {
    //                // 获取到爹的id，跟儿子的pid作比较，如果相等，说明他就是他的爹
    //                if (parent.getId().intValue() == courseType.getPid().intValue()){
    //                    parent.getChildren().add(courseType);
    //                    break;
    //                }
    //            }
    //        }
    //    }
    //
    //    return treeData;
    //}


    /*
     * @Description: 查询课程分类树数据--瓦代码
     * @Author: Director
     * @Date: 2022/9/2 10:32
     * @return: java.util.List<cn.itsource.domain.CourseType>
     **/
    @Override // redis的key值：courseType::tree ,并且该方法的返回值就是value
    @Cacheable(cacheNames = RedisCacheConstant.COURSE_TYPE_TREE, key = "'tree'")
    public List<CourseType> treeData() {
        // 1.从redis中先查询看有没有数据
        //Object courseTypeTree = redisTemplate.opsForValue().get(RedisCacheConstant.COURSE_TYPE_TREE);
        //if (courseTypeTree != null){ // 如果有那么直接返回
        //    log.info("走的缓存查询数据！！！");
        //    // 因为我们对redis单独配置了序列化，所以我们可以直接强转，并且不会报错
        //    return (List<CourseType>) courseTypeTree;
        //}else { // 如果没有查询数据库
        //    log.info("走的数据库查询数据！！！");
        //    return tree();
        //}

        return tree();
    }

    @Override
    public void updateTotalCountById(Long courseTypeId) {
        courseTypeMapper.updateTotalCountById(courseTypeId);
    }

    /*
     * @Description: 查询课程分类树数据
     * @Author: Director
     * @Date: 2022/9/2 14:32
     * @return: java.util.List<cn.itsource.domain.CourseType>
     **/
    private List<CourseType> tree() {
        // 1.查询所有分类数据
        Wrapper<CourseType> wrapper = new EntityWrapper<>();
        List<CourseType> courseTypes = selectList(null);
        // 2.将list转换为map集合 collect：stream流的终结方法，我们可以做数据类型转换
        // Collectors.toMap(key, value)：将数据转换为map，key就是map的key值，value就是map的value值  item就是list集合的每个对象
        Map<Long, CourseType> coursesMap = courseTypes.stream().collect(Collectors.toMap(CourseType::getId, item -> item));
        // 3.创建一个空的集合数据
        List<CourseType> treeData = new ArrayList<>();
        // 4.遍历集合数据
        courseTypes.forEach(e -> {
            // 4.1.自己就是顶级
            if (e.getPid() == null || e.getPid().intValue() == 0) {
                treeData.add(e);
            } else {
                // 4.2.如果自己不是顶级，那么就根据pid找到父亲，将自己放入到父亲中
                coursesMap.get(e.getPid()).getChildren().add(e);
            }
        });
        // 查询完成之后存入redis
        //redisTemplate.opsForValue().set(RedisCacheConstant.COURSE_TYPE_TREE, treeData);
        return treeData;
    }


    @Override
    //@Transactional // 当我们一个方法有多次数据更改操作时，这时候我们需要加事务
    @CacheEvict(cacheNames = RedisCacheConstant.COURSE_TYPE_TREE, key = "'tree'")
    public boolean insert(CourseType entity) {
        //boolean insert = super.insert(entity);
        //// 当数据操作成功时，我们更新缓存
        //if (insert){
        //    tree();
        //}
        //return insert;

        return super.insert(entity);
    }


    @Override
    @CacheEvict(cacheNames = RedisCacheConstant.COURSE_TYPE_TREE, key = "'tree'")
    public boolean updateById(CourseType entity) {
        //boolean updateById = super.updateById(entity);
        //// 当数据操作成功时，我们更新缓存
        //if (updateById){
        //    tree();
        //}
        //return updateById;
        return super.updateById(entity);
    }


    @Override
    @CacheEvict(cacheNames = RedisCacheConstant.COURSE_TYPE_TREE, key = "'tree'")
    public boolean deleteById(Serializable id) {
        //boolean deleteById = super.deleteById(id);
        //// 当数据操作成功时，我们更新缓存
        //if (deleteById){
        //    tree();
        //}
        return super.deleteById(id);
    }
}
