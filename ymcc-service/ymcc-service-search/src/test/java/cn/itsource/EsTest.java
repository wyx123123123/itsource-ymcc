package cn.itsource;

import cn.itsource.doc.CourseDoc;
import cn.itsource.repository.CourseEsRepository;
import io.swagger.annotations.Authorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApp.class)
public class EsTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private CourseEsRepository courseEsRepository;


    @Test
    public void testEs(){
        elasticsearchRestTemplate.createIndex(CourseDoc.class);//创建索引库
        elasticsearchRestTemplate.putMapping(CourseDoc.class);

    }

    @Test
    public void testAdd(){
        courseEsRepository.save(new CourseDoc(1L,"我是时光鸡"));


        System.out.println(courseEsRepository.findById(1L));
    }






}
