
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	private JLabel name;
	private JButton add, Delete, lookUp, Status, Picture, Friend;
	private JTextField NameField, StatusField, PicField, FriendField;

	private FacePamphletDatabase Database;

	private FacePamphletCanvas canvas = new FacePamphletCanvas();

	private FacePamphletProfile Profile; // The profile which is going to change or display.

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		// You fill this in
		Database = new FacePamphletDatabase();
		name = new JLabel("Name ");
		add(name, NORTH);
		NameField = new JTextField(TEXT_FIELD_SIZE);
		add(NameField, NORTH);
		NameField.addActionListener(this);
		add = new JButton("Add");
		add(add, NORTH);
		Delete = new JButton("Delete");
		add(Delete, NORTH);
		lookUp = new JButton("Lookup");
		add(lookUp, NORTH);

		StatusField = new JTextField(TEXT_FIELD_SIZE);
		add(StatusField, WEST);
		StatusField.addActionListener(this);
		Status = new JButton("Change Status");
		add(Status, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		PicField = new JTextField(TEXT_FIELD_SIZE);
		add(PicField, WEST);
		PicField.addActionListener(this);
		Picture = new JButton("Change Picture");
		add(Picture, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		FriendField = new JTextField(TEXT_FIELD_SIZE);
		add(FriendField, WEST);
		FriendField.addActionListener(this);
		Friend = new JButton("Add Friend");
		add(Friend, WEST);
		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		Profile = null;
		add(canvas);
		addActionListeners();

	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == add && !NameField.getText().equals("")) {
			if (Database.containsProfile(NameField.getText())) {
				FacePamphletProfile exProfile = Database.getProfile(NameField.getText()); // A profile which already
																							// exists.
				Profile = exProfile;
				canvas.displayProfile(Profile);
				canvas.showMessage("A profile with the name " + NameField.getText() + " already exists."); // You can't
																											// add same
																											// names to
																											// different
																											// profiles.
			} else {
				FacePamphletProfile newProfile = new FacePamphletProfile(NameField.getText());
				Profile = newProfile;
				Database.addProfile(newProfile);
				canvas.displayProfile(Profile);
				canvas.showMessage("New profile created: " + newProfile.getName());
			}
		} else if (src == Delete && !NameField.getText().equals("")) {
			if (Database.containsProfile(NameField.getText())) {
				Database.deleteProfile(NameField.getText());
				Profile = null;
				canvas.displayProfile(Profile);
				canvas.showMessage("Profile of " + NameField.getText() + " deleted.");
			} else {
				Profile = null;
				canvas.displayProfile(Profile);
				canvas.showMessage("Profile " + NameField.getText() + " does not exist.");
			}
		} else if (src == lookUp && !NameField.getText().equals("")) {
			if (Database.containsProfile(NameField.getText())) {
				FacePamphletProfile exProfile = Database.getProfile(NameField.getText());
				Profile = exProfile;
				canvas.displayProfile(Profile);
				canvas.showMessage("Displaying " + NameField.getText());
			} else {
				Profile = null;
				canvas.displayProfile(Profile);
				canvas.showMessage("A profile " + NameField.getText() + " does not exist.");
			}
		}

		if ((src == Status || src == StatusField) && !StatusField.getText().equals("")) {
			if (Profile != null) {
				String status = StatusField.getText();
				Profile.setStatus(status);
				canvas.displayProfile(Profile);
				canvas.showMessage("Status updated to " + status);
			} else {
				canvas.displayProfile(Profile);
				canvas.showMessage("Please select a profile to change status");
			}
		} else if ((src == Picture || src == PicField) && !PicField.getText().equals("")) {
			if (Profile != null) {
				String image = PicField.getText();
				GImage img = null;
				try {
					img = new GImage(image);
				} catch (Exception e2) {
					// TODO: handle exception
					System.out.println("Error: Please, upload available image."); // Case, if you enter unavailable
																					// image.
				}
				if (img != null) {
					Profile.setImage(img);
					canvas.displayProfile(Profile);
					canvas.showMessage("Picture updated");
				} else {
					canvas.displayProfile(Profile);
				}
			} else {
				canvas.displayProfile(Profile);
				canvas.showMessage("Please select a profile to change picture");
			}
		} else if ((src == Friend || src == FriendField) && !FriendField.getText().equals("")) {
			if (Profile != null) {
				if (Database.containsProfile(FriendField.getText())) {
					if (Profile.getName().equals(FriendField.getText())) { // you can't be friend with yourself...
						canvas.showMessage("Please add friend other than you");
					} else {
						if (Profile.addFriend(FriendField.getText())) {
							Database.getProfile(FriendField.getText()).addFriend(Profile.getName());
							canvas.displayProfile(Profile);
							canvas.showMessage(FriendField.getText() + " added as a friend.");
						} else {
							canvas.displayProfile(Profile);
							canvas.showMessage(
									Profile.getName() + " already has " + FriendField.getText() + " as a friend");
						}
					}
				} else {
					canvas.displayProfile(Profile);
					canvas.showMessage(FriendField.getText() + " does not exist.");
				}
			} else {
				canvas.displayProfile(Profile);
				canvas.showMessage("You haven't chosen a profile");
			}
		}
	}

}
