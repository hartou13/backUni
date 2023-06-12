package universalController.classmanaging;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

import gdao.inherit.DBModel;
import universalController.classmanaging.fd.FieldDescriptor;

public class Champ {
    String fieldType, inputType;
    boolean readable=true, writable=true;
    String fieldName;
    String fieldLabel;
    ArrayList<DBModel> possibleValue;
    String mainField;
    public static String determineFieldType(Field field) {
        if(field.getType()==Integer.class)
            return "integer";
        else if(field.getType()==Double.class || field.getType()==Float.class)
            return "float";
        else if(field.getType()==Date.class ){
            FieldDescriptor fd= Util.getFieldDescriptor(field);
            if(fd==null)
                return "date";
            else{
                if(fd.longDate() )
                    return "datetime";
                return "date";
            }
        }
        return "text";
    }
    public String determineInputType(Field field) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
        FieldDescriptor fd= Util.getFieldDescriptor(field);
        if(fd!=null ){
            if( fd.fk()){
                Class cl=Class.forName(fd.fkClass());
                this.possibleValue= (ArrayList<DBModel>) cl.getMethod("listAll").invoke(cl.newInstance());
                this.setMainField(fd.mainField());
                return "select";
            }
            else if(fd.img()){
                return "image";
            }
            else if(fd.ck()){
                return "ck";
            }
            else
                return Champ.determineFieldType(field); 
        }
        else
            return Champ.determineFieldType(field); 
        // return "text";
    }
    public static Champ translate(Field field) throws Exception{
        Champ ch=new Champ();
        ch.setFieldName(field.getName());
        ch.setFieldType(Champ.determineFieldType(field));
        ch.setInputType(ch.determineInputType(field));
        FieldDescriptor fd= Util.getFieldDescriptor(field);
        ch.setFieldLabel(ch.getFieldName());
        if(fd!=null){
            ch.setReadable(fd.readable());
            ch.setWritable(fd.writable());
            if(!fd.label().isEmpty())
                ch.setFieldLabel(fd.label());
        }


        return ch;
        
    }
    
    
    
    @Override
    public String toString() {
        return "Champ [fieldType=" + fieldType + ", inputType=" + inputType + ", readable=" + readable + ", writable="
                + writable + ", fieldName=" + fieldName + ", fieldLabel=" + fieldLabel + ", possibleValue="
                + possibleValue + ", mainField=" + mainField + "]";
    }
    public ArrayList<DBModel> getPossibleValue() {
        return possibleValue;
    }
    public void setPossibleValue(ArrayList<DBModel> possibleValue) {
        this.possibleValue = possibleValue;
    }
    public String getMainField() {
        return mainField;
    }
    public void setMainField(String mainField) {
        this.mainField = mainField;
    }
    public String getFieldType() {
        return fieldType;
    }
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
    public String getInputType() {
        return inputType;
    }
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    public boolean isReadable() {
        return readable;
    }
    public void setReadable(boolean readable) {
        this.readable = readable;
    }
    public boolean isWritable() {
        return writable;
    }
    public void setWritable(boolean writable) {
        this.writable = writable;
    }
    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getFieldLabel() {
        return fieldLabel;
    }
    public void setFieldLabel(String fieldLabel) {
        this.fieldLabel = fieldLabel;
    }
    
}
