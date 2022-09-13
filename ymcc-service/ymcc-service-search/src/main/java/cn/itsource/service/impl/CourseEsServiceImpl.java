package cn.itsource.service.impl;

import cn.itsource.doc.CourseDoc;
import cn.itsource.dto.CourseSearchDto;
import cn.itsource.repository.CourseEsRepository;
import cn.itsource.result.PageList;
import cn.itsource.service.ICourseEsService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CourseEsServiceImpl implements ICourseEsService {
    @Autowired
    private CourseEsRepository courseEsRepository;

    @Override
    public void saveCourseDoc(CourseDoc courseDoc) {
        courseEsRepository.save(courseDoc);
    }

    /**
     * 根据条件从Es中搜索课程
     * @param dto
     * @return
     */
    @Override
    public PageList<CourseDoc> searchCourse(CourseSearchDto dto) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //=================包装查询参数===================
        //------1.分页参数----------
        builder.withPageable(PageRequest.of(dto.getPage()-1,dto.getRows()));//注意：分页页码从0开始

        //------2.排序参数----------
        if(StringUtils.hasLength(dto.getSortField())){
            String sortField = null;
            switch (dto.getSortField().toLowerCase()){
                case "xl": sortField="saleCount";break;
                case "xp": sortField="onlineTime";break;
                case "pl": sortField="commentCount";break;
                case "jg": sortField="price";break;
                case "rq": sortField="viewCount";break;
            }
            if(StringUtils.hasLength(sortField)){
                SortOrder order = SortOrder.DESC;
                if(StringUtils.hasLength(dto.getSortType()) && dto.getSortType().equalsIgnoreCase(SortOrder.ASC.toString())){
                    order =  SortOrder.ASC;
                }
                builder.withSort(SortBuilders.fieldSort(sortField).order(order));
            }
        }

        //------3.查询条件----------
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //***** must 模糊搜索
        if(StringUtils.hasLength(dto.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("name",dto.getKeyword()));//搜索课程名称
        }

        //***** filet 等值搜索
        if(dto.getCourseTypeId() != null){
            boolQuery.filter(QueryBuilders.termQuery("courseTypeId",dto.getCourseTypeId()));
        }
        if(StringUtils.hasLength(dto.getGradeName())){
            boolQuery.filter(QueryBuilders.termQuery("gradeName",dto.getGradeName()));
        }
        if(StringUtils.hasLength(dto.getChargeName())){
            boolQuery.filter(QueryBuilders.termQuery("chargeName",dto.getChargeName()));
        }
        if(dto.getPriceMin() != null){
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(dto.getPriceMin()));
        }
        if(dto.getPriceMax() != null){
            boolQuery.filter(QueryBuilders.rangeQuery("price").lte(dto.getPriceMax()));
        }

        //设置查询条件给builder
        builder.withQuery(boolQuery);

        //=================包装查询参数===================
        SearchQuery searchQuery = builder.build();//通过构建器帮我们创建查询对象
        //根据查询条件执行查询
        Page<CourseDoc> search = courseEsRepository.search(searchQuery);
        //将查询结果包装成PageList返回
        return new PageList<>(search.getTotalElements(), search.getContent());
    }
}
