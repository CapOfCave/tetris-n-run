package graphics;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

import input.MenuKeyHandler;
import loading.SettingSaver;
import loading.SettingsLoader;
import sound.SoundPlayer;

public class MenuFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private SoundPlayer soundPlayer;
	private MenuPanel mPanel;
	private OptionPanel oPanel;
	private TutorialPanel tPanel;
	private MenuKeyHandler keyHandler;
	private ArrayList<Integer> levelSolved;
	
	private int difficulty = 1; //0: eZ, 1: normal, 2: expert, 3: impossible
	
	public static void main(String[] args) {

		SettingsLoader loader = new SettingsLoader(
				System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\settings.txt");
		File savesFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\saves");

		if (!savesFile.exists()) {
			savesFile.mkdirs();
		}

		File settingsFile = new File(System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves\\settings.txt");
		
		if (!settingsFile.exists()) {
			ArrayList<Integer> blankLevels = new ArrayList<>();
			for(int i = 0; i < 4; i++) {
				blankLevels.add(0);
			}
			new MenuFrame(1, blankLevels);
		} else {
			loader.loadAll();

			new MenuFrame(loader.getKeyCodes(), loader.getDifficulty(), loader.getLevelSolved());
		}
	}
	
	public MenuFrame(int difficulty, ArrayList<Integer> levelSolved) {
		this.difficulty = difficulty;
		this.levelSolved = levelSolved;

		mPanel = new MenuPanel(this);
		tPanel = new TutorialPanel(this);
		oPanel = new OptionPanel(this);
		keyHandler = new MenuKeyHandler(oPanel);
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
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SettingSaver.saveSettings(oPanel.getKeyCodes(), difficulty, levelSolved, System.getenv("APPDATA") + "\\tetris-n-run\\levelSaves", "settings.txt");

			}
		});


	}
	
	public MenuFrame(ArrayList<Integer> keyCodes, int difficulty, ArrayList<Integer> levelSolved) {
		this(difficulty, levelSolved);
		oPanel.setKeyCodes(keyCodes);
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

	public void playSound(String sound, float volume) {
		soundPlayer.playSound(sound, volume);
		
	}

	public ArrayList<Integer> getKeyCodes() {
	
		return oPanel.getKeyCodes();
	}

	public void startOption() {
		add(oPanel);
		remove(mPanel);
		remove(tPanel);
		
	}

	public int getLevelSolved() {
		return levelSolved.get(difficulty);
	}

	public int getDifficulty() {
		return difficulty;
	}

}
