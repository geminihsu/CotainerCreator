package spirit.fitness.scanner.receving;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.NumberFormatter;

import spirit.fitness.scanner.common.Constrant;
import spirit.fitness.scanner.common.HttpRequestCode;
import spirit.fitness.scanner.model.Containerbean;
import spirit.fitness.scanner.model.Itembean;
import spirit.fitness.scanner.restful.ContainerRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.FGRepositoryImplRetrofit;
import spirit.fitness.scanner.restful.listener.ContainerCallBackFunction;
import spirit.fitness.scanner.restful.listener.InventoryCallBackFunction;
import spirit.fitness.scanner.util.LoadingFrameHelper;

public class ContainerPannel implements ActionListener {

	public final static int ADD = 0;
	public final static int EDIT = 1;
	public final static int DELETE = 2;

	private static ContainerPannel receivingPannel = null;

	public JFrame frame;
	private LoadingFrameHelper loadingframe;
	private JFrame addContainerFrame;
	private JPanel panel;

	private JProgressBar loading;

	private ContainerRepositoryImplRetrofit containerRepositoryImplRetrofit;
	private FGRepositoryImplRetrofit fgRepository;

	private List<Containerbean> editContainers;
	private List<Containerbean> curContainers;
	private int operator;

	public static ContainerPannel getInstance() {
		if (receivingPannel == null) {
			receivingPannel = new ContainerPannel();
		}
		return receivingPannel;
	}

	public static boolean isExit() {
		return receivingPannel != null;
	}

	public static void destory() {
		receivingPannel = null;
	}

