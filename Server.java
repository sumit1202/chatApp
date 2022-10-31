// Chatting Application - Server end
// https://www.youtube.com/watch?v=st7qtH8ysCo&ab_channel=CodeforInterview

package ChatApp;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;

public class Server implements ActionListener {

    JTextField text;
    JPanel jp2;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;

    Server() {

        // Top Panel ---------------------------------------------------------
        f.setLayout(null);
        JPanel jp1 = new JPanel();
        jp1.setBackground(Color.BLACK);
        jp1.setBounds(0, 0, 450, 70);
        jp1.setLayout(null);
        f.add(jp1);

        JLabel name = new JLabel("Server");
        name.setBounds(10, 20, 100, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 20));
        jp1.add(name);

        JLabel status = new JLabel("online");
        status.setBounds(10, 40, 100, 20);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.PLAIN, 10));
        jp1.add(status);

        // Mid Panel ---------------------------------------------------------
        jp2 = new JPanel();
        jp2.setBackground(Color.WHITE);
        jp2.setBounds(5, 75, 440, 538);
      
        f.add(jp2);

        // Bottom left text field
        // ---------------------------------------------------------
        text = new JTextField();
        text.setBounds(2, 615, 360, 55);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 19));
        text.setBackground(Color.WHITE);
        f.add(text);

        // Bottom right send button
        // ---------------------------------------------------------
        JButton send = new JButton("Send");
        send.setBounds(362, 615, 84, 55);
        // send.setBackground(Color.BLACK);
        // send.setForeground(Color.WHITE);
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        send.addActionListener(this);

        f.add(send);

        // Full app frame ---------------------------------------------------------
        f.setSize(450, 700);
        f.setLocation(100, 70);
        f.getContentPane().setBackground(Color.WHITE);

        f.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        try {
            String out = text.getText();

            JPanel jp3 = formatLabel(out);

            jp2.setLayout(new BorderLayout());
            // for aligning message to right
            JPanel right = new JPanel(new BorderLayout());
            right.add(jp3, BorderLayout.LINE_END);
            // for aligning multiple messages to below vertically
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            jp2.add(vertical, BorderLayout.PAGE_START);

            dout.writeUTF(out);

            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();

        } catch (Exception ae) {
            ae.printStackTrace();
        }

    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html> <p style=\"width: 150 px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(Color.LIGHT_GRAY);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;

    }

    public static void main(String[] args) {

        new Server();

        try {

            try (ServerSocket skt = new ServerSocket(6001)) {
                while (true) {
                    Socket s = skt.accept();
                    DataInputStream din = new DataInputStream(s.getInputStream());
                    dout = new DataOutputStream(s.getOutputStream());

                    while (true) {
                        String msg = din.readUTF();
                        JPanel panel = formatLabel(msg);

                        JPanel left = new JPanel(new BorderLayout());
                        left.add(panel, BorderLayout.LINE_START);
                        vertical.add(left);
                        f.validate();

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
