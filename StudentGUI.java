import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentGUI extends JFrame {

    // Reuses the exact same logic as the console menu
    private final StudentManager manager = new StudentManager();

    // Input fields
    private final JTextField idField = new JTextField(12);
    private final JTextField nameField = new JTextField(12);
    private final JTextField gradeField = new JTextField(12);

    // Table model with editable cells disabled
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Grade"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel statusLabel = new JLabel("Ready.");

    // Constructor: builds the window
    public StudentGUI() {
        super("Student Manager GUI");
        buildUI();
    }

    // Lays out the whole frame
    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildInputPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        table.setAutoCreateRowSorter(true); // click a column header to re-sort

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);
        setLocationRelativeTo(null); // center on screen
    }

    // Top panel: ID, Name, Grade fields
    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("ID:"), c);
        c.gridx = 1;
        panel.add(idField, c);
        c.gridx = 2;
        panel.add(new JLabel("Name:"), c);
        c.gridx = 3;
        panel.add(nameField, c);
        c.gridx = 4;
        panel.add(new JLabel("Grade:"), c);
        c.gridx = 5;
        panel.add(gradeField, c);

        return panel;
    }

    // Bottom panel: buttons + status label
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        buttons.add(makeButton("Add Student", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        }));
        buttons.add(makeButton("Find by ID", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findStudent();
            }
        }));
        buttons.add(makeButton("Update Grade", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGrade();
            }
        }));
        buttons.add(makeButton("Remove", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStudent();
            }
        }));
        buttons.add(makeButton("Sort A-Z", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.sortByName();
                refreshTable();
                statusLabel.setText("Students sorted by name (A to Z).");
            }
        }));
        buttons.add(makeButton("Sort by Grade", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.sortByGrade();
                refreshTable();
                statusLabel.setText("Students sorted by grade (highest to lowest).");
            }
        }));
        buttons.add(makeButton("Clear", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        }));

        panel.add(buttons, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Small helper to create buttons
    private JButton makeButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        return button;
    }

    // 1. Add a student (validates input first)
    private void addStudent() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String gradeText = gradeField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || gradeText.isEmpty()) {
            showError("Please fill in ID, Name, and Grade.");
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeText);
        } catch (NumberFormatException ex) {
            showError("Grade must be a number.");
            return;
        }

        if (manager.findById(id) != null) {
            showError("Student ID " + id + " already exists.");
            return;
        }

        manager.addStudent(new Student(id, name, grade));
        refreshTable();
        clearFields();
        statusLabel.setText("Student added successfully.");
    }

    // 2. Find by ID (fills the Name and Grade fields)
    private void findStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter an ID to find.");
            return;
        }

        Student found = manager.findById(id);
        if (found == null) {
            showError("Student with ID " + id + " not found.");
            return;
        }

        nameField.setText(found.getName());
        gradeField.setText(String.valueOf(found.getGrade()));
        statusLabel.setText("Found: " + found);
    }

    // 3. Update the grade for the ID in the ID field
    private void updateGrade() {
        String id = idField.getText().trim();
        String gradeText = gradeField.getText().trim();

        if (id.isEmpty() || gradeText.isEmpty()) {
            showError("Please enter an ID and a new Grade.");
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeText);
        } catch (NumberFormatException ex) {
            showError("Grade must be a number.");
            return;
        }

        if (manager.findById(id) == null) {
            showError("Student with ID " + id + " not found.");
            return;
        }

        manager.updateGrade(id, grade);
        refreshTable();
        statusLabel.setText("Grade updated successfully.");
    }

    // 4. Remove the student whose ID is in the ID field
    private void removeStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter an ID to remove.");
            return;
        }

        if (manager.findById(id) == null) {
            showError("Student with ID " + id + " not found.");
            return;
        }

        manager.removeById(id);
        refreshTable();
        clearFields();
        statusLabel.setText("Student removed successfully.");
    }

    // Refills the table from the manager's list
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : manager.getStudents()) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getGrade()});
        }
    }

    // Clears the three input fields
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        gradeField.setText("");
    }

    // Shows an error dialog and mirrors it in the status bar
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText(message);
    }

    // Launches the GUI on the Event Dispatch Thread
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StudentGUI gui = new StudentGUI();
                gui.setVisible(true);
            }
        });
    }
}
