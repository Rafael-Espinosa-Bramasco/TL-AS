/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author rafael
 */
public class DAG_Obj {
    public DAG_Obj(String t, String d1, String d2){
        this.type = t;
        this.data1 = d1;
        this.data2 = d2;
        this.var = "";
    }
    
    private String type, data1, data2, var;
    
    public String getVar(){
        return this.var;
    }
    
    public void setVar(String x){
        this.var = x;
    }
    
    public String getType(){
        return this.type;
    }
    
    public String getData1(){
        return this.data1;
    }
    
    public String getData2(){
        return this.data2;
    }
    
    public void setData1(String x){
        this.data1 = x;
    }
    
    public void setData2(String x){
        this.data2 = x;
    }
    
    public boolean compareTo(DAG_Obj otherObj){
        return this.type.equals(otherObj.getType()) && this.data1.equals(otherObj.getData1()) && this.data2.equals(otherObj.getData2()) && this.var.equals(otherObj.getVar());
    }
}