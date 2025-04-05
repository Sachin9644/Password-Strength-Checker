import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Testfile extends JFrame {
    private JPasswordField passwordField;
    private JProgressBar progressBar;
    private JPanel panel;
    private JLabel lengthLabel, uppercaseLabel, lowercaseLabel, numberLabel, symbolLabel;
    private JButton toggleVisibilityButton;
    private boolean isPasswordVisible = false;
    private JLabel strengthLabel;
    private Image backgroundImage;
    private JLabel backgroundLabel;

    public Testfile() {
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

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("bckgd.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Background Panel
        backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setLayout(new GridBagLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        // Create the frosted glass effect panel
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(25, 25, 25, 160));  // Dark translucent background
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        updatePanelSize();

        // Resize listeners
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                updatePanelSize();
                scaleBackgroundImage();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Central Heading
        JLabel centralHeading = new JLabel("Check Strength", SwingConstants.CENTER);
        centralHeading.setFont(new Font("Arial", Font.BOLD, 20));
        centralHeading.setOpaque(true);
        centralHeading.setBackground(Color.BLACK);
        centralHeading.setForeground(Color.WHITE);
        centralHeading.setPreferredSize(new Dimension(200, 30));
        panel.add(centralHeading, gbc);

        // Password Field
        gbc.gridy++;
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Enter Password:");
        passwordPanel.add(label);

        passwordField = new JPasswordField(35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                checkStrength(new String(passwordField.getPassword()));
            }
        });
        passwordPanel.add(passwordField);

        toggleVisibilityButton = new JButton("👁");
        toggleVisibilityButton.addActionListener(e -> {
            isPasswordVisible = !isPasswordVisible;
            passwordField.setEchoChar(isPasswordVisible ? (char) 0 : '*');
        });
        passwordPanel.add(toggleVisibilityButton);
        panel.add(passwordPanel, gbc);

        // Progress Bar
        gbc.gridy++;
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 30));
        progressBar.setValue(0);
        panel.add(progressBar, gbc);

        // Criteria Labels
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

        // Strength Text
        gbc.gridy++;
        strengthLabel = new JLabel("Strength: None", SwingConstants.CENTER);
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(strengthLabel, gbc);

        backgroundLabel.add(panel);  // Attach frosted panel to background
    }

    private void updatePanelSize() {
        panel.setPreferredSize(new Dimension(
                (int) (getWidth() * 0.5),
                (int) (getHeight() * 0.4)
        ));
        panel.revalidate();
        panel.repaint();
    }

    private void scaleBackgroundImage() {
        if (backgroundImage != null) {
            ImageIcon scaledIcon = new ImageIcon(backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH));
            backgroundLabel.setIcon(scaledIcon);
        }
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

        int strengthValue = (criteria * 100) / 5;
        progressBar.setValue(strengthValue);
        String strengthText = getStrengthLabel(criteria);
        progressBar.setString(strengthText);
        strengthLabel.setText("Strength: " + strengthText);
        updateStrengthColor(criteria);
    }

    private String getStrengthLabel(int criteria) {
        if (criteria <= 2) return "Too Weak";
        else if (criteria <= 3) return "Weak";
        else if (criteria <= 4) return "Moderate";
        else return "Strong";
    }

    private void updateStrengthColor(int criteria) {
        if (criteria <= 2) {
            progressBar.setForeground(Color.RED);
            strengthLabel.setForeground(new Color(255, 0, 0, 200));
        } else if (criteria <= 3) {
            progressBar.setForeground(Color.ORANGE);
            strengthLabel.setForeground(Color.ORANGE);
        } else if (criteria <= 4) {
            progressBar.setForeground(new Color(228, 255, 26));
            strengthLabel.setForeground(new Color(244, 255, 45, 175));
        } else {
            progressBar.setForeground(Color.GREEN);
            strengthLabel.setForeground(Color.GREEN);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Testfile().setVisible(true);
        });
    }
}
