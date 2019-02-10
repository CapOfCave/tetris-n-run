package graphics;

import java.awt.CardLayout;
import java.awt.Toolkit;


import javax.swing.JFrame;

import input.KeyHandler;
import sound.SoundPlayer;

public class MenuFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private SoundPlayer soundPlayer;
	private MenuPanel mPanel;
	private OptionPanel oPanel;
	private TutorialPanel tPanel;
	private KeyHandler keyHandler;
	
	public MenuFrame() {
		mPanel = new MenuPanel(this);
		tPanel = new TutorialPanel(this);
		oPanel = new OptionPanel();
		keyHandler = new KeyHandler();


		soundPlayer = new SoundPlayer();

		
		setLayout(new CardLayout());
		add(mPanel);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/gegner.png")));
		addKeyListener(keyHandler);
		setResizable(false);
		setDefaultCloseOperation(0);
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);


	}

	public void startMenu() {
		add(mPanel);
		remove(tPanel);
		remove(oPanel);
		
	}
	public void startTutorial() {
		add(tPanel);
		remove(mPanel);
		remove(oPanel);
		
	}

}
