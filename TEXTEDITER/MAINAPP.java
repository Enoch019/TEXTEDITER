package TEXTEDITER;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MainApp extends JFrame {
    private JTextArea textArea;
    private JTextArea fileInfoArea;
    private JTextArea commandTextArea;
    private String lastOpenedFilePath;
    private int lineCount = 0;
    private JTextArea lineNumberArea; 

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            openFileFromPath(selectedFile.getAbsolutePath());
        }
    }

    public MainApp() {
        // JFrame 설정
        setTitle("Simple Text Editor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon newLogoIcon = new ImageIcon("TEXTEDITER\\Things\\logo.png");


        Image newLogoImage = newLogoIcon.getImage(); // ImageIcon에서 Image로 변환
        this.setIconImage(newLogoImage); // JFrame에 이미지 설정


        
        // 텍스트 입력을 받을 JTextArea 생성
        textArea = new JTextArea();
        fileInfoArea = new JTextArea();
        commandTextArea = new JTextArea();
        lineNumberArea = new JTextArea(); // 줄 번호를 표시할 JTextArea 생성

        // 텍스트 영역 배경색 설정
        textArea.setBackground(Color.WHITE);
        fileInfoArea.setBackground(Color.WHITE);
        fileInfoArea.setForeground(Color.BLUE);
        commandTextArea.setBackground(Color.BLACK);
        commandTextArea.setForeground(Color.WHITE);
        lineNumberArea.setBackground(Color.LIGHT_GRAY); // 줄 번호 영역의 배경색 설정

        fileInfoArea.setEditable(false);
        fileInfoArea.setFocusable(false);
        lineNumberArea.setEditable(false); // 줄 번호 영역은 편집할 수 없도록 설정
        lineNumberArea.setFocusable(false);

        // 스크롤 기능 추가
        JScrollPane scrollPane = new JScrollPane(textArea);
        JScrollPane fileInfoScrollPane = new JScrollPane(fileInfoArea);
        JScrollPane commandScrollPane = new JScrollPane(commandTextArea);
        JScrollPane lineNumberScrollPane = new JScrollPane(lineNumberArea); // 줄 번호 영역을 스크롤 가능하도록 JScrollPane으로 감싸줌
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        fileInfoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        commandScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // 수직 스크롤바만 없애기 위해 setVerticalScrollBarPolicy 메서드 호출
        lineNumberScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        // 수평 스크롤바를 없애기 위해 setHorizontalScrollBarPolicy 메서드 호출
        lineNumberScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 큰 창에 JTextArea 추가
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(fileInfoScrollPane, BorderLayout.NORTH);
        

        // JTextArea를 JPanel로 감싸기 위해 새로운 JPanel 생성
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.add(commandScrollPane, BorderLayout.CENTER); // JTextArea를 JPanel에 추가
        commandPanel.setPreferredSize(new Dimension(getWidth(), 20)); // JPanel의 높이를 20으로 설정하여 한 칸의 여백 추가
        getContentPane().add(commandPanel, BorderLayout.SOUTH); // JPanel을 프레임에 추가
        getContentPane().add(lineNumberScrollPane, BorderLayout.WEST); // 줄 번호 영역을 프레임의 서쪽에 추가

        // 키 이벤트 리스너 등록
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !textArea.getText().trim().isEmpty()) {
                    String input = textArea.getText().trim();
                    if (input.startsWith("/open")) {
                        openFile();
                    } else if (input.startsWith("/save")) {
                        saveToFile();
                    }
                }
            }
        
            @Override
            public void keyReleased(KeyEvent e) {
                // 텍스트 영역의 줄 수 계산
                int newLineCount = textArea.getLineCount();
                if (newLineCount != lineCount) {
                    lineCount = newLineCount;
                    updateLineNumbers(); // 줄 수가 변경되면 문자열 순서 업데이트
                }
            }
        });
        

        commandTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !commandTextArea.getText().trim().isEmpty()) {
                    String input = commandTextArea.getText().trim();
                    if (input.startsWith("/open")) {
                        openFile();
                    } else if (input.startsWith("/save")) {
                        saveToFile();
                    }
                    commandTextArea.setText(""); // 커맨드 입력 창 초기화
                }
            }
        });
    }

    private void openFileFromPath(String filename) {
        File file = new File(filename);
        if (file.exists() && !file.isDirectory()) {
            String content = readFile(filename);
            if (content != null) {
                textArea.setText(content);
                lastOpenedFilePath = filename; // 마지막으로 열었던 파일의 경로 기억
                updateFileInfo();
            } else {
                JOptionPane.showMessageDialog(this, "파일을 열 수 없습니다: " + filename);
            }
        } else {
            JOptionPane.showMessageDialog(this, "파일을 찾을 수 없습니다: " + filename);
        }
    }

    private void updateLineNumbers() {
        StringBuilder numbers = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            numbers.append(i).append("\n");
        }
        lineNumberArea.setText(numbers.toString());
    }
    

    private String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try {
            // 파일 읽기
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveToFile() {
        if (lastOpenedFilePath == null) {
            JOptionPane.showMessageDialog(this, "저장할 파일이 없습니다.");
            return;
        }

        try {
            FileWriter writer = new FileWriter(lastOpenedFilePath);
            writer.write(textArea.getText());
            writer.close();
            JOptionPane.showMessageDialog(this, "파일이 성공적으로 저장되었습니다: " + lastOpenedFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "파일을 저장할 수 없습니다: " + lastOpenedFilePath);
        }
    }

    private void updateFileInfo() {
        if (lastOpenedFilePath != null) {
            File file = new File(lastOpenedFilePath);
            String fileName = file.getName();
            String filePath = file.getAbsolutePath();
            String fileInfo = "File Name: " + fileName + "\nFile Path: " + filePath;
            fileInfoArea.setText(fileInfo);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}
