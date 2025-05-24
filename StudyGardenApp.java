import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudyGardenApp extends JFrame {
    private JTextField nicknameField;
    private JTextField taskTitleField;
    private JButton startButton;
    private JLabel plantStatusLabel;

    public StudyGardenApp() {
        setTitle("StudyGarden 🌱");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2));

        topPanel.add(new JLabel("닉네임:"));
        nicknameField = new JTextField();
        topPanel.add(nicknameField);

        topPanel.add(new JLabel("과제 제목:"));
        taskTitleField = new JTextField();
        topPanel.add(taskTitleField);

        startButton = new JButton("시작하기");
        // TODO: 타이머 시작 및 포인트 누적 로직 연결 예정
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 타이머 시작 기능 구현 예정
                JOptionPane.showMessageDialog(null, "타이머 시작 (미구현)");
            }
        });

        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);

        // 식물 상태를 표시할 공간
        plantStatusLabel = new JLabel("🌱 현재 상태: 씨앗", SwingConstants.CENTER);
        add(plantStatusLabel, BorderLayout.CENTER);

        // TODO: 타이머, 포인트, 식물 성장 상태 업데이트 기능 추가
    }

    public static void main(String[] args) {
        new StudyGardenApp();
    }
}
