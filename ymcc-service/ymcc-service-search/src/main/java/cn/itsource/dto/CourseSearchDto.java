package cn.itsource.dto;

import cn.itsource.query.BaseQuery;
import lombok.Data;

@Data
public class CourseSearchDto extends BaseQuery {

        private Integer courseTypeId;
        private String gradeName;
        private String chargeName;
        private Double priceMin;
        private Double priceMax;
        private String sortField;
        private String sortType;
}
