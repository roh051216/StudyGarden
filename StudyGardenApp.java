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
    private Timer idleTimer;
    private int elapsedSeconds = 0;
    private boolean isIdle = false;
    private final int IDLE_TIMEOUT = 30; // 30초

    public StudyGardenApp() {
        setTitle("StudyGarden 🌱");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        initIdleDetection();

        setVisible(false); // 로그인 성공 전까지는 숨김
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
        startButton.addActionListener(e -> startTimer());
        topPanel.add(startButton);

        stopButton = new JButton("🛑 종료하기");
        stopButton.setBackground(new Color(200, 100, 100));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        stopButton.addActionListener(e -> stopTimer());
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
            timer.stop();
        }

        timer = new Timer(1000, e -> {
            elapsedSeconds++;
            timerLabel.setText("⏱ 경과 시간: " + elapsedSeconds + "초");

            if (elapsedSeconds == 10) {
                plantStatusLabel.setText("🌿 현재 상태: 새싹");
                plantStatusLabel.setForeground(new Color(34, 139, 34));
            } else if (elapsedSeconds == 20) {
                plantStatusLabel.setText("🌳 현재 상태: 나무");
                plantStatusLabel.setForeground(new Color(0, 100, 0));
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

    private void updatePlantStatus() {
        if (elapsedSeconds < 10) {
            plantStatusLabel.setText("🌱 현재 상태: 씨앗");
            plantStatusLabel.setForeground(new Color(0, 128, 0));
        } else if (elapsedSeconds < 20) {
            plantStatusLabel.setText("🌿 현재 상태: 새싹");
            plantStatusLabel.setForeground(new Color(34, 139, 34));
        } else {
            plantStatusLabel.setText("🌳 현재 상태: 나무");
            plantStatusLabel.setForeground(new Color(0, 100, 0));
        }
    }

    private void resetIdleTimer() {
        if (isIdle) {
            isIdle = false;
            updatePlantStatus();
        }
        idleTimer.restart();
    }

    private void initIdleDetection() {
        idleTimer = new Timer(IDLE_TIMEOUT * 1000, e -> {
            isIdle = true;
            plantStatusLabel.setText("😴 현재 상태: 방치 중");
            plantStatusLabel.setForeground(Color.GRAY);
        });
        idleTimer.setRepeats(false);

        MouseAdapter activityTracker = new MouseAdapter() {
            public void mousePressed(MouseEvent e) { resetIdleTimer(); }
            public void mouseMoved(MouseEvent e) { resetIdleTimer(); }
        };
        KeyAdapter keyTracker = new KeyAdapter() {
            public void keyPressed(KeyEvent e) { resetIdleTimer(); }
        };

        addMouseListener(activityTracker);
        addMouseMotionListener(activityTracker);
        addKeyListener(keyTracker);
        setFocusable(true);
    }

    public static void main(String[] args) {
        StudyGardenApp app = new StudyGardenApp();
        LoginDialog login = new LoginDialog(app);
        login.setLocationRelativeTo(null);
        login.setVisible(true);

        if (login.isAuthenticated()) {
            app.setVisible(true);
        } else {
            System.exit(0);
        }
    }
}

class LoginDialog extends JDialog {
    private boolean authenticated = false;

    public LoginDialog(JFrame parent) {
        super(parent, "로그인", true);
        setLayout(new GridLayout(3, 2));
        setSize(300, 150);

        JLabel userLabel = new JLabel("아이디:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("비밀번호:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("user") && pass.equals("1234")) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패!");
            }
        });

        add(userLabel); add(userField);
        add(passLabel); add(passField);
        add(new JLabel()); add(loginButton);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}