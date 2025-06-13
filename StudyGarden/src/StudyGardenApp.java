import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;

public class StudyGardenApp extends JFrame {
    private JTextField nicknameField;
    private JTextField taskTitleField;
    private JButton startButton, stopButton, loadButton, deleteButton;
    private JLabel plantStatusLabel, timerLabel;

    private Timer timer;
    private Timer idleTimer;
    private int elapsedSeconds = 0;
    private boolean isIdle = false;
    private final int IDLE_TIMEOUT = 30; // 30초

    public StudyGardenApp() {
        setTitle("StudyGarden 🌱");
        setSize(400, 350);
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

        JPanel topPanel = new JPanel(new GridLayout(4, 2));
        topPanel.setBackground(new Color(200, 240, 200));

        topPanel.add(new JLabel("닉네임:"));
        nicknameField = new JTextField();
        topPanel.add(nicknameField);

        topPanel.add(new JLabel("과제 제목:"));
        taskTitleField = new JTextField();
        topPanel.add(taskTitleField);

        startButton = new JButton("시작하기");
        startButton.setBackground(new Color(150, 200, 150));
        startButton.addActionListener(e -> startTimer());
        topPanel.add(startButton);

        stopButton = new JButton("종료하기");
        stopButton.setBackground(new Color(200, 100, 100));
        stopButton.setForeground(Color.WHITE);
        stopButton.addActionListener(e -> stopTimer());
        topPanel.add(stopButton);

        loadButton = new JButton("기록 불러오기");
        loadButton.setBackground(new Color(100, 150, 200));
        loadButton.setForeground(Color.WHITE);
        loadButton.addActionListener(e -> loadStudyRecords());
        topPanel.add(loadButton);

        deleteButton = new JButton("기록 초기화");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteStudyRecords());
        topPanel.add(deleteButton);

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
            saveStudyRecord(); // ⬅️ 저장 기능
            JOptionPane.showMessageDialog(this, "⏹ 타이머가 종료되었습니다!");
        }
    }

    private void saveStudyRecord() {
        String nickname = nicknameField.getText();
        String task = taskTitleField.getText();
        String date = LocalDateTime.now().toString();
        String record = nickname + "," + task + "," + elapsedSeconds + "," + date + "\n";

        try (FileWriter writer = new FileWriter("study_log.csv", true)) {
            writer.write(record);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "기록 저장 실패: " + e.getMessage());
        }
    }

    private void loadStudyRecords() {
        String[] columnNames = {"닉네임", "과제명", "공부시간(초)", "날짜"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader("study_log.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) model.addRow(data);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(350, 200));
            JOptionPane.showMessageDialog(this, scrollPane, "📚 공부 기록", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "기록을 불러올 수 없습니다: " + e.getMessage());
        }
    }

    private void deleteStudyRecords() {
        int confirm = JOptionPane.showConfirmDialog(this, "정말로 기록을 삭제하시겠습니까?", "기록 초기화", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new PrintWriter("study_log.csv").close();
                JOptionPane.showMessageDialog(this, "기록이 삭제되었습니다.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "기록 삭제 실패: " + e.getMessage());
            }
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

// 로그인 다이얼로그는 변경 없음
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

            if (user.equals("user") && pass.equals("1111")) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "로그인을 실패하였습니다. 다시 시도하시오.");
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
