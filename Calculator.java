import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener, KeyListener {
    JTextField display;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[8];
    JButton addButton, subButton, mulButton, divButton;
    JButton decButton, equButton, delButton, clrButton;
    JPanel panel;

    Font myFont = new Font("Arial", Font.BOLD, 40);

    double num1 = 0, num2 = 0, result = 0;
    char operator;
    StringBuilder expression = new StringBuilder();

    public Calculator() {
        setLayout(null);
        setTitle("Calculator");
        setSize(420, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(Color.DARK_GRAY);         
   
        display = new JTextField();
        display.setBounds(50, 25, 300, 50);
        display.setFont(myFont);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.DARK_GRAY);
        display.setEditable(false);
        display.addKeyListener(this); // Add KeyListener to display
        add(display);

        // Make the frame focusable and add KeyListener
        setFocusable(true);
        addKeyListener(this);

        String[] functionNames = {"+", "-", "*", "/", ".", "=", "Del", "Clr"};
        for (int i = 0; i < 8; i++) {
            functionButtons[i] = new JButton(functionNames[i]);
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
            functionButtons[i].setForeground(Color.DARK_GRAY);
            functionButtons[i].setBackground(Color.ORANGE);
        }

        addButton = functionButtons[0];
        subButton = functionButtons[1];
        mulButton = functionButtons[2];
        divButton = functionButtons[3];
        decButton = functionButtons[4];
        equButton = functionButtons[5];
        delButton = functionButtons[6];
        clrButton = functionButtons[7];

        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
            numberButtons[i].setForeground(Color.WHITE);
            numberButtons[i].setBackground(Color.LIGHT_GRAY);
        }

        delButton.setBounds(150, 430, 100, 50);
        clrButton.setBounds(250, 430, 100, 50);

        add(delButton);
        add(clrButton);

        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.DARK_GRAY);
  
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);

        add(panel);
        setVisible(true);
    }

    private void processOperator(char op) {
        try {
            if (!expression.toString().isEmpty()) {
                num1 = Double.parseDouble(expression.toString());
                operator = op;
                expression.append(" " + op + " ");
                display.setText(expression.toString());
            }
        } catch (NumberFormatException ex) {
            // Ignore if the current expression can't be parsed as a number
        }
    }

    private void processNumber(char num) {
        expression.append(num);
        display.setText(expression.toString());
    }

    private void processEquals() {
        try {
            String[] parts = expression.toString().split("[ +/*-]");
            if (parts.length > 1) {
                num2 = Double.parseDouble(parts[parts.length - 1]);
                
                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        result = num1 / num2;
                        break;
                }
                display.setText(expression.toString() + " = " + result);
                expression = new StringBuilder(String.valueOf(result));
                num1 = result;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            // Ignore if the expression can't be evaluated
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 10; i++) {
            if (e.getSource() == numberButtons[i]) {
                processNumber((char)('0' + i));
            }
        }
        if (e.getSource() == decButton) {
            expression.append(".");
            display.setText(expression.toString());
        }
        if (e.getSource() == addButton) processOperator('+');
        if (e.getSource() == subButton) processOperator('-');
        if (e.getSource() == mulButton) processOperator('*');
        if (e.getSource() == divButton) processOperator('/');
        if (e.getSource() == equButton) processEquals();
        if (e.getSource() == clrButton) {
            display.setText("");
            expression = new StringBuilder();
        }
        if (e.getSource() == delButton && expression.length() > 0) {
            expression.deleteCharAt(expression.length() - 1);
            display.setText(expression.toString());
        }
    }

    // KeyListener implementation
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        
        // Handle numbers
        if (Character.isDigit(c)) {
            processNumber(c);
        }
        // Handle operators
        else if (c == '+' || c == '-' || c == '*' || c == '/') {
            processOperator(c);
        }
        // Handle equals (both Enter and =)
        else if (c == '=' || c == KeyEvent.VK_ENTER) {
            processEquals();
        }
        // Handle decimal point
        else if (c == '.') {
            expression.append(".");
            display.setText(expression.toString());
        }
        // Handle delete (backspace)
        else if (c == KeyEvent.VK_BACK_SPACE && expression.length() > 0) {
            expression.deleteCharAt(expression.length() - 1);
            display.setText(expression.toString());
        }
        // Handle clear (escape)
        else if (c == KeyEvent.VK_ESCAPE) {
            display.setText("");
            expression = new StringBuilder();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Handle special keys if needed
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            processEquals();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            display.setText("");
            expression = new StringBuilder();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed but required by KeyListener interface
    }

    public static void main(String[] args) {
        new Calculator();
    }
}