
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentExpenseManager extends JFrame {

    private final String[] days = {
            "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"
    };

    private final String[] descriptions = new String[100];
    private final String[] expenseDays = new String[100];
    private final double[] amounts = new double[100];

    private int count = 0;

    private JComboBox<String> dayBox;
    private JTextField descriptionField;
    private JTextField amountField;

    private DefaultTableModel tableModel;
    private JTable expenseTable;

    private JLabel totalLabel;
    private JLabel averageLabel;
    private JLabel highestLabel;

    public StudentExpenseManager() {
        setTitle("Student Expense Manager");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel title = new JLabel(
                "STUDENT EXPENSE MANAGER",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitle = new JLabel(
                "Track your spending and manage your money",
                SwingConstants.CENTER
        );

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.add(title);
        header.add(subtitle);

        mainPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 8));

        formPanel.add(new JLabel("Day"));
        formPanel.add(new JLabel("Description"));
        formPanel.add(new JLabel("Amount (GH₵)"));
        formPanel.add(new JLabel("Action"));

        dayBox = new JComboBox<>(days);
        descriptionField = new JTextField();
        amountField = new JTextField();

        JButton addButton = new JButton("Add Expense");

        formPanel.add(dayBox);
        formPanel.add(descriptionField);
        formPanel.add(amountField);

        formPanel.add(addButton);

        String[] columns = {"Day", "Description", "Amount (GH₵)"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);

        JButton deleteButton = new JButton("Delete Selected");

        JButton summaryButton = new JButton("Refresh Summary");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(summaryButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        totalLabel = new JLabel("Total: GH₵0.00");

        averageLabel = new JLabel("Average: GH₵0.00");
        highestLabel = new JLabel("Highest expense: GH₵0.00");

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        summaryPanel.add(totalLabel);
        summaryPanel.add(averageLabel);
        summaryPanel.add(highestLabel);

        mainPanel.add(summaryPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        summaryButton.addActionListener(e -> updateSummary());

        add(mainPanel);

    }

    private void addExpense() {
        if (count >= amounts.length) {
            JOptionPane.showMessageDialog(
                    this, "Expense limit reached."
            );
            return;
        }

        String description = descriptionField.getText().trim();
        String amountText = amountField.getText().trim();

        if (description.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a description and amount."
            );

            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            if (!Double.isFinite(amount) || amount < 0) {
                JOptionPane.showMessageDialog(
                        this, "Enter a valid, non-negative amount."
                );
                return;
            }

            String day = (String) dayBox.getSelectedItem();

            expenseDays[count] = day;
            descriptions[count] = description;
            amounts[count] = amount;

            tableModel.addRow(new Object[]{
                    day, description, String.format("%.2f", amount)
            });

            count++;

            descriptionField.setText("");
            amountField.setText("");

            updateSummary();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Please enter a valid number for the amount."
            );
        }
    }

    private void deleteExpense() {

        int selectedRow = expenseTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an expense to delete."
            );
            return;
        }

        for (int i = selectedRow; i < count - 1; i++) {
            expenseDays[i] = expenseDays[i + 1];
            descriptions[i] = descriptions[i + 1];
            amounts[i] = amounts[i + 1];
        }

        count--;

        expenseDays[count] = null;
        descriptions[count] = null;

        amounts[count] = 0;

        tableModel.removeRow(selectedRow);

        updateSummary();
    }

    private void updateSummary() {
        double total = 0;
        double highest = 0;

        for (int i = 0; i < count; i++) {
            total += amounts[i];

            if (i == 0 || amounts[i] > highest) {
                highest = amounts[i];
            }
        }

        double average = count > 0 ? total / count : 0;


        totalLabel.setText(String.format("Total: GH₵%.2f", total));
        averageLabel.setText(String.format("Average: GH₵%.2f", average));
        highestLabel.setText(
                String.format("Highest expense: GH₵%.2f", highest)
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentExpenseManager app = new StudentExpenseManager();
            app.setVisible(true);
        });
    }
}

