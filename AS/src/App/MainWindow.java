/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package App;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;

/**
 *
 * @author rafael
 */
public class MainWindow extends javax.swing.JFrame {
    
    /**
     * Creates new form MainWindow
     */
    
    /*
        <E> :: <T><RE>
        <RE> :: + <T><RE>
        <RE> :: - <T><RE>
        <RE> :: * <T><RE>
        <RE> :: / <T><RE>
        <RE> :: λ
        <T> :: <N> | <ID> | (E)
        <N> :: {isNum}
        <ID> :: {isID}
    */
     
    public MainWindow() {
        initComponents();
        this.lastInput = "";
        this.Error = false;
        
        this.mySymbolsTable = null;
        this.myDAGTable = null;
    }
    String lastInput;
    boolean Error;
    
    SymbolsTable mySymbolsTable;
    DAG_TABLE myDAGTable;
    
    /////////////// get token list functions ///////////////
    private ArrayList getTokens(ArrayList<Character> X){
        ArrayList<String> tokens = new ArrayList<>();
        String token = "";
        char act;
        
        while(!X.isEmpty()){
           act = X.get(0);
           
           if(isLetter(act)){
               token = token + act;
               X.remove(0);
               
               while(!X.isEmpty() && (isNum(X.get(0)) || isLetter(X.get(0)))){
                   token = token + X.get(0);
                   X.remove(0);
               }
               
               tokens.add(token);
               token = "";
           }
           else if(isNum(act)){
               token = token + act;
               X.remove(0);
               
               while(!X.isEmpty() && isNum(X.get(0))){
                   token = token + X.get(0);
                   X.remove(0);
               }
               
               tokens.add(token);
               token = "";
           }
           else if(isOpenPar(act)){
               tokens.add("(");
               X.remove(0);
           }
           else if(isClosePar(act)){
               tokens.add(")");
               X.remove(0);
           }
           else if(isOP(act)){
               tokens.add(String.valueOf(act));
               X.remove(0);
           }
           else if(isSpace(act)){
               X.remove(0);
           }
           else{
               JOptionPane.showMessageDialog(this, "Error 0: \nNot acceptable character '" + act + "'.");
               this.Error = true;
               return tokens;
           }
        }
        
        return tokens;
    }
    
    private boolean isSpace(char c){
        return c == ' ';
    }
    private boolean isLetter(char c){
        int x = (int) c;
        return ((x >= 97 && x <= 122) || (x >= 65 && x <= 90));
    }
    private boolean isNum(char c){
        int x = (int) c;
        return (x >= 48 && x <= 57);
    }
    private boolean isOpenPar(char c){
        return c == '(';
    }
    private boolean isClosePar(char c){
        return c == ')';
    }
    private boolean isOpenPar(String s){
        return "(".equals(s);
    }
    private boolean isClosePar(String s){
        return ")".equals(s);
    }
    private boolean isOP(char c){
        switch(c){
            case '+','-','*','/' -> {return true;}
            default -> {return false;}
        }
    }
    private boolean isOP(String s){
        switch(s){
            case "+","-","*","/" -> {return true;}
            default -> {return false;}
        }
    }
    
    private ArrayList getSymbolTable(ArrayList<String> tokens){
        ArrayList<String> symbolTable = new ArrayList<>();
        
        for(int i = 0 ; i < tokens.size() ; i++){
            if(symbolTable.indexOf(tokens.get(i)) == -1 && !notAccept(tokens.get(i)) && !isNumber(tokens.get(i))){
                symbolTable.add(tokens.get(i));
            }
        }
        
        return symbolTable;
    }
    
