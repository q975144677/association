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
}
