import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudyGardenApp extends JFrame {
    private JTextField nicknameField;
    private JTextField taskTitleField;
    private JButton startButton;
    private JLabel plantStatusLabel;
    private JLabel timerLabel;         // ⏱ 타이머 라벨

    private Timer timer;
    private int elapsedSeconds = 0;

    public StudyGardenApp() {
        setTitle("StudyGarden 🌱");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new GridLayout(3, 2));

        topPanel.add(new JLabel("닉네임:"));
        nicknameField = new JTextField();
        topPanel.add(nicknameField);

        topPanel.add(new JLabel("과제 제목:"));
        taskTitleField = new JTextField();
        topPanel.add(taskTitleField);

        startButton = new JButton("시작하기");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });

        topPanel.add(startButton);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널에 타이머 + 식물 상태 표시
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));

        timerLabel = new JLabel("⏱ 경과 시간: 0초", SwingConstants.CENTER);
        centerPanel.add(timerLabel);

        plantStatusLabel = new JLabel("🌱 현재 상태: 씨앗", SwingConstants.CENTER);
        centerPanel.add(plantStatusLabel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void startTimer() {
        elapsedSeconds = 0;

        if (timer != null && timer.isRunning()) {
            timer.stop();  // 중복 실행 방지
        }

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                timerLabel.setText("⏱ 경과 시간: " + elapsedSeconds + "초");

                // 식물 상태 변경
                if (elapsedSeconds == 10) {
                    plantStatusLabel.setText("🌿 현재 상태: 새싹");
                } else if (elapsedSeconds == 20) {
                    plantStatusLabel.setText("🌳 현재 상태: 나무");
                }
            }
        });

        timer.start();
        JOptionPane.showMessageDialog(this, "타이머가 시작되었습니다!");
    }

    public static void main(String[] args) {
        new StudyGardenApp();
    }
}
