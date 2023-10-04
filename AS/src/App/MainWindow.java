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
        expr -> term res_expr
        res_expr -> + term res_expr
        res_expr -> - term res_expr
        res_expr -> * term res_expr
        res_expr -> / term res_expr
        res_expr -> <LAMBDA>
        sign -> + | - | * | /
        term -> (expr)
        term -> 0...9
    */
     
    public MainWindow() {
        initComponents();
        this.Error = false;
        this.lastInput = "";
        this.postfixResult = "";
    }
    
    // Vars
    boolean Error;
    String lastInput;
    String postfixResult;
        
    //Function
    private void E(ArrayList<Character> input, boolean root){
        char first = input.get(0);
        
        if(first == '('){
            int pa = 1;
            int counter = 1;
            while(pa > 0 && counter < input.size()){
                if(input.get(counter) == '('){
                    pa++;
                }
                else if(input.get(counter) == ')'){
                    pa--;
                }
                counter++;
            }
            if(pa > 0 || counter > input.size()){
                this.Error = true;
            }else{
                ArrayList<Character> TermArray = new ArrayList<>();
                for(int i = 0 ; i < counter ; i++){
                    TermArray.add(input.get(0));
                    input.remove(0);
                }
                
                this.T(TermArray);
                this.RE(input);
            }
        }else{
            input.remove(0);
            
            this.postfixResult = this.postfixResult + first;

            this.T(first);
            this.RE(input);
        }
        
        if(root){
            if(this.Error){
                JOptionPane.showMessageDialog(this, "La cadena '".concat(this.lastInput).concat("' no es aceptada!"));
                this.Error = false;
                this.status.setText("La cadena '".concat(this.lastInput).concat("' no es aceptada!"));
                this.postfix.setText("ERROR!");
                this.resultado.setText("No hay resultado!");
            }else{
                this.postfixResult = this.InfixToPostfix(this.lastInput);
                
                JOptionPane.showMessageDialog(this, "La cadena '".concat(this.lastInput).concat("' es aceptada!"));
                this.status.setText("La cadena '".concat(this.lastInput).concat("' es aceptada!"));
                this.postfix.setText(this.postfixResult);
                this.resultado.setText(this.calculatePostfix(this.postfixResult));
                this.postfixResult = "";
            }
        }
    }
    private void T(char input){
        switch(input){
            case '0','1','2','3','4','5','6','7','8','9' -> {return;}
            default -> {this.Error = true;}
        }
    }
    private void T(ArrayList<Character> input){
        if(input.size() >= 3){
            input.remove(0);
            input.remove(input.size() - 1);
            
            this.E(input,false);
        }
        else{
            this.Error = true;
        }
    }
    private void RE(ArrayList<Character> input){
        if(!input.isEmpty() && input.size() >= 2){
            char sign = input.get(0);
            input.remove(0);

            char term = input.get(0);
            
            if(term == '('){
                int pa = 1;
                int counter = 1;
                while(pa > 0 && counter < input.size()){
                    if(input.get(counter) == '('){
                        pa++;
                    }
                    else if(input.get(counter) == ')'){
                        pa--;
                    }
                    counter++;
                }
                if(pa > 0 || counter > input.size()){
                    this.Error = true;
                }else{
                    ArrayList<Character> TermArray = new ArrayList<>();
                    for(int i = 0 ; i < counter ; i++){
                        TermArray.add(input.get(0));
                        input.remove(0);
                    }
                    
                    this.S(sign);
                    
                    this.T(TermArray);
                    this.RE(input);
                }
            }else{
                input.remove(0);
                
                this.postfixResult = this.postfixResult + term;
                
                this.S(sign);
                
                this.T(term);
                this.RE(input);
            }
        }
        else if (!input.isEmpty() && input.size() == 1) {
            this.Error = true;
        }
    }
    private void S(char sign){
        switch(sign){
            case '+','-','*','/' -> {return;}
            default -> {this.Error = true;}
        }
    }
    
    private boolean isNum(char c){
        switch(c){
            case '0','1','2','3','4','5','6','7','8','9' -> {return true;}
            default -> {return false;}
        }
    }
    
    private boolean isSign(char c){
        switch(c){
            case '+','-','*','/' -> {return true;}
            default -> {return false;}
        }
    }
    
    public String calculatePostfix(String input){
        Stack<String> numStack = new Stack<>();
        
        for(int i = 0 ; i < input.length() ; i++){
            if(this.isNum(input.charAt(i))){
                numStack.push(String.valueOf(input.charAt(i)));
            }
            else if(this.isSign(input.charAt(i))){
                double num2 = Double.parseDouble(numStack.pop());
                double num1 = Double.parseDouble(numStack.pop());
                
                switch(input.charAt(i)){
                    case '+' -> {
                        numStack.push(String.valueOf(num1 + num2));
                    }
                    case '-' -> {
                        numStack.push(String.valueOf(num1 - num2));
                    }
                    case '*' -> {
                        numStack.push(String.valueOf(num1 * num2));
                    }
                    case '/' -> {
                        numStack.push(String.valueOf(num1 / num2));
                    }
                }
            }
        }
        
        return numStack.pop();
    }
    
    private int precedence(char x){
        switch (x) {
            case '^' -> {
                return 2;
            }
            case '*', '/' -> {
                return 1;
            }
            case '+', '-' -> {
                return 0;
            }
            default -> {
            }
        }
        return -1;
    }

    private String InfixToPostfix(String str){

        Stack<Character>stk= new Stack<>();             // used for converting infix to postfix

        String ans="";                // string containing our final answer

        int n= str.length();

        for (int i = 0; i <n ; i++) {
            char x= str.charAt(i);

            if(x>='0' && x<='9'){
                ans+=x;
            }

            else if(x=='('){     // push directly in the stack
                stk.push('(');
            }
            else if(x==')'){

                while(!stk.isEmpty() && stk.peek()!='('){          // keep popping till opening bracket is found
                    ans+=stk.pop();
                }
                if(!stk.isEmpty()){
                    stk.pop();
                }

            }
            else{

                while(!stk.isEmpty() && precedence(stk.peek())>=precedence(x)){    // remove all higher precedence values
                    ans+=stk.pop();
                }
                stk.push(x);

            }
        }
        while(!stk.isEmpty()){
            ans+=stk.pop();
        }
        return ans;
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
        jLabel1 = new javax.swing.JLabel();
        status = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        last = new javax.swing.JLabel();
        last_button = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        postfix = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        resultado = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador Sintactico");
        setResizable(false);

        inputLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        inputLabel.setText("Cadena de entrada:");

        AnalyzeBTN.setText("Analizar");
        AnalyzeBTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnalyzeBTNActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Estado:");

        status.setText("NO INFO");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Ultima:");

        last.setText("NO DATA");

        last_button.setText("Anterior");
        last_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                last_buttonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("PostFija:");

        postfix.setText("NO DATA");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Resultado:");

        resultado.setText("NO DATA");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText("Analizador Sintactico");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(jSeparator1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(last)
                                    .addComponent(status)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(postfix))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resultado)))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(status))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(last)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(postfix))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(resultado))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void AnalyzeBTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalyzeBTNActionPerformed
        // TODO add your handling code here:
        
        if(this.inputField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "La Entrada no puede ser vacia!");
            return;
        }
        
        String data = this.inputField.getText();
        this.lastInput = this.inputField.getText();
        ArrayList<Character> Cadena = new ArrayList<>();
        
        for (int i=0 ; i< data.length(); i++){
            Cadena.add(data.charAt(i));
        }
        
        Cadena.removeIf(c -> (c == '\s'));
        
        this.inputField.setText("");
        
        this.E(Cadena,true);
        
        this.last.setText(this.lastInput);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel last;
    private javax.swing.JButton last_button;
    private javax.swing.JLabel postfix;
    private javax.swing.JLabel resultado;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables
}