package gdao.genericdao;
import java.lang.annotation.*;
@Retention(value=RetentionPolicy.RUNTIME)  
@Target(value=ElementType.TYPE)  
public @interface TableName {
    public String value() default "";
}
