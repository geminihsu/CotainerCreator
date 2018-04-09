package spirit.fitness.scanner.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class LoadingFrameHelper extends JFrame{
	private JProgressBar loading;
	private Container content;
	private Border border;
	
	public LoadingFrameHelper(String title) 
	{
		
		 content = this.getContentPane();
		 loading = new JProgressBar();
	}
	
	public JProgressBar loadingSample(String title) 
	{
			this.setTitle(title);
			this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setSize(200, 100);
			Border border = BorderFactory.createTitledBorder(title);
		    loading.setValue(25);
		    loading.setStringPainted(true);
		    loading.setBorder(border);
		    content.add(loading, BorderLayout.NORTH);
			this.setLocationRelativeTo(null);
		    this.setVisible(true);
		    this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					dispose();
					setVisible(false);
				}
			});

		    return loading;
	}
	
	public void updateTitle(String title) {
		this.setTitle(title);
		Border border = BorderFactory.createTitledBorder(title);
		loading.setBorder(border);
		content.add(loading, BorderLayout.NORTH);
	}
}
