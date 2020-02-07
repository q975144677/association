package component;

import lombok.Data;

import java.util.Date;

@Data
public class ConditionForPagin {
Integer pageIndex ;
Integer pageSize ;
Integer offset ;
Long recordCount ;
Integer pageCount ;
Date from;
Date to ;
Integer limit ;
public Integer getLimit(){
    return pageSize;
}
public Integer getOffset(){
    return pageIndex != null ? (pageIndex - 1) * pageSize : 0 ;
}
public PaginProto.Page prepare(Long recordCount){
    if(pageIndex == null || pageIndex < 1){
        pageIndex = 1 ;
    }
    if(pageSize == null || pageSize < 1){
        pageSize = 10 ;
    }
     offset = getOffset();
     limit = getLimit() ;
     if(recordCount != null) {
         this.recordCount = recordCount;
         pageCount = (int) Math.ceil((double) recordCount / (double) pageSize);
         return new PaginProto.Page(pageIndex, pageSize, pageCount, Math.toIntExact(recordCount));
     }
     return null ;
}
}