    private boolean notAccept(String s){
        switch(s){
            case "+","-","*","/","(",")" -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean sintacticA(ArrayList<String> tokens){
        return this.E(tokens);
    }
    
    private boolean E(ArrayList<String> tokens){
        boolean T = false;
        boolean RE = false;
        
        if(!tokens.isEmpty() && (isNumber(tokens.get(0)) || isIdentifier(tokens.get(0)))){
            T = this.T(tokens.get(0));
            tokens.remove(0);
            RE = this.RE(tokens);
        }else if(isOpenPar(tokens.get(0))){
            ArrayList<String> termArray = new ArrayList<>();
                
            int numPar = 1;
            tokens.remove(0);
            termArray.add("(");
            while(numPar > 0 && !tokens.isEmpty()){
                if(isOpenPar(tokens.get(0))){
                    numPar++;
                }else if(isClosePar(tokens.get(0))){
                    numPar--;
                }
                
                termArray.add(tokens.get(0));
                tokens.remove(0);
            }

            if(numPar >= 1){
                return false;
            }else{
                T = this.T(termArray);
                RE = this.RE(tokens);
                return T && RE;
            }
        }
        
        return RE && T;
    }
    
    private boolean RE(ArrayList<String> tokens){
        boolean T = false;
        boolean RE = false;
        
        if(tokens.isEmpty()){
            return true;
        }
        else if(tokens.size() >= 2 && isOP(tokens.get(0))){
            tokens.remove(0);
            
            if(!tokens.isEmpty() && (isNumber(tokens.get(0)) || isIdentifier(tokens.get(0)))){
                T = this.T(tokens.get(0));
                tokens.remove(0);
                RE = this.RE(tokens);
            }else if(isOpenPar(tokens.get(0))){
                ArrayList<String> termArray = new ArrayList<>();
                
                int numPar = 1;
                tokens.remove(0);
                termArray.add("(");
                while(numPar > 0 && !tokens.isEmpty()){
                    if(isOpenPar(tokens.get(0))){
                        numPar++;
                    }else if(isClosePar(tokens.get(0))){
                        numPar--;
                    }

                    termArray.add(tokens.get(0));
                    tokens.remove(0);
                }
                
                if(numPar >= 1){
                    return false;
                }else{
                    T = this.T(termArray);
                    RE = this.RE(tokens);
                    return T && RE;
                }
            }
            
            return T && RE;
        }else{
            return false;
        }
    }
    
    private boolean T(ArrayList<String> tokens){
        if(tokens.size() >= 3){
            tokens.remove(0);
            tokens.remove(tokens.size() - 1);
            
            return this.E(tokens);
        }
        
        return false;
    }
    private boolean T(String token){
        if(isNumber(token)){
            return this.N(token);
        }else if(isIdentifier(token)){
            return this.ID(token);
        }
        return false;
    }
    
    private boolean N(String number){
        return isNumber(number);
    }
    
    private boolean ID(String identifier){
        return isIdentifier(identifier);
    }
    
    private boolean isNumber(String num){
        for(int i = 0 ; i < num.length() ; i++){
            if(!isNum(num.charAt(i))){
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isIdentifier(String id){
        return isLetter(id.charAt(0));
    }
    
    private void createSymbolsTable(ArrayList<String> symbols){
        if(this.mySymbolsTable == null){
            this.mySymbolsTable = new SymbolsTable();
            
            
            for(int i = 0 ; i < symbols.size() ; i++){
                this.mySymbolsTable.addSymbol(symbols.get(i), (isIdentifier(symbols.get(i)) ? "Identifier" : "Number"));
            }
            
            this.mySymbolsTable.setVisible(true);
        }
    }
    
    private void createDAGTable(ArrayList<String> tokens){
        String ACT;
        
        ArrayList<DAG_Obj> DAG_Table = new ArrayList<>();
        Stack<DAG_Obj> preAnalisys = new Stack<>();
        DAG_Obj aux;
        
        for(int i = 0 ; i < tokens.size() ; i++){
            ACT = tokens.get(i);
            if(isIdentifier(ACT) || isNumber(ACT)){
                aux = new DAG_Obj((isIdentifier(ACT) ? "ID" : "NUM"),ACT,"");
                if(!DAG_OBJ_EXISTS(aux,DAG_Table)){
                    DAG_Table.add(aux);
                }
                preAnalisys.push(aux);
            }else if(isClosePar(ACT)){
                makeProcess(preAnalisys, DAG_Table);
            }else if(isOP(ACT)){
                aux = new DAG_Obj(ACT,"","");
                preAnalisys.push(aux);
            }
        }
        
        // Finish Process
        DAG_Obj L,C,R;
        while(preAnalisys.size() > 1){
            R = preAnalisys.pop();
            C = preAnalisys.pop();
            L = preAnalisys.pop();

            int indexR, indexL;
            indexL = DAG_OBJ_INDEX(L, DAG_Table);
            indexR = DAG_OBJ_INDEX(R, DAG_Table);

            C.setData1(String.valueOf(indexL));
            C.setData2(String.valueOf(indexR));
            
            if(!DAG_OBJ_EXISTS(C, DAG_Table)){
                DAG_Table.add(C);
            }
            preAnalisys.add(C);
        }
        
        if(this.myDAGTable == null){
            this.myDAGTable = new DAG_TABLE();
            
            
            for(int i = 0 ; i < DAG_Table.size() ; i++){
                aux = DAG_Table.get(i);
                if("NUM".equals(aux.getType()) || "ID".equals(aux.getType())){
                    this.myDAGTable.addSymbol(String.valueOf(i), aux.getType(), aux.getData1(), "N/A", "N/A");
                }else{
                    this.myDAGTable.addSymbol(String.valueOf(i), aux.getType(), aux.getType(), aux.getData1(), aux.getData2());
                }
            }
            
            this.myDAGTable.setVisible(true);
        }
        
        create3WaysCode(DAG_Table);
        
        
    }
    
    public void create3WaysCode(ArrayList<DAG_Obj> DAG_Table){
        GeneratedCode ThreeWaysCode = new GeneratedCode();
        for(int i = 0 ; i< DAG_Table.size();i++){
            String aux = "t"+String.valueOf(i+1)+"= ";
            if(!(DAG_Table.get(i).getType() == "NUM" || DAG_Table.get(i).getType() == "ID")){
                
                aux += DAG_Table.get(Integer.parseInt(DAG_Table.get(i).getData1())).getData1()+DAG_Table.get(i).getType() + DAG_Table.get(Integer.parseInt(DAG_Table.get(i).getData2())).getData1();
            }
            else{
                aux += DAG_Table.get(i).getData1();
            }
            ThreeWaysCode.addLine(aux);
        }
        ThreeWaysCode.setTitle("[Normal] 3 Addresses Code");
        ThreeWaysCode.setVisible(true);
    }
    
    private void makeProcess(Stack<DAG_Obj> preAnalisys, ArrayList<DAG_Obj> DAG_Table){
        DAG_Obj L,C,R;
        while(preAnalisys.size() > 1){
            R = preAnalisys.pop();
            C = preAnalisys.pop();
            L = preAnalisys.pop();

            int indexR, indexL;
            indexL = DAG_OBJ_INDEX(L, DAG_Table);
            indexR = DAG_OBJ_INDEX(R, DAG_Table);

            C.setData1(String.valueOf(indexL));
            C.setData2(String.valueOf(indexR));

            //comparar si C existe en la tabla, si si entonces simplemente agregrlo al pre analisis, si no entonces agregarlo a ambos
            if(!DAG_OBJ_EXISTS(C, DAG_Table)){
                DAG_Table.add(C);
                preAnalisys.add(C);
            }else{
                preAnalisys.add(C);
                break;
            }
        }
    }
    
    private int DAG_OBJ_INDEX(DAG_Obj E, ArrayList<DAG_Obj> DAG_Table){
        for(int i = 0 ; i < DAG_Table.size() ; i++){
            if(E.compareTo(DAG_Table.get(i))){
                return i;
            }
        }
        
        return -1;
    }
    
    private boolean DAG_OBJ_EXISTS(DAG_Obj E, ArrayList<DAG_Obj> DAG_Table){
        for(int i = 0 ; i < DAG_Table.size() ; i++){
            if(E.compareTo(DAG_Table.get(i))){
                return true;
            }
        }
        return false;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputLabel = new javax.swing.JLabel();
        inputField = new javax.swing.JTextField();
        AnalyzeBTN = new javax.swing.JButton();
        last_button = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador Front-End");
        setResizable(false);

        inputLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        inputLabel.setText("Cadena de entrada:");

        AnalyzeBTN.setText("Analizar");
        AnalyzeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnalyzeBTNActionPerformed(evt);
            }
        });

        last_button.setText("Anterior");
        last_button.setEnabled(false);
        last_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                last_buttonActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Analizador Front-End");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(inputLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(AnalyzeBTN, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(last_button))
                            .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addGap(69, 69, 69))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputLabel)
                    .addComponent(inputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AnalyzeBTN)
                    .addComponent(last_button))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void AnalyzeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalyzeBTNActionPerformed
        // TODO add your handling code here:
        
