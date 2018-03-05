package com.customerinvite;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.codehaus.jackson.map.ObjectMapper;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.ScrollPane;
import javax.swing.JTabbedPane;

public class CustomerSelection extends JFrame {

	private JPanel contentPane;
	private JTextField txt_InputFilePath;
	private JTextField txtDistance;
	private JTextArea txtOutputWindow;
	private JScrollPane scroll;
	private JTextField txtOfficeLatitude;
	private JTextField txtOfficeLogitude;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		String fileDir;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerSelection frame = new CustomerSelection();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public  CustomerSelection() {
		setTitle("Customer Invite Tool");
		setFont(new Font("Arial", Font.PLAIN, 12));
		setForeground(new Color(0, 128, 128));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 837, 606);
		contentPane = new JPanel();
		
		contentPane.setBackground(new Color(135, 206, 235));
		contentPane.setForeground(Color.PINK);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setLayout(null);
		
		txt_InputFilePath = new JTextField();
		txt_InputFilePath.setBounds(41, 7, 578, 26);
		contentPane.add(txt_InputFilePath);
		txt_InputFilePath.setColumns(10);
		
		JButton btnSelectFile = new JButton("Input File(*.txt)");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				   JFileChooser chooser = new JFileChooser();
				   chooser. setAcceptAllFileFilterUsed(false); 
				   FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				   chooser.setFileFilter(filter);
		           chooser.showOpenDialog(null);
		           File f = chooser.getSelectedFile();
		           String filename = f.getAbsolutePath();
		           
