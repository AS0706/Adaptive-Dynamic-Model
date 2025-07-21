import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class WarSimulation extends JFrame {
    // Input fields
    private JTextField blueStrengthField, redStrengthField, exchangeRatioField;
    private JTextField blueAttritionField, redAttritionField;
    private JTextField blueExponentField, redExponentField;
    private JTextField maxWithdrawalField, blueThresholdField, redThresholdField;
    private JTextField groundProsecutionField;
    private JTextField blueReinforcementDaysField, redReinforcementDaysField;
    
    // Output components
    private JTextArea outputArea;
    private ChartPanel chartPanel;
    private JLabel statusLabel;
    
    // Simulation data
    private int currentDay = 1;
    private double blueStrength, redStrength;
    private double exchangeRatio;
    private double blueAttrition, redAttrition;
    private double blueExponent, redExponent;
    private double maxWithdrawal, blueThreshold, redThreshold;
    private double groundProsecution;
    private double withdrawal = 0.0;
    private Set<Integer> blueReinforcementDays = new HashSet<>();
    private Set<Integer> redReinforcementDays = new HashSet<>();
    private List<Point2D> blueDataPoints = new ArrayList<>();
    private List<Point2D> redDataPoints = new ArrayList<>();
    private boolean simulationEnded = false;
    
    // Constants
    private static final double BLUE_REINFORCEMENT_STRENGTH = 5038.0;
    private static final double RED_REINFORCEMENT_STRENGTH = 9600.0; // 4 divisions * 2400
    
    public WarSimulation() {
        setTitle("War Simulation Model");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLayout(new BorderLayout(10, 10));
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create input panel
        JPanel inputPanel = createInputPanel();
        
        // Create button panel with larger buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton nextDayButton = new JButton("Next Day");
        nextDayButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextDayButton.setPreferredSize(new Dimension(120, 40));
        nextDayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!simulationEnded) {
                    calculateNextDay();
                }
            }
        });
        
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setPreferredSize(new Dimension(120, 40));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSimulation();
            }
        });
        
        buttonPanel.add(nextDayButton);
        buttonPanel.add(Box.createHorizontalStrut(20)); // Add space between buttons
        buttonPanel.add(resetButton);
        
        // Create top panel containing input and buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create output panel
        JPanel outputPanel = new JPanel(new BorderLayout(0, 10));
        outputArea = new JTextArea(8, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        // Create chart panel with border
        chartPanel = new ChartPanel();
        chartPanel.setPreferredSize(new Dimension(800, 350));
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        statusLabel = new JLabel("Enter initial values and click 'Next Day' to start simulation");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        outputPanel.add(scrollPane, BorderLayout.NORTH);
        outputPanel.add(chartPanel, BorderLayout.CENTER);
        outputPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Set default values
        setDefaultValues();
        
        setLocationRelativeTo(null);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 4, 10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Row 1
        panel.add(new JLabel("Initial Blue Strength:"));
        blueStrengthField = new JTextField();
        panel.add(blueStrengthField);
        
        panel.add(new JLabel("Initial Red Strength:"));
        redStrengthField = new JTextField();
        panel.add(redStrengthField);
        
        // Row 2
        panel.add(new JLabel("Initial Exchange Ratio (rho0):"));
        exchangeRatioField = new JTextField();
        panel.add(exchangeRatioField);
        
        panel.add(new JLabel("Max Withdrawal %:"));
        maxWithdrawalField = new JTextField();
        panel.add(maxWithdrawalField);
        
        // Row 3
        panel.add(new JLabel("Initial Blue Attrition:"));
        blueAttritionField = new JTextField();
        panel.add(blueAttritionField);
        
        panel.add(new JLabel("Initial Red Attrition:"));
        redAttritionField = new JTextField();
        panel.add(redAttritionField);
        
        // Row 4
        panel.add(new JLabel("Blue Exponent:"));
        blueExponentField = new JTextField();
        panel.add(blueExponentField);
        
        panel.add(new JLabel("Red Exponent:"));
        redExponentField = new JTextField();
        panel.add(redExponentField);
        
        // Row 5
        panel.add(new JLabel("Blue Threshold:"));
        blueThresholdField = new JTextField();
        panel.add(blueThresholdField);
        
        panel.add(new JLabel("Red Threshold:"));
        redThresholdField = new JTextField();
        panel.add(redThresholdField);
        
        // Row 6
        panel.add(new JLabel("Initial Ground Prosecution Rate:"));
        groundProsecutionField = new JTextField();
        panel.add(groundProsecutionField);
        
        // Row 7
        panel.add(new JLabel("Blue Reinforcement Days (comma-separated):"));
        blueReinforcementDaysField = new JTextField();
        panel.add(blueReinforcementDaysField);
        
        panel.add(new JLabel("Red Reinforcement Days (comma-separated):"));
        redReinforcementDaysField = new JTextField();
        panel.add(redReinforcementDaysField);
        
        return panel;
    }
    
    private void setDefaultValues() {
        // Set some reasonable default values
        blueStrengthField.setText("50000");
        redStrengthField.setText("70000");
        exchangeRatioField.setText("1.5");
        blueAttritionField.setText("0.05");
        redAttritionField.setText("0.03");
        blueExponentField.setText("0.5");
        redExponentField.setText("0.5");
        maxWithdrawalField.setText("0.3"); // 30%
        blueThresholdField.setText("0.1"); // 10%
        redThresholdField.setText("0.1"); // 10%
        groundProsecutionField.setText("0.03"); // Same as initial red attrition
        blueReinforcementDaysField.setText("3,7,10");
        redReinforcementDaysField.setText("5,12");
    }
    
    private void resetSimulation() {
        currentDay = 1;
        withdrawal = 0.0;
        simulationEnded = false;
        blueDataPoints.clear();
        redDataPoints.clear();
        outputArea.setText("");
        statusLabel.setText("Simulation reset. Enter values and click 'Next Day' to start.");
        chartPanel.repaint();
    }
    
    private void initializeSimulation() {
        try {
            // Parse input values
            blueStrength = Double.parseDouble(blueStrengthField.getText());
            redStrength = Double.parseDouble(redStrengthField.getText());
            exchangeRatio = Double.parseDouble(exchangeRatioField.getText());
            blueAttrition = Double.parseDouble(blueAttritionField.getText());
            redAttrition = Double.parseDouble(redAttritionField.getText());
            blueExponent = Double.parseDouble(blueExponentField.getText());
            redExponent = Double.parseDouble(redExponentField.getText());
            maxWithdrawal = Double.parseDouble(maxWithdrawalField.getText());
            blueThreshold = Double.parseDouble(blueThresholdField.getText());
            redThreshold = Double.parseDouble(redThresholdField.getText());
            groundProsecution = Double.parseDouble(groundProsecutionField.getText());
            
            // Parse reinforcement days
            blueReinforcementDays = parseReinforcementDays(blueReinforcementDaysField.getText());
            redReinforcementDays = parseReinforcementDays(redReinforcementDaysField.getText());
            
            // Add initial data points
            blueDataPoints.add(new Point2D(blueStrength, currentDay));
            redDataPoints.add(new Point2D(redStrength, currentDay));
            
            // Display initial values
            outputArea.setText("Day " + currentDay + " (Initial Values):\n");
            outputArea.append(String.format("Blue Strength: %.2f\n", blueStrength));
            outputArea.append(String.format("Red Strength: %.2f\n", redStrength));
            outputArea.append(String.format("Exchange Ratio: %.4f\n", exchangeRatio));
            outputArea.append(String.format("Withdrawal: %.4f\n", withdrawal));
            outputArea.append(String.format("Red Attrition: %.4f\n", redAttrition));
            outputArea.append(String.format("Ground Prosecution Rate: %.4f\n", groundProsecution));
            
            chartPanel.updateData(blueDataPoints, redDataPoints);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for all fields.", 
                                         "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Set<Integer> parseReinforcementDays(String daysStr) {
        Set<Integer> days = new HashSet<>();
        if (daysStr != null && !daysStr.trim().isEmpty()) {
            String[] parts = daysStr.split(",");
            for (String part : parts) {
                try {
                    days.add(Integer.parseInt(part.trim()));
                } catch (NumberFormatException e) {
                    // Skip invalid entries
                }
            }
        }
        return days;
    }
    
    private void calculateNextDay() {
        // Initialize on first calculation
        if (currentDay == 1) {
            initializeSimulation();
            currentDay++;
            return;
        }
        
        // Calculate dynamic exchange ratio
        double dynamicExchangeRatio;
        try {
            // Handle potential division by zero or negative values
            if (blueStrength <= 0 || redStrength <= 0) {
                handleEndOfSimulation();
                return;
            }
            
            double numerator = Math.pow(blueStrength, blueExponent);
            double denominator = Math.pow(redStrength, redExponent);
            
            if (denominator == 0) {
                // Handle division by zero
                dynamicExchangeRatio = exchangeRatio * 10; // Arbitrary large value
            } else {
                dynamicExchangeRatio = exchangeRatio * (numerator / denominator);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating exchange ratio: " + e.getMessage(), 
                                         "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate reinforcements for this day
        double blueReinforcement = blueReinforcementDays.contains(currentDay) ? BLUE_REINFORCEMENT_STRENGTH : 0;
        double redReinforcement = redReinforcementDays.contains(currentDay) ? RED_REINFORCEMENT_STRENGTH : 0;
        
        // Calculate new blue strength
        double newBlueStrength;
        try {
            if (dynamicExchangeRatio == 0) {
                // If exchange ratio is zero, blue takes maximum casualties
                newBlueStrength = 0;
            } else {
                newBlueStrength = blueStrength - (blueAttrition / dynamicExchangeRatio) + blueReinforcement;
            }
            
            // Ensure strength doesn't go negative
            newBlueStrength = Math.max(0, newBlueStrength);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating blue strength: " + e.getMessage(), 
                                         "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate new red strength
        double newRedStrength = redStrength * (1 - redAttrition) + redReinforcement;
        newRedStrength = Math.max(0, newRedStrength); // Ensure strength doesn't go negative
        
        // Calculate new blue attrition
        double newBlueAttrition;
        try {
            double beta = blueStrength; // Previous day's blue strength
            double term = newBlueStrength - blueReinforcement;
            
            if (beta == 0) {
                // Handle division by zero
                newBlueAttrition = 1.0; // Maximum attrition
            } else {
                newBlueAttrition = (beta - term) / beta;
            }
            
            // Ensure attrition is between 0 and 1
            newBlueAttrition = Math.max(0, Math.min(1, newBlueAttrition));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating blue attrition: " + e.getMessage(), 
                                         "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate new withdrawal
        double newWithdrawal;
        if (newBlueAttrition <= blueThreshold) {
            newWithdrawal = withdrawal; // No change
        } else {
            double term = (maxWithdrawal - withdrawal) / (1 - blueThreshold);
            newWithdrawal = withdrawal + term * (newBlueAttrition - blueThreshold);
            // Ensure withdrawal is between 0 and maxWithdrawal
            newWithdrawal = Math.max(0, Math.min(maxWithdrawal, newWithdrawal));
        }
        
        // Calculate new ground prosecution rate
        double newGroundProsecution;
        try {
            if (redAttrition > redThreshold) {
                double term = (redThreshold - groundProsecution) / redThreshold;
                newGroundProsecution = groundProsecution - term * (redAttrition - redThreshold);
            } else {
                newGroundProsecution = groundProsecution; // No change
            }
            
            // Ensure ground prosecution is between 0 and 1
            newGroundProsecution = Math.max(0, Math.min(1, newGroundProsecution));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating ground prosecution: " + e.getMessage(), 
                                         "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Calculate new red attrition
        double newRedAttrition = newGroundProsecution * (1 - (newWithdrawal / maxWithdrawal));
        newRedAttrition = Math.max(0, Math.min(1, newRedAttrition)); // Ensure attrition is between 0 and 1
        
        // Update values for next day
        blueStrength = newBlueStrength;
        redStrength = newRedStrength;
        exchangeRatio = dynamicExchangeRatio;
        blueAttrition = newBlueAttrition;
        redAttrition = newRedAttrition;
        withdrawal = newWithdrawal;
        groundProsecution = newGroundProsecution;
        
        // Add data points
        blueDataPoints.add(new Point2D(blueStrength, currentDay));
        redDataPoints.add(new Point2D(redStrength, currentDay));
        
        // Display results
        outputArea.append("\nDay " + currentDay + ":\n");
        outputArea.append(String.format("Blue Strength: %.2f" + (blueReinforcement > 0 ? " (+%.2f reinforcement)" : "") + "\n", 
                                      blueStrength, blueReinforcement));
        outputArea.append(String.format("Red Strength: %.2f" + (redReinforcement > 0 ? " (+%.2f reinforcement)" : "") + "\n", 
                                      redStrength, redReinforcement));
        outputArea.append(String.format("Exchange Ratio: %.4f\n", exchangeRatio));
        outputArea.append(String.format("Withdrawal: %.4f\n", withdrawal));
        outputArea.append(String.format("Blue Attrition: %.4f\n", blueAttrition));
        outputArea.append(String.format("Red Attrition: %.4f\n", redAttrition));
        outputArea.append(String.format("Ground Prosecution Rate: %.4f\n", groundProsecution));
        
        // Update chart
        chartPanel.updateData(blueDataPoints, redDataPoints);
        
        // Check if simulation should end
        if (blueStrength <= 0 || withdrawal >= maxWithdrawal || redStrength <= 0) {
            handleEndOfSimulation();
        } else {
            currentDay++;
        }
    }
    
    private void handleEndOfSimulation() {
        simulationEnded = true;
        String winner;
        String reason;
        
        if (blueStrength <= 0) {
            winner = "Red";
            reason = "Blue army was completely defeated";
        } else if (redStrength <= 0) {
            winner = "Blue";
            reason = "Red army was completely defeated";
        } else if (withdrawal >= maxWithdrawal) {
            winner = "Red";
            reason = "Blue army reached maximum withdrawal percentage";
        } else {
            winner = "Undetermined";
            reason = "Simulation ended due to calculation error";
        }
        
        statusLabel.setText(String.format("Simulation ended on Day %d. %s wins! (%s)", 
                                        currentDay, winner, reason));
        
        outputArea.append("\n" + statusLabel.getText() + "\n");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WarSimulation().setVisible(true);
            }
        });
    }
    
    // Inner class for chart panel
    private class ChartPanel extends JPanel {
        private List<Point2D> bluePoints = new ArrayList<>();
        private List<Point2D> redPoints = new ArrayList<>();
        private int padding = 60;
        private int labelPadding = 30;
        private Font labelFont = new Font("Arial", Font.BOLD, 12);
        private Font titleFont = new Font("Arial", Font.BOLD, 16);
        private Font tickFont = new Font("Arial", Font.PLAIN, 10);
        private Color gridColor = new Color(200, 200, 200);
        private Color blueLineColor = new Color(0, 0, 255);
        private Color redLineColor = new Color(255, 0, 0);
        private Stroke dataStroke = new BasicStroke(2f);
        private Stroke gridStroke = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1f, new float[]{5f}, 0f);
        private Stroke axisStroke = new BasicStroke(2f);
        
        public ChartPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        }
        
        public void updateData(List<Point2D> blueData, List<Point2D> redData) {
            this.bluePoints = new ArrayList<>(blueData);
            this.redPoints = new ArrayList<>(redData);
            repaint();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            // Draw white background
            g2.setColor(Color.WHITE);
            g2.fillRect(padding, padding, getWidth() - 2 * padding, getHeight() - 2 * padding);
            
            // Draw title
            g2.setFont(titleFont);
            g2.setColor(Color.BLACK);
            String title = "Army Strength Over Time";
            FontMetrics metrics = g2.getFontMetrics();
            int titleWidth = metrics.stringWidth(title);
            g2.drawString(title, (getWidth() - titleWidth) / 2, 25);
            
            // Check if we have data
            if (bluePoints.isEmpty() && redPoints.isEmpty()) {
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                String msg = "No data available - Click 'Next Day' to start simulation";
                int msgWidth = g2.getFontMetrics().stringWidth(msg);
                g2.drawString(msg, (getWidth() - msgWidth) / 2, getHeight() / 2);
                return;
            }
            
            // Find min and max values
            double minX = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double minY = Double.MAX_VALUE;
            double maxY = Double.MIN_VALUE;
            
            for (Point2D point : bluePoints) {
                minX = Math.min(minX, point.x);
                maxX = Math.max(maxX, point.x);
                minY = Math.min(minY, point.y);
                maxY = Math.max(maxY, point.y);
            }
            
            for (Point2D point : redPoints) {
                minX = Math.min(minX, point.x);
                maxX = Math.max(maxX, point.x);
                minY = Math.min(minY, point.y);
                maxY = Math.max(maxY, point.y);
            }
            
            // Add some margin
            minX = Math.max(0, minX * 0.9);
            maxX = maxX * 1.1;
            minY = Math.max(0, minY * 0.9);
            maxY = maxY * 1.1;
            
            // Draw grid
            g2.setColor(gridColor);
            g2.setStroke(gridStroke);
            int numXDivisions = 10;
            int numYDivisions = 10;
            
            // Draw horizontal grid lines
            for (int i = 0; i <= numYDivisions; i++) {
                int y = getHeight() - padding - i * (getHeight() - 2 * padding) / numYDivisions;
                g2.drawLine(padding, y, getWidth() - padding, y);
            }
            
            // Draw vertical grid lines
            for (int i = 0; i <= numXDivisions; i++) {
                int x = padding + i * (getWidth() - 2 * padding) / numXDivisions;
                g2.drawLine(x, padding, x, getHeight() - padding);
            }
            
            // Draw axis
            g2.setColor(Color.BLACK);
            g2.setStroke(axisStroke);
            g2.drawLine(padding, getHeight() - padding, getWidth() - padding, getHeight() - padding); // x-axis
            g2.drawLine(padding, getHeight() - padding, padding, padding); // y-axis
            
            // Draw axis labels
            g2.setFont(labelFont);
            
            // X-axis label
            String xLabel = "Strength";
            metrics = g2.getFontMetrics();
            int xLabelWidth = metrics.stringWidth(xLabel);
            g2.drawString(xLabel, (getWidth() - xLabelWidth) / 2, getHeight() - padding / 3);
            
            // Y-axis label
            String yLabel = "Day";
            g2.rotate(-Math.PI / 2, padding / 3, getHeight() / 2);
            g2.drawString(yLabel, padding / 3, getHeight() / 2);
            g2.rotate(Math.PI / 2, padding / 3, getHeight() / 2);
            
            // Draw scale
            g2.setFont(tickFont);
            int hatchMarkSize = 5;
            
            // X-axis scale
            for (int i = 0; i <= numXDivisions; i++) {
                int x = padding + i * (getWidth() - 2 * padding) / numXDivisions;
                int y = getHeight() - padding;
                g2.drawLine(x, y, x, y + hatchMarkSize);
                double value = minX + (maxX - minX) * i / numXDivisions;
                String label = String.format("%.0f", value);
                int labelWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x - labelWidth / 2, y + labelPadding);
            }
            
            // Y-axis scale
            for (int i = 0; i <= numYDivisions; i++) {
                int x = padding;
                int y = getHeight() - padding - i * (getHeight() - 2 * padding) / numYDivisions;
                g2.drawLine(x, y, x - hatchMarkSize, y);
                double value = minY + (maxY - minY) * i / numYDivisions;
                String label = String.format("%.0f", value);
                int labelWidth = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, x - labelWidth - 5, y + 4);
            }
            
            // Draw data points with thicker lines
            g2.setStroke(dataStroke);
            
            // Draw data points - Blue
            drawPoints(g2, bluePoints, blueLineColor, minX, maxX, minY, maxY);
            
            // Draw data points - Red
            drawPoints(g2, redPoints, redLineColor, minX, maxX, minY, maxY);
            
            // Draw legend with border
            int legendX = getWidth() - 150;
            int legendY = 50;
            int legendWidth = 130;
            int legendHeight = 70;
            
            g2.setColor(new Color(255, 255, 255, 220)); // Semi-transparent white
            g2.fillRect(legendX, legendY, legendWidth, legendHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(legendX, legendY, legendWidth, legendHeight);
            
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString("Legend", legendX + 10, legendY + 20);
            
            // Blue army legend
            g2.setColor(blueLineColor);
            g2.fillRect(legendX + 10, legendY + 30, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawString("Blue Army", legendX + 35, legendY + 43);
            
            // Red army legend
            g2.setColor(redLineColor);
            g2.fillRect(legendX + 10, legendY + 50, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawString("Red Army", legendX + 35, legendY + 63);
        }
        
        private void drawPoints(Graphics2D g2, List<Point2D> points, Color color, 
                              double minX, double maxX, double minY, double maxY) {
            g2.setColor(color);
            int pointWidth = 6;
            
            // Draw lines between points
            if (points.size() > 1) {
                for (int i = 0; i < points.size() - 1; i++) {
                    int x1 = mapX(points.get(i).x, minX, maxX);
                    int y1 = mapY(points.get(i).y, minY, maxY);
                    int x2 = mapX(points.get(i + 1).x, minX, maxX);
                    int y2 = mapY(points.get(i + 1).y, minY, maxY);
                    g2.drawLine(x1, y1, x2, y2);
                }
            }
            
            // Draw points
            for (Point2D point : points) {
                int x = mapX(point.x, minX, maxX);
                int y = mapY(point.y, minY, maxY);
                g2.fillOval(x - pointWidth / 2, y - pointWidth / 2, pointWidth, pointWidth);
            }
        }
        
        private int mapX(double x, double minX, double maxX) {
            if (maxX - minX == 0) return padding;
            return (int) (padding + (x - minX) * (getWidth() - 2 * padding) / (maxX - minX));
        }
        
        private int mapY(double y, double minY, double maxY) {
            if (maxY - minY == 0) return getHeight() - padding;
            return (int) (getHeight() - padding - (y - minY) * (getHeight() - 2 * padding) / (maxY - minY));
        }
    }
    
    // Simple Point class for chart data
    private class Point2D {
        double x;
        double y;
        
        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}