import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudyGardenApp extends JFrame {
    private JTextField nicknameField;
    private JTextField taskTitleField;
    private JButton startButton;
    private JButton stopButton;
    private JLabel plantStatusLabel;
    private JLabel timerLabel;

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
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setBackground(new Color(210, 250, 210));
        add(backgroundPanel);

        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        topPanel.setBackground(new Color(200, 240, 200));

        JLabel nicknameLabel = new JLabel("닉네임:");
        nicknameLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        topPanel.add(nicknameLabel);

        nicknameField = new JTextField();
        topPanel.add(nicknameField);

        JLabel taskLabel = new JLabel("과제 제목:");
        taskLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        topPanel.add(taskLabel);

        taskTitleField = new JTextField();
        topPanel.add(taskTitleField);

        startButton = new JButton("🌼 시작하기");
        startButton.setBackground(new Color(150, 200, 150));
        startButton.setForeground(Color.BLACK);
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });
        topPanel.add(startButton);

        // 종료 버튼 추가
        stopButton = new JButton("🛑 종료하기");
        stopButton.setBackground(new Color(200, 100, 100));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        });
        topPanel.add(stopButton);

        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setBackground(new Color(180, 230, 250));

        timerLabel = new JLabel("⏱ 경과 시간: 0초", SwingConstants.CENTER);
        timerLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        centerPanel.add(timerLabel);

        plantStatusLabel = new JLabel("🌱 현재 상태: 씨앗", SwingConstants.CENTER);
        plantStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        plantStatusLabel.setForeground(new Color(0, 128, 0));
        centerPanel.add(plantStatusLabel);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private void startTimer() {
        elapsedSeconds = 0;

        if (timer != null && timer.isRunning()) {
            timer.stop();  // 재시작 시 기존 타이머 중단
        }

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                elapsedSeconds++;
                timerLabel.setText("⏱ 경과 시간: " + elapsedSeconds + "초");

                if (elapsedSeconds == 10) {
                    plantStatusLabel.setText("🌿 현재 상태: 새싹");
                    plantStatusLabel.setForeground(new Color(34, 139, 34));
                } else if (elapsedSeconds == 20) {
                    plantStatusLabel.setText("🌳 현재 상태: 나무");
                    plantStatusLabel.setForeground(new Color(0, 100, 0));
                }
            }
        });

        timer.start();
        JOptionPane.showMessageDialog(this, "타이머가 시작되었습니다!");
    }

    private void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "⏹ 타이머가 종료되었습니다!");
        }
    }

    public static void main(String[] args) {
        new StudyGardenApp();
    }
}