		          // String extension = Files.getFileExtension(path);
		           txt_InputFilePath.setText(filename);
			}
		});
		btnSelectFile.setBounds(622, 6, 143, 29);
		contentPane.add(btnSelectFile);
		//txtOutputWindow.setBounds(15, 152, 404, 382);
		
		 scroll = new JScrollPane (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		 scroll.setBounds(0, 143, 412, 372);
		contentPane.add(scroll);
		
		
		 txtOutputWindow = new JTextArea(10,60);
		 scroll.setViewportView(txtOutputWindow);
		contentPane.setSize(400,400);
		//contentPane.setLocationRelativeTo(null);
		contentPane.setVisible (true);
		
		
		txtDistance = new JTextField();
		txtDistance.setText("100");
		txtDistance.setBounds(128, 59, 94, 26);
		contentPane.add(txtDistance);
		txtDistance.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Enter Distance : ");
		lblNewLabel.setBounds(15, 62, 122, 20);
		contentPane.add(lblNewLabel);
		
		JTextArea txtMessageBox = new JTextArea();
		txtMessageBox.setBackground(Color.CYAN);
		txtMessageBox.setBounds(427, 360, 373, 155);
		contentPane.add(txtMessageBox);
		
		JLabel lblNewLabel_1 = new JLabel("Message Box :");
		lblNewLabel_1.setBounds(427, 339, 101, 20);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Customer Invite List Output :");
		lblNewLabel_2.setBounds(15, 120, 207, 20);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton_1 = new JButton("Provide List");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	
				// Open the file
				String filename = txt_InputFilePath.getText();
				System.out.println(filename);
				ComputeInvitation( filename);

			}

			private void ComputeInvitation(String filename) {
				
				txtMessageBox.selectAll();
			    txtMessageBox.replaceSelection("");
				txtMessageBox.append("Please Select customer Input File in a *.txt : (JSON-encoded)" );
				Path path = Paths.get(filename);
				String fileDir = path.getParent().toString();
				
				
				// Open the file
				FileInputStream fstream=null;
				try {
					fstream = new FileInputStream(filename);
				} catch (FileNotFoundException e) {e.printStackTrace();}
				BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				TreeMap<Integer, String> tmap = 
			             new TreeMap<Integer, String>();	
						try {
							while ((strLine = br.readLine()) != null)   {
								try {
							ObjectMapper objectmapper = new ObjectMapper();
							Customer_Data obj = new Customer_Data();
							//System.out.println(strLine);
							obj= objectmapper.readValue(strLine, Customer_Data.class);
						    //"-------Afer converting to java object-------- 
							Double cus_latitude = obj.getLatitude();
							Double cus_logitude = obj.getLongitude();
							if((cus_latitude== null)||(cus_logitude== null))
							continue;
							double OfficeLatitude = Double.parseDouble(txtOfficeLatitude.getText());
							double OfficeLongitude = Double.parseDouble(txtOfficeLogitude.getText());
							double distance = getDistance(OfficeLatitude, OfficeLongitude, cus_latitude, cus_logitude);

							if (distance <=Integer.parseInt(txtDistance.getText()))
							{tmap.put(obj.getUser_id(), obj.getName());}
                          } catch (IOException e) { 
									txtMessageBox.selectAll();
									txtMessageBox.replaceSelection("");
									txtMessageBox.setText("Invalide JSON FOUND IN YOUR DATASET");
							        e.printStackTrace(); 
							      }
				    	}
				       } catch (NumberFormatException | IOException e2) {e2.printStackTrace();}
					//Close the input stream	
					try {
						br.close();
					} catch (IOException e) {e.printStackTrace();}   
					txtOutputWindow.selectAll();
					txtOutputWindow.replaceSelection("");
					txtOutputWindow.append("Customer Invite List : ");
					File file =new File(fileDir+"\\CustomerInviteList.txt");
					FileWriter writer=null;
					try {
						file.createNewFile();
			            writer = new FileWriter(file);
			         }			     
			        catch (IOException e) {e.printStackTrace();}
					/* Display content using Iterator */
				      Set set = tmap.entrySet();
				      Iterator iterator = set.iterator();
				      while(iterator.hasNext()) {
				         Map.Entry mentry = (Map.Entry)iterator.next();
				         txtOutputWindow.append("\n" + "user_id : "+ mentry.getKey() + " & name : "+ mentry.getValue());
				         try {
							writer.write("Customer ID = "+ mentry.getKey() + " & Customer Name = "+ mentry.getValue()+"\n");
							writer.flush();
						} catch (IOException e1) {e1.printStackTrace();}     
				      }
				      try {
						writer.close();
					} catch (IOException e1) {e1.printStackTrace();}	
				      txtMessageBox.selectAll();
				      txtMessageBox.replaceSelection("");
					  txtMessageBox.append("\n \nCustomer Invite List is available in CustomerInviteList.txt at : \n"+fileDir+"//CustomerInviteList.txt");		
			}
			
			public double toRadians(double deg) {
		        return  deg * (Math.PI / 180);
		    }
			
		    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
		        double R = 6371;
		        double diffLat = toRadians(lat2 - lat1);
		        double diffLon = toRadians(lon2 - lon1);
		        double a = Math.sin(diffLat/2) * Math.sin(diffLat/2) +
		                Math.cos(toRadians(lat1)) * Math.cos(toRadians(lat2)) *
		                        Math.sin(diffLon/2) * Math.sin(diffLon/2);
		        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		        double d = R * c;
		        return d;
		    }
		});
		btnNewButton_1.setBackground(new Color(250, 128, 114));
		btnNewButton_1.setBounds(518, 156, 115, 29);
		contentPane.add(btnNewButton_1);
		
		txtOfficeLatitude = new JTextField();
		txtOfficeLatitude.setText("53.339428");
		txtOfficeLatitude.setBounds(354, 59, 136, 26);
		contentPane.add(txtOfficeLatitude);
		txtOfficeLatitude.setColumns(10);
		
		JLabel lblOfficeLatitude = new JLabel("Office Latitude :");
		lblOfficeLatitude.setBounds(237, 62, 115, 20);
		contentPane.add(lblOfficeLatitude);
		
		JLabel lblOfficeLogitudde = new JLabel("Office Logitude :");
		lblOfficeLogitudde.setBounds(505, 62, 128, 20);
		contentPane.add(lblOfficeLogitudde);
		
		txtOfficeLogitude = new JTextField();
		txtOfficeLogitude.setText("-6.257664");
		txtOfficeLogitude.setBounds(632, 59, 146, 26);
		contentPane.add(txtOfficeLogitude);
		txtOfficeLogitude.setColumns(10);
	}
}
