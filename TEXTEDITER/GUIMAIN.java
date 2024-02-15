package TEXTEDITER;
import javax.swing.*;
import java.awt.*;

public class GUIMAIN extends JFrame {
    public GUIMAIN() {
        // JFrame 설정
        setTitle(":> TextEditor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 텍스트 입력을 받을 JTextArea 생성
        JTextArea textArea = new JTextArea();

        // 스크롤 기능 추가
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // 큰 창에 JTextArea 추가
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