	public ContainerPannel() {
		loadingframe = new LoadingFrameHelper("Loading Data from Server...");
		loading = loadingframe.loadingSample("Loading Data from Server...");
		initialCallback();
		loadContainerList();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void containerList(List<Containerbean> containerList) {

		// JFrame.setDefaultLookAndFeelDecorated(false);
		// JDialog.setDefaultLookAndFeelDecorated(false);
		frame = new JFrame("Query Pannel");
		// Setting the width and height of frame
		frame.setSize(760, 600);
		frame.setLocationRelativeTo(null);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setResizable(false);

		panel = new JPanel();
		panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		panel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		frame.add(panel);

		placeConatinerPanel(panel, containerList);

		// frame.setUndecorated(true);
		// frame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		// frame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				frame.dispose();
				frame.setVisible(false);
			}
		});

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.toFront();
				frame.repaint();
			}
		});
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContainerPannel window = new ContainerPannel();
					// QueryResult window = new QueryResult();
					// window.setContent(0, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void addContainerInfo() {
		addContainerFrame = new JFrame("");
		// Setting the width and height of frame
		// scanResultFrame.setSize(600, 800);
		addContainerFrame.setSize(600, 500);
		addContainerFrame.setLocationRelativeTo(null);
		addContainerFrame.setLocationRelativeTo(null);
		addContainerFrame.setUndecorated(true);
		addContainerFrame.setResizable(false);

		JPanel scanDisplayPanel = new JPanel();
		scanDisplayPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Constrant.FRAME_BORDER_BACKGROUN_COLOR));

		scanDisplayPanel.setBackground(Constrant.BACKGROUN_COLOR);
		// adding panel to frame
		addContainerFrame.add(scanDisplayPanel);

		addContainerPanel(scanDisplayPanel);

		addContainerFrame.setBackground(Color.WHITE);
		addContainerFrame.setVisible(true);
		addContainerFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addContainerFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				addContainerFrame.dispose();
				addContainerFrame.setVisible(false);
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	private void placeConatinerPanel(JPanel panel, List<Containerbean> containerList) {

		panel.setLayout(null);

		Font font = new Font("Verdana", Font.BOLD, 18);

		JLabel modelLabel = new JLabel("Please select one container before EDIT and DELETE.");

		modelLabel.setBounds(30, 30, 800, 25);
		modelLabel.setFont(font);
		panel.add(modelLabel);
		// ScrollPane for Result
		JScrollPane scrollZonePane = new JScrollPane();

		scrollZonePane.setBackground(Constrant.TABLE_COLOR);

		if (!containerList.isEmpty()) {

			LinkedHashMap<String, List<Containerbean>> map = new LinkedHashMap<String, List<Containerbean>>();

			for (Containerbean item : containerList) {

				if (map.containsKey(item.ContainerNo)) {
					List<Containerbean> items = map.get(item.ContainerNo);
					items.add(item);
				} else {
					List<Containerbean> items = new ArrayList<Containerbean>();
					items.add(item);
					map.put(item.ContainerNo, items);
				}
			}

			int rowIndex = 0;

			final Object[][] containerItems = new Object[map.size()][4];

			for (Map.Entry<String, List<Containerbean>> location : map.entrySet()) {

				List<Containerbean> list = location.getValue();

				int qty = 0;
				for (int s = 0; s < list.size(); s++) {
					qty += Integer.valueOf(list.get(0).SNEnd.substring(10, 16))
							- Integer.valueOf(list.get(0).SNBegin.substring(10, 16));
				}

				for (int j = 0; j < 4; j++) {

					String date = "";
					if( list.get(0).date != null)
							date = list.get(0).date.substring(0, 10);
					containerItems[rowIndex][0] = date;

					containerItems[rowIndex][1] = location.getKey();
					
					containerItems[rowIndex][2] = location.getValue().get(0).ModelNo;
					
					containerItems[rowIndex][3] = qty + 1;

				}

				rowIndex++;
			}

			final Class[] columnClass = new Class[] { String.class, String.class, String.class,Integer.class };

			Object columnNames[] = { "RECEIVED DATE", "CONTAINERNO", "ModelNo", "QTY" };

			DefaultTableModel container = new DefaultTableModel(containerItems, columnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					curContainers = map.get(containerItems[row][1]);
					return false;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					return columnClass[columnIndex];
				}
			};

			JTable containerTable = new JTable(container);
			containerTable.getTableHeader().setBackground(Constrant.TABLE_COLOR);
			containerTable.getTableHeader().setFont(font);

			containerTable.setBackground(Constrant.TABLE_COLOR);
			containerTable.setRowHeight(40);
			containerTable.setFont(font);

			DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
			leftRenderer.setHorizontalAlignment(JLabel.CENTER);
			TableColumn date = containerTable.getColumnModel().getColumn(0);
			date.setCellRenderer(leftRenderer);
			TableColumn no = containerTable.getColumnModel().getColumn(1);
			no.setPreferredWidth(150);
			no.setCellRenderer(leftRenderer);
			
			TableColumn model = containerTable.getColumnModel().getColumn(2);
			model.setCellRenderer(leftRenderer);

			TableColumn qty = containerTable.getColumnModel().getColumn(3);
			qty.setCellRenderer(leftRenderer);
			qty.setPreferredWidth(20);

			int heigh = 0;

			if (50 * containerItems.length + 20 > 380)
				heigh = 380;
			else
				heigh = 50 * containerItems.length + 20;
			scrollZonePane.setBounds(33, 91, 700, heigh);
			scrollZonePane.setViewportView(containerTable);

			panel.add(scrollZonePane);

		}

		JButton add = new JButton(new AbstractAction("ADD") {

			@Override
			public void actionPerformed(ActionEvent e) {
				operator = ADD;

				if (curContainers != null)
					curContainers.clear();
				else
					curContainers = new ArrayList<Containerbean>();

				frame.dispose();
				frame.setVisible(false);
				addContainerInfo();
			}
		});

		JButton edit = new JButton(new AbstractAction("EDIT") {

			@Override
			public void actionPerformed(ActionEvent e) {

				operator = EDIT;
				if (curContainers != null) {
					frame.dispose();
					frame.setVisible(false);
					addContainerInfo();
				} else
					JOptionPane.showMessageDialog(null, "Please select one container before EDIT.");

			}
		});

		JButton delete = new JButton(new AbstractAction("DELETE") {

			@Override
			public void actionPerformed(ActionEvent e) {
				operator = DELETE;
				if (curContainers != null) {
					int result = JOptionPane.showConfirmDialog(frame, "Do you want to delete the container?", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						deleteContainerInfo(Integer.valueOf(curContainers.get(0).Seq));
					}
				} else
					JOptionPane.showMessageDialog(null, "Please select one container before DELETE.");

			}
		});

		JButton exit = new JButton(new AbstractAction("Exit") {

			@Override
			public void actionPerformed(ActionEvent e) {

				frame.dispose();
				frame.setVisible(false);
				receivingPannel = null;
			}
		});

		add.setFont(font);
		edit.setFont(font);
		delete.setFont(font);
		exit.setFont(font);
		add.setBounds(30, 520, 150, 50);
		edit.setBounds(200, 520, 150, 50);
		delete.setBounds(370, 520, 150, 50);
		exit.setBounds(540, 520, 100, 50);

		panel.add(add);
		panel.add(edit);
		panel.add(delete);
		panel.add(exit);
	}

	private void addContainerPanel(JPanel panel) {

		panel.setLayout(null);
		Font font = new Font("Verdana", Font.BOLD, 18);

		int seq = (curContainers.isEmpty() && operator == ADD) ? 0 : curContainers.get(0).Seq;
		String date = (curContainers.isEmpty() && operator == ADD) ? "" : curContainers.get(0).date;
		String containerNoTxt = (curContainers.isEmpty() && operator == ADD) ? "" : curContainers.get(0).ContainerNo;
		String serialNoBegin = (curContainers.isEmpty() && operator == ADD) ? "" : curContainers.get(0).SNBegin;
		String serialNoEnd = (curContainers.isEmpty() && operator == ADD) ? "" : curContainers.get(0).SNEnd;
		
		JLabel dateLabel = new JLabel("Received Date");

		dateLabel.setBounds(50, 50, 200, 25);
		dateLabel.setFont(font);
		panel.add(dateLabel);

		JTextField dateText = new JTextField(20);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		//if (date.equals(""))
		//	dateText.setText(timeStamp);
		//else
		dateText.setText(date);
		dateText.setFont(font);
		dateText.setBounds(250, 50, 320, 50);

		panel.add(dateText);

		JLabel locationLabel = new JLabel("Container#");
		locationLabel.setFont(font);
		locationLabel.setBounds(50, 120, 200, 50);
		panel.add(locationLabel);

		JTextField containerNo = new JTextField(20);
		containerNo.setText(containerNoTxt);
		containerNo.setFont(font);
		containerNo.setBounds(250, 120, 320, 50);

		panel.add(containerNo);

		addContainerFrame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				containerNo.requestFocus();
			}
		});

		JLabel snbeginLabel = new JLabel("Serial no. begin");
		snbeginLabel.setFont(font);
		snbeginLabel.setBounds(50, 190, 200, 50);
		panel.add(snbeginLabel);

		JTextField snbegin = new JTextField(20);
		snbegin.setText(serialNoBegin);
		snbegin.setFont(font);
		snbegin.setBounds(250, 190, 320, 50);

		panel.add(snbegin);

		JLabel snendLabel = new JLabel("Serial no. End");
		snendLabel.setFont(font);
		snendLabel.setBounds(50, 260, 200, 50);
		panel.add(snendLabel);

		JTextField snend = new JTextField(20);
		snend.setText(serialNoEnd);
		snend.setFont(font);
		snend.setBounds(250, 260, 320, 50);

		panel.add(snend);

		JButton queryButton = new JButton("Submit");
		queryButton.setFont(font);
		queryButton.setBounds(250, 330, 150, 50);
		queryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ( !containerNo.getText().toString().equals("")
						&& !snbegin.getText().toString().equals("") && !snend.getText().toString().equals("")
						&& snbegin.getText().toString().trim().length() == 16
						&& snend.getText().toString().trim().length() == 16) {

					if (!snbegin.getText().toString().trim().substring(0, 6)
							.equals(snend.getText().toString().trim().substring(0, 6)))
						JOptionPane.showMessageDialog(null,
								"If the container has more than one model, please create another container.");

					else if (snbegin.getText().toString().trim().equals(snend.getText().toString().trim()))
						JOptionPane.showMessageDialog(null,
								"The SN start number the same as the end number. Please check them.");

					else if (Integer.valueOf(snend.getText().toString().trim().substring(10, 16))
							- Integer.valueOf(snbegin.getText().toString().trim().substring(10, 16)) < 0)
						JOptionPane.showMessageDialog(null,
								"The SN start number is smaller than SN end number. Please check them.");

					else {
						
						int n = JOptionPane.showConfirmDialog(
							    frame,
							    "Are you sure add "+containerNo.getText().toString().trim()+" to list ?",
							    "Confirmation",
							    JOptionPane.YES_NO_OPTION,
					               JOptionPane.WARNING_MESSAGE);
						
						if(n == 0) {
						editContainers = new ArrayList<Containerbean>();
						Containerbean container = new Containerbean();
						container.Seq = seq;
						container.date = dateText.getText().toString();
						container.ContainerNo = containerNo.getText().toString().trim();
						container.SNBegin = snbegin.getText().toString().trim();
						container.SNEnd = snend.getText().toString().trim();
						container.ModelNo = container.SNBegin.substring(0,6);
						container.Close = false;
						editContainers.add(container);

						// check SN exits or not
						List<Itembean> items = new ArrayList<Itembean>();
						Itembean snbegin = new Itembean();
						snbegin.SN = container.SNBegin;
						snbegin.ModelNo = snbegin.SN.substring(0, 6);
						snbegin.Location = "000";
						items.add(snbegin);

						Itembean snend = new Itembean();
						snend.SN = container.SNEnd;
						snend.ModelNo = snend.SN.substring(0, 6);
						snend.Location = "000";
						items.add(snend);

						checkItemExits(items);
						}
					}
				}else
					JOptionPane.showMessageDialog(null,
							"Please enter correct informtaion.");

			}
		});
		panel.add(queryButton);

		// Creating Query button
		JButton resetButton = new JButton("Clear");
		resetButton.setFont(font);
		resetButton.setBounds(420, 330, 150, 50);
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				containerNo.setText("");
				snbegin.setText("");
				snend.setText("");

			}
		});

		panel.add(resetButton);

		JButton backButton = new JButton("Back");
		backButton.setFont(font);
		backButton.setBounds(250, 390, 320, 50);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(true);
				addContainerFrame.setVisible(false);
				addContainerFrame.dispose();
			}
		});

		JButton exitButton = new JButton("Exit");
		exitButton.setFont(font);
		exitButton.setBounds(420, 390, 150, 50);
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addContainerFrame.setVisible(false);
				addContainerFrame.dispose();
				receivingPannel = null;
			}
		});

		panel.add(backButton);
		//panel.add(exitButton);
	}

	private void initialCallback() {

		fgRepository = new FGRepositoryImplRetrofit();
		fgRepository.setinventoryServiceCallBackFunction(new InventoryCallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub

			}

			@Override
			public void getInventoryItems(List<Itembean> items) {

			}

			@Override
			public void checkInventoryZone2Items(int result, List<Itembean> items) {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkMoveItems(List<Itembean> items) {
				if (items.size() == 0)
					JOptionPane.showMessageDialog(null, "Items already exist.");
				else {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								loadingframe = new LoadingFrameHelper("Add data...");
								loading = loadingframe.loadingSample("Add data...");

								if (operator == ADD)
									addContainerInfo(editContainers);
								else if (operator == EDIT)
									updateContainerInfo(editContainers);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

				}
			}

			@Override
			public void checkReceiveItem(List<Itembean> items) {

			}

			@Override
			public void exception(String error) {
				// TODO Auto-generated method stub
				
			}
		});

		containerRepositoryImplRetrofit = new ContainerRepositoryImplRetrofit();
		containerRepositoryImplRetrofit.setContainerServiceCallBackFunction(new ContainerCallBackFunction() {

			@Override
			public void resultCode(int code) {
				// TODO Auto-generated method stub
				if (code == HttpRequestCode.HTTP_REQUEST_INSERT_DATABASE_ERROR) {

				} else if (code == HttpRequestCode.HTTP_REQUEST_SN_CONFLICT_ERROR)
					JOptionPane.showMessageDialog(null, "SN already exist!");
			}

			@Override
			public void getContainerItems(List<Containerbean> items) {
				loading.setValue(100);
				loadingframe.setVisible(false);
				loadingframe.dispose();

				if (addContainerFrame != null) {
					addContainerFrame.setVisible(false);
					addContainerFrame.dispose();
					addContainerFrame = null;
				}

				if (!items.isEmpty() && operator == EDIT)
					JOptionPane.showMessageDialog(null, "update Data Success!");

				// if(frame == null)
				containerList(items);
				// else {
				// placeConatinerPanel(panel, items);
				// frame.setVisible(true);
				// }
			}

			@Override
			public void addContainerInfo(List<Containerbean> items) {

				loadingframe.setVisible(false);
				loadingframe.dispose();

				if (items == null)
					JOptionPane.showMessageDialog(null, "SN already exist.");
				else if (!items.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Insert Data Success.");

					loadContainerList();
					frame = null;
					addContainerFrame.setVisible(false);
					addContainerFrame.dispose();
				}

			}

			@Override
			public void deleteContainerIteam(boolean result) {
				if (result) {
					if (frame != null) {
						frame.setVisible(false);
						frame.dispose();
						frame = null;
					}

					JOptionPane.showMessageDialog(null, "Delete Data Success!");
					loadContainerList();
				}
			}

			@Override
			public void getContainerItemsByContainerNo(List<Containerbean> items) {
				// TODO Auto-generated method stub
				
			}

		});

	}

	// Loading Models data from Server
	private void loadContainerList() {

		// loading model and location information from Server
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					containerRepositoryImplRetrofit.getAllItems();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void addContainerInfo(List<Containerbean> containers) {

		// loading model and location information from Server
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					containerRepositoryImplRetrofit.createItem(containers);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void updateContainerInfo(List<Containerbean> containers) {

		// loading model and location information from Server
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					containerRepositoryImplRetrofit.updateItem(containers);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void deleteContainerInfo(Integer itemId) {

		// loading model and location information from Server
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					containerRepositoryImplRetrofit.deleteItem(itemId);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void checkItemExits(List<Itembean> items) {
		try {
			fgRepository.getMoveItemBySNList(items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
