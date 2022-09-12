package cn.itsource.repository;

import cn.itsource.doc.CourseDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository   //申明操作Es的实例
public interface CourseEsRepository extends ElasticsearchRepository<CourseDoc,Long> {
}
