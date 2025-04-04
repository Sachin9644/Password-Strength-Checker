import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordStrengthChecker extends JFrame {
    private JPasswordField passwordField;
    private JProgressBar progressBar;
    private JPanel panel;
    private JLabel lengthLabel, uppercaseLabel, lowercaseLabel, numberLabel, symbolLabel;
    private JButton toggleVisibilityButton;
    private boolean isPasswordVisible = false;
    private JLabel strengthLabel; // New label to show strength message

    public PasswordStrengthChecker() {
        setTitle("Password Strength Checker");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Label
        JLabel headerLabel = new JLabel("Password Strength Checker", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(3, 21, 47, 255));
        headerLabel.setForeground(Color.white);
        headerLabel.setPreferredSize(new Dimension(getWidth(), 50));
        add(headerLabel, BorderLayout.NORTH);

        // Background Panel with Image
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ImageIcon("background.jpg").getImage().getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH)));
        background.setLayout(new GridBagLayout());
        add(background);

        // White Panel in Center
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        updatePanelSize();

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updatePanelSize();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Central Box Heading
        JLabel centralHeading = new JLabel("Check Strength", SwingConstants.CENTER);
        centralHeading.setFont(new Font("Arial", Font.BOLD, 20));
        centralHeading.setOpaque(true);
        centralHeading.setBackground(Color.BLACK);
        centralHeading.setForeground(Color.WHITE);
        centralHeading.setPreferredSize(new Dimension(200, 30));
        panel.add(centralHeading, gbc);

        gbc.gridy++;
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Enter Password:");
        passwordPanel.add(label);
        
        passwordField = new JPasswordField(35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkStrength(new String(passwordField.getPassword()));
            }
        });
        passwordPanel.add(passwordField);

        // Eye Icon Button
        toggleVisibilityButton = new JButton("👁");
        toggleVisibilityButton.addActionListener(e -> {
            isPasswordVisible = !isPasswordVisible;
            passwordField.setEchoChar(isPasswordVisible ? (char) 0 : '*');
        });
        passwordPanel.add(toggleVisibilityButton);
        panel.add(passwordPanel, gbc);

        gbc.gridy++;
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 30));
        progressBar.setValue(0);
        panel.add(progressBar, gbc);

        gbc.gridy++;
        JPanel criteriaPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        lengthLabel = new JLabel("❌ Length");
        lengthLabel.setForeground(Color.RED);
        uppercaseLabel = new JLabel("❌ Uppercase");
        uppercaseLabel.setForeground(Color.RED);
        lowercaseLabel = new JLabel("❌ Lowercase");
        lowercaseLabel.setForeground(Color.RED);
        numberLabel = new JLabel("❌ Numbers");
        numberLabel.setForeground(Color.RED);
        symbolLabel = new JLabel("❌ Symbols");
        symbolLabel.setForeground(Color.RED);
        criteriaPanel.add(lengthLabel);
        criteriaPanel.add(uppercaseLabel);
        criteriaPanel.add(lowercaseLabel);
        criteriaPanel.add(numberLabel);
        criteriaPanel.add(symbolLabel);
        panel.add(criteriaPanel, gbc);

        // Strength Label
        gbc.gridy++;
        strengthLabel = new JLabel("Strength: None", SwingConstants.CENTER);
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(strengthLabel, gbc);

        background.add(panel);
    }

    private void updatePanelSize() {
        panel.setPreferredSize(new Dimension(
                (int) (getWidth() * 0.5),
                (int) (getHeight() * 0.4)
        ));
        panel.revalidate();
        panel.repaint();
    }

    private void checkStrength(String password) {
        int criteria = 0;
        if (password.length() >= 8) {
            criteria++;
            lengthLabel.setText("✔ Length");
            lengthLabel.setForeground(Color.GREEN);
        } else {
            lengthLabel.setText("❌ Length");
            lengthLabel.setForeground(Color.RED);
        }
        if (password.matches(".*[A-Z].*")) {
            criteria++;
            uppercaseLabel.setText("✔ Uppercase");
            uppercaseLabel.setForeground(Color.GREEN);
        } else {
            uppercaseLabel.setText("❌ Uppercase");
            uppercaseLabel.setForeground(Color.RED);
        }
        if (password.matches(".*[a-z].*")) {
            criteria++;
            lowercaseLabel.setText("✔ Lowercase");
            lowercaseLabel.setForeground(Color.GREEN);
        } else {
            lowercaseLabel.setText("❌ Lowercase");
            lowercaseLabel.setForeground(Color.RED);
        }
        if (password.matches(".*[0-9].*")) {
            criteria++;
            numberLabel.setText("✔ Numbers");
            numberLabel.setForeground(Color.GREEN);
        } else {
            numberLabel.setText("❌ Numbers");
            numberLabel.setForeground(Color.RED);
        }
        if (password.matches(".*[^A-Za-z0-9].*")) {
            criteria++;
            symbolLabel.setText("✔ Symbols");
            symbolLabel.setForeground(Color.GREEN);
        } else {
            symbolLabel.setText("❌ Symbols");
            symbolLabel.setForeground(Color.RED);
        }

        // Update progress bar and strength label
        int strengthValue = (criteria * 100) / 5; // 5 criteria
        progressBar.setValue(strengthValue);
        String strengthText = getStrengthLabel(criteria);
        progressBar.setString(strengthText);

        // Update strength message and color
        strengthLabel.setText("Strength: " + strengthText);
        updateStrengthColor(criteria);
    }

    private String getStrengthLabel(int criteria) {
        if (criteria <= 2) {
            return "Too Weak";
        } else if (criteria <= 3) {
            return "Weak";
        } else if (criteria <= 4) {
            return "Moderate";
        } else {
            return "Strong";
        }
    }

    private void updateStrengthColor(int criteria) {
        if (criteria <= 2) {
            progressBar.setForeground(Color.RED);
            strengthLabel.setForeground(new Color(255, 0, 0, 200));
        } else if (criteria <= 3) {
            progressBar.setForeground(Color.ORANGE);
            strengthLabel.setForeground(Color.orange);
        } else if (criteria <= 4) {
            progressBar.setForeground(new Color(228, 255, 26));
            strengthLabel.setForeground(new Color(244, 255, 45, 175));
        } else {
            progressBar.setForeground(Color.GREEN);
            strengthLabel.setForeground(Color.green);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PasswordStrengthChecker().setVisible(true);
        });
    }
}