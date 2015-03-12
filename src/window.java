import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class window
{
	private Pinger ping = new Pinger();

	public BufferedImage red = null;
	public BufferedImage green = null;
	public BufferedImage yellow = null;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					window window = new window();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public window()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		// get the SystemTray instance
		SystemTray tray = SystemTray.getSystemTray();

		// load an image
		Image image = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + File.separator + "led-red.png");
		// create a action listener to listen for default action executed on the tray icon
		ActionListener listener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// execute default action of the application
				// ...
			}
		};
		// create a popup menu
		PopupMenu popup = new PopupMenu();
		// create menu item for the default action
		MenuItem defaultItem = new MenuItem("Test");
		defaultItem.addActionListener(listener);
		popup.add(defaultItem);
		/// ... add other items
		// construct a TrayIcon
		final TrayIcon trayIcon = new TrayIcon(image, "Tray Demo", popup);
		trayIcon.setImageAutoSize(true);
		// set the TrayIcon properties
		trayIcon.addActionListener(listener);
		// ...
		// add the tray image
		try
		{
			tray.add(trayIcon);
		}
		catch (AWTException e)
		{
			System.err.println(e);
		}

		frame = new JFrame();
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				ping.close();
				System.exit(0);
			}
		});

		frame.setBounds(100, 100, 159, 209);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		try
		{
			red = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "led-red.png"));
			green = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "led-green.png"));
			yellow = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "led-yellow.png"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final JLabel picLabel = new JLabel(new ImageIcon(red));
		picLabel.setBounds(0, 0, 150, 150);
		frame.getContentPane().add(picLabel);

		ping.start();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 461, 354, 132);
		frame.getContentPane().add(scrollPane);

		final JLabel lblPing = new JLabel("ping");
		lblPing.setBounds(10, 161, 46, 14);
		frame.getContentPane().add(lblPing);

		JTextArea ConsoleOut = new JTextArea();
		ConsoleOut.setEditable(false);
		scrollPane.setViewportView(ConsoleOut);
		ConsoleOut.getDocument().addDocumentListener(new DocumentListener()
		{

			@Override
			public void changedUpdate(DocumentEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void insertUpdate(DocumentEvent arg0)
			{

				if (ping.getPing() > 100)
				{
					picLabel.setIcon(new ImageIcon(red));
					trayIcon.setImage(red);
				}
				else if (ping.getPing() > 60)
				{
					picLabel.setIcon(new ImageIcon(yellow));
					trayIcon.setImage(yellow);
				}
				else if (ping.getPing() > 0)
				{
					picLabel.setIcon(new ImageIcon(green));
					trayIcon.setImage(green);
				}
				lblPing.setText(ping.getPing() + " ms");
				trayIcon.setToolTip(ping.getPing() + "");

			}

			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				// TODO Auto-generated method stub

			}
		});

		PrintStream printStream = new PrintStream(new TextAreaOutputStream(ConsoleOut));

		System.setOut(printStream);
		System.setErr(printStream);

	}
}
