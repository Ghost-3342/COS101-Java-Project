import javax.swing.*;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
    private JButton viewAppointmentsButton;
    private JButton timeSlotButton;
    private JComboBox<String> timeSlotComboBox;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private HashMap<String, Boolean> timeSlots;

    public AppointmentBookingSystem() {
        frame = new JFrame("Appointment Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        inputPanel = new JPanel(new GridLayout(12, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fullNameField = new JTextField(20);
        bookingNameField = new JTextField(20);
        emailField = new JTextField(20);
        cashRadioButton = new JRadioButton("Cash");
        onlineRadioButton = new JRadioButton("Online");
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cashRadioButton);
        paymentGroup.add(onlineRadioButton);
        JPanel paymentMethodPanel = new JPanel();
        paymentMethodPanel.add(cashRadioButton);
        paymentMethodPanel.add(onlineRadioButton);
        cardNameField = new JTextField(20);
        cardNumberField = new JTextField(20);
        expirationDateField = new JTextField(7);
        cvvField = new JTextField(4);

        bookButton = new JButton("Book");
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });

        viewAppointmentsButton = new JButton("View Appointments");
        viewAppointmentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAdminAppointments();
            }
        });

        timeSlotButton = new JButton("Check Time Slots");
        timeSlotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkTimeSlots();
            }
        });

        timeSlotComboBox = new JComboBox<>();
        timeSlotComboBox.addItem("08:00 AM - 12:00 PM");
        timeSlotComboBox.addItem("02:00 PM - 06:00 PM");

        inputPanel.add(new JLabel("Full Name:"));
        inputPanel.add(fullNameField);
        inputPanel.add(new JLabel("Name for Booking:"));
        inputPanel.add(bookingNameField);
        inputPanel.add(new JLabel("Email Address:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Payment Method:"));
        inputPanel.add(paymentMethodPanel);
        inputPanel.add(new JLabel("Name on Card:"));
        inputPanel.add(cardNameField);
        inputPanel.add(new JLabel("Card Number:"));
        inputPanel.add(cardNumberField);
        inputPanel.add(new JLabel("Expiration Date (MM/YY):"));
        inputPanel.add(expirationDateField);
        inputPanel.add(new JLabel("CVV/CVC Number:"));
        inputPanel.add(cvvField);
        inputPanel.add(new JLabel("Select Time Slot:"));
        inputPanel.add(timeSlotComboBox);
        inputPanel.add(timeSlotButton);

        JPanel rightPanel = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        calendar.setPreferredSize(new Dimension(300, 300));
        rightPanel.add(calendar, BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(rightPanel, BorderLayout.EAST);
        frame.add(bookButton, BorderLayout.SOUTH);
        frame.add(viewAppointmentsButton, BorderLayout.NORTH);

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
        String selectedTimeSlot = (String) timeSlotComboBox.getSelectedItem();

        String bookingInfo = "Full Name: " + fullName + "\n"
                + "Name for Booking: " + bookingName + "\n"
                + "Email Address: " + email + "\n"
                + "Selected Date: " + selectedDate + "\n"
                + "Selected Time Slot: " + selectedTimeSlot + "\n"
                + "Payment Method: " + paymentMethod + "\n";

        if (paymentMethod.equals("Online")) {
            bookingInfo += "Name on Card: " + cardName + "\n"
                    + "Card Number: " + cardNumber + "\n"
                    + "Expiration Date: " + expirationDate + "\n"
                    + "CVV/CVC Number: " + cvv + "\n";
        }

        if (timeSlots.containsKey(selectedTimeSlot) && timeSlots.get(selectedTimeSlot)) {
            writeBookingToFile(bookingInfo);
            timeSlots.put(selectedTimeSlot, false);
            JOptionPane.showMessageDialog(frame, "Booking Information:\n" + bookingInfo);
            updateTimeSlotComboBox();
        } else {
            JOptionPane.showMessageDialog(frame, "Selected time slot is already booked. Please choose another.");
        }

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

    private void showAdminAppointments() {
        try {
            FileReader fileReader = new FileReader("bookings.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder appointments = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                appointments.append(line).append("\n");
            }

            bufferedReader.close();

            JTextArea textArea = new JTextArea(appointments.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JFrame adminAppointmentsFrame = new JFrame("Admin Appointments");
            adminAppointmentsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            adminAppointmentsFrame.setSize(600, 400);
            adminAppointmentsFrame.add(scrollPane);
            adminAppointmentsFrame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void checkTimeSlots() {
        timeSlots = new HashMap<>();
        String selectedDateStr = dateFormat.format(calendar.getDate());
        timeSlots.put("08:00 AM - 12:00 PM", true);
        timeSlots.put("02:00 PM - 06:00 PM", true);

        try {
            FileReader fileReader = new FileReader("bookings.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(selectedDateStr)) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String timeSlot = parts[1].trim();
                        timeSlots.put(timeSlot, false);
                    }
                }
            }

            bufferedReader.close();

            updateTimeSlotComboBox();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTimeSlotComboBox() {
        timeSlotComboBox.removeAllItems();

        for (String timeSlot : timeSlots.keySet()) {
            if (timeSlots.get(timeSlot)) {
                timeSlotComboBox.addItem(timeSlot);
            }
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
