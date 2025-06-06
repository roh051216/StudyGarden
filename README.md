 # <img width="281" alt="image" src="https://github.com/user-attachments/assets/176a0d5e-ee6c-49b7-b9a6-7e40bacf2b66" />
        
# 프로그램 설명

StudyGarden은 사용자가 과제를 진행할수록 가상의 식물이 자라나는 Java 기반 자기관리 애플리케이션입니다.  
과제를 미루면 식물이 시들고, 꾸준히 하면 꽃이 피는 등 시각적 피드백을 제공해줍니다.

# 사용자 정의

-  과제를 미루는 청주대학교교 재학생
-  집중력이 부족하고 자기관리가 힘든 사용자


# 이 프로그램의 필요성

-  타이머나 체크리스트 같은 앱들은 많지만 성장이라는 시각적 피드백을 통해 과제 뿐만이 아니라 공부   나 집중할 일의 진행의 동기부여

-  성취감을 누릴 수 있는 자기관리 도구가 필요하기에 선정

# 순서도
<img width="459" alt="image" src="https://github.com/user-attachments/assets/6a19306e-bac7-499b-9479-2cf402460838" />


# 개발 방법

1.  사용자 로그인 및 과제 등록 
2.  타이머 설정
3.  타이머가 돌아가는 동안 점수 누적
4.  누적된 점수에 따라 식물이 성장
5.  종료 시 결과 요약 및 성취도 표시
6.  Java swing GUI 라이브러리를 통한 개발

```java
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
```

# 동작 과정 설명

사용자 시작 단계

#### 입력 화면

- 사용자 로그인 (또는 닉네임 입력)
```java
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
```
    
- 과제 등록
    
    - 과제 제목
        
    - 예상 소요 시간 (ex: 3시간)
        
    - 마감일 선택
        
---

###  집중 세션 설정

#### 타이머 

- Pomodoro 모드 (25분 집중 + 5분 휴식)  
    또는 사용자 정의 시간 (ex: 50분 집중 / 10분 휴식)

  ```java
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

  ```
    

#### UI 

- "시작" 버튼 클릭 시 타이머 작동
    
- 화면에는 타이머 + 현재 성장 상태의 식물 이미지 표시
    

---

###  점수 시스템 + 식물 성장 

#### 타이머 작동 중

- 1분마다 집중 포인트(+1) 누적
    
-  25분 동안 완료 시 +25포인트
    

#### 성장 단계 로직 

| 누적 포인트 | 성장 상태 |
| ------ | ----- |
| 0~10   | 씨앗    |
| 11~30  | 새싹    |
| 31~60  | 줄기 성장 |
| 61~100 | 꽃 피움  |
| 101 이상 | 열매 맺음 |


---

### 미사용/방치 감지 

#### 감지 조건

-  24시간 이상 집중 세션 없음 → 식물이 시듦
    
-  3일 이상 방치 시 식물 "죽음" 상태 (화면에 고사 이미지 출력)
    

>  방치 상태일 경우 앱 실행 시 경고 메시지 출력  
>  단, 사용자가 다시 집중하면 “회복 모드” 진입 가능 (물주기 이미지 등)

---

### 세션 종료 시

- 오늘의 집중 통계 출력
    
    - 총 집중 시간
        
    - 새로 얻은 포인트
        
    - 식물 변화 여부
        
- 데이터 저장
    
    - 사용자별 누적 포인트
        
    - 현재 식물 상태
        
    - 과제 완료 여부
        

---

# 사용자는 누구인가 ? 

-  청주대학교 재학생 중 공부나 과제를 집중적으로 하고싶은 사람
-  집중력이 부족한 사람
-  키우는 걸 좋아하는 사람

# 사용자가 얻을 수 있는 이점은 ? 

-  단기적 목표 달성시 작은 성취감 그로인해서 식물이 성장하게 되고 그로인한 행복감
-  과제를 할 수록 시각적인 보상 제공


