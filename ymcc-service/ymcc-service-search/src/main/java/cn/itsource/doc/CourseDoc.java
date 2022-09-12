package cn.itsource.doc;

import com.alibaba.druid.filter.AutoLoad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * indexName  指明索引库
 * type    没有表
 *
 *
 */
@Document(indexName = "course" , type = "_doc")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDoc {

    @Id   // 申明文档ID  使用传入的Id值作为文档的ID
    private Long  id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String name;

}
