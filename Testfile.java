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

        JLabel headerLabel = new JLabel("Password Strength Checker", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(3, 21, 47, 255));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(getWidth(), 40));
        add(headerLabel, BorderLayout.NORTH);

        try {
            backgroundImage = ImageIO.read(new File("bckgd.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setLayout(new GridBagLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 75));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        updatePanelSize();

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

        JLabel centralHeading = new JLabel("Check Strength", SwingConstants.CENTER);
        centralHeading.setFont(new Font("Arial", Font.BOLD, 20));
        centralHeading.setOpaque(true);
        centralHeading.setBackground(new Color(255, 255, 255, 80));
        centralHeading.setForeground(Color.BLACK);
        centralHeading.setPreferredSize(new Dimension(200, 30));
        panel.add(centralHeading, gbc);

        gbc.gridy++;
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setOpaque(false);
        JLabel label = new JLabel("Enter Password:");
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
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

        toggleVisibilityButton = new JButton("\uD83D\uDC41");
        passwordPanel.add(toggleVisibilityButton);
        toggleVisibilityButton.addActionListener(e -> {
            isPasswordVisible = !isPasswordVisible;
            passwordField.setEchoChar(isPasswordVisible ? (char) 0 : '*');
        });
        panel.add(passwordPanel, gbc);

        gbc.gridy++;
        progressBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String text = getString();
                if (text != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    Color textColor;
                    switch (text.toLowerCase()) {
                        case "too weak":
                        case "weak":
                        case "moderate":
                        case "strong":
                        default:
                            textColor = Color.BLACK;
                            break;
                    }
                    g2.setColor(textColor);
                    g2.drawString(text, x, y);
                }
            }
        };
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(400, 30));
        progressBar.setValue(0);
        progressBar.setString("0%");
        panel.add(progressBar, gbc);

        gbc.gridy++;
        JPanel criteriaPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        criteriaPanel.setOpaque(false);
        lengthLabel = new JLabel("\u274C Length");
        uppercaseLabel = new JLabel("\u274C Uppercase");
        lowercaseLabel = new JLabel("\u274C Lowercase");
        numberLabel = new JLabel("\u274C Numbers");
        symbolLabel = new JLabel("\u274C Symbols");

        JLabel[] allLabels = { lengthLabel, uppercaseLabel, lowercaseLabel, numberLabel, symbolLabel };
        for (JLabel lbl : allLabels) {
            lbl.setForeground(Color.WHITE);
            criteriaPanel.add(lbl);
        }
        panel.add(criteriaPanel, gbc);

        gbc.gridy++;
        strengthLabel = new JLabel("Strength: None", SwingConstants.CENTER);
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        strengthLabel.setForeground(Color.WHITE);
        panel.add(strengthLabel, gbc);

        backgroundLabel.add(panel);
    }

    private void updatePanelSize() {
        panel.setPreferredSize(new Dimension(
                (int) (getWidth() * 0.5),
                (int) (getHeight() * 0.4)));
        panel.revalidate();
        panel.repaint();
    }

    private void scaleBackgroundImage() {
        if (backgroundImage != null) {
            ImageIcon scaledIcon = new ImageIcon(
                    backgroundImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH));
            backgroundLabel.setIcon(scaledIcon);
        }
    }

    private void checkStrength(String password) {
        int criteria = 0;

        if (password.length() >= 8) {
            criteria++;
            lengthLabel.setText("\u2714 Length");
            lengthLabel.setForeground(Color.GREEN);
        } else {
            lengthLabel.setText("\u274C Length");
            lengthLabel.setForeground(Color.RED);
        }

        if (password.matches(".*[A-Z].*")) {
            criteria++;
            uppercaseLabel.setText("\u2714 Uppercase");
            uppercaseLabel.setForeground(Color.GREEN);
        } else {
            uppercaseLabel.setText("\u274C Uppercase");
            uppercaseLabel.setForeground(Color.WHITE);
        }

        if (password.matches(".*[a-z].*")) {
            criteria++;
            lowercaseLabel.setText("\u2714 Lowercase");
            lowercaseLabel.setForeground(Color.GREEN);
        } else {
            lowercaseLabel.setText("\u274C Lowercase");
            lowercaseLabel.setForeground(Color.WHITE);
        }

        if (password.matches(".*[0-9].*")) {
            criteria++;
            numberLabel.setText("\u2714 Numbers");
            numberLabel.setForeground(Color.GREEN);
        } else {
            numberLabel.setText("\u274C Numbers");
            numberLabel.setForeground(Color.WHITE);
        }

        if (password.matches(".*[^A-Za-z0-9].*")) {
            criteria++;
            symbolLabel.setText("\u2714 Symbols");
            symbolLabel.setForeground(Color.GREEN);
        } else {
            symbolLabel.setText("\u274C Symbols");
            symbolLabel.setForeground(Color.WHITE);
        }

        int strengthValue = (criteria * 100) / 5;
        progressBar.setValue(strengthValue);
        String strengthText = getStrengthLabel(criteria);
        progressBar.setString(strengthText);
        strengthLabel.setText("Strength: " + strengthText);
        updateStrengthColor(criteria);
    }

    private String getStrengthLabel(int criteria) {
        if (criteria <= 2)
            return "Too Weak";
        else if (criteria <= 3)
            return "Weak";
        else if (criteria <= 4)
            return "Moderate";
        else
            return "Strong";
    }

    private void updateStrengthColor(int criteria) {
        if (criteria <= 2) {
            progressBar.setForeground(new Color(252, 32, 32));
            strengthLabel.setForeground(new Color(252, 32, 32));
            strengthLabel.setToolTipText("Too Weak: Add more character types and length.");
        } else if (criteria <= 3) {
            progressBar.setForeground(new Color(255, 109, 46));
            strengthLabel.setForeground(new Color(255, 109, 46));
            strengthLabel.setToolTipText("Weak: Try adding symbols or uppercase letters.");
        } else if (criteria <= 4) {
            progressBar.setForeground(new Color(255, 251, 35));
            strengthLabel.setForeground(new Color(255, 251, 35));
            strengthLabel.setToolTipText("Moderate: Improve by using all character types.");
        } else {
            progressBar.setForeground(new Color(61, 255, 47));
            strengthLabel.setForeground(new Color(61, 255, 47));
            strengthLabel.setToolTipText("Strong: Great password!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Testfile().setVisible(true);
        });
    }
}