        if(this.mySymbolsTable != null && this.mySymbolsTable.isEnabled()){
            this.mySymbolsTable.dispose();
            this.mySymbolsTable = null;
        }
        if(this.myDAGTable != null && this.myDAGTable.isEnabled()){
            this.myDAGTable.dispose();
            this.myDAGTable = null;
        }
        
        System.out.println("================================================");
        this.last_button.setEnabled(true);
        String input = this.inputField.getText();
        this.lastInput = input;
        
        ArrayList<Character> inputChars = new ArrayList<>();
        
        for(int i = 0 ; i < input.length() ; i++){
            inputChars.add(input.charAt(i));
        }
        
        ArrayList<String> allTokens = this.getTokens(inputChars);
        if(this.Error){
            this.Error = false;
            return;
        }
        ArrayList<String> symbolTable = this.getSymbolTable(allTokens);
        
        boolean AS = this.sintacticA((ArrayList<String>) allTokens.clone());
        
        if(!AS){
            return;
        }
        
        this.createSymbolsTable(symbolTable);
        
        // Create DAG table
        this.createDAGTable(allTokens);
        
        System.out.println(allTokens);
        System.out.println(symbolTable);
        System.out.println("AS Result: ".concat(String.valueOf(AS)));
        
        this.inputField.setText("");
    }//GEN-LAST:event_AnalyzeBTNActionPerformed

    private void last_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_last_buttonActionPerformed
        // TODO add your handling code here:
        this.inputField.setText(this.lastInput);
    }//GEN-LAST:event_last_buttonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalyzeBTN;
    private javax.swing.JTextField inputField;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton last_button;
    // End of variables declaration//GEN-END:variables
}