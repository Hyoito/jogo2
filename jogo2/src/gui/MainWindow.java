package gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import models.ChosenWord;
import persistence.ChosenWordRepository;
import persistence.ChosenWordRepositorySQLite;

public class MainWindow implements ActionListener {

	private JFrame gameFrame;
	private WordPanel[] wordPanelArray = new WordPanel[6];
	private UserPanel userPanel;
	private String wordString;
	private int count = 0;
	private JTextField wordTF;
	private JButton btnOK;
	private ChosenWordRepositorySQLite repository;
	
	
	// cria a tela do game onde cria 7 fileras em 1 coluna onde 6 seram ocupadas pelas fileiras e 1 pelo tf e o botao
	public MainWindow() {
		repository = new ChosenWordRepositorySQLite();
		
		gameFrame = new JFrame("Game");
		gameFrame.setSize(300, 300);
		gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameFrame.setLayout(new GridLayout(7, 1));
		gameFrame.setVisible(true);
		gameFrame.setLocationRelativeTo(null);
		
		
		//cria os paneis corforme o tanto de indices
		for (int i = 0; i < 6; i++) {
			wordPanelArray[i] = new WordPanel();
			gameFrame.add(wordPanelArray[i]);
		}
		userPanel = new UserPanel();
		userPanel.getBtnOK().addActionListener(this);
		gameFrame.add(userPanel);
		gameFrame.revalidate();//reposiciona os componentes filhos no caso WordPanel e UserPanel
		
		//armazena no wordString a palavra q sorteamos	
//		wordString = getWordString();
		wordString = getWordDB();
		System.out.println("A palavra sorteada é : " + wordString);
	}
	//
	class WordPanel extends JPanel {
		//cria uma label para cada coluna da row e adiciona no wordPanel
		JLabel[] row = new JLabel[5];

		public WordPanel() {
			this.setLayout(new GridLayout());
			Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY); //crias bordas nos label, sem eles fica so uma uma tela toda cinza
			for (int i = 0; i < 5; i++) {
				row[i] = new JLabel();
				row[i].setHorizontalAlignment(JLabel.CENTER);
				row[i].setOpaque(true);//para poder ver as cores quando mudar
				row[i].setBorder(border);
				this.add(row[i]);
			}
		}

		public void setPanelText(String charValue, int position, Color color) {
			this.row[position].setText(charValue);
			this.row[position].setBackground(color);
		}
	}
	
	class UserPanel extends JPanel {

		public UserPanel() {
			this.setLayout(new GridLayout(1, 2));
			wordTF = new JTextField();
			this.add(wordTF);
			btnOK = new JButton("OK");
			this.add(btnOK);
		}

		public JTextField getWordTF() {
			return wordTF;
		}

		public JButton getBtnOK() {
			return btnOK;
		}

	}

// pega o texto q digitado pelo usuario 
	@Override
	public void actionPerformed(ActionEvent e) {
		String userWord = this.userPanel.getWordTF().getText();
//verifica se a palavra digitada tem 5 letras onde se tiver menos de 5 apenas retorna e se não ele faz a checagem da letras onde se todas tiverem certar no caso verdes ele da como vitoria
		if (userWord.length() < 5) {
			return;
		}else {
			if (isWordleWordEqualTo(userWord.trim().toUpperCase())) {
				JOptionPane.showMessageDialog(null, "Vitoria!!!", "", JOptionPane.INFORMATION_MESSAGE);
				gameFrame.dispose();
				return;
			}
		}
		// usado pra avançar no index das rows se n as palavras ficam se sobreescrevendo na mesma row
		count++;
	}

	//informa em qual fileira ta
	public WordPanel getActivePanel() {
		return this.wordPanelArray[count];
	}
	
	
	// checa as letras e coloca a cor conforme a palavra selecionada onde para palavras na posiçao certa coloca verde e amarelo para letras que estam na palavra so que em outra posição nas posiçoes erradas
	private boolean isWordleWordEqualTo(String userWord) {
		String userWord1 = this.userPanel.getWordTF().getText();
		List<String> wordleWordsList = Arrays.asList(wordString.split(""));
		String[] userWordsArray = userWord1.split("");
		List<Boolean> wordMatchesList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			if (wordleWordsList.contains(userWordsArray[i])) {
				if (wordleWordsList.get(i).equals(userWordsArray[i])) {
					getActivePanel().setPanelText(userWordsArray[i], i, Color.GREEN);
					wordMatchesList.add(true);
				} else {
					getActivePanel().setPanelText(userWordsArray[i], i, Color.YELLOW);
					wordMatchesList.add(false);
				}
			} else {
				getActivePanel().setPanelText(userWordsArray[i], i, Color.GRAY);
				wordMatchesList.add(false);
			}
		}
		return !wordMatchesList.contains(false);
	}


//vai na pasta e  cria uma lista com as palavras q estao la
	public String getWordString() {
		Path path = Paths.get("words/Words.txt");
		List<String> wordList = new ArrayList<>();
		try {
			wordList = Files.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sortea um numero q estege de acordo com o tamanho da lista e o wordlist armazena a palavra sorteada nela é retirada os espaços caso tenha e transforma 
		//tudo em letra maiuscula pois como a gente transformou anteriormente as letras q são impresas no label para maiusculo estas tbm devem ser maiusculas
		Random random = new Random();
		int position = random.nextInt(wordList.size());
		return wordList.get(position).trim().toUpperCase();
	}
	
//Codigo que era pra chamar uma palavra do DB	 
	public String getWordDB() {
		Random random = new Random();
		int num = random.nextInt(10)+1;
		ChosenWord word = repository.findBy(num)[0];
		return word.toString();
	}
//	
	public static void main(String[] args) {
		new MainWindow();
	}
}