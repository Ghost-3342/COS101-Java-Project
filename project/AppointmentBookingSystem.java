import javax.swing.*;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentBookingSystem {
    private JFrame frame;
    private JTextField fullNameField;
    private JTextField bookingNameField;
    private JTextField emailField;
    private JPanel inputPanel;
    private JCalendar calendar;
    private JRadioButton cashRadioButton;
    private JRadioButton onlineRadioButton;
    private JTextField cardNameField;
    private JTextField cardNumberField;
    private JTextField expirationDateField;
    private JTextField cvvField;
    private JButton bookButton;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AppointmentBookingSystem() {
        frame = new JFrame("Appointment Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500); // Windowed mode
        frame.setLayout(new BorderLayout());

        // Left Panel for Input Fields
        inputPanel = new JPanel(new GridLayout(10, 2, 10, 10)); // Rows, Columns, Horizontal Gap, Vertical Gap
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add some padding

        inputPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField(20);
        inputPanel.add(fullNameField);

        inputPanel.add(new JLabel("Name for Booking:"));
        bookingNameField = new JTextField(20);
        inputPanel.add(bookingNameField);

        inputPanel.add(new JLabel("Email Address:"));
        emailField = new JTextField(20);
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Payment Method:"));
        cashRadioButton = new JRadioButton("Cash");
        onlineRadioButton = new JRadioButton("Online");
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cashRadioButton);
        paymentGroup.add(onlineRadioButton);
        JPanel paymentMethodPanel = new JPanel();
        paymentMethodPanel.add(cashRadioButton);
        paymentMethodPanel.add(onlineRadioButton);
        inputPanel.add(paymentMethodPanel);

        inputPanel.add(new JLabel("Name on Card:"));
        cardNameField = new JTextField(20);
        inputPanel.add(cardNameField);

        inputPanel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField(20);
        inputPanel.add(cardNumberField);

        inputPanel.add(new JLabel("Expiration Date (MM/YY):"));
        expirationDateField = new JTextField(7);
        inputPanel.add(expirationDateField);

        inputPanel.add(new JLabel("CVV/CVC Number:"));
        cvvField = new JTextField(4);
        inputPanel.add(cvvField);

        bookButton = new JButton("Book");
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });

        // Right Panel for Calendar
        JPanel rightPanel = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        JScrollPane calendarScrollPane = new JScrollPane(calendar);
        calendarScrollPane.setPreferredSize(new Dimension(300, 300)); // Set the initial size
        calendarScrollPane.setMinimumSize(new Dimension(200, 200)); // Set the minimum size
        rightPanel.add(calendarScrollPane, BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(bookButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void bookAppointment() {
        String fullName = fullNameField.getText().trim();
        String bookingName = bookingNameField.getText().trim();
        String email = emailField.getText().trim();
        Date selectedDate = calendar.getDate();
        String paymentMethod = cashRadioButton.isSelected() ? "Cash" : "Online";
        String cardName = cardNameField.getText().trim();
        String cardNumber = cardNumberField.getText().trim();
        String expirationDate = expirationDateField.getText().trim();
        String cvv = cvvField.getText().trim();

        // Validation and booking logic can be implemented here
        // For demonstration purposes, we'll just display the entered information
        String bookingInfo = "Full Name: " + fullName + "\n"
                + "Name for Booking: " + bookingName + "\n"
                + "Email Address: " + email + "\n"
                + "Selected Date: " + selectedDate + "\n"
                + "Payment Method: " + paymentMethod + "\n";

        if (paymentMethod.equals("Online")) {
            bookingInfo += "Name on Card: " + cardName + "\n"
                    + "Card Number: " + cardNumber + "\n"
                    + "Expiration Date: " + expirationDate + "\n"
                    + "CVV/CVC Number: " + cvv + "\n";
        }

        writeBookingToFile(bookingInfo);

        JOptionPane.showMessageDialog(frame, "Booking Information:\n" + bookingInfo);

        // Clear fields after booking
        fullNameField.setText("");
        bookingNameField.setText("");
        emailField.setText("");
        calendar.setCalendar(null);
        cashRadioButton.setSelected(true);
        cardNameField.setText("");
        cardNumberField.setText("");
        expirationDateField.setText("");
        cvvField.setText("");
    }

    private void writeBookingToFile(String bookingInfo) {
        try (FileWriter writer = new FileWriter("bookings.txt", true)) {
            writer.write("Booking Date: " + dateFormat.format(new Date()) + "\n");
            writer.write(bookingInfo);
            writer.write("------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AppointmentBookingSystem();
            }
        });
    }
}
