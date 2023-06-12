package universalController.classmanaging.fd;
import java.lang.annotation.*;

@Retention(value=RetentionPolicy.RUNTIME)  
@Target(value=ElementType.FIELD) 
public @interface FieldDescriptor {
    public boolean readable()default true;
    public boolean writable()default true;
    public String label() default "";
    public boolean fk() default false;
    public boolean longDate() default false;
    public String fkClass() default "";
    public String mainField() default "";
    public boolean ck() default false;
    public boolean img() default false;
    
}   
