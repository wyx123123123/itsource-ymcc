package cn.itsource.doc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * indexName  指明索引库
 * type    没有表
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

    /**
     * 适用人群
     */
    @Field(type = FieldType.Keyword)  //不分词
    private String forUser;
    /**
     * 课程分类
     */
    @Field(type = FieldType.Long)
    private Long courseTypeId;

    @Field(type = FieldType.Keyword)
    private String gradeName;
    /**
     * 课程等级
     */
    @Field(type = FieldType.Long)
    private Long gradeId;
    /**
     * 课程状态，下线：0 ， 上线：1
     */
    @Field(type = FieldType.Integer)
    private Integer status;

    /**
     * 课程的开课时间
     */
    @Field(type = FieldType.Date)
    private Date startTime;
    /**
     * 课程的节课时间
     */
    @Field(type = FieldType.Date)
    private Date endTime;
    /**
     * 封面，云存储地址
     */
    @Field(type = FieldType.Keyword)
    private String pic;
    /**
     * 时长，以分钟为单位
     */
    @Field(type = FieldType.Integer)
    private Integer totalMinute;

    @Field(type = FieldType.Date)
    private Date onlineTime;
    /**
     * 章节数
     */
    @Field(type = FieldType.Integer)
    private Integer chapterCount;
    /**
     * 讲师，逗号分隔多个
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String teacherNames;


    /**
     * 收费规则：，收费1免费，2收费
     */
    @Field(type = FieldType.Keyword)
    private String chargeName;

    /**
     * 价格
     */
    @Field(type = FieldType.Double)
    private BigDecimal price;
    /**
     * 原价
     */
    @Field(type = FieldType.Double)
    private BigDecimal priceOld;


    /**
     * 销量
     */
    @Field(type = FieldType.Integer)
    private Integer saleCount;
    /**
     * 浏览量
     */
    @Field(type = FieldType.Integer)
    private Integer viewCount;
    /**
     * 评论数
     */
    @Field(type = FieldType.Integer)
    private Integer commentCount;







}
