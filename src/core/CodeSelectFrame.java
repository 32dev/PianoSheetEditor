package core;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;

import piano.Piano;

public class CodeSelectFrame extends JFrame{
	public CodeSelectFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(12, 0));
		setSize(1200,500);
		setLocationRelativeTo(null);
		readBasicKeyMaps();
		setVisible(true);
	}
	public void readBasicKeyMaps() {
		Path dir = Paths.get("basic_keymaps/"); // 디렉토리 경로 지정

        try (Stream<Path> stream = Files.list(dir)) {
            stream.forEach(path -> {
            	String p = (path.getFileName().toString().split("\\."))[0];
                JButton button = new JButton(p);
                button.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						new Piano(p);
						
					}
                	
				});
                add(button);
                if (Files.isDirectory(path)) {
                    System.out.println("(디렉토리)");
                } else if (Files.isRegularFile(path)) {
                    System.out.println(p);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public static void main(String[] args) {
		new CodeSelectFrame();
	}

}
