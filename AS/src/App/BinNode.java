/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package App;

/**
 *
 * @author rafael
 */
public class BinNode {
    public BinNode(String _Element, BinNode _RightChild, BinNode _LeftChild){
        this.Element = _Element;
        this.RC = _RightChild;
        this.LC = _LeftChild;
    }
    
    private final String Element;
    private final BinNode RC, LC;
    
    public String getElement(){
        return this.Element;
    }
    
    public BinNode getLC(){
        return this.LC;
    }
    
    public BinNode getRC(){
        return this.RC;
    }
}
