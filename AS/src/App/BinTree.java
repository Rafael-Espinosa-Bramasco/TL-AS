/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author rafael
 */
public class BinTree {
    public BinTree(){
        this.head = null;
    }
    
    private BinNode head;
    
    public BinNode getRoot(){
        return head;
    }
    
    public void setRoot(BinNode NewRoot){
        this.head = NewRoot;
    }
}