
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NetworkClientGUI extends JFrame {

    private JTextField hostField;
    private JTextField portField;
    private JTextField messageField;

    private JComboBox<String> protocolBox;

    private JButton connectButton;
    private JButton disconnectButton;
    private JButton sendButton;
    private JButton clearButton;

    private JTextArea logArea;
    private JLabel statusLabel;

    private boolean connected = false;

    public NetworkClientGUI() {

        setTitle("Network Client Interface");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        setLayout(new BorderLayout(10, 10));

        // Top Panel: Connection Settings
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(
            BorderFactory.createTitledBorder("Connection Settings")
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        hostField = new JTextField("localhost", 15);
        portField = new JTextField("8080", 8);

        String[] protocols = {"TCP", "UDP"};
        protocolBox = new JComboBox<>(protocols);

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Host:"), gbc);

        gbc.gridx = 1;
        topPanel.add(hostField, gbc);

        gbc.gridx = 2;
        topPanel.add(new JLabel("Port:"), gbc);

        gbc.gridx = 3;
        topPanel.add(portField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new JLabel("Protocol:"), gbc);

        gbc.gridx = 1;
        topPanel.add(protocolBox, gbc);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Message Log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logArea);

        scrollPane.setBorder(
            BorderFactory.createTitledBorder("Messages / Logs")
        );

        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));

        messageField = new JTextField();
        messageField.setToolTipText("Enter your message here");

        sendButton = new JButton("Send");

        messagePanel.add(new JLabel("Message:"), BorderLayout.WEST);
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        clearButton = new JButton("Clear");

        buttonPanel.add(connectButton);
        buttonPanel.add(disconnectButton);
        buttonPanel.add(clearButton);

        statusLabel = new JLabel("Status: Disconnected");

        bottomPanel.add(messagePanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Event Listeners

        connectButton.addActionListener(e -> connect());

        disconnectButton.addActionListener(e -> disconnect());

        sendButton.addActionListener(e -> sendMessage());

        clearButton.addActionListener(e -> {
            logArea.setText("");
            log("Log cleared.");
        });

        log("Network Client started.");
        log("Status: Disconnected");

        setVisible(true);
    }

    // Host Validation
    private boolean isValidHost(String host) {

        if (host == null || host.isEmpty()
                || host.length() > 253
                || host.contains(" ")) {
            return false;
        }

        if (host.equalsIgnoreCase("localhost")) {
            return true;
        }

        String[] labels = host.split("\\.", -1);

        for (String label : labels) {
            if (label.isEmpty()
                    || label.length() > 63
                    || !label.matches(
                        "[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?"
                    )) {
                return false;
            }
        }

        return true;
    }

    // Connect Method
    private void connect() {

        if (connected) {
            log("Already connected!");
            return;
        }

        String host = hostField.getText().trim();
        String portText = portField.getText().trim();

        if (!isValidHost(host)) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid hostname or IPv4 address.",
                "Invalid Host",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int port;

        try {
            port = Integer.parseInt(portText);

            if (port < 1 || port > 65535) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Port must be a number between 1 and 65535.",
                "Invalid Port",
                JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String protocol = (String) protocolBox.getSelectedItem();

        connected = true;

        statusLabel.setText("Status: Connected");

        log("Connecting to " + host + ":" + port);
        log("Protocol: " + protocol);
        log("Connection established successfully!");
        log("Status: Connected");
    }

    // Disconnect Method
    private void disconnect() {

        if (!connected) {
            log("Already disconnected!");
            return;
        }

        connected = false;

        statusLabel.setText("Status: Disconnected");

        log("Connection closed.");
        log("Status: Disconnected");
    }

    // Send Message Method
    private void sendMessage() {

        if (!connected) {
            log("Error: Connect before sending a message.");
            return;
        }

        String message = messageField.getText().trim();

        if (message.isEmpty()) {
            log("Error: Message cannot be empty.");
            return;
        }

        log("Client: " + message);

        messageField.setText("");
    }

    // Log Method
    private void log(String message) {

        logArea.append(message + "\n");

        logArea.setCaretPosition(
            logArea.getDocument().getLength()
        );
    }

    // Main Method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new NetworkClientGUI();
        });
    }
}
