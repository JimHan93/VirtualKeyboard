import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyVirtualKeyboard extends JFrame{
    private JTextArea text = new JTextArea();
    private JLabel infoL = new JLabel("<html>Type some text using your keyboard. The keys you press will be highlighted and the text will be displayed." +
            "<br>Note: Clicking the buttons with your mouse will not perform any action.</html>");
    private JPanel keyboardP = new JPanel();
    private String keyboard[][] = {
            {"~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "+", "Backspace"},
            {"Tab", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"},
            {"Caps", "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"", "Enter"},
            {"Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "?", "^"},
            {" ", "<", "v", ">"}
    };
    private boolean isCaps = false;

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    public MyVirtualKeyboard(){
        setTitle("Typing Tutor");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel contentP = new JPanel(new BorderLayout());
        contentP.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoL.setFont(new Font("Arial", Font.BOLD, 15));
        contentP.add(infoL, BorderLayout.NORTH);

        text.setFont(new Font("Arial", Font.BOLD, 20));
        text.setFocusable(true);
        text.requestFocusInWindow();
        contentP.add(text, BorderLayout.CENTER);


        keyboardP.setLayout(new BoxLayout(keyboardP, BoxLayout.Y_AXIS));
        keyboardP.setFont(new Font("ariel", Font.BOLD, 40));
        keyboardP.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentP.add(keyboardP, BorderLayout.SOUTH);

        Map<String, JButton> keyButtonMap = new HashMap<>();
        Map<String, Integer> specialKeyMap = new HashMap<>();
        specialKeyMap.put("Caps", KeyEvent.VK_CAPS_LOCK);
        specialKeyMap.put("Backspace", KeyEvent.VK_BACK_SPACE);
        specialKeyMap.put("Tab", KeyEvent.VK_TAB);
        specialKeyMap.put("Shift", KeyEvent.VK_SHIFT);
        specialKeyMap.put("Enter", KeyEvent.VK_ENTER);
        specialKeyMap.put(" ", KeyEvent.VK_SPACE);
        specialKeyMap.put("^", KeyEvent.VK_UP);
        specialKeyMap.put("<", KeyEvent.VK_LEFT);
        specialKeyMap.put("v", KeyEvent.VK_DOWN);
        specialKeyMap.put(">", KeyEvent.VK_RIGHT);
        specialKeyMap.put("-", KeyEvent.VK_MINUS);
        specialKeyMap.put("+", KeyEvent.VK_EQUALS);
        specialKeyMap.put("?", KeyEvent.VK_SLASH);
        specialKeyMap.put("~", KeyEvent.VK_BACK_QUOTE);



        for (String[] row : keyboard){
            JPanel rowP = new JPanel();
            rowP.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            rowP.setAlignmentX(Component.LEFT_ALIGNMENT);

            for(String key : row){
                JButton keyB = new JButton(key);
                keyB.setFocusable(false);

                if(key.equals("Backspace") || key.equals("Enter") || key.equals("Shift")){
                    keyB.setPreferredSize(new Dimension(100, 50));
                } else if(key.equals("Tab") || key.equals("Caps")){
                    keyB.setPreferredSize(new Dimension(75, 50));
                } else if(key.equals("^")){
                    JLabel spaceL1 = new JLabel();
                    spaceL1.setPreferredSize(new Dimension(25,50));
                    rowP.add(spaceL1);
                    keyB.setPreferredSize(new Dimension(50, 50));
                } else if(key.equals(" ")) {
                    JLabel spaceL2 = new JLabel();
                    spaceL2.setPreferredSize(new Dimension(195, 50));
                    keyB.setPreferredSize(new Dimension(310, 50));
                    rowP.add(spaceL2);
                } else if(key.equals("<")){
                    JLabel spaceL3 = new JLabel();
                    spaceL3.setPreferredSize(new Dimension(70, 50));
                    rowP.add(spaceL3);
                    keyB.setPreferredSize(new Dimension(50, 50));
                }else{
                    keyB.setPreferredSize(new Dimension(50, 50));
                }

                rowP.add(keyB);

                if(specialKeyMap.containsKey(key)){
                    keyButtonMap.put(String.valueOf(specialKeyMap.get(key)), keyB);
                }else if (key.length() == 1){
                    keyButtonMap.put(String.valueOf((int) key.charAt(0)), keyB);
                }
            }

            keyboardP.add(rowP);
        }

        setContentPane(contentP);

        InputMap inputMap = contentP.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentP.getActionMap();

        for (String key : keyButtonMap.keySet()){
            try{
                int keyCode = Integer.parseInt(key);
                inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), keyCode + "pressed");
                inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), keyCode + "released");
            } catch(NumberFormatException e){
                inputMap.put(KeyStroke.getKeyStroke(key.charAt(0)), key + "pressed");
                inputMap.put(KeyStroke.getKeyStroke(key.charAt(0), 0, true), key + "released");
            }

            actionMap.put(key + "pressed", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = keyButtonMap.get(key);
                    if(button != null){
                        button.setBackground(Color.GREEN);
                    }
                }
            });

            if (!key.equals(String.valueOf(KeyEvent.VK_CAPS_LOCK))) {
                actionMap.put(key + "released", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = keyButtonMap.get(key);
                        if (button != null){
                            button.setBackground(null);
                        }
                    }
                });
            } else {
                actionMap.put(key + "released", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = keyButtonMap.get(key);
                        if (button != null){
                            isCaps = toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
                            button.setBackground(isCaps ? Color.GREEN : null);
                        }
                    }
                });
            }
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    JButton button = keyButtonMap.get(String.valueOf(e.getKeyCode()));
                    if(button != null){
                        button.setBackground(Color.GREEN);
                    }
                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getKeyCode() != KeyEvent.VK_CAPS_LOCK) {
                        JButton button = keyButtonMap.get(String.valueOf(e.getKeyCode()));
                        if(button != null){
                            button.setBackground(null);
                        }
                    }
                }
                return false;
            }
        });
    }

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            MyVirtualKeyboard keyboard = new MyVirtualKeyboard();
            keyboard.setVisible(true);
        });
    }
}
