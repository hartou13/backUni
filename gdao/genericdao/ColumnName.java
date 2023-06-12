package gdao.genericdao;
import java.lang.annotation.*;
@Retention(value=RetentionPolicy.RUNTIME)  
@Target(value=ElementType.FIELD)  
public @interface ColumnName {
    public String value() default "";
    public boolean pk() default false;
    public boolean fk() default false;
}
