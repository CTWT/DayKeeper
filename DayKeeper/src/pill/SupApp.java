package pill;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SupApp extends JFrame {
	private CardLayout cardLayout = new CardLayout();
	private JPanel mainPanel = new JPanel(cardLayout);

	private SupplementListPanel listPanel;
	private AddSupplementPanel addPanel;

	public SupApp() {
		setTitle("daykeeper");
		setSize(600, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		SupplementImagePanel imagePanel = new SupplementImagePanel();
		listPanel = new SupplementListPanel();
		addPanel = new AddSupplementPanel();

		mainPanel.add(imagePanel, "image");
		mainPanel.add(listPanel, "list");
		mainPanel.add(addPanel, "add");

		add(mainPanel);

		imagePanel.addImageClickListener(e -> cardLayout.show(mainPanel, "list"));
		listPanel.addAddButtonListener(e -> cardLayout.show(mainPanel, "add"));
		listPanel.addBackButtonListener(e -> cardLayout.show(mainPanel, "image"));
		addPanel.addRegisterListener(name -> {
			listPanel.addSupplement(name);
			cardLayout.show(mainPanel, "list");
		});

		cardLayout.show(mainPanel, "image");
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new SupApp());
	}
}

// 📌 처음 영양제 이미지 화면
class SupplementImagePanel extends JPanel {
	private JButton imageButton = new JButton("영양제 이미지");

	public SupplementImagePanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("daykeeper", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);

		imageButton.setPreferredSize(new Dimension(150, 150));
		imageButton.setBackground(Color.ORANGE);
		add(imageButton, BorderLayout.CENTER);
	}

	public void addImageClickListener(ActionListener listener) {
		imageButton.addActionListener(listener);
	}
}

// 📌 영양제 목록 화면
class SupplementListPanel extends JPanel {
	private JPanel supplementContainer = new JPanel(new FlowLayout());
	private JButton addButton = new JButton("영양제 추가하기");
	private JButton backButton = new JButton("뒤로가기");

	public SupplementListPanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("daykeeper", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);

		add(new JScrollPane(supplementContainer), BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.add(addButton);
		btnPanel.add(backButton);
		add(btnPanel, BorderLayout.SOUTH);
	}

	public void addSupplement(String name) {
		JLabel supLabel = new JLabel(name, SwingConstants.CENTER);
		supLabel.setOpaque(true);
		supLabel.setBackground(Color.ORANGE);
		supLabel.setPreferredSize(new Dimension(100, 100));
		supplementContainer.add(supLabel);
		revalidate();
		repaint();
	}

	public void addAddButtonListener(ActionListener listener) {
		addButton.addActionListener(listener);
	}

	public void addBackButtonListener(ActionListener listener) {
		backButton.addActionListener(listener);
	}
}

// 📌 영양제 추가 화면
class AddSupplementPanel extends JPanel {
	private JTextField nameField = new JTextField(20);
	private JButton registerButton = new JButton("등록하기");

	public AddSupplementPanel() {
		setLayout(new BorderLayout());
		JLabel title = new JLabel("daykeeper", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 20));
		add(title, BorderLayout.NORTH);

		JPanel center = new JPanel(new FlowLayout());
		center.add(new JLabel("영양제 이름:"));
		center.add(nameField);
		center.add(registerButton);
		add(center, BorderLayout.CENTER);
	}

	public void addRegisterListener(SupplementRegisterListener listener) {
		registerButton.addActionListener(e -> {
			String name = nameField.getText().trim();
			if (!name.isEmpty()) {
				listener.onRegister(name);
				nameField.setText("");
			} else {
				JOptionPane.showMessageDialog(this, "이름을 입력해주세요.");
			}
		});
	}

	interface SupplementRegisterListener {
		void onRegister(String name);
	}
}
